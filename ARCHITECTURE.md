# Arquitetura do Projeto

## 1. Contexto

Este workspace inicia um produto ainda sem codigo de aplicacao. A configuracao atual define uma equipe multi-agent com os papeis `coordinator`, `architect`, `backend_dev` e `frontend_dev`, orientada por Clean Code, SOLID, Clean Architecture, Arquitetura Hexagonal, DDD, testes automatizados, seguranca, performance, escalabilidade e observabilidade.

A arquitetura final nao deve ser fechada antes do envio do escopo detalhado do produto. O papel da equipe nao e apenas escrever codigo: e entender o problema, propor a melhor solucao possivel e construir um sistema robusto, seguro, escalavel, compreensivel por outros desenvolvedores e facil de manter.

## 2. Regra Arquitetural Principal

Nenhuma arquitetura definitiva deve ser implementada ou tratada como final antes de existir um escopo claro contendo:

- problema que o projeto ira resolver;
- usuarios impactados;
- objetivos de negocio;
- fluxos principais;
- regras de negocio;
- requisitos funcionais;
- requisitos nao funcionais;
- restricoes tecnicas;
- volume esperado de uso;
- requisitos de seguranca;
- necessidades de integracao;
- criterios de sucesso.

Antes desse escopo, este documento deve conter apenas diretrizes, stack obrigatoria, criterios de decisao e um processo para chegar a arquitetura correta.

## 3. Stack Obrigatoria

### 3.1 Backend

- Java 17.
- Spring Boot em versao estavel compativel com Java 17.
- Spring Web ou Spring WebFlux somente apos analise do problema.
- Spring Validation.
- Spring Security quando houver autenticacao, autorizacao ou protecao de endpoints.
- Spring Data JPA ou alternativa de persistencia definida apos o escopo.
- Flyway ou Liquibase para migracoes de banco.
- JUnit 5, Mockito, AssertJ e Testcontainers.
- Maven ou Gradle, a definir antes do scaffold.

Versao sugerida no planejamento atual:

- Spring Boot 3.5.x estavel para Java 17.

Observacao:
Spring Boot 4.x tambem exige Java 17, mas a escolha entre 3.5.x e 4.x deve considerar maturidade de bibliotecas, compatibilidade do ecossistema e risco de adocao no momento do scaffold.

### 3.2 Banco de dados

- PostgreSQL.
- Migracoes versionadas.
- Indices definidos por padrao de consulta real.
- Transacoes explicitas para casos de uso criticos.
- Testes de integracao com banco real via Testcontainers.

### 3.3 Frontend

- Angular em versao estavel e suportada oficialmente.
- Angular CLI na mesma versao principal do Angular.
- TypeScript conforme matriz oficial de compatibilidade do Angular.
- RxJS conforme matriz oficial de compatibilidade do Angular.
- Angular Router.
- Reactive Forms.
- Angular HttpClient.
- Testes com Jasmine/Karma ou Vitest quando tecnicamente justificavel.
- Testes E2E com Playwright.

Versao sugerida no planejamento atual:

- Angular 21.x para projetos novos, se o ecossistema de dependencias do projeto estiver compativel.
- Angular 20.x LTS pode ser escolhido se a prioridade for maior estabilidade de bibliotecas e suporte de longo prazo.

### 3.4 Infraestrutura e contratos

- Docker Compose para ambiente local.
- OpenAPI 3.1 para contratos REST quando houver API HTTP.
- CI/CD com validacao obrigatoria de lint, build e testes.
- Observabilidade com logs estruturados, health checks, metricas e tracing quando aplicavel.

## 4. Workflow Multi-Agent Profissional

### 4.1 Papeis

- `coordinator`: coordena o fluxo, valida qualidade, integra entregas, bloqueia decisoes frageis e garante documentacao.
- `architect`: analisa o problema, modela dominio, avalia trade-offs e propoe arquitetura apos receber o escopo.
- `backend_dev`: implementa backend somente apos aprovacao da arquitetura e dos contratos.
- `frontend_dev`: implementa frontend somente apos aprovacao da estrategia de UX, arquitetura frontend e contratos.

### 4.2 Runtime Docker Agent

O projeto possui um runtime Docker para carregar os agentes definidos em `agents.yml`.

Componentes:

- `Dockerfile`: cria a imagem `docker-codex-team-agent:local` com Codex CLI, Python e suporte a YAML.
- `docker-compose.yml`: define o servico principal `coordinator` e os servicos especializados `architect`, `backend_dev` e `frontend_dev` sob profile `agents`.
- `scripts/render_agent_prompt.py`: le `agents.yml`, `AGENTS.md` e documentos obrigatorios para montar o prompt operacional de cada agente.
- `scripts/run-agent.sh`: seleciona o agente, renderiza o prompt e inicia o Codex CLI.

Regra operacional:

