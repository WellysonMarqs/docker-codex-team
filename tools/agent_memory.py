#!/usr/bin/env python3
"""CLI para memoria persistente dos agentes."""

from __future__ import annotations

import argparse
import json
import secrets
import subprocess
import sys
import time
import uuid
from dataclasses import dataclass
from datetime import UTC, datetime
from pathlib import Path
from typing import Any, Iterable, Protocol

ROOT_DIR = Path(__file__).resolve().parents[1]
SQLSERVER_BOOTSTRAP_PATH = ROOT_DIR / "memory" / "sqlserver" / "bootstrap.sql"
SQLSERVER_SCHEMA_PATH = ROOT_DIR / "memory" / "sqlserver" / "schema.sql"
SQLSERVER_COMPOSE_PATH = ROOT_DIR / "docker-compose.agent-memory-sqlserver.yml"
SQLSERVER_ENV_PATH = ROOT_DIR / ".memory" / "sqlserver.env"
VALID_CATEGORIES = ("context", "rule", "lesson", "improvement")
VALID_SCOPES = ("global", "project", "task")
DEFAULT_SQLSERVER_DATABASE = "MemoriaAgentes"
DEFAULT_SQLSERVER_PORT = 14333
DEFAULT_SQLSERVER_SERVICE = "agent-memory-sqlserver"
DEFAULT_SQLSERVER_CONTAINER = "agent-memory-sqlserver"
DEFAULT_SQLSERVER_HOST = "host.docker.internal"


def now_iso() -> str:
    return datetime.now(UTC).replace(microsecond=0).isoformat()


def normalize_tags(raw_tags: str | None) -> str:
    if not raw_tags:
        return ""
    cleaned: list[str] = []
    seen: set[str] = set()
    for part in raw_tags.split(","):
        tag = part.strip().lower()
        if tag and tag not in seen:
            cleaned.append(tag)
            seen.add(tag)
    return ",".join(cleaned)


def parse_tags(raw_tags: str) -> list[str]:
    if not raw_tags:
        return []
    return [tag for tag in raw_tags.split(",") if tag]


def sql_string(value: str) -> str:
    return "N'" + value.replace("'", "''") + "'"


def sql_nullable_string(value: str | None) -> str:
    return "NULL" if value is None else sql_string(value)


def parse_datetime_string(value: str | None) -> str | None:
    if value in (None, "", "NULL"):
        return None
    normalized = value.strip().replace("Z", "+00:00")
    return datetime.fromisoformat(normalized).isoformat()


@dataclass
class Entry:
    id: str
    agent_name: str
    category: str
    scope: str
    title: str
    content: str
    tags: str
    source_kind: str
    source_ref: str | None
    status: str
    confidence: int
    created_at: str
    updated_at: str
    last_used_at: str | None

    @classmethod
    def from_mapping(cls, row: dict[str, Any]) -> "Entry":
        normalized = dict(row)
        normalized.setdefault("source_ref", None)
        normalized.setdefault("last_used_at", None)
        normalized["confidence"] = int(normalized["confidence"])
        normalized["last_used_at"] = parse_datetime_string(normalized.get("last_used_at"))
        normalized["created_at"] = parse_datetime_string(normalized["created_at"]) or ""
        normalized["updated_at"] = parse_datetime_string(normalized["updated_at"]) or ""
        return cls(**normalized)

    def to_dict(self) -> dict[str, object]:
        payload = self.__dict__.copy()
        payload["tags"] = parse_tags(self.tags)
        return payload


class Repository(Protocol):
    def init_db(self) -> None: ...
    def upsert_entry(
        self,
        *,
        agent_name: str,
        category: str,
        scope: str,
        title: str,
        content: str,
        tags: str | None,
        source_kind: str,
        source_ref: str | None,
        confidence: int,
        actor: str,
    ) -> tuple[str, Entry]: ...
    def get_entry(self, entry_id: str) -> Entry | None: ...
    def list_entries(
        self,
        *,
        agent_name: str | None,
        category: str | None,
        query: str | None,
        tag: str | None,
        include_archived: bool,
        limit: int,
    ) -> list[Entry]: ...
    def archive_entry(self, *, entry_id: str, actor: str) -> Entry: ...
    def touch_entry(self, *, entry_id: str, actor: str) -> Entry: ...
    def stats(self) -> dict[str, object]: ...


