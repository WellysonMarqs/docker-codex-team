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

## ADR-009: Runtime Docker com coordinator como orquestrador

Status: Aceita.

Decisao:
Criar um runtime Docker baseado em `agents.yml`, com `coordinator` como ponto de entrada principal e `architect`, `backend_dev` e `frontend_dev` como agentes especializados executaveis sob demanda.

Motivo:
O projeto precisa de um mecanismo operacional claro para materializar o workflow multi-agent definido na documentacao, mantendo uma unica fonte de verdade para papeis e instrucoes.

Beneficios:
- padroniza a execucao dos agentes;
- usa `agents.yml` como fonte primaria;
- preserva o `coordinator` como orquestrador;
- permite validar cada agente isoladamente;
- evita misturar runtime de agentes com codigo da aplicacao.

Trade-offs:
- o build da imagem depende de acesso a registries de Docker e npm;
- a execucao real depende de autenticacao do Codex no `CODEX_HOME` montado;
- a orquestracao logica ainda depende das instrucoes e do fluxo do `coordinator`.

Alternativas descartadas:
- Rodar agentes sem Docker: descartado como caminho principal por reduzir padronizacao do ambiente.
- Criar quatro imagens diferentes: descartado por duplicar runtime sem necessidade.
- Implementar codigo de aplicacao junto com runtime: descartado para manter separacao de responsabilidades.
