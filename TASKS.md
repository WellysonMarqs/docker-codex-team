# Tarefas do Projeto

## Estado Atual

Workspace analisado em 2026-05-23.
Escopo do Sistema de Controle e Auditoria de Customizacoes recebido em 2026-05-24.

Arquivos encontrados:

- `AGENTS.md`
- `agents.yml`
- `README.md`
- `ARCHITECTURE.md`
- `DECISIONS.md`
- `TASKS.md`
- `API_CONTRACT.md`

Ha scaffold inicial de aplicacao em `apps/backend` e `apps/frontend`, com PostgreSQL no Docker Compose. A implementacao esta autorizada para evolucao incremental dentro da arquitetura aprovada. Clientes, ambientes, customizacoes e versoes oficiais ja possuem fluxo vertical inicial. A vertical de verificacao manual auditavel ja existe no backend e segue em andamento para fechamento ponta a ponta.

## Premissas Atualizadas

- Backend obrigatorio: Java 17 com Spring Boot estavel compativel.
- Banco de dados obrigatorio: PostgreSQL.
- Frontend obrigatorio: Angular em versao estavel e suportada.
- Arquitetura final: evolutiva; a arquitetura candidata foi aprovada para inicio de desenvolvimento controlado.
- O objetivo da equipe e solucionar problemas com engenharia, nao apenas escrever codigo.
- Sistema alvo: controle e auditoria de customizacoes do legado, incluindo clientes SaaS e on-premise, hashes oficiais, verificacoes contra MySQL, divergencias e notificacao REST ao legado.

## Workflow Multi-Agent

### Etapa 0: Planejamento inicial

Status: Concluida.

- [x] Ler `AGENTS.md`.
- [x] Ler `agents.yml`.
- [x] Analisar workspace.
- [x] Gerar planejamento inicial.
- [x] Ajustar stack obrigatoria para Java 17, Spring Boot, PostgreSQL e Angular.
- [x] Registrar que a arquitetura final depende do escopo detalhado do problema.
- [x] Configurar Docker agent a partir de `agents.yml`.
- [x] Definir `coordinator` como orquestrador principal.
- [x] Validar build da imagem Docker.
- [x] Validar `coordinator` via Docker em modo `DRY_RUN`.
- [x] Validar `architect` via Docker em modo `DRY_RUN`.
- [x] Validar `backend_dev` via Docker em modo `DRY_RUN`.
- [x] Validar `frontend_dev` via Docker em modo `DRY_RUN`.
- [x] Atualizar `ARCHITECTURE.md`.
- [x] Atualizar `DECISIONS.md`.
- [x] Atualizar `TASKS.md`.

### Etapa 1: Receber escopo detalhado do problema

Responsavel principal: usuario.

Status: Concluida em 2026-05-24.

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

- [x] Problema compreendido.
- [x] Usuarios e fluxos identificados.
- [x] Requisitos funcionais e nao funcionais documentados.
- [x] Restricoes conhecidas.

### Etapa 2: Analise arquitetural do problema

Responsavel principal: `architect`.

Status: Validada e aprovada para scaffold inicial em 2026-05-24.

- [x] Analisar o problema.
- [x] Identificar dominio e subdominios.
- [x] Identificar bounded contexts candidatos.
- [x] Identificar riscos de seguranca.
- [x] Identificar riscos de escala.
- [x] Identificar requisitos de consistencia e transacao.
- [x] Identificar necessidades de auditoria.
- [x] Identificar integracoes externas.
- [x] Comparar alternativas arquiteturais.
- [x] Propor arquitetura candidata adequada ao problema.
- [x] Explicar beneficios e trade-offs.
- [x] Atualizar `ARCHITECTURE.md`.
- [x] Atualizar `DECISIONS.md`.

Gate de saida:

- [x] `coordinator` valida que a proposta candidata e coerente para seguir refinamento.
- [x] Alternativas descartadas documentadas.
- [x] Riscos e mitigacoes documentados.
- [ ] Perguntas pendentes respondidas.
- [x] Arquitetura aprovada para scaffold inicial.

### Etapa 3: Contratos e modelagem

Responsavel principal: `architect`.

Status: Em elaboracao incremental. Endpoints de clientes, ambientes, customizacoes, versoes oficiais e verificacoes manuais ja existem no backend em diferentes niveis de maturidade.

- [x] Definir modelo de dominio inicial.
- [x] Definir casos de uso candidatos.
- [ ] Definir entidades, value objects e agregados quando aplicavel.
- [x] Definir contratos REST candidatos.
- [x] Definir DTOs conceituais de request e response.
- [x] Definir modelo padrao de erro candidato.
- [x] Definir autenticacao e autorizacao candidatas.
- [x] Definir estrategia de persistencia PostgreSQL candidata.
- [ ] Definir estrategia de migracoes.
- [x] Atualizar `API_CONTRACT.md`.
- [x] Atualizar `ARCHITECTURE.md`.
- [x] Atualizar `DECISIONS.md`.