@dataclass
class SqlServerSettings:
    compose_file: Path = SQLSERVER_COMPOSE_PATH
    env_file: Path = SQLSERVER_ENV_PATH
    service_name: str = DEFAULT_SQLSERVER_SERVICE
    container_name: str = DEFAULT_SQLSERVER_CONTAINER
    database: str = DEFAULT_SQLSERVER_DATABASE
    port: int = DEFAULT_SQLSERVER_PORT
    sa_user: str = "sa"
    host: str = DEFAULT_SQLSERVER_HOST
    target_port: int = 1433

    def load_password(self) -> str:
        env_map = parse_env_file(self.env_file)
        password = env_map.get("MSSQL_SA_PASSWORD")
        if not password:
            raise RuntimeError(f"Senha do SQL Server nao encontrada em {self.env_file}")
        return password


def parse_env_file(path: Path) -> dict[str, str]:
    values: dict[str, str] = {}
    if not path.exists():
        return values
    for raw_line in path.read_text(encoding="utf-8").splitlines():
        line = raw_line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, value = line.split("=", 1)
        values[key.strip()] = value.strip()
    return values


def ensure_sqlserver_env_file(settings: SqlServerSettings) -> None:
    if settings.env_file.exists():
        return
    settings.env_file.parent.mkdir(parents=True, exist_ok=True)
    password = f"AgentMem_{secrets.token_urlsafe(18)}Aa1!"
    settings.env_file.write_text(
        "\n".join(
            [
                f"MSSQL_SA_PASSWORD={password}",
                f"MSSQL_PORT={settings.port}",
            ]
        )
        + "\n",
        encoding="utf-8",
    )


