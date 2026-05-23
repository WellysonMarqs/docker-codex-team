# Tarefas do Projeto

## Estado Atual

Workspace analisado em 2026-05-23.

Arquivos encontrados:

- `AGENTS.md`
- `agents.yml`
- `README.md`
- `ARCHITECTURE.md`
- `DECISIONS.md`
- `TASKS.md`
- `API_CONTRACT.md`

Nao ha codigo de aplicacao, manifesto de dependencias, infraestrutura, testes ou repositorio Git inicializado neste workspace.

## Premissas Atualizadas

- Backend obrigatorio: Java 17 com Spring Boot estavel compativel.
- Banco de dados obrigatorio: PostgreSQL.
- Frontend obrigatorio: Angular em versao estavel e suportada.
- Arquitetura final: somente apos envio e analise do escopo detalhado do problema.
- O objetivo da equipe e solucionar problemas com engenharia, nao apenas escrever codigo.

## Workflow Multi-Agent

### Etapa 0: Planejamento inicial

Status: Concluida.

- [x] Ler `AGENTS.md`.
- [x] Ler `agents.yml`.
- [x] Analisar workspace.
- [x] Gerar planejamento inicial.
- [x] Ajustar stack obrigatoria para Java 17, Spring Boot, PostgreSQL e Angular.
- [x] Registrar que a arquitetura final depende do escopo detalhado do problema.
- [x] Configurar Docker Agent oficial a partir de `agents.yml`.
- [x] Definir `coordinator` como orquestrador principal.
- [x] Validar `docker agent debug config agents.yml`.
- [x] Validar toolsets com `docker agent debug toolsets agents.yml`.
- [ ] Validar build de imagem quando a CLI local expuser `docker agent build`.
- [x] Configurar Docker Model Runner local como provider principal.
- [x] Configurar Codex CLI como ferramenta auxiliar via shell.
- [ ] Validar Docker Model Runner habilitado localmente.
- [ ] Validar modelo `ai/qwen3:14B-Q6_K` disponivel localmente.
- [ ] Validar `docker agent run agents.yml --dry-run` apos habilitar Docker Model Runner.
- [x] Atualizar `ARCHITECTURE.md`.
- [x] Atualizar `DECISIONS.md`.
- [x] Atualizar `TASKS.md`.

### Etapa 1: Receber escopo detalhado do problema

Responsavel principal: usuario.

Status: Pendente.

O escopo deve explicar detalhadamente:

- qual problema o projeto ira resolver;
- quem sofre esse problema;
- quais usuarios ou perfis usarao o sistema;
- quais objetivos de negocio precisam ser atendidos;
- quais processos atuais existem;
- quais dores ou ineficiencias existem hoje;
- quais fluxos principais o sistema deve suportar;
- quais regras de negocio sao obrigatorias;
- quais dados precisam ser armazenados;
- quais integracoes externas sao necessarias;
- quais requisitos de seguranca existem;
- qual volume esperado de usuarios, dados e transacoes;
- quais criterios definem sucesso.

Gate de saida:

- [ ] Problema compreendido.
- [ ] Usuarios e fluxos identificados.
- [ ] Requisitos funcionais e nao funcionais documentados.
- [ ] Restricoes conhecidas.

### Etapa 2: Analise arquitetural do problema

Responsavel principal: `architect`.

Status: Bloqueada por Etapa 1.

- [ ] Analisar o problema.
- [ ] Identificar dominio e subdominios.
- [ ] Identificar bounded contexts candidatos.
- [ ] Identificar riscos de seguranca.
- [ ] Identificar riscos de escala.
- [ ] Identificar requisitos de consistencia e transacao.
- [ ] Identificar necessidades de auditoria.
- [ ] Identificar integracoes externas.
- [ ] Comparar alternativas arquiteturais.
- [ ] Propor arquitetura adequada ao problema.
- [ ] Explicar beneficios e trade-offs.
- [ ] Atualizar `ARCHITECTURE.md`.
- [ ] Atualizar `DECISIONS.md`.

Gate de saida:

- [ ] `coordinator` aprova a arquitetura proposta.
- [ ] Alternativas descartadas documentadas.
- [ ] Riscos e mitigacoes documentados.

### Etapa 3: Contratos e modelagem

Responsavel principal: `architect`.

Status: Bloqueada por Etapa 2.

- [ ] Definir modelo de dominio inicial.
- [ ] Definir casos de uso.
- [ ] Definir entidades, value objects e agregados quando aplicavel.
- [ ] Definir contratos REST se houver API HTTP.
- [ ] Definir DTOs de request e response.
- [ ] Definir modelo padrao de erro.
- [ ] Definir autenticacao e autorizacao.
- [ ] Definir estrategia de persistencia PostgreSQL.
- [ ] Definir estrategia de migracoes.
- [ ] Atualizar `API_CONTRACT.md`.
- [ ] Atualizar `ARCHITECTURE.md`.
- [ ] Atualizar `DECISIONS.md`.

