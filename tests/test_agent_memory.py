from __future__ import annotations

import json
import os
import subprocess
import sys
import unittest
import uuid
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parents[1]
SCRIPT_PATH = ROOT_DIR / "tools" / "agent_memory.py"
SQLSERVER_HOST = os.environ.get("AGENT_MEMORY_SQLSERVER_HOST", "host.docker.internal")
SQLSERVER_PORT = os.environ.get("AGENT_MEMORY_SQLSERVER_PORT", "1433")


class AgentMemoryCliTest(unittest.TestCase):
    def setUp(self) -> None:
        self.database_name = f"MemoriaAgentesTeste_{uuid.uuid4().hex[:8]}"
        self.run_cli("init")

    def run_cli(self, *args: str) -> subprocess.CompletedProcess[str]:
        command = [
            sys.executable,
            str(SCRIPT_PATH),
            "--sqlserver-host",
            SQLSERVER_HOST,
            "--sqlserver-target-port",
            SQLSERVER_PORT,
            "--sqlserver-database",
            self.database_name,
            *args,
        ]
        return subprocess.run(
            command,
            cwd=ROOT_DIR,
            check=False,
            capture_output=True,
            text=True,
        )

    def test_init_creates_database(self) -> None:
        result = self.run_cli("stats")
        self.assertEqual(result.returncode, 0, result.stderr)
        payload = json.loads(result.stdout)
        self.assertEqual(payload["entries"], [])
        self.assertEqual(payload["events"], [])

    def test_upsert_updates_existing_entry(self) -> None:
        create = self.run_cli(
            "upsert",
            "--agent",
            "root",
            "--category",
            "rule",
            "--title",
            "Atualizar docs obrigatorias",
            "--content",
            "Primeira versao",
            "--actor",
            "root",
        )
        self.assertEqual(create.returncode, 0, create.stderr)
        created_id = self.extract_id(create.stdout)

        update = self.run_cli(
            "upsert",
            "--agent",
            "root",
            "--category",
            "rule",
            "--title",
            "Atualizar docs obrigatorias",
            "--content",
            "Versao consolidada",
            "--tags",
            "docs,governanca",
            "--actor",
            "root",
        )
        self.assertEqual(update.returncode, 0, update.stderr)
        updated_id = self.extract_id(update.stdout)
        self.assertEqual(created_id, updated_id)

        listing = self.run_cli("list", "--agent", "root", "--format", "json")
        self.assertEqual(listing.returncode, 0, listing.stderr)
        payload = json.loads(listing.stdout)
        self.assertEqual(len(payload), 1)
        self.assertEqual(payload[0]["content"], "Versao consolidada")
        self.assertEqual(payload[0]["tags"], ["docs", "governanca"])

    def test_search_filters_and_archive_hides_entry_by_default(self) -> None:
        self.run_cli(
            "upsert",
            "--agent",
            "shared",
            "--category",
            "lesson",
            "--title",
            "Nao repetir falha de contratos",
            "--content",
            "Sempre revisar API_CONTRACT antes de implementar integracao.",
            "--tags",
            "contrato,qa",
            "--actor",
            "qa",
        )
        create = self.run_cli(
            "upsert",
            "--agent",
            "shared",
            "--category",
            "improvement",
            "--title",
            "Consultar memoria antes de codificar",
            "--content",
            "Pesquisar contexto recorrente antes de mudar a arquitetura.",
            "--tags",
            "workflow,contexto",
            "--actor",
            "architect",
        )
        entry_id = self.extract_id(create.stdout)

        search = self.run_cli(
            "search",
            "--agent",
            "shared",
            "--query",
            "arquitetura",
            "--format",
            "json",
        )
        self.assertEqual(search.returncode, 0, search.stderr)
        payload = json.loads(search.stdout)
        self.assertEqual(len(payload), 1)
        self.assertEqual(payload[0]["id"], entry_id)

        archive = self.run_cli("archive", "--id", entry_id, "--actor", "root")
        self.assertEqual(archive.returncode, 0, archive.stderr)

        active_list = self.run_cli("list", "--agent", "shared", "--format", "json")
        self.assertEqual(active_list.returncode, 0, active_list.stderr)
        active_payload = json.loads(active_list.stdout)
        self.assertEqual(len(active_payload), 1)
        self.assertEqual(active_payload[0]["category"], "lesson")

        full_list = self.run_cli(
            "list", "--agent", "shared", "--include-archived", "--format", "json"
        )
        self.assertEqual(full_list.returncode, 0, full_list.stderr)
        full_payload = json.loads(full_list.stdout)
        self.assertEqual(len(full_payload), 2)

    @staticmethod
    def extract_id(output: str) -> str:
        for line in output.splitlines():
            if line.startswith("Memoria ") and ": " in line:
                return line.split(": ", 1)[1].strip()
        raise AssertionError(f"ID nao encontrado na saida: {output}")


if __name__ == "__main__":
    unittest.main()