class SqlServerKnowledgeRepository:
    def __init__(self, settings: SqlServerSettings) -> None:
        self.settings = settings

    def init_db(self) -> None:
        ensure_sqlserver_env_file(self.settings)
        self._docker_compose("up", "-d", self.settings.service_name)
        self._wait_until_ready()
        self._ensure_database_exists()
        self._run_sql_file(SQLSERVER_SCHEMA_PATH, database=self.settings.database)

    def upsert_entry(
        self,
        *,
        agent_name: str,
        category: str,
        scope: str,
        title: str,
        content: str,
        tags: str | None,
        source_kind: str,
        source_ref: str | None,
        confidence: int,
        actor: str,
    ) -> tuple[str, Entry]:
        timestamp = now_iso()
        entry_id = str(uuid.uuid4())
        normalized_tags = normalize_tags(tags)
        query = f"""
        SET NOCOUNT ON;
        DECLARE @EntryId UNIQUEIDENTIFIER;
        DECLARE @Action NVARCHAR(16);

        SELECT @EntryId = id
        FROM dbo.memorias_agentes
        WHERE nome_agente = {sql_string(agent_name)}
          AND categoria = {sql_string(category)}
          AND titulo = {sql_string(title)};

        IF @EntryId IS NULL
        BEGIN
            SET @EntryId = CAST({sql_string(entry_id)} AS UNIQUEIDENTIFIER);
            INSERT INTO dbo.memorias_agentes (
                id, nome_agente, categoria, escopo, titulo, conteudo, tags,
                tipo_origem, referencia_origem, situacao, confianca, criado_em, atualizado_em
            ) VALUES (
                @EntryId,
                {sql_string(agent_name)},
                {sql_string(category)},
                {sql_string(scope)},
                {sql_string(title)},
                {sql_string(content)},
                {sql_string(normalized_tags)},
                {sql_string(source_kind)},
                {sql_nullable_string(source_ref)},
                N'active',
                {confidence},
                CAST({sql_string(timestamp)} AS DATETIME2(0)),
                CAST({sql_string(timestamp)} AS DATETIME2(0))
            );
            SET @Action = N'created';
        END
        ELSE
        BEGIN
                UPDATE dbo.memorias_agentes
                SET escopo = {sql_string(scope)},
                    conteudo = {sql_string(content)},
                    tags = {sql_string(normalized_tags)},
                    tipo_origem = {sql_string(source_kind)},
                    referencia_origem = {sql_nullable_string(source_ref)},
                    situacao = N'active',
                    confianca = {confidence},
                    atualizado_em = CAST({sql_string(timestamp)} AS DATETIME2(0))
            WHERE id = @EntryId;
            SET @Action = N'updated';
        END;

        INSERT INTO dbo.eventos_memorias_agentes (memoria_id, acao, autor, observacao_evento, criado_em)
        VALUES (
            @EntryId,
            @Action,
            {sql_string(actor)},
            {sql_string(f"{category}:{title}")},
            CAST({sql_string(timestamp)} AS DATETIME2(0))
        );

        SELECT @Action AS action, CAST(@EntryId AS NVARCHAR(36)) AS entry_id
        FOR JSON PATH, WITHOUT_ARRAY_WRAPPER;
        """
        payload = self._execute_json_query(query)
        action = str(payload["action"])
        persisted_id = str(payload["entry_id"])
        entry = self.get_entry(persisted_id)
        if entry is None:
            raise RuntimeError("Falha ao recuperar memoria gravada.")
        return action, entry

    def get_entry(self, entry_id: str) -> Entry | None:
        query = self._entry_select_query(where_clause=f"WHERE id = CAST({sql_string(entry_id)} AS UNIQUEIDENTIFIER)")
        payload = self._execute_json_query(query)
        if payload is None:
            return None
        return Entry.from_mapping(payload)

    def list_entries(
        self,
        *,
        agent_name: str | None,
        category: str | None,
        query: str | None,
        tag: str | None,
        include_archived: bool,
        limit: int,
    ) -> list[Entry]:
        clauses: list[str] = []
        if agent_name:
            clauses.append(f"nome_agente = {sql_string(agent_name)}")
        if category:
            clauses.append(f"categoria = {sql_string(category)}")
        if query:
            like_value = sql_string(f"%{query}%")
            clauses.append(f"(titulo LIKE {like_value} OR conteudo LIKE {like_value})")
        if tag:
            clauses.append(f"(N',' + tags + N',') LIKE {sql_string(f'%,{tag.lower()},%')}")
        if not include_archived:
            clauses.append("situacao = N'active'")
        where_sql = f"WHERE {' AND '.join(clauses)}" if clauses else ""
        query_sql = self._entry_select_query(where_clause=where_sql, top=limit, as_array=True)
        payload = self._execute_json_query(query_sql)
        if not payload:
            return []
        return [Entry.from_mapping(item) for item in payload]

    def archive_entry(self, *, entry_id: str, actor: str) -> Entry:
        timestamp = now_iso()
        query = f"""
        SET NOCOUNT ON;
        UPDATE dbo.memorias_agentes
        SET situacao = N'archived',
            atualizado_em = CAST({sql_string(timestamp)} AS DATETIME2(0))
        WHERE id = CAST({sql_string(entry_id)} AS UNIQUEIDENTIFIER);

        IF @@ROWCOUNT = 0
        BEGIN
            THROW 51000, 'Memoria nao encontrada', 1;
        END;

        INSERT INTO dbo.eventos_memorias_agentes (memoria_id, acao, autor, observacao_evento, criado_em)
        VALUES (
            CAST({sql_string(entry_id)} AS UNIQUEIDENTIFIER),
            N'archived',
            {sql_string(actor)},
            N'archived via cli',
            CAST({sql_string(timestamp)} AS DATETIME2(0))
        );
        """
        self._execute_non_query(query)
        entry = self.get_entry(entry_id)
        if entry is None:
            raise RuntimeError("Memoria arquivada mas nao encontrada.")
        return entry

    def touch_entry(self, *, entry_id: str, actor: str) -> Entry:
        timestamp = now_iso()
        query = f"""
        SET NOCOUNT ON;
        UPDATE dbo.memorias_agentes
        SET ultimo_uso_em = CAST({sql_string(timestamp)} AS DATETIME2(0)),
            atualizado_em = CAST({sql_string(timestamp)} AS DATETIME2(0))
        WHERE id = CAST({sql_string(entry_id)} AS UNIQUEIDENTIFIER);

        IF @@ROWCOUNT = 0
        BEGIN
            THROW 51000, 'Memoria nao encontrada', 1;
        END;

        INSERT INTO dbo.eventos_memorias_agentes (memoria_id, acao, autor, observacao_evento, criado_em)
        VALUES (
            CAST({sql_string(entry_id)} AS UNIQUEIDENTIFIER),
            N'touched',
            {sql_string(actor)},
            N'reused in execution',
            CAST({sql_string(timestamp)} AS DATETIME2(0))
        );
        """
        self._execute_non_query(query)
        entry = self.get_entry(entry_id)
        if entry is None:
            raise RuntimeError("Memoria tocada mas nao encontrada.")
        return entry

    def stats(self) -> dict[str, object]:
        query = """
        SET NOCOUNT ON;
        SELECT (
            SELECT categoria AS category, situacao AS status, COUNT(*) AS total
            FROM dbo.memorias_agentes
            GROUP BY categoria, situacao
            ORDER BY categoria, situacao
            FOR JSON PATH
        ) AS entries_json,
        (
            SELECT acao AS action, COUNT(*) AS total
            FROM dbo.eventos_memorias_agentes
            GROUP BY acao
            ORDER BY acao
            FOR JSON PATH
        ) AS events_json
        FOR JSON PATH, WITHOUT_ARRAY_WRAPPER;
        """
        payload = self._execute_json_query(query) or {}
        entries_payload = payload.get("entries_json") or []
        events_payload = payload.get("events_json") or []
        entries = json.loads(entries_payload) if isinstance(entries_payload, str) else entries_payload
        events = json.loads(events_payload) if isinstance(events_payload, str) else events_payload
        return {"entries": entries, "events": events}

    def _entry_select_query(self, *, where_clause: str, top: int | None = None, as_array: bool = False) -> str:
        top_sql = f"TOP ({top}) " if top is not None else ""
        wrapper = "FOR JSON PATH" if as_array else "FOR JSON PATH, WITHOUT_ARRAY_WRAPPER"
        return f"""
        SET NOCOUNT ON;
        SELECT {top_sql}
            CAST(id AS NVARCHAR(36)) AS id,
            nome_agente AS agent_name,
            categoria AS category,
            escopo AS scope,
            titulo AS title,
            conteudo AS content,
            tags,
            tipo_origem AS source_kind,
            referencia_origem AS source_ref,
            situacao AS status,
            confianca AS confidence,
            CONVERT(VARCHAR(33), criado_em, 127) AS created_at,
            CONVERT(VARCHAR(33), atualizado_em, 127) AS updated_at,
            CONVERT(VARCHAR(33), ultimo_uso_em, 127) AS last_used_at
        FROM dbo.memorias_agentes
        {where_clause}
        ORDER BY
            CASE WHEN ultimo_uso_em IS NULL THEN 1 ELSE 0 END,
            ultimo_uso_em DESC,
            atualizado_em DESC
        {wrapper};
        """

    def _wait_until_ready(self) -> None:
        deadline = time.time() + 180
        last_error = "SQL Server ainda nao respondeu"
        while time.time() < deadline:
            try:
                self._execute_scalar("SELECT 1 AS ready", database="master")
                return
            except RuntimeError as exc:
                last_error = str(exc)
                time.sleep(3)
        raise RuntimeError(f"Timeout aguardando SQL Server local: {last_error}")

    def _ensure_database_exists(self) -> None:
        query = f"""
        IF DB_ID({sql_string(self.settings.database)}) IS NULL
        BEGIN
            DECLARE @sql NVARCHAR(MAX);
            SET @sql = N'CREATE DATABASE ' + QUOTENAME({sql_string(self.settings.database)});
            EXEC (@sql);
        END;
        """
        self._run_sqlcmd("-d", "master", "-Q", query, include_headers=False)

    def _run_sql_file(self, script_path: Path, *, database: str) -> None:
        script = script_path.read_text(encoding="utf-8")
        self._run_sqlcmd("-d", database, "-Q", script, include_headers=False)

    def _execute_scalar(self, query: str, *, database: str) -> str:
        output = self._run_sqlcmd("-d", database, "-Q", query, include_headers=False)
        for line in output.splitlines():
            value = line.strip()
            if value:
                return value
        raise RuntimeError("Consulta sem retorno.")

    def _execute_non_query(self, query: str) -> None:
        self._run_sqlcmd("-d", self.settings.database, "-Q", query, include_headers=False)

    def _execute_json_query(self, query: str) -> Any:
        raw_output = self._run_sqlcmd("-d", self.settings.database, "-Q", query, include_headers=False)
        payload_lines = [line.strip() for line in raw_output.splitlines() if line.strip()]
        if not payload_lines:
            return None
        payload_text = "".join(payload_lines)
        return json.loads(payload_text)

    def _run_sqlcmd(self, *sqlcmd_args: str, include_headers: bool = True) -> str:
        password = self.settings.load_password()
        command = [
            "docker",
            "exec",
            self.settings.container_name,
            "/bin/bash",
            "-lc",
            self._sqlcmd_shell_command(password, *sqlcmd_args, include_headers=include_headers),
        ]
        result = subprocess.run(
            command,
            cwd=ROOT_DIR,
            check=False,
            capture_output=True,
            text=True,
        )
        if result.returncode != 0:
            raise RuntimeError(result.stderr.strip() or result.stdout.strip() or "Falha ao executar sqlcmd.")
        return result.stdout

    def _sqlcmd_shell_command(self, password: str, *sqlcmd_args: str, include_headers: bool) -> str:
        sqlcmd_path_probe = (
            "if [ -x /opt/mssql-tools18/bin/sqlcmd ]; then SQLCMD=/opt/mssql-tools18/bin/sqlcmd; "
            "elif [ -x /opt/mssql-tools/bin/sqlcmd ]; then SQLCMD=/opt/mssql-tools/bin/sqlcmd; "
            "else echo 'sqlcmd nao encontrado dentro do container' >&2; exit 1; fi; "
        )
        header_flags = "" if include_headers else "-h -1 "
        args = " ".join(shell_quote(arg) for arg in sqlcmd_args)
        return (
            sqlcmd_path_probe
            + f"$SQLCMD -C -S {shell_quote(f'{self.settings.host},{self.settings.target_port}')} -U {shell_quote(self.settings.sa_user)} "
            + f"-P {shell_quote(password)} -w 65535 -y 8000 -Y 8000 {header_flags}{args}"
        )

    def _docker_compose(self, *args: str) -> None:
        result = subprocess.run(
            ["docker", "compose", "-f", str(self.settings.compose_file), *args],
            cwd=ROOT_DIR,
            check=False,
            capture_output=True,
            text=True,
        )
        if result.returncode != 0:
            raise RuntimeError(result.stderr.strip() or result.stdout.strip() or "Falha ao executar docker compose.")