- O `coordinator` e o ponto de entrada padrao.
- `architect`, `backend_dev` e `frontend_dev` podem ser executados manualmente apenas quando houver delegacao clara.
- Todos os agentes usam `agents.yml` como fonte primaria de papel, instrucao e orquestracao.
- O runtime nao depende de ferramentas internas de colaboracao do Codex; subagentes sao executados como servicos Docker dedicados.
- O runtime nao substitui o workflow: ele apenas executa os papeis definidos.
- O runtime usa `gpt-5.4` como modelo padrao dos agentes para equilibrar qualidade de codigo e duracao da janela de uso.
- Um modelo especifico pode ser definido por execucao com `CODEX_MODEL`, desde que esteja disponivel para a conta autenticada.
- `gpt-5.4-mini` deve ser preferido para tarefas simples, repetitivas ou de menor risco; `gpt-5.5` deve ficar reservado para revisoes complexas e decisoes criticas.
- Dentro do container, o sandbox interno do Codex e desativado em favor da isolacao do Docker para evitar falhas de namespace com `bubblewrap`.

### 4.3 Fluxo obrigatorio

1. Usuario envia escopo detalhado do problema.
2. `architect` analisa problema, dominio, riscos, requisitos e restricoes.
3. `architect` propoe uma ou mais alternativas de arquitetura com trade-offs.
4. `coordinator` valida se a proposta resolve o problema com robustez, seguranca, escalabilidade, clareza e manutenibilidade.
5. `architect` registra decisoes em `DECISIONS.md`.
6. `architect` atualiza `ARCHITECTURE.md` com a arquitetura aprovada.
7. Contratos sao definidos em `API_CONTRACT.md` quando houver integracao.
8. `backend_dev` e `frontend_dev` implementam apenas tarefas aprovadas em `TASKS.md`.
9. `coordinator` valida integracao, lint, build, testes e documentacao.

## 5. Criterios Para Definir a Arquitetura

A arquitetura deve ser escolhida com base no problema real, nao por preferencia previa de padrao ou framework.

Criticos:

- aderencia ao dominio;
- escalabilidade necessaria;
- seguranca exigida;
- robustez operacional;
- facilidade de manutencao;
- clareza para novos desenvolvedores;
- testabilidade;
- observabilidade;
- custo de operacao;
- tempo de entrega responsavel;
- complexidade essencial versus complexidade acidental.

Perguntas obrigatorias antes da decisao:

- O problema exige modular monolith, monolito simples, arquitetura hexagonal completa ou servicos separados?
- O dominio possui bounded contexts reais ou ainda nao?
- Ha necessidade de processamento assincrono?
- Ha necessidade de auditoria?
- Ha dados sensiveis?
- Ha integracoes externas criticas?
- Quais operacoes precisam ser transacionais?
- Quais consultas precisam de alta performance?
- Quais falhas precisam ser toleradas?

## 6. Diretrizes de Backend

A arquitetura backend sera definida apos o escopo. Ainda assim, qualquer backend deve respeitar:

- separacao clara entre regra de negocio, entrada HTTP, persistencia e integracoes externas;
- validacao de entrada;
- tratamento padronizado de erros;
- transacoes controladas;
- logs estruturados;
- testes unitarios e de integracao;
- migracoes de banco versionadas;
- configuracao externa por ambiente;
- protecao contra vazamento de dados sensiveis.

Padroes candidatos conforme necessidade:

- Repository Pattern.
- Service Pattern.
- Factory Pattern.
- DTO Pattern.
- Dependency Injection.
- Domain Events.
- Specification Pattern.
- Strategy Pattern.

Esses padroes so devem ser usados quando resolverem um problema real de design.

## 7. Diretrizes de Frontend

A arquitetura frontend sera definida apos o escopo. Ainda assim, qualquer frontend Angular deve respeitar:

- organizacao por funcionalidades quando o dominio justificar;
- componentes pequenos e reutilizaveis;
- separacao entre componentes de apresentacao, servicos e estado;
- formularios reativos para fluxos com validacao relevante;
- acessibilidade desde o inicio;
- responsividade;
- tratamento padronizado de loading, empty, error e success states;
- integracao com API por contratos claros;
- testes unitarios, integracao e E2E para fluxos criticos.

Padroes candidatos conforme necessidade:

- Container/Presentational Components.
- Facade para features com estado e integracoes complexas.
- Smart services para orquestracao de chamadas.
- Guards e interceptors para seguranca, autorizacao e cross-cutting concerns.

## 8. Contratos de API

Os contratos devem ser definidos depois do escopo e antes da implementacao integrada.

Principios:

- endpoints orientados ao dominio;
- DTOs explicitos;
- erros padronizados;
- paginacao padronizada quando houver listas;
- versionamento quando houver risco de incompatibilidade;
- idempotencia em operacoes sensiveis;
- exemplos de request e response;
- autenticacao e autorizacao documentadas.