Gate de saida:

- [ ] Contratos aprovados pelo `coordinator`.
- [ ] Backend e frontend possuem interfaces claras.
- [ ] Testes de contrato planejados.

### Etapa 4: Setup do projeto

Responsaveis: `backend_dev`, `frontend_dev`, validacao do `coordinator`.

Status: Iniciada.

- [x] Definir organizacao inicial do repositorio conforme arquitetura aprovada.
- [x] Criar backend Spring Boot com Java 17.
- [x] Configurar Maven.
- [x] Configurar PostgreSQL local com Docker Compose.
- [x] Configurar migracoes com Flyway.
- [x] Criar frontend Angular.
- [ ] Configurar lint backend.
- [x] Configurar lint frontend.
- [x] Configurar build backend.
- [x] Configurar build frontend.
- [x] Configurar testes backend.
- [x] Configurar testes frontend.
- [x] Atualizar `README.md`.

Gate de saida:

- [x] Backend compila.
- [x] Frontend compila.
- [x] Lint executa.
- [x] Testes base backend executam.
- [ ] Testes base frontend executam em ambiente com Chrome/Chromium.
- [x] Documentacao atualizada.

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

Status: Iniciada.

- [x] Implementar estrutura Angular aprovada.
- [x] Implementar rotas.
- [x] Implementar componentes base.
- [x] Implementar formularios reativos.
- [x] Implementar services de integracao HTTP.
- [ ] Implementar interceptors quando necessario.
- [ ] Implementar guards quando necessario.
- [x] Implementar estados loading, empty, error e success.
- [x] Implementar acessibilidade.
- [x] Implementar responsividade.
- [ ] Implementar testes unitarios.
- [ ] Implementar testes E2E com Playwright.
- [x] Atualizar `README.md`.

Gate de saida:

- [x] Build frontend passa.
- [x] Lint frontend passa.
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
- [x] Docker agent validado localmente.

### TASK-001A: Validar runtime multi-agent Docker

Responsavel: `coordinator`.

Status: Concluida.

Criterios de aceite:

- [x] `docker compose build coordinator` executa com sucesso.
- [x] `docker compose run --rm -e DRY_RUN=1 coordinator` carrega o papel `coordinator`.
- [x] `docker compose --profile agents run --rm -e DRY_RUN=1 architect` carrega o papel `architect`.
- [x] `docker compose --profile agents run --rm -e DRY_RUN=1 backend_dev` carrega o papel `backend_dev`.
- [x] `docker compose --profile agents run --rm -e DRY_RUN=1 frontend_dev` carrega o papel `frontend_dev`.
- [x] Todos os agentes usam `agents.yml` como fonte de instrucao.

### TASK-001B: Ajustar runtime para Codex CLI local

Responsavel: `coordinator`.

Status: Concluida.

Criterios de aceite:

- [x] Agentes configurados inicialmente com `model: default`.
- [x] Runtime Docker permite usar o modelo padrao do Codex CLI local.
- [x] Runtime Docker permite override por `CODEX_MODEL`.
- [x] Runtime Docker evita sandbox aninhado com `bubblewrap`.
- [x] Runtime Docker evita `SpawnAgent` interno para subagentes.
- [x] Documentacao de uso local atualizada em `README.md`.

### TASK-001C: Ajustar modelo padrao para preservar janela de uso

Responsavel: `coordinator`.

Status: Concluida.

Contexto:
O Codex CLI local selecionava `gpt-5.5` quando os agentes usavam `model: default`, consumindo mais rapidamente a janela de uso.

Problema que resolve:
Equilibra qualidade de codigo e duracao operacional dentro do limite de uso de 5 horas.

Escopo:
- Configurar `gpt-5.4` como modelo padrao dos agentes.
- Manter override por `CODEX_MODEL`.
- Documentar quando usar `gpt-5.4-mini` e `gpt-5.5`.

Fora de escopo:
- Alterar autenticacao local do Codex.
- Remover suporte a modelos por agente.

Criterios de aceite:
- [x] `coordinator` usa `gpt-5.4`.
- [x] `architect` usa `gpt-5.4`.
- [x] `backend_dev` usa `gpt-5.4`.
- [x] `frontend_dev` usa `gpt-5.4`.
- [x] Override por `CODEX_MODEL` preservado.
- [x] Documentacao obrigatoria atualizada.

Impacto em arquitetura:
Baixo. Afeta runtime dos agentes, nao a arquitetura da aplicacao.

Impacto em contratos:
Nenhum.