def shell_quote(value: str) -> str:
    return "'" + value.replace("'", "'\"'\"'") + "'"


def build_repository(args: argparse.Namespace) -> Repository:
    settings = SqlServerSettings(
        compose_file=Path(args.sqlserver_compose_file),
        env_file=Path(args.sqlserver_env_file),
        service_name=args.sqlserver_service,
        container_name=args.sqlserver_container,
        database=args.sqlserver_database,
        port=args.sqlserver_port,
        sa_user=args.sqlserver_user,
        host=args.sqlserver_host,
        target_port=args.sqlserver_target_port,
    )
    return SqlServerKnowledgeRepository(settings)


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Memoria persistente para agentes.")
    parser.add_argument("--sqlserver-compose-file", default=str(SQLSERVER_COMPOSE_PATH))
    parser.add_argument("--sqlserver-env-file", default=str(SQLSERVER_ENV_PATH))
    parser.add_argument("--sqlserver-service", default=DEFAULT_SQLSERVER_SERVICE)
    parser.add_argument("--sqlserver-container", default=DEFAULT_SQLSERVER_CONTAINER)
    parser.add_argument("--sqlserver-database", default=DEFAULT_SQLSERVER_DATABASE)
    parser.add_argument("--sqlserver-port", type=int, default=DEFAULT_SQLSERVER_PORT)
    parser.add_argument("--sqlserver-user", default="sa")
    parser.add_argument("--sqlserver-host", default=DEFAULT_SQLSERVER_HOST)
    parser.add_argument("--sqlserver-target-port", type=int, default=1433)

    subparsers = parser.add_subparsers(dest="command", required=True)
    subparsers.add_parser("init", help="Inicializa o banco de memoria.")

    upsert_parser = subparsers.add_parser("upsert", help="Cria ou atualiza uma memoria.")
    add_shared_entry_arguments(upsert_parser)
    upsert_parser.add_argument("--source-kind", default="manual")
    upsert_parser.add_argument("--source-ref")
    upsert_parser.add_argument("--confidence", type=int, default=3)
    upsert_parser.add_argument("--actor", default="system")

    list_parser = subparsers.add_parser("list", help="Lista memorias.")
    add_query_arguments(list_parser)

    search_parser = subparsers.add_parser("search", help="Pesquisa memorias.")
    add_query_arguments(search_parser)

    show_parser = subparsers.add_parser("show", help="Exibe uma memoria.")
    show_parser.add_argument("--id", required=True)

    archive_parser = subparsers.add_parser("archive", help="Arquiva uma memoria.")
    archive_parser.add_argument("--id", required=True)
    archive_parser.add_argument("--actor", required=True)

    touch_parser = subparsers.add_parser("touch", help="Marca uma memoria como reutilizada.")
    touch_parser.add_argument("--id", required=True)
    touch_parser.add_argument("--actor", required=True)

    subparsers.add_parser("stats", help="Exibe estatisticas.")
    return parser


