# Docker Codex Team

Workspace de planejamento e coordenacao multi-agent para um projeto que ainda nao possui codigo de aplicacao.

## Estado Atual

- Codigo de aplicacao: ainda nao iniciado.
- Arquitetura final: pendente do escopo detalhado do problema.
- Backend definido como stack obrigatoria: Java 21 com Spring Boot 4 estavel.
- Banco de dados definido como stack obrigatoria: PostgreSQL.
- Frontend definido como stack obrigatoria: Angular em versao estavel e suportada.
- Documentacao de API obrigatoria: OpenAPI 3.1 com Swagger UI.
- Lombok permitido no backend para reduzir boilerplate tecnico.

## Principio de Trabalho

A equipe nao deve apenas escrever codigo. A equipe deve entender o problema, propor a melhor solucao possivel e construir um sistema seguro, robusto, escalavel, compreensivel por outros desenvolvedores e facil de manter.

## Proximo Passo

Enviar o escopo detalhado do problema que o projeto ira resolver. A partir desse escopo, o `architect` devera propor a arquitetura adequada, o `coordinator` devera validar a proposta e somente depois backend e frontend poderao ser implementados.

## Docker Agent

Este projeto usa o Docker Agent oficial. O arquivo `agents.yml` define o time multi-agent.

Modo de execucao escolhido:

- Docker Agent usa OpenAI como provider principal via `OPENAI_API_KEY`.
- O modelo configurado em `agents.yml` e `openai-main`, apontando para `gpt-5.4`.
- O `root` atua como `coordinator`.

Pre-requisitos:

```bash
docker agent version
```

Configurar a chave no PowerShell antes de executar o time:

```bash
$env:OPENAI_API_KEY="sua_chave_aqui"
```

Opcionalmente, persista a variavel no perfil ou no ambiente do sistema para nao precisar exportar a cada sessao.

Executar o time:

```bash
docker agent run agents.yml
```

O agente `root` atua como `coordinator` e pode delegar tarefas para:

- `architect`
- `backend_dev`
- `frontend_dev`
- `qa`

Executar com prompt inicial:

```bash
docker agent run agents.yml "Analise o estado atual do projeto e conduza o workflow multi-agent."
```

Executar com o escopo do sistema de auditoria de customizacoes:

```bash
docker agent run agents.yml "$(cat prompts/customization-audit-scope.md)"
```

Validar a configuracao sem iniciar uma sessao real:

```bash
docker agent debug config agents.yml
docker agent run agents.yml --dry-run
```

Validar toolsets:

```bash
docker agent debug toolsets agents.yml
```

Gerar uma imagem Docker do agente, quando sua versao do Docker Agent suportar o comando `build`:

```bash
docker agent build agents.yml team-agents:latest
```

Observacao: a CLI local validada neste workspace e `docker agent v1.57.0`; ela executa `run`, `debug config` e `debug toolsets`, mas nao expoe `build` nesta instalacao.

## Documentos

- `AGENTS.md`: regras globais da equipe.
- `agents.yml`: definicao dos agentes.
- `ARCHITECTURE.md`: diretrizes arquiteturais e processo de decisao.
- `DECISIONS.md`: decisoes tecnicas registradas.
- `TASKS.md`: organizacao das tarefas.
- `API_CONTRACT.md`: contratos de API, pendentes ate existir escopo e modelagem.
