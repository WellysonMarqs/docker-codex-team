# Docker Codex Team

Workspace de planejamento e coordenacao multi-agent para o Sistema de Controle e Auditoria de Customizacoes do sistema legado.

## Estado Atual

- Codigo de aplicacao: scaffold inicial iniciado e validado.
- Escopo inicial: recebido em 2026-05-24.
- Arquitetura candidata: aprovada pelo usuario para inicio controlado do desenvolvimento.
- Arquitetura final: continua evolutiva; hashing, coleta on-premise, RBAC final e OpenAPI completa seguem pendentes.
- Backend definido como stack obrigatoria: Java 17 com Spring Boot estavel compativel.
- Banco de dados definido como stack obrigatoria: PostgreSQL.
- Frontend definido como stack obrigatoria: Angular em versao estavel e suportada.
- Sistema legado integrado: MySQL e notificacao REST.

## Produto

O sistema deve controlar customizacoes oficiais do legado por cliente e ambiente, armazenar hashes oficiais, verificar periodicamente ou sob demanda se os objetos instalados no MySQL continuam equivalentes, registrar historico de verificacoes, detectar divergencias e notificar o sistema legado por API REST.

Ambientes considerados:

- SaaS, hospedado e controlado pela empresa.
- On-premise, instalado no ambiente do cliente.

Modos de coleta atualmente modelados por ambiente:

- `CENTRAL_PULL`: o sistema central inicia a coleta diretamente na fonte do cliente. Destinado a SaaS e a ambientes com conectividade controlada, credenciais restritas e allowlist.
- `LOCAL_AGENT_PUSH`: um agente local instalado no ambiente do cliente realiza a coleta e envia a evidencia ao sistema central. Destinado a on-premise sem acesso inbound seguro ou com restricoes de rede.
- `MANUAL_SIGNATURE_UPLOAD`: a evidencia e informada manualmente por operador autorizado. Deve existir apenas como contingencia auditavel e, no estado atual do scaffold, e o modo que mais se aproxima do fluxo manual ja implementado.

Tipos de customizacao candidatos:

- tabela;
- funcao;
- procedure;
- trigger;
- script SQL;
- outro objeto customizado.

## Principio de Trabalho

A equipe nao deve apenas escrever codigo. A equipe deve entender o problema, propor a melhor solucao possivel e construir um sistema seguro, robusto, escalavel, compreensivel por outros desenvolvedores e facil de manter.

## Aplicacao Local

O scaffold inicial implementa os primeiros fluxos verticais de governanca:

- backend Spring Boot em `apps/backend`;
- frontend Angular em `apps/frontend`;
- PostgreSQL local via Docker Compose;
- migracao Flyway `V1__create_customers.sql`;
- migracao Flyway `V2__create_customer_environments.sql`;
- migracao Flyway `V3__create_customizations.sql`;
- migracao Flyway `V4__create_customization_versions.sql`;
- migracao Flyway `V5__create_verification_runs.sql`;
- migracao Flyway `V6__create_verification_results.sql`;
- migracao Flyway `V7__create_divergences.sql`;
- migracao Flyway `V8__create_legacy_notifications.sql`;
- endpoints `POST /api/v1/customers` e `GET /api/v1/customers`;
- endpoints `POST /api/v1/customers/{customerId}/environments` e `GET /api/v1/customers/{customerId}/environments`;
- endpoints `POST /api/v1/customizations` e `GET /api/v1/customizations`;
- endpoints `POST /api/v1/customizations/{customizationId}/versions` e `GET /api/v1/customizations/{customizationId}/versions`;
- endpoints `POST /api/v1/verification-runs`, `GET /api/v1/verification-runs` e `GET /api/v1/verification-runs/{verificationRunId}`;
- `GET /api/v1/verification-runs` aceita filtros opcionais por `customerId` e `environmentId`;
- endpoints `GET /api/v1/divergences`, `GET /api/v1/divergences/{divergenceId}` e `PATCH /api/v1/divergences/{divergenceId}/status`;
- endpoints `GET /api/v1/legacy-notifications` e `GET /api/v1/legacy-notifications/{legacyNotificationId}`;
- UI inicial com menu lateral por etapas para cadastrar/listar clientes, ambientes, customizacoes oficiais, versoes oficiais e verificacoes manuais auditaveis sem concentrar todos os formularios em uma unica grade horizontal.
- UI inicial inclui acompanhamento de divergencias persistidas por cliente e ambiente, com acoes de reconhecimento e resolucao diretamente na etapa de divergencias.
- UI inicial inclui acompanhamento do outbox `legacy-notifications` por cliente e ambiente.