## 9. Testes

Estrategia minima esperada quando houver codigo:

- Backend unitario: dominio, services, use cases, validators e mappers.
- Backend integracao: repositories, controllers, seguranca, banco PostgreSQL com Testcontainers.
- Frontend unitario: componentes, services, pipes, guards e validators.
- Frontend integracao: fluxos com HttpClient mocks e componentes conectados.
- Contrato: compatibilidade entre OpenAPI, backend e cliente frontend.
- E2E: fluxos criticos com Playwright.
- Acessibilidade: validacao automatizada nos fluxos principais.

## 10. CI/CD

Pipeline minimo quando houver codigo:

1. instalar dependencias com cache;
2. executar lint backend;
3. executar lint frontend;
4. executar build backend;
5. executar build frontend;
6. executar testes unitarios;
7. executar testes de integracao;
8. executar testes de contrato;
9. executar E2E nos fluxos criticos quando aplicavel;
10. executar scan de dependencias;
11. publicar artefatos apenas em branches ou tags autorizadas.

## 11. Seguranca

Diretrizes obrigatorias:

- validar toda entrada;
- usar Spring Security quando houver endpoints protegidos;
- aplicar autorizacao no backend, nao apenas no frontend;
- evitar exposicao de stack traces;
- proteger secrets por variaveis de ambiente ou secret manager;
- usar HTTPS em ambientes nao locais;
- registrar eventos relevantes sem vazar dados sensiveis;
- aplicar rate limiting quando houver endpoints publicos ou risco de abuso;
- auditar operacoes sensiveis quando o dominio exigir.

## 12. Observabilidade

Diretrizes obrigatorias:

- logs estruturados;
- correlation id/request id;
- health checks;
- metricas de latencia, erro e throughput;
- rastreabilidade de erros frontend-backend;
- tracing distribuido quando houver necessidade operacional.

## 13. Pendencias Antes da Arquitetura Final

- Receber o escopo detalhado do problema. Status: concluido em 2026-05-24.
- Identificar dominios, fluxos e regras. Status: analise inicial concluida.
- Identificar requisitos nao funcionais. Status: analise inicial concluida.
- Avaliar riscos de seguranca e escala. Status: analise inicial concluida.
- Definir arquitetura apropriada ao problema. Status: proposta candidata registrada, pendente de refinamento de contratos e decisao final.
- Atualizar `DECISIONS.md`. Status: atualizado com decisoes candidatas e restricoes.
- Atualizar `API_CONTRACT.md`. Status: atualizado com contratos preliminares, sem OpenAPI final.
- Atualizar `TASKS.md`. Status: atualizado com proximas tarefas.

## 14. Analise Inicial do Dominio: Controle e Auditoria de Customizacoes

### 14.1 Problema

O produto deve controlar customizacoes oficiais do sistema legado por cliente e verificar se o objeto instalado no banco MySQL do cliente continua equivalente a versao registrada oficialmente. A divergencia entre hash oficial e hash atual deve gerar historico, evidencia auditavel e notificacao ao sistema legado por API REST.

O dominio principal e governanca de customizacoes de sistemas legados, com foco em rastreabilidade, integridade, auditoria e seguranca operacional.

### 14.2 Usuarios e perfis

- Equipe de suporte: registra customizacoes oficiais, acompanha verificacoes e trata divergencias.
- Administrador tecnico interno: configura ambientes, politicas de verificacao, credenciais e integracoes.
- Sistema legado: recebe notificacoes de divergencia e pode consultar status por integracao REST.
- Cliente on-premise: pode executar um agente/conector local ou disponibilizar conectividade controlada, conforme politica aprovada.

### 14.3 Entidades candidatas

- `Customer`: cliente dono das customizacoes.
- `CustomerEnvironment`: ambiente do cliente, com tipo `SAAS` ou `ON_PREMISE`, estado operacional e estrategia de coleta.
- `LegacySystemVersion`: versao do legado relacionada a uma customizacao.
- `Customization`: registro conceitual da customizacao oficial por cliente e ambiente.
- `CustomizationVersion`: versao oficial da customizacao, contendo tipo de objeto, identificador, algoritmo de normalizacao, algoritmo de hash, hash oficial, metadados e responsavel.
- `CustomizationObjectReference`: referencia ao objeto customizado no MySQL, como schema, nome, tipo e parametros de consulta.
- `VerificationRun`: execucao de verificacao com trilha auditavel; no estado atual do scaffold, a execucao manual ja existe no backend.
- `VerificationResult`: resultado por customizacao verificada, com hash atual, status, evidencias e erro quando houver; no estado atual, o backend compara hash informado manualmente contra hash oficial persistido.
- `Divergence`: divergencia auditavel associada a uma verificacao, com severidade, status de tratamento e notificacoes.
- `LegacyNotification`: tentativa de notificacao REST ao legado, com payload, status, retries e correlation id.
- `AuditEvent`: registro de acoes administrativas e eventos relevantes.
- `CredentialReference`: referencia segura para credenciais armazenadas em vault/secret manager, sem persistir segredo em texto claro no PostgreSQL.

