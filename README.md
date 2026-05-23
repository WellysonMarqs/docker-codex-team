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

O runtime Docker usa `agents.yml` como fonte dos papeis e inicia o `coordinator` como orquestrador principal.

Build da imagem:

```bash
docker compose build coordinator
```

Executar o coordinator:

```bash
docker compose run --rm coordinator "Analise o escopo do projeto e coordene os agentes conforme o workflow."
```

Validar sem consumir uma execucao real do Codex:

```bash
docker compose run --rm -e DRY_RUN=1 coordinator "Teste de roteamento"
```

Executar um agente especializado manualmente, apenas quando o coordinator delegar:

```bash
docker compose --profile agents run --rm architect "Analise o problema e proponha alternativas arquiteturais."
docker compose --profile agents run --rm backend_dev "Execute a tarefa backend aprovada."
docker compose --profile agents run --rm frontend_dev "Execute a tarefa frontend aprovada."
```

O diretorio atual e montado em `/workspace`, e o `CODEX_HOME` local e montado em `/codex-home` para reutilizar autenticacao e configuracao do Codex.

## Documentos

- `AGENTS.md`: regras globais da equipe.
- `agents.yml`: definicao dos agentes.
- `ARCHITECTURE.md`: diretrizes arquiteturais e processo de decisao.
- `DECISIONS.md`: decisoes tecnicas registradas.
- `TASKS.md`: organizacao das tarefas.
- `API_CONTRACT.md`: contratos de API, pendentes ate existir escopo e modelagem.