def add_shared_entry_arguments(parser: argparse.ArgumentParser) -> None:
    parser.add_argument("--agent", required=True)
    parser.add_argument("--category", required=True, choices=VALID_CATEGORIES)
    parser.add_argument("--scope", default="project", choices=VALID_SCOPES)
    parser.add_argument("--title", required=True)
    parser.add_argument("--content", required=True)
    parser.add_argument("--tags")


def add_query_arguments(parser: argparse.ArgumentParser) -> None:
    parser.add_argument("--agent")
    parser.add_argument("--category", choices=VALID_CATEGORIES)
    parser.add_argument("--query")
    parser.add_argument("--tag")
    parser.add_argument("--include-archived", action="store_true")
    parser.add_argument("--limit", type=int, default=10)
    parser.add_argument("--format", choices=("text", "json"), default="text")


def render_entry(entry: Entry) -> str:
    tags = ", ".join(parse_tags(entry.tags)) or "-"
    return "\n".join(
        [
            f"ID: {entry.id}",
            f"Agent: {entry.agent_name}",
            f"Category: {entry.category}",
            f"Scope: {entry.scope}",
            f"Title: {entry.title}",
            f"Tags: {tags}",
            f"Source: {entry.source_kind}{f' ({entry.source_ref})' if entry.source_ref else ''}",
            f"Status: {entry.status}",
            f"Confidence: {entry.confidence}",
            f"Created At: {entry.created_at}",
            f"Updated At: {entry.updated_at}",
            f"Last Used At: {entry.last_used_at or '-'}",
            "Content:",
            entry.content,
        ]
    )