Subir a stack:

```bash
docker compose --profile app up --build -d
```

URLs locais:

- Frontend: `http://localhost:4200`
- Backend health: `http://localhost:8080/actuator/health`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- API clientes: `http://localhost:8080/api/v1/customers`
- API customizacoes: `http://localhost:8080/api/v1/customizations`
- API versoes oficiais: `http://localhost:8080/api/v1/customizations/{customizationId}/versions`
- API verificacoes: `http://localhost:8080/api/v1/verification-runs`

Validacoes principais:

```bash
docker run --rm -v "$PWD/apps/backend:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-17 mvn -q verify
cd apps/frontend && npm run lint
cd apps/frontend && npm run build
```

Validacao HTTP real registrada em 2026-05-25:
- `POST /api/v1/verification-runs` retornando `201` com `status=COMPLETED` e `result.status=MATCH`;
- `GET /api/v1/verification-runs` retornando a execucao persistida;
- `GET /api/v1/verification-runs/{verificationRunId}` retornando o detalhe da mesma execucao.
- `GET /api/v1/verification-runs?customerId=...&environmentId=...` retornando o historico filtrado do contexto selecionado.
- `POST /api/v1/verification-runs` com hash divergente retornando `201` com `status=COMPLETED_WITH_DIVERGENCE` e `result.status=DIVERGENT`;
- `GET /api/v1/divergences` retornando a divergencia persistida automaticamente;
- `GET /api/v1/divergences?customerId=...&environmentId=...` retornando a divergencia filtrada do contexto selecionado;
- `GET /api/v1/divergences/{divergenceId}` retornando o detalhe da divergencia aberta.
- `PATCH /api/v1/divergences/{divergenceId}/status` retornando `ACKNOWLEDGED` e depois `RESOLVED` com `resolvedAt` persistido.
- `GET /api/v1/legacy-notifications` retornando a tentativa persistida de notificacao ao legado;
- `GET /api/v1/legacy-notifications/{legacyNotificationId}` retornando o detalhe do outbox persistido;
- `POST /api/v1/verification-runs` com hash divergente retornando `201` e criando `legacy-notification` `PENDING` com `attempts=0`.

Observacao:
o perfil Docker do backend usa `SPRING_PROFILES_ACTIVE=local` para permitir desenvolvimento sem provedor OAuth local. O perfil padrao mantem Resource Server JWT e RBAC por escopos.

Observacao sobre coleta:
os tres modos acima ja sao persistidos no cadastro de ambiente, mas a coleta automatica MySQL ainda nao foi implementada. Hoje o sistema usa essa informacao como decisao arquitetural do ambiente e oferece apenas a verificacao manual auditavel por hash.

Observacao sobre testes backend:
os web slice tests usam `TestSecurityConfig` com `Clock` compartilhado para manter o `GlobalExceptionHandler` deterministico e evitar quebra do rebuild local quando o tratamento global de erro exige timestamp injetado.

## Proximo Passo

Prioridade atual do `coordinator`: evoluir do outbox persistido para envio outbound controlado ao legado, sem romper a fase atual que ainda permanece sem coletor MySQL e sem canonicalizacao automatica.

Delegacoes aprovadas:

- `architect`: consolidar o contrato da verificacao manual auditavel, incluindo semantica de `MATCH` e `DIVERGENT`, evidencias, filtros minimos de consulta e modelo de erro esperado nesta fase sem coletor.
- `backend_dev`: endurecimento principal concluido em 2026-05-25, cobrindo controller/web slice, correcoes de compilacao e coerencia de servico/contrato; a validacao HTTP real dos endpoints segue como gate operacional do `coordinator`.
- `frontend_dev`: integracao Angular concluida em 2026-05-25 no dashboard principal, consumindo `POST /api/v1/verification-runs`, `GET /api/v1/verification-runs` e `GET /api/v1/verification-runs/{verificationRunId}` sem quebrar a arquitetura atual de tela unica.
- `coordinator`: validar integracao ponta a ponta, manter contratos/documentacao sincronizados e conduzir a proxima fatia operacional sem antecipar coletor MySQL ou canonicalizacao automatica.

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