Semantica atual de `CustomerEnvironment.collectionMode`:

- `CENTRAL_PULL`: o sistema central inicia a coleta da definicao do objeto ou da evidencia tecnica. Aplica-se a SaaS e a ambientes com conectividade controlada.
- `LOCAL_AGENT_PUSH`: um agente local no ambiente do cliente inicia o envio da evidencia ao sistema central. Aplica-se a on-premise sem conectividade inbound segura ou com restricoes de seguranca.
- `MANUAL_SIGNATURE_UPLOAD`: um operador autorizado informa manualmente a evidencia. Deve ser tratado como contingencia auditavel, nao como fluxo primario de automacao.

### 14.4 Bounded contexts candidatos

- Cadastro e Governanca de Customizacoes: clientes, ambientes, objetos customizados, versoes oficiais e ownership.
- Verificacao de Integridade: coleta de definicoes no MySQL, normalizacao, hashing, comparacao e historico de execucoes.
- Divergencias e Notificacoes: abertura de divergencias, estado de tratamento, notificacao REST ao legado e retry.
- Seguranca e Auditoria: usuarios, papeis, credenciais, trilha de auditoria, logs e politicas de acesso.

Nesta fase, a recomendacao candidata e um modular monolith com arquitetura hexagonal, mantendo esses contextos como modulos internos. Separar em microservicos agora aumentaria custo operacional e complexidade de consistencia sem evidencia de escala independente.

### 14.5 Arquitetura candidata

Backend em Spring Boot com arquitetura hexagonal:

- camada de dominio sem dependencia de Spring;
- casos de uso explicitos para cadastro, versionamento, verificacao, divergencia e notificacao;
- portas de saida para persistencia PostgreSQL, acesso MySQL legado, secret manager, scheduler e cliente REST do legado;
- adapters de entrada REST para UI Angular e integracoes;
- adapters de saida para PostgreSQL, MySQL, vault/secret manager e REST legado.

Frontend Angular:

- organizacao por features: clientes, ambientes, customizacoes, verificacoes, divergencias e auditoria;
- services/facades por feature;
- estados explicitos de loading, erro, vazio e sucesso;
- controle de permissao na UI sem substituir autorizacao backend.

Persistencia do novo sistema:

- PostgreSQL como fonte de verdade para cadastros, versoes oficiais, historico, divergencias, notificacoes e auditoria.
- Migracoes versionadas com Flyway ou Liquibase.
- Indices esperados em cliente, ambiente, tipo de objeto, identificador, versao do legado, status de verificacao e data de execucao.

### 14.6 Alternativas arquiteturais avaliadas

Alternativa A: modular monolith hexagonal.
Beneficios: menor complexidade inicial, consistencia transacional simples, boundaries internos claros, testes integrados mais diretos e deploy menos custoso. Trade-off: exige disciplina modular para evitar acoplamento interno.

Alternativa B: microservicos por contexto.
Beneficios: escala e deploy independentes para verificacao, notificacao e cadastro. Trade-off: aumenta complexidade operacional, mensageria, observabilidade distribuida e consistencia eventual antes de haver evidencia de necessidade.

Alternativa C: sistema central puxando diretamente todos os bancos on-premise.
Beneficios: operacao centralizada e menor instalacao no cliente. Trade-off: falha para clientes sem acesso de rede, amplia superficie de ataque e torna credenciais/onboarding mais sensiveis.

Alternativa D: agente local on-premise enviando assinaturas/hashes ao sistema central.
Beneficios: evita expor banco do cliente para a internet, reduz guarda central de credenciais on-premise e funciona sem inbound para rede do cliente. Trade-off: exige empacotamento, instalacao, versionamento e observabilidade do agente.

Recomendacao candidata:
usar modular monolith hexagonal no sistema central e suportar dois modos de coleta: coleta central para SaaS e ambientes com conectividade controlada; agente/conector local para on-premise sem acesso direto ou com restricoes de seguranca.

### 14.7 Estrategia de hashing e normalizacao

Hash deve ser calculado sobre uma representacao canonica, nao sobre texto bruto capturado diretamente do MySQL.

Pipeline candidato:

1. coletar definicao por tipo de objeto usando metadados estaveis do MySQL, como `information_schema`, `SHOW CREATE FUNCTION`, `SHOW CREATE PROCEDURE`, `SHOW CREATE TRIGGER` ou estrategias especificas por objeto;
2. normalizar encoding, quebras de linha, espacos redundantes, delimitadores, comentarios irrelevantes e casing de keywords quando seguro;
3. remover metadados volateis que nao alteram semantica, como definers, datas geradas e atributos variaveis, quando essa remocao for validada por tipo de objeto;
4. gerar documento canonico com campos ordenados de forma deterministica;
5. calcular hash com SHA-256 no minimo, registrando `hash_algorithm` e `canonicalization_version`.

