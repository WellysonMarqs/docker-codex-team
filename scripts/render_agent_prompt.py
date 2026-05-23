#!/usr/bin/env python3
from __future__ import annotations

import argparse
from pathlib import Path
from typing import Any

import yaml


ROOT = Path("/workspace")
FALLBACK_ROOT = Path.cwd()


def root() -> Path:
    return ROOT if ROOT.exists() else FALLBACK_ROOT


def read_text(path: Path) -> str:
    if not path.exists():
        return ""
    return path.read_text(encoding="utf-8").strip()


def load_agents(path: Path) -> dict[str, Any]:
    with path.open("r", encoding="utf-8") as file:
        data = yaml.safe_load(file) or {}
    if "agents" not in data:
        raise SystemExit("agents.yml invalido: chave 'agents' nao encontrada.")
    return data


def agent_model(agent: dict[str, Any]) -> str:
    model = str(agent.get("model", "")).strip()
    return model.removeprefix("openai/")


def render_prompt(agent_name: str, task: str | None) -> str:
    base = root()
    data = load_agents(base / "agents.yml")
    agents = data["agents"]
    orchestration = data.get("orchestration", {})

    if agent_name not in agents:
        available = ", ".join(sorted(agents))
        raise SystemExit(f"Agente '{agent_name}' nao encontrado. Disponiveis: {available}")

    agent = agents[agent_name]
    global_rules = read_text(base / "AGENTS.md")
    architecture = read_text(base / "ARCHITECTURE.md")
    decisions = read_text(base / "DECISIONS.md")
    tasks = read_text(base / "TASKS.md")
    api_contract = read_text(base / "API_CONTRACT.md")

    sub_agents = agent.get("sub_agents") or []
    sub_agents_text = "\n".join(f"- {item}" for item in sub_agents) if sub_agents else "- nenhum"

    runtime_rules = """
REGRAS DE EXECUCAO DO DOCKER AGENT:
- O coordinator e o ponto de entrada principal.
- architect, backend_dev e frontend_dev devem atuar como agentes especializados.
- Nao implemente codigo de aplicacao sem escopo detalhado, arquitetura aprovada e tarefa registrada.
- A stack obrigatoria e Java 17, Spring Boot estavel compativel, PostgreSQL e Angular.
- A arquitetura final deve ser proposta somente apos o problema estar detalhado.
- Sempre atualize a documentacao obrigatoria quando houver impacto.
- Nunca reverta alteracoes de outro agente sem instrucao explicita.
""".strip()

    prompt = f"""
# Docker Codex Team Agent

## Agent
Nome: {agent_name}
Modelo configurado: {agent.get("model", "default")}

## Description
{str(agent.get("description", "")).strip()}

## Instruction
{str(agent.get("instruction", "")).strip()}

## Sub-agents
{sub_agents_text}

## Orchestration
{yaml.safe_dump(orchestration, allow_unicode=True, sort_keys=False).strip()}

## Runtime Rules
{runtime_rules}

## Global Rules: AGENTS.md
{global_rules}

## Current Architecture Document
{architecture}

## Current Decisions Document
{decisions}

## Current Tasks Document
{tasks}

## Current API Contract Document
{api_contract}
""".strip()

    if task:
        prompt += f"\n\n## User Task\n{task.strip()}"
    else:
        prompt += "\n\n## User Task\nAguardar instrucao do usuario e atuar conforme o papel do agente."

    return prompt + "\n"


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("agent_name")
    parser.add_argument("task", nargs="*")
    parser.add_argument("--model-only", action="store_true")
    args = parser.parse_args()

    base = root()
    data = load_agents(base / "agents.yml")
    agent = data["agents"].get(args.agent_name)
    if agent is None:
        available = ", ".join(sorted(data["agents"]))
        raise SystemExit(f"Agente '{args.agent_name}' nao encontrado. Disponiveis: {available}")

    if args.model_only:
        print(agent_model(agent))
        return

    task = " ".join(args.task).strip() or None
    print(render_prompt(args.agent_name, task), end="")


if __name__ == "__main__":
    main()
