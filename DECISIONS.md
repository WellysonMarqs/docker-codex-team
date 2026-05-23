# Decisoes Tecnicas

Este documento registra decisoes tecnicas e arquiteturais. Decisoes de arquitetura de aplicacao devem ser tomadas somente apos o envio e analise do escopo detalhado do problema que o projeto ira resolver.

## ADR-001: Nao fechar arquitetura antes do escopo do problema

Status: Aceita.

Decisao:
A arquitetura final do sistema nao sera definida, sugerida como definitiva ou implementada antes do recebimento do escopo detalhado do projeto.

Motivo:
Arquitetura deve resolver um problema real. Sem entender o problema, usuarios, regras de negocio, restricoes, riscos, volume, seguranca e objetivos, qualquer decisao arquitetural seria prematura.

Beneficios:
- evita solucao improvisada;
- evita overengineering;
- evita arquitetura desconectada do dominio;
- aumenta robustez e manutenibilidade;
- melhora clareza para outros desenvolvedores;
- favorece decisoes tecnicas defensaveis.

Trade-offs:
- nao ha scaffold executavel neste momento;
- o projeto avanca primeiro por descoberta e modelagem;
- algumas decisoes ficam intencionalmente pendentes.

Alternativas descartadas:
- Definir arquitetura completa agora: descartada por ausencia de escopo.
- Criar scaffold generico: descartado por risco de induzir decisoes erradas.

## ADR-002: Backend obrigatorio em Java 17 com Spring Boot estavel

Status: Aceita.

Decisao:
O backend sera construido com Java 17 e Spring Boot em uma versao estavel compativel com Java 17.

Versao sugerida para planejamento atual:
Spring Boot 3.5.x estavel.

Motivo:
Java 17 e uma base madura para backend corporativo. Spring Boot oferece ecossistema consolidado para APIs, seguranca, persistencia, validacao, testes, configuracao, observabilidade e operacao em producao.

Beneficios:
- ecossistema maduro;
- forte suporte a seguranca;
- suporte amplo a testes;
- compatibilidade com PostgreSQL;
- facilidade para outros desenvolvedores Java;
- boa base para sistemas robustos e mantiveis.

Trade-offs:
- mais configuracao e disciplina arquitetural que stacks minimalistas;
- exige cuidado para nao acoplar regra de negocio ao framework;
- Spring Boot 4.x deve ser avaliado com cautela por compatibilidade de bibliotecas.

Alternativas descartadas:
- Node.js/Fastify: descartado por decisao de stack do projeto.
- Python: descartado por decisao de stack do projeto.
- Go: descartado por decisao de stack do projeto.

## ADR-003: Banco de dados obrigatorio PostgreSQL

Status: Aceita.

Decisao:
O banco relacional principal sera PostgreSQL.

Motivo:
PostgreSQL e robusto, maduro, escalavel e adequado para sistemas transacionais com integridade, consultas complexas, indices avancados e recursos de consistencia.

Beneficios:
- confiabilidade;
- suporte a transacoes;
- integridade referencial;
- ecossistema amplo;
- bom suporte em Spring;
- otimo suporte para testes com Testcontainers.

Trade-offs:
- exige modelagem cuidadosa;
- exige estrategia de indices e migracoes;
- pode demandar tuning conforme carga.

Alternativas descartadas:
- Bancos NoSQL como base principal: descartados ate que o escopo justifique.
- Banco em memoria ou arquivo: descartado para produto robusto.

## ADR-004: Frontend obrigatorio em Angular

Status: Aceita.

Decisao:
O frontend sera construido com Angular em versao estavel e suportada oficialmente.

Versao sugerida para planejamento atual:
Angular 21.x para projeto novo quando as dependencias forem compativeis. Angular 20.x LTS permanece como alternativa conservadora caso estabilidade de bibliotecas seja prioridade.

Motivo:
Angular fornece uma estrutura opinionada, consistente e adequada para equipes, com roteamento, formularios, HTTP, DI, CLI, padroes de organizacao e boas praticas de manutencao.

Beneficios:
- padronizacao;
- boa manutenibilidade;
- arquitetura frontend consistente;
- CLI forte;
- suporte empresarial;
- facilidade de onboarding para desenvolvedores Angular.

Trade-offs:
- maior curva de aprendizado;
- atualizacoes de major exigem disciplina;
- arquitetura frontend deve evitar excesso de boilerplate.

Alternativas descartadas:
- React: descartado por decisao de stack do projeto.
- Vue: descartado por decisao de stack do projeto.
- Frontend sem framework: descartado por baixa escalabilidade para aplicacoes complexas.

## ADR-005: Contratos devem nascer apos entendimento do dominio

Status: Aceita.

Decisao:
Os contratos de API serao definidos apenas apos o escopo detalhado e a modelagem inicial do dominio.

Motivo:
Contratos devem expressar capacidades de negocio e fluxos reais. Definir endpoints antes de entender o problema aumenta risco de API procedural, acoplada ou incompleta.

Beneficios:
- API mais alinhada ao dominio;
- menos retrabalho;
- melhor integracao frontend-backend;
- testes de contrato mais relevantes.

Trade-offs:
- `API_CONTRACT.md` fica pendente ate haver escopo;
- frontend e backend ainda nao podem ser implementados de forma integrada.

Alternativas descartadas:
- Criar CRUD generico antecipado: descartado por nao resolver necessariamente o problema real.

## ADR-006: Testes obrigatorios em camadas

