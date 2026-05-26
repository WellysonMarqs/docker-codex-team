# Tarefas do Projeto

## Estado Atual

Workspace analisado em 2026-05-26.

Arquivos encontrados:

- `AGENTS.md`
- `agents.yml`
- `README.md`
- `ARCHITECTURE.md`
- `DECISIONS.md`
- `TASKS.md`
- `API_CONTRACT.md`

Nao ha codigo de aplicacao, manifesto de dependencias, infraestrutura executavel ou suites de teste implementadas neste workspace.

## Premissas Atualizadas

- Arquitetura final: somente apos envio e analise do escopo detalhado do problema.
- Stack tecnica final: somente apos analise arquitetural e validacao do coordinator.
- Contratos e integracoes: somente apos modelagem inicial do dominio.
- O objetivo da equipe e solucionar problemas com engenharia, nao apenas escrever codigo.

## Workflow Multi-Agent

### Etapa 0: Planejamento inicial

Status: Concluida.

- [x] Ler `AGENTS.md`.
- [x] Ler `agents.yml`.
- [x] Analisar workspace.
- [x] Gerar planejamento inicial.
- [x] Registrar que a stack tecnica final depende da analise do problema.
- [x] Registrar que a arquitetura final depende do escopo detalhado do problema.
- [x] Configurar Docker Agent oficial a partir de `agents.yml`.
- [x] Definir `coordinator` como orquestrador principal.
- [x] Validar `docker agent debug config agents.yml`.
- [x] Validar toolsets com `docker agent debug toolsets agents.yml`.
- [ ] Validar build de imagem quando a CLI local expuser `docker agent build`.
- [x] Configurar OpenAI como provider principal via `OPENAI_API_KEY`.
- [ ] Validar `OPENAI_API_KEY` disponivel no ambiente local.
- [ ] Validar `docker agent run agents.yml --dry-run` com provider OpenAI.
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
- [ ] Definir estrategia de persistencia.
- [ ] Definir estrategia de migracoes.
- [ ] Atualizar `API_CONTRACT.md`.
- [ ] Atualizar `ARCHITECTURE.md`.
- [ ] Atualizar `DECISIONS.md`.

Gate de saida:

- [ ] Contratos aprovados pelo `coordinator`.
- [ ] Backend e frontend possuem interfaces claras.
- [ ] Testes de contrato planejados.

### Etapa 4: Setup do projeto

Responsaveis: `backend_dev`, `frontend_dev` e `qa`, com validacao do `coordinator`.

Status: Bloqueada por Etapa 3.

- [ ] Definir organizacao final do repositorio conforme arquitetura aprovada.
- [ ] Criar backend na stack aprovada.
- [ ] Configurar ferramenta de build backend aprovada.
- [ ] Configurar persistencia local conforme arquitetura aprovada.
- [ ] Configurar estrategia de migracoes conforme arquitetura aprovada.
- [ ] Criar frontend na stack aprovada.
- [ ] Preparar pontos de observabilidade e testabilidade necessarios para o qa.
- [ ] Configurar lint backend com apoio do qa.
- [ ] Configurar lint frontend com apoio do qa.
- [ ] Configurar build backend com apoio do qa.
- [ ] Configurar build frontend com apoio do qa.
- [ ] Atualizar `README.md`.

Gate de saida:

- [ ] Backend compila.
- [ ] Frontend compila.
- [ ] Lint executa.
- [ ] Base minima de testabilidade preparada para o qa.
- [ ] Documentacao atualizada.

### Etapa 5: Backend

Responsavel principal: `backend_dev`.

Status: Bloqueada por Etapa 4.

- [ ] Implementar estrutura backend aprovada.
- [ ] Implementar configuracao por ambiente.
- [ ] Implementar validacao de entrada.
- [ ] Implementar tratamento padronizado de erros.
- [ ] Implementar persistencia aprovada.
- [ ] Implementar migracoes.
- [ ] Implementar seguranca conforme escopo.
- [ ] Implementar logs estruturados.
- [ ] Implementar health checks.
- [ ] Garantir testabilidade da camada backend para o qa.
- [ ] Atualizar `API_CONTRACT.md`.
- [ ] Atualizar `README.md`.

Gate de saida:

- [ ] Build backend passa.
- [ ] Lint backend passa.
- [ ] Testes unitarios backend passam.
- [ ] Testes de integracao backend passam.
- [ ] Contratos passam.
- [ ] Camada backend entregue com pontos de testabilidade e observabilidade necessarios para o qa.
- [ ] Dependencias de teste do backend documentadas para o qa.

### Etapa 6: Frontend

Responsavel principal: `frontend_dev`.

Status: Bloqueada por Etapa 4.

- [ ] Implementar estrutura frontend aprovada.
- [ ] Implementar rotas.
- [ ] Implementar componentes base.
- [ ] Implementar formularios reativos.
- [ ] Implementar services de integracao HTTP.
- [ ] Implementar interceptors quando necessario.
- [ ] Implementar guards quando necessario.
- [ ] Implementar estados loading, empty, error e success.
- [ ] Implementar acessibilidade.
- [ ] Implementar responsividade.
- [ ] Garantir testabilidade da camada frontend para o qa.
- [ ] Atualizar `README.md`.

Gate de saida:

- [ ] Build frontend passa.
- [ ] Lint frontend passa.
- [ ] Camada frontend entregue com pontos de testabilidade e observabilidade necessarios para o qa.
- [ ] Dependencias de teste do frontend documentadas para o qa.