```bash
docker compose run --rm coordinator "$(cat prompts/customization-audit-scope.md)"
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

O `coordinator` nao usa `SpawnAgent` interno do Codex. Delegacoes para `architect`, `backend_dev` e `frontend_dev` devem ser executadas pelos servicos Docker acima, mantendo a orquestracao explicita e auditavel.

O diretorio atual e montado em `/workspace`, e o `CODEX_HOME` local e montado em `/codex-home` para reutilizar autenticacao e configuracao do Codex.

### Uso com Codex CLI local

Por padrao, os agentes usam `model: gpt-5.4` em `agents.yml`. Essa escolha reduz consumo da janela de uso em relacao ao `gpt-5.5`, mantendo boa qualidade para desenvolvimento diario.

Dentro do container, `CODEX_SANDBOX` usa `danger-full-access` para evitar sandbox aninhado com `bubblewrap`. O isolamento esperado vem do proprio Docker e dos volumes declarados no `docker-compose.yml`.

Para tarefas simples ou mais longas, use o override mais economico:

```bash
docker compose run --rm -e CODEX_MODEL=gpt-5.4-mini coordinator "$(cat prompts/customization-audit-scope.md)"
```

Para tarefas mais complexas, use temporariamente um modelo mais forte:

```bash
docker compose run --rm -e CODEX_MODEL=gpt-5.5 coordinator "Revise arquitetura e riscos criticos."
```

Para validar qual agente sera carregado sem consumir uma execucao real:

```bash
docker compose run --rm -e DRY_RUN=1 coordinator "Teste de roteamento"
```

## Documentos

- `AGENTS.md`: regras globais da equipe.
- `agents.yml`: definicao dos agentes.
- `ARCHITECTURE.md`: diretrizes arquiteturais, analise do dominio, arquitetura candidata e scaffold implementado.
- `DECISIONS.md`: decisoes tecnicas registradas e propostas pendentes de validacao final.
- `TASKS.md`: organizacao das tarefas, gates e delegacoes.
- `API_CONTRACT.md`: contrato preliminar e endpoints ja implementados.

## Status de Validacao

Validado em 2026-05-24:

- `docker compose config --quiet`
- backend: `mvn -q verify` em container Maven
- backend Docker build com testes durante `mvn package`
- Swagger UI: `GET /swagger-ui.html` redirecionou e retornou `200`
- OpenAPI JSON: `GET /v3/api-docs` retornou `200`
- frontend: `npm run lint`
- frontend: `npm run build`
- stack local: `docker compose --profile app up --build -d`
- health backend: `GET /actuator/health` retornou `UP`
- API clientes: `POST /api/v1/customers` retornou `201`
- API clientes: `GET /api/v1/customers` retornou o cliente criado
- API ambientes: `POST /api/v1/customers/{customerId}/environments` retornou `201`
- API ambientes: `GET /api/v1/customers/{customerId}/environments` retornou o ambiente criado
- API customizacoes: `POST /api/v1/customizations` retornou `201`
- API customizacoes: `GET /api/v1/customizations` retornou a customizacao criada
- API versoes oficiais: `POST /api/v1/customizations/{customizationId}/versions` retornou `201`
- API versoes oficiais: `GET /api/v1/customizations/{customizationId}/versions` retornou a versao criada
- OpenAPI JSON inclui `/api/v1/customizations`, `CreateCustomizationRequest` e `CustomizationResponse`
- OpenAPI JSON inclui `/api/v1/customizations/{customizationId}/versions`, `CreateCustomizationVersionRequest` e `CustomizationVersionResponse`
- frontend respondeu `200 OK` em `http://localhost:4200`

Validado em 2026-05-25:

- frontend: `npm run lint`
- frontend: `npm run build`
- backend: o codigo da vertical de verificacao manual esta presente no workspace, mas a reexecucao local de `mvn verify` neste ambiente ficou bloqueada por ausencia de `mvn` fora de container

Teste unitario Angular via Karma ainda depende de Chrome/Chromium local. O bundle de teste foi gerado, mas o ambiente informou ausencia de `CHROME_BIN`.