Testes obrigatorios:
Validar renderizacao do agente em modo `DRY_RUN`.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md` e `ARCHITECTURE.md`.

### TASK-002: Receber e documentar escopo detalhado

Responsavel: usuario e `architect`.

Status: Concluida.

Criterios de aceite:

- [x] Problema documentado.
- [x] Usuarios documentados.
- [x] Fluxos documentados.
- [x] Regras documentadas.
- [x] Requisitos nao funcionais documentados.

### TASK-003: Propor arquitetura orientada ao problema

Responsavel: `architect`.

Status: Aprovada para scaffold inicial.

Criterios de aceite:

- [x] Alternativas comparadas.
- [x] Arquitetura candidata recomendada.
- [x] Trade-offs explicados.
- [x] Riscos e mitigacoes registrados.
- [x] `coordinator` aprova continuidade do refinamento.
- [x] Usuario aprova inicio do desenvolvimento.

### TASK-004: Criar contratos iniciais de API

Responsavel: `architect`.

Status: Em elaboracao preliminar.

Criterios de aceite:

- [x] Endpoints candidatos definidos quando necessarios.
- [x] DTOs conceituais definidos.
- [x] Modelo de erros candidato definido.
- [x] Autenticacao e autorizacao candidatas documentadas.
- [ ] Exemplos completos incluidos.
- [ ] OpenAPI 3.1 gerada e aprovada.

### TASK-005: Preparar scaffold tecnico

Responsaveis: `backend_dev` e `frontend_dev`.

Status: Iniciada.

Criterios de aceite:

- [x] Backend Spring Boot criado.
- [x] Frontend Angular criado.
- [x] PostgreSQL local configurado.
- [x] Comandos de build, lint e testes disponiveis.
- [x] Documentacao atualizada.

### TASK-010: Implementar primeiro fluxo vertical de clientes

Responsavel: `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida para scaffold inicial.

Contexto:
O desenvolvimento foi autorizado apos aprovacao da arquitetura candidata.

Problema que resolve:
Cria uma base executavel para validar organizacao hexagonal, persistencia PostgreSQL, migracoes, API REST e UI Angular.

Escopo:
- Criar dominio `Customer`.
- Criar caso de uso de cadastro de cliente.
- Criar porta de repositorio.
- Criar adapter PostgreSQL com Spring Data JPA.
- Criar controller REST para criar e listar clientes.
- Criar migracao Flyway da tabela `customers`.
- Criar tela Angular para cadastrar e listar clientes.
- Subir stack local com PostgreSQL, backend e frontend.

Fora de escopo:
- Ambientes de cliente.
- Customizacoes oficiais.
- Hashing/canonicalizacao.
- Verificacao contra MySQL.
- Agente on-premise.
- RBAC final com provedor OAuth real.

Criterios de aceite:
- [x] `POST /api/v1/customers` cria cliente.
- [x] `GET /api/v1/customers` lista clientes.
- [x] Backend usa dominio separado de adapters.
- [x] Migração Flyway executa no PostgreSQL.
- [x] UI Angular carrega e expõe fluxo inicial.
- [x] Documentacao obrigatoria atualizada.

Impacto em arquitetura:
Medio. Materializa o primeiro modulo do modular monolith hexagonal.

Impacto em contratos:
Medio. Promove endpoints de clientes de candidatos para implementados inicialmente.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; teste Angular pendente de Chrome/Chromium local.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-011: Implementar ambientes de cliente