Status: Aceita.

Decisao:
Quando houver codigo, a estrategia deve incluir testes unitarios, integracao, contrato, E2E e acessibilidade conforme aplicavel.

Motivo:
O projeto prioriza robustez, seguranca, escalabilidade e manutencao. Isso exige validacao automatizada, nao apenas revisao manual.

Beneficios:
- reducao de regressao;
- validacao de arquitetura;
- confianca em refatoracoes;
- melhor qualidade de integracao;
- suporte a CI/CD profissional.

Trade-offs:
- pipeline mais longo;
- maior custo inicial de configuracao;
- exige disciplina na escrita de testes.

Alternativas descartadas:
- Testes apenas manuais: descartados por baixa confiabilidade.
- Testes apenas unitarios: descartados por nao validar integracao real.

## ADR-007: CI/CD com quality gates obrigatorios

Status: Aceita.

Decisao:
O pipeline devera validar lint, build, testes e contratos antes de aceitar alteracoes.

Motivo:
Um workflow multi-agent precisa de verificacoes objetivas para impedir regressao, inconsistencias e entregas incompletas.

Beneficios:
- qualidade repetivel;
- feedback rapido;
- governanca tecnica;
- menor risco de quebra em integracao.

Trade-offs:
- configuracao inicial maior;
- necessidade de manter pipeline rapido e confiavel.

Alternativas descartadas:
- Validacao manual como gate principal: descartada por nao ser auditavel nem escalavel.

## ADR-008: Decisoes devem priorizar solucao do problema

Status: Aceita.

Decisao:
Todas as decisoes tecnicas devem ser justificadas pela capacidade de resolver o problema do projeto com escalabilidade, seguranca, robustez, clareza e manutencao.

Motivo:
O objetivo nao e aplicar padroes por formalidade, mas resolver problemas reais com engenharia de software profissional.

Beneficios:
- evita complexidade acidental;
- preserva foco no valor do produto;
- torna decisoes compreensiveis;
- melhora evolucao por outros desenvolvedores.

Trade-offs:
- algumas decisoes exigirao mais analise inicial;
- pode haver necessidade de comparar alternativas antes de implementar.

Alternativas descartadas:
- Escolher padroes por preferencia pessoal: descartado.
- Implementar rapidamente sem analise: descartado.

## ADR-009: Docker Agent oficial com coordinator como orquestrador

Status: Aceita.

Decisao:
Usar o Docker Agent oficial com `agents.yml` como configuracao do time multi-agent. O agente `root` atua como coordinator para permitir o comando padrao `docker agent run agents.yml`, e o agente `coordinator` tambem fica disponivel para execucao explicita.

Motivo:
O Docker Agent e o framework oficial da Docker para definir e executar times de agentes especializados por YAML, com delegacao via `sub_agents`.

Beneficios:
- segue a documentacao oficial da Docker;
- usa `docker agent run agents.yml` como comando principal;
- preserva `agents.yml` como fonte primaria;
- permite delegacao hierarquica para architect, backend_dev e frontend_dev;
- evita runtime customizado desnecessario;
- permite gerar imagem com `docker agent build agents.yml`.

Trade-offs:
- depende do Docker Agent instalado no ambiente;
- a execucao real depende do provider configurado; no modo atual, depende do Docker Model Runner local;
- o agente padrao precisa se chamar `root`, por isso o comportamento de coordinator fica nesse agente.

Alternativas descartadas:
- Runtime customizado com Dockerfile e Docker Compose: descartado porque o objetivo e usar o Docker Agent oficial.
- Criar quatro imagens diferentes: descartado porque Docker Agent ja suporta time multi-agent em um unico YAML.
- Implementar codigo de aplicacao junto com configuracao de agentes: descartado para manter separacao de responsabilidades.

## ADR-010: Modo hibrido com Docker Model Runner e Codex CLI

Status: Aceita.

Decisao:
Usar Docker Model Runner local como provider principal do Docker Agent e permitir que o coordinator chame o Codex CLI local via shell quando precisar de apoio especializado de coding, revisao, refatoracao ou validacao tecnica.

Motivo:
O objetivo e evitar dependencia obrigatoria de `OPENAI_API_KEY` no Docker Agent, mantendo o Docker Agent como orquestrador multi-agent e usando o Codex CLI local autenticado pelo usuario como ferramenta auxiliar quando necessario.

Beneficios:
- reduz dependencia de API key para o fluxo principal do Docker Agent;
- mantem o Docker Agent oficial como runtime de orquestracao;
- permite usar modelo local com Docker Model Runner;
- permite aproveitar o Codex CLI local para tarefas de coding mais especificas;
- preserva o controle do coordinator sobre delegacao, arquitetura, tarefas e documentacao.

Trade-offs:
- depende do Docker Model Runner estar habilitado e com modelo local baixado;
- qualidade do raciocinio padrao depende do modelo local escolhido;
- chamadas ao Codex CLI continuam dependendo da autenticacao local do Codex;
- o Codex CLI deve ser usado como ferramenta auxiliar, nao como substituto do fluxo multi-agent.

Alternativas descartadas:
- Usar apenas `OPENAI_API_KEY`: descartado como caminho principal porque a preferencia atual e rodar com modelo local.
- Usar apenas Codex CLI sem Docker Agent: descartado porque o objetivo e manter Docker Agent como orquestrador.
- Criar provider customizado para Codex CLI: descartado por fragilidade e falta de suporte oficial.