### Etapa 6A: QA e Validacao

Responsavel principal: `qa`.

Status: Bloqueada por Etapas 4, 5 e 6.

- [ ] Definir estrategia de testes por fluxo critico.
- [ ] Definir quality gates minimos por tipo de entrega.
- [ ] Definir criterio de parecer final: aprovado, aprovado com ressalvas ou bloqueado.
- [ ] Implementar suites de teste e automacoes necessarias.
- [ ] Configurar infraestrutura de testes e dados de apoio.
- [ ] Preparar matriz resumida por fluxo ou requisito x tipo de teste x status.
- [ ] Validar cobertura minima de backend.
- [ ] Validar cobertura minima de frontend.
- [ ] Executar lint.
- [ ] Executar build.
- [ ] Executar testes unitarios.
- [ ] Executar testes de integracao.
- [ ] Executar testes de contrato quando aplicavel.
- [ ] Executar E2E nos fluxos criticos quando aplicavel.
- [ ] Executar validacao automatizada de acessibilidade quando aplicavel.
- [ ] Executar smoke de seguranca basica quando aplicavel.
- [ ] Executar testes exploratorios guiados por risco quando aplicavel.
- [ ] Registrar bugs, riscos e lacunas de cobertura.
- [ ] Registrar evidencias objetivas de execucao.
- [ ] Aprovar ou reprovar quality gates.

Gate de saida:

- [ ] Evidencias de validacao registradas.
- [ ] Falhas criticas tratadas ou bloqueios formalizados.
- [ ] Parecer final emitido com status objetivo.
- [ ] Quality gates aprovados para integracao ou bloqueio formalizado.
- [ ] Fluxos criticos E2E passam.

### Etapa 7: CI/CD

Responsaveis principais: `coordinator` e `qa`.

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

Responsaveis: todos os agentes, com `qa` como responsavel pela frente de testes.

Status: Bloqueada por Etapas 5, 6 e 7.

- [ ] Revisar seguranca.
- [ ] Revisar autorizacao.
- [ ] Revisar tratamento de erros.
- [ ] Revisar performance backend.
- [ ] Revisar performance frontend.
- [ ] Revisar estrategia de indices e consultas da persistencia aprovada.
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

- [ ] Escopo detalhado recebido e validado.
- [ ] Stack backend aprovada quando houver arquitetura.
- [ ] Stack frontend aprovada quando houver arquitetura.
- [ ] Estrategia de persistencia aprovada quando houver arquitetura.
- [ ] Estrategia de contratos aprovada quando houver arquitetura.
- [ ] Arquitetura final mantida como pendente ate o escopo.
- [x] Docker Agent oficial validado localmente para configuracao e toolsets.
- [ ] `OPENAI_API_KEY` configurada no ambiente local.

### TASK-001C: Configurar sub-agent de QA

Responsavel: `coordinator`.

Status: Pendente.

Criterios de aceite:

- [x] Agente `qa` definido em `agents.yml`.
- [x] `root` pode delegar tarefas para `qa`.
- [x] `docker agent debug toolsets agents.yml` confirma toolsets do `qa`.
- [x] Workflow e documentacao atualizados para incluir `qa`.

### TASK-001A: Validar Docker Agent oficial

Responsavel: `coordinator`.

Status: Pendente.

Criterios de aceite:

- [x] `docker agent debug config agents.yml` executa com sucesso.
- [ ] `docker agent run agents.yml --dry-run` executa com sucesso com `OPENAI_API_KEY` configurada.
- [ ] `docker agent run agents.yml` inicia o root/coordinator.
- [x] `docker agent debug toolsets agents.yml` lista ferramentas dos agentes.
- [ ] `docker agent build agents.yml backoffice-team-agents:latest` gera a imagem do agente quando disponivel na CLI local.
- [x] Todos os agentes usam `agents.yml` como fonte de instrucao.
- [x] `agents.yml` usa OpenAI como provider principal.

Observacao:
A CLI local validada e `docker agent v1.57.0`. Ela suporta `run`, `debug config` e `debug toolsets`, mas nao expoe `build` nesta instalacao, apesar de a documentacao oficial atual listar esse comando. O `run --dry-run` agora depende de `OPENAI_API_KEY` valido no ambiente.

### TASK-001B: Configurar provider OpenAI no ambiente local

Responsavel: usuario e `coordinator`.

Status: Pendente.

Criterios de aceite:

- [ ] `OPENAI_API_KEY` configurada no shell ou no ambiente do usuario.
- [ ] `docker agent run agents.yml --dry-run` executa com sucesso.
- [ ] `docker agent run agents.yml` inicia usando OpenAI como provider principal.

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

- [ ] Backend na stack aprovada criado.
- [ ] Frontend na stack aprovada criado.
- [ ] Persistencia local aprovada configurada.
- [ ] Comandos de build, lint e testes disponiveis.
- [ ] Documentacao atualizada.

### TASK-006: Estruturar estrategia de QA

Responsavel: `qa`.

Status: Bloqueada por TASK-005.

Criterios de aceite:

- [ ] Matriz de testes definida por fluxo critico.
- [ ] Quality gates definidos por tipo de entrega.
- [ ] Estrategia de evidencias de execucao definida.
- [ ] Modelo de parecer final definido.
- [ ] Comportamento sem ambiente ou sem codigo definido.
- [ ] Responsabilidades entre implementacao e validacao documentadas.

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