def render_entries(entries: Iterable[Entry]) -> str:
    lines: list[str] = []
    for entry in entries:
        tags = ",".join(parse_tags(entry.tags)) or "-"
        lines.append(
            " | ".join(
                [
                    entry.id,
                    entry.agent_name,
                    entry.category,
                    entry.scope,
                    entry.status,
                    f"confidence={entry.confidence}",
                    f"tags={tags}",
                    entry.title,
                ]
            )
        )
    return "\n".join(lines) if lines else "Nenhuma memoria encontrada."


def print_json(payload: object) -> None:
    print(json.dumps(payload, ensure_ascii=True, indent=2))


def main(argv: list[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)
    repository = build_repository(args)
    try:
        if args.command == "init":
            repository.init_db()
            print(
                "Banco SQL Server inicializado em "
                f"{args.sqlserver_database} via {args.sqlserver_compose_file}"
            )
            return 0

        repository.init_db()

        if args.command == "upsert":
            action, entry = repository.upsert_entry(
                agent_name=args.agent,
                category=args.category,
                scope=args.scope,
                title=args.title,
                content=args.content,
                tags=args.tags,
                source_kind=args.source_kind,
                source_ref=args.source_ref,
                confidence=args.confidence,
                actor=args.actor,
            )
            print(f"Memoria {action}: {entry.id}")
            print(render_entry(entry))
            return 0

        if args.command in {"list", "search"}:
            if args.command == "search" and not args.query:
                parser.error("search exige --query")
            entries = repository.list_entries(
                agent_name=args.agent,
                category=args.category,
                query=args.query,
                tag=args.tag,
                include_archived=args.include_archived,
                limit=args.limit,
            )
            if args.format == "json":
                print_json([entry.to_dict() for entry in entries])
            else:
                print(render_entries(entries))
            return 0

        if args.command == "show":
            entry = repository.get_entry(args.id)
            if entry is None:
                raise KeyError(f"Memoria nao encontrada: {args.id}")
            print(render_entry(entry))
            return 0

        if args.command == "archive":
            entry = repository.archive_entry(entry_id=args.id, actor=args.actor)
            print(f"Memoria arquivada: {entry.id}")
            print(render_entry(entry))
            return 0

        if args.command == "touch":
            entry = repository.touch_entry(entry_id=args.id, actor=args.actor)
            print(f"Memoria reutilizada: {entry.id}")
            print(render_entry(entry))
            return 0

        if args.command == "stats":
            print_json(repository.stats())
            return 0
    except KeyError as exc:
        print(str(exc), file=sys.stderr)
        return 1
    except (RuntimeError, json.JSONDecodeError) as exc:
        print(str(exc), file=sys.stderr)
        return 1

    parser.error(f"Comando nao suportado: {args.command}")
    return 2


if __name__ == "__main__":
    raise SystemExit(main())