Responsavel: `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida para scaffold inicial.

Contexto:
Ambientes do cliente sao necessarios para separar SaaS, on-premise, modo de coleta e referencias futuras de credenciais.

Problema que resolve:
Permite cadastrar e consultar ambientes por cliente antes de implementar customizacoes, verificacoes e conectores.

Escopo:
- Criar dominio `CustomerEnvironment`.
- Criar enums `EnvironmentType`, `CollectionMode` e `EnvironmentStatus`.
- Criar caso de uso de cadastro de ambiente.
- Criar porta de repositorio.
- Criar adapter PostgreSQL com Spring Data JPA.
- Criar controller REST para criar e listar ambientes.
- Criar migracao Flyway da tabela `customer_environments`.
- Ampliar UI Angular para cadastrar e listar ambientes do cliente selecionado.

Fora de escopo:
- Conexao real com MySQL.
- Secret manager/vault real.
- Agente on-premise.
- Regras finais de canonicalizacao.

Criterios de aceite:
- [x] `POST /api/v1/customers/{customerId}/environments` cria ambiente.
- [x] `GET /api/v1/customers/{customerId}/environments` lista ambientes.
- [x] Backend valida existencia do cliente no caso de uso.
- [x] Migração Flyway V2 executa no PostgreSQL.
- [x] UI Angular permite cadastrar e listar ambientes.
- [x] Documentacao obrigatoria atualizada.

Impacto em arquitetura:
Medio. Materializa parte do contexto de governanca e prepara a coleta hibrida.

Impacto em contratos:
Medio. Promove endpoints de ambientes de candidatos para implementados inicialmente.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; validacao HTTP real contra stack Docker.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-012: Adicionar Lombok no backend

Responsavel: `backend_dev`.

Status: Concluida.

Contexto:
O backend com Java tende a crescer em boilerplate de getters, setters e construtores.

Problema que resolve:
Reduz codigo mecanico mantendo invariantes de dominio por factories explicitas.

Escopo:
- Adicionar dependencia Lombok.
- Refatorar `Customer` e `CustomerEnvironment`.
- Refatorar entidades JPA para construtores gerados.
- Ajustar testes para usar factories de dominio.

Fora de escopo:
- Transformar todos os DTOs em Lombok.
- Remover validacoes de dominio.

Criterios de aceite:
- [x] Backend compila com annotation processing.
- [x] Testes passam.
- [x] Invariantes de dominio preservadas.

Impacto em arquitetura:
Baixo.

Impacto em contratos:
Nenhum.

Testes obrigatorios:
Backend `mvn verify`.

Documentacao:
Atualizar `DECISIONS.md`, `TASKS.md` e `ARCHITECTURE.md`.

### TASK-013: Adicionar Swagger/OpenAPI no backend

Responsavel: `backend_dev`.

Status: Concluida.

Contexto:
Os contratos REST precisam ser navegaveis e verificaveis durante o desenvolvimento.

Problema que resolve:
Disponibiliza documentacao automatica dos endpoints implementados.

Escopo:
- Adicionar springdoc OpenAPI.
- Configurar metadados da API.
- Liberar `/swagger-ui.html`, `/swagger-ui/**` e `/v3/api-docs/**` na seguranca.
- Validar Swagger UI e OpenAPI JSON na stack Docker.

Fora de escopo:
- Gerar arquivo OpenAPI versionado final.
- Fechar OpenAPI 3.1 completo para todos os recursos candidatos.

Criterios de aceite:
- [x] `GET /v3/api-docs` retorna `200`.
- [x] `GET /swagger-ui.html` redireciona para Swagger UI.
- [x] Swagger UI retorna `200`.
- [x] Backend permanece saudavel.

Impacto em arquitetura:
Baixo.

Impacto em contratos:
Medio, pois expõe contratos implementados automaticamente.

Testes obrigatorios:
Backend `mvn verify`; validacao HTTP real contra stack Docker.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-014: Implementar cadastro inicial de customizacoes

Responsavel: `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida para scaffold inicial.

Contexto:
Customizacoes oficiais dependem de cliente e ambiente e sao a base para versionamento, hashing e verificacoes futuras.

Problema que resolve:
Permite registrar e consultar objetos customizados oficiais por cliente e ambiente, mantendo o desenvolvimento incremental dentro do contexto de governanca.

Escopo:
- Criar dominio `Customization`.
- Criar enums `CustomizationObjectType` e `CustomizationStatus`.
- Criar caso de uso de cadastro de customizacao.
- Criar porta de repositorio.
- Criar adapter PostgreSQL com Spring Data JPA.
- Criar controller REST para criar e listar customizacoes.
- Criar migracao Flyway da tabela `customizations`.
- Ampliar UI Angular para cadastrar e listar customizacoes oficiais.

Fora de escopo:
- Versoes oficiais de customizacao.
- Hash oficial e canonicalizacao.
- Verificacao contra MySQL.
- Filtros, paginacao e busca avancada.

Criterios de aceite:
- [x] `POST /api/v1/customizations` cria customizacao.
- [x] `GET /api/v1/customizations` lista customizacoes.
- [x] Backend valida existencia de cliente e ambiente.
- [x] Migração Flyway V3 executa no PostgreSQL.
- [x] UI Angular permite cadastrar e listar customizacoes.
- [x] Swagger/OpenAPI inclui os novos contratos.
- [x] Documentacao obrigatoria atualizada.

Impacto em arquitetura:
Medio. Completa a primeira fatia do contexto de Cadastro e Governanca de Customizacoes antes de versionamento e verificacao.

Impacto em contratos:
Medio. Promove endpoints de customizacoes de candidatos para implementados inicialmente.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; validacao HTTP real contra stack Docker.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-015: Implementar versoes oficiais de customizacao

Responsavel: `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida para scaffold inicial.

Contexto:
Cada customizacao precisa de uma ou mais versoes oficiais por versao do legado, com hash oficial, algoritmo e versao de canonicalizacao.

Problema que resolve:
Cria a referencia oficial persistida que sera usada por verificacoes futuras para comparar hashes coletados do MySQL.

Escopo:
- Criar dominio `CustomizationVersion`.
- Criar enum `CustomizationVersionStatus`.
- Criar caso de uso de registro de versao oficial.
- Criar porta de repositorio.
- Criar adapter PostgreSQL com Spring Data JPA.
- Criar controller REST para criar e listar versoes por customizacao.
- Criar migracao Flyway da tabela `customization_versions`.
- Ampliar UI Angular para registrar e listar versoes oficiais da customizacao selecionada.

Fora de escopo:
- Calculo automatico de hash.
- Coleta MySQL.
- Politica final de versao ativa unica.
- Criptografia ou segregacao final de assinatura canonica.

Criterios de aceite:
- [x] `POST /api/v1/customizations/{customizationId}/versions` cria versao oficial.
- [x] `GET /api/v1/customizations/{customizationId}/versions` lista versoes oficiais.
- [x] Backend valida existencia da customizacao.
- [x] Migração Flyway V4 executa no PostgreSQL.
- [x] UI Angular permite registrar e listar versoes oficiais.
- [x] Swagger/OpenAPI inclui os novos contratos.
- [x] Documentacao obrigatoria atualizada.

Impacto em arquitetura:
Medio. Materializa a referencia oficial de integridade sem acoplar o dominio ao coletor MySQL.

Impacto em contratos:
Medio. Promove endpoints de versoes oficiais de candidatos para implementados inicialmente.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; validacao HTTP real contra stack Docker.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-016: Concluir vertical de verificacao manual auditavel

Responsavel: `architect`, `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida.

Contexto:
O backend ja possui `VerificationRun` e `VerificationResult` com persistencia, contrato REST e cobertura de service/query/controller. O frontend passou a expor o fluxo no dashboard em 2026-05-25, e a vertical ainda depende da validacao HTTP real pelo `coordinator`.

Problema que resolve:
Entrega a primeira verificacao de integridade auditavel ponta a ponta, sem depender ainda de coletor MySQL, canonicalizacao automatica ou agente on-premise.

Escopo:
- Consolidar o contrato REST da verificacao manual.
- Validar e endurecer o backend ja implementado.
- Integrar Angular para criar, listar e consultar verificacoes manuais.
- Registrar historico e resultado na documentacao funcional e tecnica.
- Expor filtros minimos de historico por cliente e ambiente para o dashboard.

Fora de escopo:
- Coleta real de definicoes do MySQL.
- Calculo automatico de hash.
- Abertura automatica de divergencias.
- Notificacao REST ao legado.

Criterios de aceite:
- [x] `POST /api/v1/verification-runs` cria verificacao manual auditavel no backend.
- [x] `GET /api/v1/verification-runs` lista verificacoes persistidas no backend.
- [x] `GET /api/v1/verification-runs/{verificationRunId}` consulta o detalhe da verificacao no backend.
- [x] Backend possui testes web slice para os endpoints de verificacao.
- [x] Frontend Angular permite disparar verificacao manual a partir de versao oficial selecionada.
- [x] Frontend Angular exibe historico e resultado da verificacao.
- [x] Validacao HTTP real dos endpoints de verificacao registrada pelo `coordinator`.
- [x] Documentacao obrigatoria sincronizada com o estado real da vertical.

Impacto em arquitetura:
Medio. Materializa o bounded context de verificacao sem acoplar o sistema ao coletor MySQL.

Impacto em contratos:
Medio. Promove `verification-runs` para implementado no backend e mantem `verification-results` como recurso persistido retornado de forma embutida.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; validacao HTTP real contra stack Docker; testes Angular continuam dependentes de Chrome/Chromium local.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

Observacao de andamento em 2026-05-25:
o dashboard Angular foi reorganizado com menu lateral por etapas e contexto persistente para corrigir a quebra de layout causada pela concentracao de formularios e listagens em uma unica pagina larga.
Os web slice tests do backend tambem foram estabilizados com `Clock` compartilhado em `TestSecurityConfig`, removendo uma quebra operacional do rebuild local.
A validacao HTTP real foi registrada com uma execucao `MATCH` persistida em `verification-runs` na stack Docker local.
O historico de verificacoes agora aceita filtros opcionais por `customerId` e `environmentId`, consumidos pelo dashboard no contexto selecionado.

### TASK-017: Persistir divergencias a partir de verificacoes manuais divergentes

Responsavel: `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida.

Contexto:
A verificacao manual auditavel ja persiste `VerificationRun` e `VerificationResult`. Falta materializar o artefato operacional de divergencia para preparar tratamento e futura notificacao ao legado.

Problema que resolve:
Transforma um resultado `DIVERGENT` em `Divergence` persistida, consultavel por API e visivel no dashboard.

Escopo:
- Persistir `Divergence` automaticamente quando uma verificacao resultar em `DIVERGENT`.
- Expor `GET /api/v1/divergences` e `GET /api/v1/divergences/{divergenceId}`.
- Permitir filtros minimos por cliente e ambiente.
- Exibir divergencias no frontend Angular.

Fora de escopo:
- Notificacao automatica ao legado.
- Severidade dinamica por regra de negocio.

Criterios de aceite:
- [x] Verificacao `DIVERGENT` cria `Divergence` persistida no backend.
- [x] `GET /api/v1/divergences` lista divergencias persistidas.
- [x] `GET /api/v1/divergences/{divergenceId}` consulta detalhe da divergencia.
- [x] Backend possui testes unitarios e web slice da vertical.
- [x] Frontend Angular exibe divergencias do contexto selecionado.
- [x] Validacao HTTP real dos endpoints de divergencia registrada.
- [x] Documentacao obrigatoria sincronizada com o estado real da vertical.

Impacto em arquitetura:
Medio. Materializa o bounded context de divergencias sem acoplar ainda notificacao outbound.

Impacto em contratos:
Medio. Promove `divergences` de candidato para implementado inicialmente.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; validacao HTTP real contra stack Docker.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-018: Permitir tratamento minimo de divergencias por status

Responsavel: `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida.

Contexto:
Depois de materializar `Divergence`, o suporte ainda ficava sem forma de registrar andamento operacional no sistema.

Problema que resolve:
Permite reconhecer e resolver divergencias sem sair do fluxo atual do dashboard.

Escopo:
- Expor `PATCH /api/v1/divergences/{divergenceId}/status`.
- Permitir inicialmente transicoes para `ACKNOWLEDGED` e `RESOLVED`.
- Persistir `resolvedAt` quando a divergencia for resolvida.
- Exibir acoes operacionais minimas no frontend Angular.

Fora de escopo:
- Notificacao automatica ao legado.
- Workflow completo com justificativa para `IGNORED_WITH_JUSTIFICATION`.
- Historico detalhado de transicoes.

Criterios de aceite:
- [x] Backend aceita `PATCH /api/v1/divergences/{divergenceId}/status`.
- [x] `ACKNOWLEDGED` persiste sem `resolvedAt`.
- [x] `RESOLVED` persiste com `resolvedAt`.
- [x] Backend possui testes de dominio, aplicacao e web slice para as transicoes.
- [x] Frontend Angular permite reconhecer e resolver divergencias.
- [x] Validacao HTTP real registrada para `OPEN -> ACKNOWLEDGED -> RESOLVED`.
- [x] Documentacao obrigatoria sincronizada com o estado real da vertical.

Impacto em arquitetura:
Medio. Fecha o ciclo operacional minimo do bounded context de divergencias sem introduzir orquestracao externa.

Impacto em contratos:
Medio. Promove o endpoint de patch de divergencia de candidato para implementado inicialmente.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; validacao HTTP real contra stack Docker.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-019: Persistir outbox inicial de notificacao ao legado

Responsavel: `backend_dev`, `frontend_dev` e `coordinator`.

Status: Concluida.

Contexto:
Depois de abrir e tratar `Divergence`, ainda faltava a trilha transacional que prepara a integracao futura com o legado.

Problema que resolve:
Materializa a tentativa de notificacao ao legado como outbox persistido, consultavel por API e visivel no dashboard.

Escopo:
- Persistir `LegacyNotification` automaticamente quando uma `Divergence` for criada.
- Expor `GET /api/v1/legacy-notifications` e `GET /api/v1/legacy-notifications/{legacyNotificationId}`.
- Permitir filtros minimos por cliente e ambiente.
- Exibir `legacy-notifications` no frontend Angular.

Fora de escopo:
- Dispatcher HTTP outbound para o legado.
- Retry, backoff e circuit breaker.
- Atualizacao de status para `SENT` ou `FAILED`.

Criterios de aceite:
- [x] Verificacao `DIVERGENT` cria `LegacyNotification` persistida com `status=PENDING`.
- [x] `GET /api/v1/legacy-notifications` lista notificacoes persistidas.
- [x] `GET /api/v1/legacy-notifications/{legacyNotificationId}` consulta detalhe da notificacao persistida.
- [x] Backend possui testes unitarios e web slice da vertical.
- [x] Frontend Angular exibe `legacy-notifications` do contexto selecionado.
- [x] Validacao HTTP real registrada para criacao e consulta do outbox.
- [x] Documentacao obrigatoria sincronizada com o estado real da vertical.

Impacto em arquitetura:
Medio. Materializa o outbox de notificacao sem introduzir ainda integracao outbound.

Impacto em contratos:
Medio. Promove `legacy-notifications` de candidato para implementado inicialmente.

Testes obrigatorios:
Backend `mvn verify`; frontend lint/build; validacao HTTP real contra stack Docker.

Documentacao:
Atualizar `README.md`, `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` e `API_CONTRACT.md`.

### TASK-006: Refinar estrategia de hashing e canonicalizacao SQL

Responsavel: `architect`.

Status: Pendente.

Contexto:
O sistema precisa comparar customizacoes oficiais com objetos reais no MySQL sem gerar falsos positivos por formatacao e sem esconder mudancas semanticamente relevantes.

Problema que resolve:
Define como funcoes, procedures, triggers, tabelas, scripts SQL e outros objetos serao coletados, normalizados e hashados.

Escopo:
- Definir algoritmo de canonicalizacao por tipo de objeto.
- Definir `canonicalizationVersion`.
- Definir algoritmo de hash inicial, preferencialmente SHA-256.
- Definir fixtures de teste com exemplos reais de MySQL.
- Definir politica de evolucao da canonicalizacao.

Fora de escopo:
- Implementacao de parser ou coletor.

Criterios de aceite:
- [ ] Regras por tipo de objeto documentadas.
- [ ] Riscos de falso positivo e falso negativo documentados.
- [ ] Estrategia de testes definida.
- [ ] Impacto no contrato documentado.

Impacto em arquitetura:
Alto, pois define porta de dominio para servico de hashing/canonicalizacao.

Impacto em contratos:
Medio, pois `hashAlgorithm` e `canonicalizationVersion` entram nos DTOs.

Testes obrigatorios:
Planejar testes unitarios com fixtures e testes de integracao contra MySQL.

Documentacao:
Atualizar `ARCHITECTURE.md`, `API_CONTRACT.md` e `DECISIONS.md`.

### TASK-007: Definir estrategia segura de coleta para SaaS e on-premise

Responsavel: `architect`.

Status: Pendente.

Contexto:
O sistema precisa verificar bases MySQL em ambientes SaaS e on-premise, incluindo clientes sem acesso direto de rede.

Problema que resolve:
Define quando o sistema central puxa dados, quando um agente local envia resultados e quais controles de seguranca sao obrigatorios.

Escopo:
- Definir modos `CENTRAL_PULL`, `LOCAL_AGENT_PUSH` e eventual `MANUAL_SIGNATURE_UPLOAD`.
- Definir requisitos de rede, credenciais, TLS/VPN/allowlist e timeouts.
- Definir estrategia para clientes on-premise sem inbound.
- Definir responsabilidades do agente local candidato.

Esclarecimento ja consolidado na documentacao atual:
- `CENTRAL_PULL` = o sistema central inicia a coleta.
- `LOCAL_AGENT_PUSH` = o agente local inicia o envio.
- `MANUAL_SIGNATURE_UPLOAD` = a evidencia entra manualmente de forma auditavel.
- no scaffold atual, esses modos ja sao persistidos no ambiente, mas ainda nao acionam coletores automaticos distintos.
- Definir limites de retry e tratamento de falhas.

Fora de escopo:
- Implementacao do agente local.
- Implementacao de conectores MySQL.

Criterios de aceite:
- [ ] Matriz de decisao por tipo de ambiente.
- [ ] Controles de seguranca documentados.
- [ ] Riscos operacionais documentados.
- [ ] Contratos candidatos para agente documentados.

Impacto em arquitetura:
Alto, pois define adapters de saida/entrada e possivel componente distribuido.

Impacto em contratos:
Alto, pois pode criar endpoints para agente on-premise.

Testes obrigatorios:
Planejar testes de integracao, resiliencia, timeout e idempotencia.

Documentacao:
Atualizar `ARCHITECTURE.md`, `API_CONTRACT.md` e `DECISIONS.md`.

### TASK-008: Definir seguranca, auditoria e gestao de credenciais

Responsavel: `architect`.

Status: Pendente.

Contexto:
O sistema pode acessar bancos de clientes e manipular informacoes sensiveis de customizacoes.

Problema que resolve:
Define como proteger credenciais, APIs, auditoria, segregacao por cliente e logs.

Escopo:
- Definir modelo de autenticacao e autorizacao.
- Definir secret manager/vault alvo ou fallback controlado.
- Definir politica de mascaramento.
- Definir trilha de auditoria obrigatoria.
- Definir segregacao multi-tenant.
- Definir eventos de seguranca e observabilidade.

Fora de escopo:
- Implementacao de Spring Security.
- Implementacao de vault.

Criterios de aceite:
- [ ] Politica de credenciais documentada.
- [ ] Papeis e permissoes candidatos definidos.
- [ ] Eventos auditaveis definidos.
- [ ] Requisitos de logs e metricas definidos.

Impacto em arquitetura:
Alto.

Impacto em contratos:
Alto, pois altera autenticacao, autorizacao e dados expostos.

Testes obrigatorios:
Planejar testes de autorizacao, mascaramento e auditoria.

Documentacao:
Atualizar `ARCHITECTURE.md`, `API_CONTRACT.md` e `DECISIONS.md`.

### TASK-009: Finalizar contrato REST e OpenAPI 3.1

Responsavel: `architect`.

Status: Bloqueada por TASK-006, TASK-007 e TASK-008.

Contexto:
O contrato preliminar existe, mas nao deve guiar implementacao sem refinamento e aprovacao.

Problema que resolve:
Transforma recursos candidatos em contrato formal para backend, frontend, agente on-premise e legado.

Escopo:
- Definir DTOs finais.
- Definir endpoints finais.
- Definir paginacao, filtros e ordenacao.
- Definir modelo de erros final.
- Definir regras de idempotencia.
- Definir OpenAPI 3.1.

Fora de escopo:
- Implementacao backend/frontend.

Criterios de aceite:
- [ ] OpenAPI 3.1 documentada.
- [ ] Exemplos de request e response incluidos.
- [ ] Contrato de notificacao ao legado aprovado.
- [ ] Contrato de agente aprovado ou explicitamente adiado.
- [ ] `coordinator` aprova contratos.

Impacto em arquitetura:
Medio.

Impacto em contratos:
Alto.

Testes obrigatorios:
Planejar testes de contrato e validacao OpenAPI.

Documentacao:
Atualizar `API_CONTRACT.md`, `ARCHITECTURE.md`, `TASKS.md` e `README.md`.

## Delegacoes Registradas

### 2026-05-24: Delegacao ao `architect`

Status: Registrada e consolidada documentalmente.

Escopo delegado:
- analisar dominio de controle e auditoria de customizacoes;
- identificar bounded contexts candidatos;
- levantar riscos de seguranca, conectividade, hashing, auditoria e escala;
- comparar alternativas arquiteturais;
- propor arquitetura candidata;
- sugerir modelo inicial de dados;
- sugerir estrategias de hashing, coleta, notificacao, seguranca, auditoria e observabilidade.

Resultado:
- analise inicial registrada em `ARCHITECTURE.md`;
- ADRs candidatas registradas em `DECISIONS.md`;
- contrato preliminar registrado em `API_CONTRACT.md`;
- tarefas de refinamento registradas em `TASKS.md`.

Restricao:
`backend_dev` e `frontend_dev` permanecem bloqueados para implementacao ate arquitetura final e contratos serem aprovados pelo `coordinator`.

### 2026-05-24: Validacao do `coordinator`

Status: Concluida para a rodada atual.

Resultado:
- arquitetura candidata validada como coerente para continuidade do refinamento;
- modular monolith hexagonal aceito como direcao candidata, nao como arquitetura final;
- coleta hibrida aceita como direcao candidata para SaaS e on-premise;
- hashing canonico versionado aceito como premissa obrigatoria;
- seguranca de credenciais, RBAC, auditoria, canonicalizacao detalhada e OpenAPI 3.1 permanecem como gates antes do scaffold.

Restricao:
nenhum codigo de aplicacao, schema PostgreSQL, conector MySQL, agente on-premise ou cliente Angular deve ser implementado nesta fase.

### 2026-05-24: Aprovacao do usuario para desenvolvimento

Status: Concluida.

Resultado:
- usuario aprovou a arquitetura proposta;
- scaffold inicial foi iniciado;
- primeiro fluxo vertical de clientes foi implementado e validado;
- backend, frontend e PostgreSQL sobem com `docker compose --profile app up --build -d`.

Restricao:
o desenvolvimento deve continuar incremental e aderente a arquitetura aprovada. Conectores MySQL, agente on-premise, hashing/canonicalizacao e OpenAPI final ainda exigem refinamento especifico antes da implementacao.

### 2026-05-25: Delegacao coordenada da vertical de verificacao manual

Status: Registrada.

Escopo delegado ao `architect`:
- revisar o contrato da verificacao manual auditavel;
- explicitar o que esta implementado nesta fase sem coletor;
- preparar a evolucao contratual futura para estados `OBJECT_NOT_FOUND`, `UNREACHABLE` e `ERROR`.

Escopo delegado ao `backend_dev`:
- validar e endurecer o modulo de verificacao manual existente;
- adicionar testes web slice do controller;
- padronizar erros e confirmar coerencia de persistencia e contrato.

Escopo delegado ao `frontend_dev`:
- integrar formulario de disparo de verificacao manual;
- integrar listagem historica de verificacoes;
- integrar visualizacao do resultado com `MATCH` e `DIVERGENT`.

Gate do `coordinator`:
- nao liberar coletor MySQL, divergencias persistidas ou notificacao ao legado antes de concluir `TASK-016` com build, lint, testes e documentacao.

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