Gate de saida:

- [ ] Contratos aprovados pelo `coordinator`.
- [ ] Backend e frontend possuem interfaces claras.
- [ ] Testes de contrato planejados.

### Etapa 4: Setup do projeto

Responsaveis: `backend_dev`, `frontend_dev`, validacao do `coordinator`.

Status: Bloqueada por Etapa 3.

- [ ] Definir organizacao final do repositorio conforme arquitetura aprovada.
- [ ] Criar backend Spring Boot com Java 17.
- [ ] Configurar Maven ou Gradle.
- [ ] Configurar PostgreSQL local com Docker Compose.
- [ ] Configurar migracoes com Flyway ou Liquibase.
- [ ] Criar frontend Angular.
- [ ] Configurar lint backend.
- [ ] Configurar lint frontend.
- [ ] Configurar build backend.
- [ ] Configurar build frontend.
- [ ] Configurar testes backend.
- [ ] Configurar testes frontend.
- [ ] Atualizar `README.md`.

Gate de saida:

- [ ] Backend compila.
- [ ] Frontend compila.
- [ ] Lint executa.
- [ ] Testes base executam.
- [ ] Documentacao atualizada.

### Etapa 5: Backend

Responsavel principal: `backend_dev`.

Status: Bloqueada por Etapa 4.

- [ ] Implementar estrutura backend aprovada.
- [ ] Implementar configuracao por ambiente.
- [ ] Implementar validacao de entrada.
- [ ] Implementar tratamento padronizado de erros.
- [ ] Implementar persistencia PostgreSQL.
- [ ] Implementar migracoes.
- [ ] Implementar seguranca conforme escopo.
- [ ] Implementar logs estruturados.
- [ ] Implementar health checks.
- [ ] Implementar testes unitarios.
- [ ] Implementar testes de integracao com Testcontainers.
- [ ] Atualizar `API_CONTRACT.md`.
- [ ] Atualizar `README.md`.

Gate de saida:

- [ ] Build backend passa.
- [ ] Lint backend passa.
- [ ] Testes unitarios backend passam.
- [ ] Testes de integracao backend passam.
- [ ] Contratos passam.

### Etapa 6: Frontend

Responsavel principal: `frontend_dev`.

Status: Bloqueada por Etapa 4.

- [ ] Implementar estrutura Angular aprovada.
- [ ] Implementar rotas.
- [ ] Implementar componentes base.
- [ ] Implementar formularios reativos.
- [ ] Implementar services de integracao HTTP.
- [ ] Implementar interceptors quando necessario.
- [ ] Implementar guards quando necessario.
- [ ] Implementar estados loading, empty, error e success.
- [ ] Implementar acessibilidade.
- [ ] Implementar responsividade.
- [ ] Implementar testes unitarios.
- [ ] Implementar testes E2E com Playwright.
- [ ] Atualizar `README.md`.

Gate de saida:

- [ ] Build frontend passa.
- [ ] Lint frontend passa.
- [ ] Testes unitarios frontend passam.
- [ ] Fluxos criticos E2E passam.
- [ ] Validacao de acessibilidade realizada.

### Etapa 7: CI/CD

Responsavel principal: `coordinator`.

Status: Bloqueada por Etapa 4.

- [ ] Configurar pipeline de CI.
- [ ] Configurar cache de dependencias Java.
- [ ] Configurar cache de dependencias Node.
- [ ] Executar lint backend no CI.
- [ ] Executar lint frontend no CI.
- [ ] Executar build backend no CI.
- [ ] Executar build frontend no CI.
- [ ] Executar testes unitarios no CI.
- [ ] Executar testes de integracao no CI.
- [ ] Executar testes de contrato no CI.
- [ ] Executar E2E quando aplicavel.
- [ ] Executar scan de dependencias.
- [ ] Documentar pipeline em `README.md`.

Gate de saida:

- [ ] Pull requests bloqueados por quality gates.
- [ ] Pipeline documentado.
- [ ] Falhas sao visiveis e acionaveis.

### Etapa 8: Hardening

Responsaveis: todos os agentes.

Status: Bloqueada por Etapas 5, 6 e 7.

- [ ] Revisar seguranca.
- [ ] Revisar autorizacao.
- [ ] Revisar tratamento de erros.
- [ ] Revisar performance backend.
- [ ] Revisar performance frontend.
- [ ] Revisar indices PostgreSQL.
- [ ] Revisar acessibilidade.
- [ ] Revisar observabilidade.
- [ ] Revisar cobertura de testes.
- [ ] Revisar documentacao obrigatoria.

Gate de saida:

- [ ] `coordinator` aprova qualidade final.
- [ ] Todos os documentos obrigatorios atualizados.

## Backlog Inicial

### TASK-001: Validar premissas tecnicas

Responsavel: `coordinator`.

Status: Pendente.

Criterios de aceite:

- [ ] Java 17 confirmado.
- [ ] Spring Boot estavel confirmado.
- [ ] PostgreSQL confirmado.
- [ ] Angular estavel confirmado.
- [ ] Arquitetura final mantida como pendente ate o escopo.
- [x] Docker Agent oficial validado localmente para configuracao e toolsets.
- [ ] Docker Model Runner habilitado.
- [ ] Modelo local `ai/qwen3:14B-Q6_K` disponivel.
- [x] Codex CLI instalado localmente.

### TASK-001A: Validar Docker Agent oficial

Responsavel: `coordinator`.

Status: Pendente.

Criterios de aceite:

- [x] `docker agent debug config agents.yml` executa com sucesso.
- [ ] `docker agent run agents.yml --dry-run` executa com sucesso apos Docker Model Runner estar rodando.
- [ ] `docker agent run agents.yml` inicia o root/coordinator.
- [ ] `docker agent run agents.yml --agent coordinator --dry-run` inicia o coordinator explicito em dry-run apos Docker Model Runner estar rodando.
- [x] `docker agent debug toolsets agents.yml` lista ferramentas dos agentes.
- [ ] `docker agent build agents.yml docker-codex-team-agent:latest` gera a imagem do agente quando disponivel na CLI local.
- [x] Todos os agentes usam `agents.yml` como fonte de instrucao.
- [x] `agents.yml` usa Docker Model Runner local como provider principal.
- [x] `agents.yml` orienta o coordinator a chamar Codex CLI via shell quando necessario.

Observacao:
A CLI local validada e `docker agent v1.57.0`. Ela suporta `run`, `debug config` e `debug toolsets`, mas nao expoe `build` nesta instalacao, apesar de a documentacao oficial atual listar esse comando. O `run --dry-run` com `local-qwen` tenta acessar o Docker Model Runner; neste ambiente ele falha enquanto o Model Runner nao estiver habilitado.

### TASK-001B: Habilitar Docker Model Runner local

Responsavel: usuario e `coordinator`.

Status: Pendente.

Criterios de aceite:

- [ ] `docker desktop enable model-runner` executado no ambiente do usuario.
- [ ] `docker model version` mostra server acessivel.
- [ ] `docker model pull ai/qwen3:14B-Q6_K` executa com sucesso.
- [ ] `docker model list` mostra `ai/qwen3:14B-Q6_K`.
- [ ] `docker agent run agents.yml --dry-run` executa sem `OPENAI_API_KEY`.
- [ ] `docker agent run agents.yml` inicia usando Docker Model Runner local.

### TASK-002: Receber e documentar escopo detalhado

Responsavel: usuario e `architect`.

Status: Pendente.

Criterios de aceite:

- [ ] Problema documentado.
- [ ] Usuarios documentados.
- [ ] Fluxos documentados.
- [ ] Regras documentadas.
- [ ] Requisitos nao funcionais documentados.

### TASK-003: Propor arquitetura orientada ao problema

Responsavel: `architect`.

Status: Bloqueada por TASK-002.

Criterios de aceite:

- [ ] Alternativas comparadas.
- [ ] Arquitetura recomendada.
- [ ] Trade-offs explicados.
- [ ] Riscos e mitigacoes registrados.
- [ ] `coordinator` aprova.

### TASK-004: Criar contratos iniciais de API

Responsavel: `architect`.

Status: Bloqueada por TASK-003.

Criterios de aceite:

- [ ] Endpoints definidos quando necessarios.
- [ ] DTOs definidos.
- [ ] Modelo de erros definido.
- [ ] Autenticacao e autorizacao documentadas.
- [ ] Exemplos incluidos.

### TASK-005: Preparar scaffold tecnico

Responsaveis: `backend_dev` e `frontend_dev`.

Status: Bloqueada por TASK-004.

Criterios de aceite:

- [ ] Backend Spring Boot criado.
- [ ] Frontend Angular criado.
- [ ] PostgreSQL local configurado.
- [ ] Comandos de build, lint e testes disponiveis.
- [ ] Documentacao atualizada.

## Modelo de Tarefa

```text
ID:
Titulo:
Responsavel:
Status:
Contexto:
Problema que resolve:
Escopo:
Fora de escopo:
Criterios de aceite:
Impacto em arquitetura:
Impacto em contratos:
Testes obrigatorios:
Documentacao:
```

## Regras de Atualizacao

- Atualizar este arquivo sempre que uma tarefa for criada, iniciada, bloqueada ou concluida.
- Nenhuma implementacao deve iniciar sem escopo, arquitetura aprovada e tarefa rastreavel.
- Nenhuma arquitetura deve ser definida como final antes da analise do problema.
- Nenhuma tarefa com codigo deve ser concluida sem lint, build, testes e documentacao.
- Alteracoes de arquitetura devem gerar entrada em `DECISIONS.md`.
- Alteracoes de API devem atualizar `API_CONTRACT.md`.
