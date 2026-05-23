# Docker Codex Team

Workspace de planejamento e coordenacao multi-agent para um projeto que ainda nao possui codigo de aplicacao.

## Estado Atual

- Codigo de aplicacao: ainda nao iniciado.
- Arquitetura final: pendente do escopo detalhado do problema.
- Backend definido como stack obrigatoria: Java 17 com Spring Boot estavel compativel.
- Banco de dados definido como stack obrigatoria: PostgreSQL.
- Frontend definido como stack obrigatoria: Angular em versao estavel e suportada.

## Principio de Trabalho

A equipe nao deve apenas escrever codigo. A equipe deve entender o problema, propor a melhor solucao possivel e construir um sistema seguro, robusto, escalavel, compreensivel por outros desenvolvedores e facil de manter.

## Proximo Passo

Enviar o escopo detalhado do problema que o projeto ira resolver. A partir desse escopo, o `architect` devera propor a arquitetura adequada, o `coordinator` devera validar a proposta e somente depois backend e frontend poderao ser implementados.

## Docker Agent

Este projeto usa o Docker Agent oficial. O arquivo `agents.yml` define o time multi-agent.

Modo de execução escolhido:

- Docker Agent usa Docker Model Runner local como provider principal.
- O modelo local configurado é `local-qwen`, apontando para `ai/qwen3:14B-Q6_K`.
- O `root` atua como `coordinator`.
- Quando necessário, o coordinator pode chamar o Codex CLI local via shell usando `codex exec`.
- O Codex CLI deve estar autenticado localmente, por exemplo via ChatGPT Plus no ambiente do usuário.

Pré-requisitos:

```bash
docker agent version
docker model version
codex --version
```

Habilitar Docker Model Runner:

```bash
docker desktop enable model-runner
```

Baixar o modelo local, se ainda não existir:

```bash
docker model pull ai/qwen3:14B-Q6_K
```

Executar o time:

```bash
docker agent run agents.yml
```

O agente `root` atua como `coordinator` e pode delegar tarefas para:

- `architect`
- `backend_dev`
- `frontend_dev`

Executar com prompt inicial:

```bash
docker agent run agents.yml "Analise o estado atual do projeto e conduza o workflow multi-agent."
```

Executar explicitamente o coordinator:

```bash
docker agent run agents.yml --agent coordinator
```

Validar a configuração sem iniciar uma sessão real:

```bash
docker agent debug config agents.yml
docker agent run agents.yml --dry-run
```

Validar toolsets:

```bash
docker agent debug toolsets agents.yml
```

Exemplo de chamada do Codex CLI pelo coordinator via shell:

```bash
codex exec --cd . --sandbox workspace-write --skip-git-repo-check "Revise as alterações atuais com foco em bugs, riscos, arquitetura e testes. Não altere arquivos."
```

Gerar uma imagem Docker do agente, quando sua versão do Docker Agent suportar o comando `build`:

```bash
docker agent build agents.yml docker-codex-team-agent:latest
```

Observação: a CLI local validada neste workspace é `docker agent v1.57.0`; ela executa `run`, `debug config` e `debug toolsets`, mas não expõe `build` nesta instalação.

## Documentos

- `AGENTS.md`: regras globais da equipe.
- `agents.yml`: definicao dos agentes.
- `ARCHITECTURE.md`: diretrizes arquiteturais e processo de decisao.
- `DECISIONS.md`: decisoes tecnicas registradas.
- `TASKS.md`: organizacao das tarefas.
- `API_CONTRACT.md`: contratos de API, pendentes ate existir escopo e modelagem.