Para evitar falsos positivos, a normalizacao deve ser versionada e testada com fixtures reais de MySQL. Para evitar falsos negativos, regras de normalizacao nao podem remover conteudo semanticamente relevante sem validacao explicita.

### 14.8 Coleta e verificacao

SaaS:

- o sistema central pode acessar bancos controlados pela empresa por rede privada, credenciais restritas e allowlist.
- verificacoes podem ser agendadas e tambem disparadas manualmente.

On-premise com conectividade direta aprovada:

- acesso somente com credenciais de leitura minima, TLS/VPN/allowlist, timeout curto e auditoria.
- recomendado apenas quando o cliente aprovar tecnicamente o modelo e houver controle de rede.

On-premise sem conectividade direta:

- usar agente/conector local instalado no ambiente do cliente;
- o agente coleta definicoes, calcula hash/canonico localmente ou envia assinatura segura ao sistema central;
- comunicacao deve ser outbound do cliente para o sistema central, com autenticacao forte, rotacao de credenciais e correlation id;
- o sistema central nao deve depender de acesso inbound ao banco do cliente.

Definicao operacional resumida:

- `CENTRAL_PULL`: pull significa que o sistema central abre a sessao de coleta e puxa os dados necessarios.
- `LOCAL_AGENT_PUSH`: push significa que o agente local abre a comunicacao e empurra a evidencia para o sistema central.
- `MANUAL_SIGNATURE_UPLOAD`: nao existe coleta automatica; a evidencia entra por acao humana auditavel.

Estado atual do scaffold:

- a vertical de verificacao manual auditavel ja existe no backend;
- o operador informa `currentHash` manualmente e o sistema compara esse valor com o `officialHash` da `CustomizationVersion`;
- `VerificationRun` e `VerificationResult` sao persistidos no PostgreSQL com `correlationId`, timestamps e status;
- os modos `CENTRAL_PULL`, `LOCAL_AGENT_PUSH` e `MANUAL_SIGNATURE_UPLOAD` ja sao persistidos no cadastro de ambiente, mas ainda nao acionam coletores automaticos distintos;
- ainda nao existe coletor MySQL no backend;
- ainda nao existe canonicalizacao automatica de objetos SQL;
- a abertura automatica de `Divergence` para resultado manual `DIVERGENT` ja existe;
- ainda nao existe notificacao automatica ao legado;
- ainda nao existe agente on-premise executando `LOCAL_AGENT_PUSH`.

### 14.9 Notificacao ao legado

A notificacao de divergencia deve ser assincrona do ponto de vista do caso de uso de verificacao: registrar a divergencia primeiro, persistir uma tentativa de notificacao e executar envio com retry, timeout, backoff e idempotencia.

Payload minimo candidato:

- identificador da divergencia;
- cliente;
- ambiente;
- tipo e identificador do objeto;
- versao oficial esperada;
- hash oficial;
- hash atual;
- data da verificacao;
- severidade;
- correlation id.

### 14.10 Seguranca, auditoria e observabilidade

Seguranca:

- credenciais de bancos de clientes nao devem ser armazenadas em texto claro no PostgreSQL;
- usar secret manager ou vault e persistir apenas referencias;
- principio do menor privilegio para usuarios, APIs e credenciais MySQL;
- Spring Security com RBAC e, quando houver integracao sistema-sistema, credenciais tecnicas rotacionaveis;
- mascaramento de dados sensiveis em logs, erros e auditoria;
- segregacao por cliente para impedir vazamento cross-tenant.

Auditoria:

- registrar criacao, alteracao, aprovacao, desativacao e verificacao de customizacoes;
- registrar quem executou verificacao manual;
- registrar alteracoes de credenciais por referencia, sem expor segredo;
- manter trilha de notificacoes e retries.

Observabilidade:

- logs estruturados com `correlation_id`, `customer_id`, `environment_id`, `verification_run_id` e `divergence_id`;
- metricas de duracao de verificacao, falhas de conexao, divergencias detectadas, retries de notificacao e taxa de sucesso por ambiente;
- health checks para PostgreSQL, fila/scheduler, secret manager e integracoes criticas.

### 14.11 Riscos e mitigacoes

- Acesso direto a bancos on-premise: mitigar com agente local, VPN/allowlist quando aplicavel, credenciais restritas e auditoria.
- Falsos positivos de hash: mitigar com canonicalizacao versionada, fixtures reais e comparacao por tipo de objeto.
- Falsos negativos de hash: mitigar impedindo normalizacao agressiva e mantendo evidencias canonicas.
- Indisponibilidade on-premise: mitigar com timeout, retry, status `UNREACHABLE`, janela de verificacao e historico de falhas.
- Vazamento de credenciais: mitigar com secret manager, rotacao, mascaramento e minimo privilegio.
- Acoplamento ao legado: mitigar com portas/adapters e contrato REST versionado.
- Volume alto de customizacoes: mitigar com processamento em lotes, indices, paginacao, agendamento por janela e paralelismo controlado.

