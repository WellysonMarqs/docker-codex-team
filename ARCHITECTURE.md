# Arquitetura do Projeto

## 1. Contexto

Este workspace inicia um produto ainda sem codigo de aplicacao. A configuracao atual define uma equipe multi-agent com os papeis `root`, `architect`, `backend_dev`, `frontend_dev` e `qa`, orientada por Clean Code, SOLID, Clean Architecture, Arquitetura Hexagonal, DDD, testes automatizados, seguranca, performance, escalabilidade e observabilidade.

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

Antes desse escopo, este documento deve conter apenas diretrizes, criterios de decisao e um processo para chegar a arquitetura correta.

## 3. Diretrizes Tecnicas Iniciais

Tecnologias, frameworks, banco de dados, bibliotecas e ferramentas de suporte nao devem ser definidos como obrigatorios antes da analise do problema.

Ainda assim, qualquer stack escolhida futuramente deve atender aos seguintes criterios:

- aderencia ao dominio e aos fluxos principais;
- suporte adequado a seguranca, observabilidade e testes automatizados;
- facilidade de manutencao e onboarding;
- compatibilidade com os requisitos de integracao;
- maturidade operacional e ecossistema suficiente para o problema;
- capacidade de evolucao sem acoplamento desnecessario;
- custo operacional coerente com a necessidade real do produto.

Quando a arquitetura for aprovada, este documento devera registrar:

- stack backend escolhida e justificativa;
- stack frontend escolhida e justificativa;
- estrategia de persistencia e justificativa;
- estrategia de contratos e integracoes;
- estrategia de ambiente local, build, testes e entrega.

## 4. Workflow Multi-Agent Profissional

### 4.1 Papeis

- `root`: coordena o fluxo, valida qualidade, integra entregas, bloqueia decisoes frageis e garante documentacao.
- `architect`: analisa o problema, modela dominio, avalia trade-offs e propoe arquitetura apos receber o escopo.
- `backend_dev`: implementa backend somente apos aprovacao da arquitetura, da stack e dos contratos.
- `frontend_dev`: implementa frontend somente apos aprovacao da estrategia de UX, arquitetura frontend, stack e contratos.
- `qa`: define estrategia, implementa e executa a frente de testes, registra evidencias, reporta defeitos e emite parecer objetivo sobre quality gates.

### 4.2 Docker Agent Oficial

O projeto usa o Docker Agent oficial para carregar os agentes definidos em `agents.yml`.

Formato operacional:

- `agents.yml` e o arquivo de configuracao do time multi-agent.
- `root` e o agente de entrada padrao exigido para `docker agent run agents.yml`.
- `architect`, `backend_dev`, `frontend_dev` e `qa` sao sub-agentes especializados.
- Os documentos obrigatorios sao adicionados ao contexto via `add_prompt_files`.
- Os agentes usam toolsets oficiais como `filesystem`, `shell`, `think` e `todo`.

Regra operacional:

- O comando principal e `docker agent run agents.yml`.
- O `root` atua como coordinator e delega tarefas por `sub_agents`.
- `architect`, `backend_dev`, `frontend_dev` e `qa` devem atuar dentro dos limites definidos em `agents.yml`.
- O Docker Agent gerencia a coordenacao e as delegacoes.
- O provider principal do Docker Agent e OpenAI, autenticado por `OPENAI_API_KEY`.
- O modelo configurado em `agents.yml` e `openai-main`, apontando para `gpt-5.4`.
- Nenhuma implementacao deve iniciar sem escopo, arquitetura aprovada e tarefa rastreavel.

Fluxo operacional:

```text
Docker Agent
  -> OpenAI API
  -> root
      -> architect
      -> backend_dev
      -> frontend_dev
      -> qa
```

### 4.3 Fluxo obrigatorio

1. Usuario envia escopo detalhado do problema.
2. `architect` analisa problema, dominio, riscos, requisitos e restricoes.
3. `architect` propoe uma ou mais alternativas de arquitetura com trade-offs.
4. `coordinator` valida se a proposta resolve o problema com robustez, seguranca, escalabilidade, clareza e manutenibilidade.
5. `architect` registra decisoes em `DECISIONS.md`.
6. `architect` atualiza `ARCHITECTURE.md` com a arquitetura aprovada.
7. Contratos sao definidos em `API_CONTRACT.md` quando houver integracao.
8. `backend_dev` e `frontend_dev` implementam apenas tarefas aprovadas em `TASKS.md`.
9. `qa` executa a estrategia de testes, valida quality gates, registra evidencias, reporta falhas ou lacunas e emite parecer objetivo sobre a entrega.
10. `coordinator` valida integracao, lint, build, testes e documentacao.

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
- Quais tecnologias atendem melhor aos requisitos sem antecipar complexidade?
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
- migracoes de banco versionadas quando houver persistencia relacional;
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

A arquitetura frontend sera definida apos o escopo. Ainda assim, qualquer frontend escolhido deve respeitar:

- organizacao por funcionalidades quando o dominio justificar;
- componentes pequenos e reutilizaveis;
- separacao entre componentes de apresentacao, servicos e estado;
- estrategia consistente de formularios e validacao para fluxos relevantes;
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
- Backend integracao: repositories, controllers, seguranca e persistencia realista para o ambiente escolhido.
- Frontend unitario: componentes, services, pipes, guards e validators.
- Frontend integracao: fluxos com mocks, stubs ou ambientes de teste aderentes a arquitetura escolhida.
- Contrato: compatibilidade entre os contratos de integracao aprovados e os componentes consumidores.
- E2E: fluxos criticos com Playwright.
- Acessibilidade: validacao automatizada nos fluxos principais.

Entregaveis minimos esperados do QA:

- estrategia ou plano de testes atualizado por entrega, feature ou sprint;
- matriz resumida relacionando fluxo ou requisito, tipo de teste e status;
- evidencias objetivas de execucao, como comandos, logs, CI, screenshots ou registros reproduziveis;
- defeitos registrados com passos, esperado versus atual, severidade e evidencia;
- parecer final no formato aprovado, aprovado com ressalvas ou bloqueado.

Quando nao houver codigo, ambiente ou dados disponiveis, o QA deve produzir:

- plano de testes;
- premissas e dependencias;
- riscos;
- criterios de pronto para teste.

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
- usar mecanismo de autenticacao e autorizacao compativel com a arquitetura aprovada quando houver endpoints protegidos;
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

- Receber o escopo detalhado do problema.
- Identificar dominios, fluxos e regras.
- Identificar requisitos nao funcionais.
- Avaliar riscos de seguranca e escala.
- Definir arquitetura apropriada ao problema.
- Atualizar `DECISIONS.md`.
- Atualizar `API_CONTRACT.md`.
- Atualizar `TASKS.md`.