### 14.12 Perguntas pendentes

- Quais versoes de MySQL/MariaDB precisam ser suportadas?
- O legado consegue receber notificacoes com idempotency key?
- Existe sistema corporativo de identidade, SSO ou RBAC ja definido?
- Qual secret manager/vault esta disponivel na infraestrutura alvo?
- Qual volume esperado de clientes, ambientes, customizacoes e verificacoes por dia?
- Clientes on-premise aceitam instalar agente local?
- O conteudo completo da customizacao pode ser armazenado no novo sistema ou apenas assinatura/hash por restricao contratual?
- Ha exigencia formal de retencao de auditoria e evidencias?
- Quais objetos de tela customizada existem alem de objetos SQL no MySQL?

## 15. Validacao do Coordinator

Status em 2026-05-24:
a proposta candidata do `architect` esta aprovada para continuidade do refinamento arquitetural, mas ainda nao esta aprovada para scaffold ou implementacao.

Validacao tecnica:

- O dominio principal foi corretamente identificado como governanca, integridade e auditoria de customizacoes do legado por cliente e ambiente.
- As entidades candidatas cobrem os conceitos essenciais: cliente, ambiente, customizacao, versao oficial, referencia de objeto, verificacao, resultado, divergencia, notificacao, auditoria e referencia segura de credencial.
- A recomendacao de modular monolith hexagonal e coerente para o momento atual, pois preserva boundaries e testabilidade sem introduzir custo operacional prematuro de microservicos.
- A estrategia de hashing sobre representacao canonica versionada e obrigatoria para reduzir falsos positivos por formatacao SQL e para manter rastreabilidade tecnica.
- A coleta hibrida e necessaria: `CENTRAL_PULL` atende SaaS e ambientes controlados; `LOCAL_AGENT_PUSH` atende on-premise sem inbound seguro; `MANUAL_SIGNATURE_UPLOAD` pode existir apenas como contingencia auditavel.
- A notificacao ao legado deve ser desacoplada da verificacao por fila/tabela de tentativas, retry, timeout, backoff, idempotencia e correlation id.
- A seguranca ainda e o principal gate: credenciais de clientes nao podem ficar em texto claro no PostgreSQL, e o modelo de autorizacao, segregacao por cliente e auditoria precisa ser fechado antes do scaffold.

Bloqueios para arquitetura final:

- Definir regras de canonicalizacao por tipo de objeto MySQL.
- Definir matriz de coleta para SaaS, on-premise com conectividade direta e on-premise sem conectividade direta.
- Definir secret manager/vault ou fallback controlado para ambiente local.
- Definir papeis, permissoes e politica de auditoria.
- Formalizar OpenAPI 3.1 com exemplos, paginacao, filtros, erros, idempotencia e contratos de agente/legado.
- Confirmar volumes esperados para avaliar paralelismo, indices, particionamento futuro e limites de agendamento.

Decisao operacional:
`backend_dev` e `frontend_dev` continuam bloqueados. Nenhum codigo de aplicacao deve ser criado ate a aprovacao final da arquitetura e dos contratos pelo `coordinator`.

## 16. Scaffold Inicial Implementado

Status em 2026-05-24:
o usuario aprovou a arquitetura proposta e autorizou o inicio do desenvolvimento. O scaffold inicial foi criado com fluxos verticais de clientes, ambientes, customizacoes e versoes oficiais.

Estrutura:

- `apps/backend`: Spring Boot 3.5, Java 17, Maven, arquitetura hexagonal por contexto.
- `apps/frontend`: Angular 20, organizacao inicial por features e camada `core/api`.
- `docker-compose.yml`: profile `app` com PostgreSQL, backend e frontend; profile `agents` para agentes Codex.
- `Makefile`: comandos operacionais de lint, build, testes e stack local.

Backend implementado:

- Dominio `Customer` e `CustomerStatus`.
- Dominio `CustomerEnvironment`, `EnvironmentType`, `CollectionMode` e `EnvironmentStatus`.
- Dominio `Customization`, `CustomizationObjectType` e `CustomizationStatus`.
- Dominio `CustomizationVersion` e `CustomizationVersionStatus`.
- Caso de uso `RegisterCustomerService`.
- Caso de uso `RegisterCustomerEnvironmentService`.
- Caso de uso `RegisterCustomizationService`.
- Caso de uso `RegisterCustomizationVersionService`.
- Porta `CustomerRepository`.
- Porta `CustomerEnvironmentRepository`.
- Porta `CustomizationRepository`.
- Porta `CustomizationVersionRepository`.
- Adapter JPA/PostgreSQL `PostgresCustomerRepository`.
- Adapter JPA/PostgreSQL `PostgresCustomerEnvironmentRepository`.
- Adapter JPA/PostgreSQL `PostgresCustomizationRepository`.
- Adapter JPA/PostgreSQL `PostgresCustomizationVersionRepository`.
- Controller REST `CustomerController`.
- Controller REST `CustomerEnvironmentController`.
- Controller REST `CustomizationController`.
- Controller REST `CustomizationVersionController`.
- Migração Flyway `V1__create_customers.sql`.
- Migracao Flyway `V2__create_customer_environments.sql`.
- Migracao Flyway `V3__create_customizations.sql`.
- Migracao Flyway `V4__create_customization_versions.sql`.
- Testes unitarios e web slice para clientes.
- Testes unitarios e web slice para ambientes.
- Testes unitarios e web slice para customizacoes.
- Testes unitarios e web slice para versoes oficiais.
- Perfil `local` permissivo para desenvolvimento Docker; perfil padrao mantem Resource Server JWT e RBAC por escopos.
- Lombok usado para reduzir boilerplate em objetos de dominio e entidades JPA sem remover invariantes do dominio.
- Springdoc OpenAPI exposto em `/v3/api-docs` e Swagger UI em `/swagger-ui.html`.
- `GlobalExceptionHandler` com timestamp via `Clock` injetado e `TestSecurityConfig` cobrindo o mesmo contrato nos web slice tests para manter o pipeline local consistente.

Frontend implementado:

- Dashboard inicial com menu lateral por etapas, mantendo rota unica e contexto selecionado visivel.
- `CustomerService` para API REST.
- Formulario de cadastro de cliente.
- Tabela de listagem de clientes.
- Formulario de cadastro de ambiente por cliente.
- Tabela de listagem de ambientes do cliente selecionado.
- Formulario de cadastro de customizacao vinculada a cliente e ambiente.
- Tabela de listagem de customizacoes oficiais.
- Formulario de registro de versao oficial com hash, algoritmo e canonicalizacao.
- Tabela de listagem de versoes oficiais da customizacao selecionada.
- Formulario de verificacao manual auditavel a partir da versao oficial selecionada.
- Tabela de historico de verificacoes com resultado embutido.
- Tabela de divergencias persistidas por cliente e ambiente.
- Acoes de reconhecimento e resolucao de divergencias diretamente na etapa Angular de divergencias.
- Tabela de `legacy-notifications` persistidas por cliente e ambiente.
- Estados basicos de loading, erro e vazio.

Backend adicional implementado:

- Dominio `VerificationRun`, `VerificationResult`, `VerificationRunStatus`, `VerificationResultStatus` e `VerificationTriggerType`.
- Caso de uso `CreateVerificationRunService`.
- Query `ListVerificationRunsQuery`.
- Query `ListVerificationRunsQuery` com filtros opcionais por cliente e ambiente.
- Portas `VerificationRunRepository` e `VerificationResultRepository`.
- Adapters JPA/PostgreSQL `PostgresVerificationRunRepository` e `PostgresVerificationResultRepository`.
- Controller REST `VerificationRunController`.
- Controller REST `DivergenceController`.
- Controller REST `LegacyNotificationController`.
- Caso de uso `UpdateDivergenceStatusService`.
- Query `ListLegacyNotificationsQuery`.
- Migracao Flyway `V5__create_verification_runs.sql`.
- Migracao Flyway `V6__create_verification_results.sql`.
- Migracao Flyway `V7__create_divergences.sql`.
- Migracao Flyway `V8__create_legacy_notifications.sql`.
- Testes unitarios do caso de uso de verificacao manual.
- Testes unitarios da query `ListVerificationRunsQuery`.
- Testes web slice do `VerificationRunController`.
- Testes unitarios e web slice da vertical de divergencias persistidas.
- Testes unitarios e web slice da transicao de status de divergencias.
- Testes unitarios e web slice da vertical de `legacy-notifications`.
- Validacao HTTP real da vertical concluida contra a stack Docker para criacao, listagem e detalhe de `verification-runs`.
- Validacao HTTP real da vertical concluida contra a stack Docker para criacao automatica, listagem filtrada e detalhe de `divergences`.
- Validacao HTTP real da vertical concluida contra a stack Docker para `ACKNOWLEDGED` e `RESOLVED` em `divergences`.
- Validacao HTTP real da vertical concluida contra a stack Docker para criacao automatica, listagem filtrada e detalhe de `legacy-notifications`.

Escopo funcional desta vertical:

- verificacao manual auditavel por comparacao entre `currentHash` informado e `officialHash` registrado;
- persistencia de `VerificationRun` e `VerificationResult`;
- abertura automatica de `Divergence` quando o resultado for `DIVERGENT`;
- abertura automatica de `LegacyNotification` com `status=PENDING` quando a divergencia for criada;
- transicao manual de `Divergence` para `ACKNOWLEDGED` e `RESOLVED` por endpoint REST;
- um `VerificationResult` por `VerificationRun` nesta fase manual;
- historico consultavel por API REST;
- historico consultavel por API REST com filtros minimos por cliente e ambiente;
- outbox persistido para notificacao ao legado, ainda sem dispatcher HTTP, retry ou backoff nesta fase.

Validacao executada:

- `docker compose config --quiet`.
- Backend `mvn -q verify` em container Maven.
- Frontend `npm run lint`.
- Frontend `npm run build`.
- Stack local `docker compose --profile app up --build -d`.
- `GET /actuator/health` retornou `UP`.
- `POST /api/v1/customers` retornou `201`.
- `GET /api/v1/customers` retornou cliente persistido.
- Flyway aplicou V2 para `customer_environments`.
- `POST /api/v1/customers/{customerId}/environments` retornou `201`.
- `GET /api/v1/customers/{customerId}/environments` retornou ambiente persistido.
- Flyway aplicou V3 para `customizations`.
- `POST /api/v1/customizations` retornou `201`.
- `GET /api/v1/customizations` retornou customizacao persistida.
- Flyway aplicou V4 para `customization_versions`.
- `POST /api/v1/customizations/{customizationId}/versions` retornou `201`.
- `GET /api/v1/customizations/{customizationId}/versions` retornou versao oficial persistida.
- `GET /v3/api-docs` retornou especificacao OpenAPI.
- OpenAPI inclui `/api/v1/customizations`, `CreateCustomizationRequest` e `CustomizationResponse`.
- OpenAPI inclui `/api/v1/customizations/{customizationId}/versions`, `CreateCustomizationVersionRequest` e `CustomizationVersionResponse`.
- OpenAPI inclui `/api/v1/verification-runs`, `CreateVerificationRunRequest` e `VerificationRunResponse`.
- `GET /swagger-ui.html` redirecionou para a UI e retornou `200`.
- Frontend respondeu `200 OK` em `http://localhost:4200`.
- Flyway aplicou V5 para `verification_runs`.
- Flyway aplicou V6 para `verification_results`.

Pendencias tecnicas:

- Instalar/configurar Chrome ou Chromium para executar Karma localmente.
- Formalizar OpenAPI 3.1.
- Registrar validacao HTTP real dos endpoints de verificacao manual pelo `coordinator`.
- Implementar canonicalizacao/hashing automatico, divergencias e auditoria apos fechamento dos gates atuais.
- Definir canonicalizacao SQL por tipo de objeto antes de implementar hashing.
- Definir secret manager/vault antes de implementar credenciais reais.

## 18. Plano Coordenado da Vertical de Verificacao Manual

Status em 2026-05-25:
a vertical de verificacao manual esta implementada no backend com dominio, caso de uso, persistencia, endpoints REST, filtros minimos, abertura automatica de divergencia e cobertura adicional de query/controller. A validacao HTTP real ja foi registrada pelo `coordinator`.

Decisao de coordenacao:
antes de evoluir para coletor MySQL, canonicalizacao automatica ou notificacao outbound ao legado, a equipe deve fechar a fatia operacional atual com divergencia persistida, integracao Angular, testes e contrato refinado.

Delegacoes registradas:

- `architect`: refinar o contrato da verificacao manual, explicitar limites da fase sem coletor e preparar a transicao futura para `OBJECT_NOT_FOUND`, `UNREACHABLE` e `ERROR` sem quebrar o contrato.
- `backend_dev`: concluir a robustez da API de verificacao manual com testes web slice, correcoes de compilacao/servico e coerencia de persistencia PostgreSQL.
- `frontend_dev`: implementar a feature Angular de verificacoes manuais com formulario de disparo, listagem historica e detalhamento de resultado.
- `coordinator`: validar build, lint, testes, integracao e sincronia documental antes de liberar a proxima vertical.

## 17. Politica de Uso de Modelo

Status em 2026-05-24:
os agentes Docker passam a usar `gpt-5.4` por padrao.

Motivo:
`gpt-5.5` foi escolhido automaticamente pelo Codex CLI quando os agentes estavam em `model: default`, mas esse modelo consome mais rapidamente a janela de uso. `gpt-5.4` preserva qualidade suficiente para desenvolvimento diario, revisao de codigo e manutencao da arquitetura, com melhor duracao operacional.

Politica:

- Padrao dos agentes: `gpt-5.4`.
- Tarefas simples, documentacao mecanica e validacoes repetitivas: `CODEX_MODEL=gpt-5.4-mini`.
- Revisoes arquiteturais complexas, debugging dificil ou decisoes de alto risco: `CODEX_MODEL=gpt-5.5` sob demanda.
- O script `scripts/run-agent.sh` continua aceitando override por `CODEX_MODEL`.
