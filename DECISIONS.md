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

## ADR-010: Runtime deve usar o modelo padrao do Codex CLI local

Status: Superada por ADR-010A.

Decisao:
Os agentes definidos em `agents.yml` passam a usar `model: default`, e o script `scripts/run-agent.sh` so envia `--model` ao Codex CLI quando `CODEX_MODEL` ou o agente definirem explicitamente um modelo diferente de `default`.

Motivo:
O modelo `openai/gpt-5` nao esta disponivel para toda forma de autenticacao do Codex CLI. Fixar esse modelo no runtime impedia a execucao local mesmo quando a conta possuia outros modelos validos.

Beneficios:
- reutiliza a autenticacao e a configuracao local do Codex;
- evita falhas por modelo indisponivel na conta;
- permite override controlado por `CODEX_MODEL`;
- preserva o runtime multi-agent sem acoplar o projeto a um modelo especifico.

Trade-offs:
- execucoes em maquinas diferentes podem usar modelos padrao diferentes;
- validacoes reproduziveis devem informar explicitamente `CODEX_MODEL`.

Alternativas descartadas:
- Trocar para outro modelo fixo: descartado porque a disponibilidade tambem depende da conta.
- Remover configuracao de modelo do `agents.yml`: descartado para manter a possibilidade de modelos especificos por agente no futuro.

## ADR-010A: Runtime usa gpt-5.4 como padrao operacional

Status: Aceita.

Decisao:
Configurar `gpt-5.4` como modelo padrao em `agents.yml` para `coordinator`, `architect`, `backend_dev` e `frontend_dev`.

Motivo:
Quando os agentes estavam em `model: default`, o Codex CLI selecionou `gpt-5.5`. Esse modelo e mais forte, mas consome mais rapidamente a janela de uso de 5 horas. `gpt-5.4` e uma opcao local disponivel descrita como forte para codificacao cotidiana, equilibrando qualidade e duracao.

Beneficios:
- aumenta a duracao util da janela de uso;
- mantem qualidade adequada para desenvolvimento, testes e documentacao;
- preserva override por `CODEX_MODEL` para tarefas que precisem de `gpt-5.5` ou `gpt-5.4-mini`;
- torna a execucao dos agentes mais previsivel.

Trade-offs:
- tarefas de arquitetura muito complexas podem exigir override temporario para `gpt-5.5`;
- tarefas simples ainda podem ser mais baratas com `gpt-5.4-mini`.

Alternativas avaliadas:
- Manter `default`: descartado porque voltou a selecionar `gpt-5.5`.
- Usar `gpt-5.4-mini` como padrao: adiado por risco de reduzir qualidade em implementacoes backend/frontend mais sensiveis.

## ADR-020: Ambientes de cliente como proximo fluxo vertical

Status: Aceita.

Decisao:
Implementar `CustomerEnvironment` imediatamente apos `Customer`, dentro do mesmo contexto de Cadastro e Governanca de Customizacoes.

Motivo:
Ambientes sao pre-requisito para customizacoes oficiais, coleta SaaS/on-premise, credenciais por ambiente e verificacoes futuras. Esse fluxo permite materializar a estrategia de coleta hibrida sem implementar ainda conectores MySQL, agente on-premise ou hashing.

Beneficios:
- prepara a modelagem para SaaS e on-premise;
- valida relacionamento cliente-ambiente no PostgreSQL;
- cria base para customizacoes e verificacoes;
- mantem evolucao incremental e testavel.

Trade-offs:
- `credentialReferenceId` ainda e apenas referencia textual, pois secret manager/vault final nao foi definido;
- regras avancadas por tipo de ambiente ainda serao refinadas em tarefas posteriores.

Alternativas avaliadas:
- Implementar customizacoes antes de ambientes: descartado porque customizacao depende de cliente e ambiente.
- Implementar conector MySQL agora: descartado porque canonicalizacao e estrategia segura de coleta ainda nao foram fechadas.

## ADR-021: Lombok para reduzir boilerplate no backend

Status: Aceita.

Decisao:
Usar Lombok no backend para reduzir getters, construtores e boilerplate em objetos de dominio e entidades JPA.

Motivo:
O projeto usa Java 17 e Spring Boot. Lombok reduz codigo repetitivo sem impedir a manutencao de invariantes quando combinado com factories explicitas como `register` e `restore`.

Beneficios:
- reduz volume de codigo mecanico;
- melhora legibilidade dos objetos de dominio;
- preserva imutabilidade em dominio com `@Value`;
- simplifica entidades JPA com `@NoArgsConstructor` e `@AllArgsConstructor`.

Trade-offs:
- exige annotation processing no build e na IDE;
- deve ser usado com disciplina para nao esconder regras de negocio.

Alternativas avaliadas:
- Manter getters e construtores manuais: descartado por boilerplate crescente.
- Usar records para tudo: descartado para entidades JPA e por necessidade de factories com invariantes.

## ADR-022: Swagger/OpenAPI com springdoc no backend

Status: Aceita.

Decisao:
Adicionar `springdoc-openapi-starter-webmvc-ui` para gerar OpenAPI em `/v3/api-docs` e Swagger UI em `/swagger-ui.html`.

Motivo:
A documentacao de API precisa acompanhar o desenvolvimento incremental e dar visibilidade aos endpoints implementados para backend, frontend e validacao do coordinator.

Beneficios:
- gera documentacao navegavel da API;
- facilita validacao manual dos contratos;
- reduz divergencia entre controllers e documentacao;
- prepara caminho para OpenAPI 3.1 formal posterior.

Trade-offs:
- a versao `2.8.x` apresentou incompatibilidade de resource handler com Spring Boot 3.5 neste ambiente;
- foi usada `2.7.0`, validada localmente com Swagger UI e OpenAPI JSON.

Alternativas avaliadas:
- Documentar apenas em Markdown: descartado como unico mecanismo por risco de divergencia.
- Usar Springfox: descartado por baixa adequacao a Spring Boot moderno.

## ADR-023: Customizacoes como terceira vertical do scaffold

Status: Aceita.

Decisao:
Implementar `Customization` apos clientes e ambientes, antes de versionamento, hashing e verificacoes.

Motivo:
Customizacao e o agregado conceitual que conecta cliente, ambiente, tipo de objeto e identificador no legado. Sem esse cadastro, nao ha base estavel para registrar versoes oficiais, calcular hashes ou executar verificacoes.

Beneficios:
- consolida o contexto de Cadastro e Governanca de Customizacoes;
- valida o relacionamento cliente-ambiente-customizacao no PostgreSQL;
- prepara o contrato para `CustomizationVersion` sem antecipar canonicalizacao;
- entrega fluxo utilizavel no frontend para registrar objetos oficiais.

Trade-offs:
- ainda nao registra hash oficial nem versao do legado;
- ainda nao valida existencia real do objeto no MySQL;
- filtros, paginacao e busca serao adicionados em refinamento posterior.

Alternativas avaliadas:
- Implementar hashing agora: descartado porque regras de canonicalizacao ainda precisam ser fechadas.
- Implementar verificacao antes do cadastro de customizacao: descartado por falta de entidade oficial a verificar.

## ADR-024: Versoes oficiais antes de verificacao automatica

Status: Aceita.

Decisao:
Implementar `CustomizationVersion` como registro de hash oficial, algoritmo, versao de canonicalizacao e assinatura de conteudo antes de implementar coleta MySQL e verificacoes.

Motivo:
O sistema precisa de uma referencia oficial persistida para comparar resultados futuros. Registrar a versao oficial primeiro permite validar contrato, persistencia, UI e auditoria basica sem antecipar a estrategia final de canonicalizacao automatica.

Beneficios:
- cria a base transacional para verificacoes futuras;
- registra `hashAlgorithm` e `canonicalizationVersion` desde o inicio;
- permite historico por versao do legado;
- mantem o coletor MySQL e parser/canonicalizador desacoplados.

Trade-offs:
- o hash oficial ainda e informado pela administracao, nao calculado pelo sistema;
- ainda nao ha politica de unicidade de versao ativa;
- assinatura canonica e armazenada como texto simples nesta fase e deve ser revisada antes de dados sensiveis reais.

Alternativas avaliadas:
- Calcular hash automaticamente agora: descartado ate fechar regras de canonicalizacao por tipo de objeto.
- Deixar hash para verificacao futura: descartado porque impediria validar o contrato central de integridade.

## ADR-011: Evitar sandbox aninhado do Codex dentro do Docker

Status: Aceita.

Decisao:
Os servicos Docker dos agentes usam `CODEX_SANDBOX=danger-full-access` para que o Codex CLI execute comandos dentro do container sem tentar criar um sandbox adicional com `bubblewrap`.

Motivo:
O kernel do ambiente atual bloqueia namespaces nao privilegiados dentro do container, causando falhas como `bwrap: No permissions to create a new namespace`. Como o agente ja roda em Docker com volume de workspace controlado, o sandbox adicional impede a operacao local.

Beneficios:
- permite executar o Codex CLI local dentro do container;
- evita falhas de `bubblewrap` em ambientes Docker restritos;
- mantem a isolacao operacional no nivel do container.

Trade-offs:
- comandos do Codex dentro do container nao ficam restritos pelo sandbox interno do Codex;
- a seguranca passa a depender da configuracao de volumes e permissoes do Docker.

Alternativas descartadas:
- Habilitar namespaces nao privilegiados no host: descartado por exigir alteracao global do sistema.
- Rodar container privilegiado: descartado por ampliar permissoes desnecessariamente.

## ADR-012: Subagentes devem ser executados como servicos Docker explicitos

Status: Aceita.

Decisao:
O runtime instrui o `coordinator` a nao usar ferramentas internas de colaboracao do Codex, como `SpawnAgent` e `Wait`. Delegacoes para `architect`, `backend_dev` e `frontend_dev` devem ser registradas e executadas pelos servicos Docker dedicados.

Motivo:
A execucao com `SpawnAgent` falhou ao tentar criar fork de historico completo com parametros incompatíveis, deixando o coordinator aguardando indefinidamente. O projeto ja possui orquestracao Docker explicita para cada agente, que e mais previsivel e auditavel neste contexto.

Beneficios:
- evita travamento do coordinator;
- preserva rastreabilidade operacional;
- mantem cada papel em um servico Docker claro;
- reduz dependencia de comportamento interno do Codex CLI.

Trade-offs:
- a delegacao entre agentes nao e automatica dentro de uma unica sessao;
- execucoes especializadas precisam ser chamadas explicitamente quando necessarias.

Alternativas descartadas:
- Usar `SpawnAgent` interno: descartado por falha operacional nesta versao/configuracao.
- Remover agentes especializados: descartado porque os papeis continuam relevantes para o workflow.

## ADR-013: Escopo inicial aceito para controle e auditoria de customizacoes

Status: Aceita.

Decisao:
O escopo inicial do produto passa a ser um sistema de controle e auditoria de customizacoes do sistema legado, com cadastro de clientes, ambientes, customizacoes oficiais, hashes oficiais, verificacoes contra bancos MySQL de clientes, historico, divergencias e notificacao REST ao legado.

Motivo:
O problema foi detalhado o suficiente para permitir analise de dominio, riscos, bounded contexts candidatos, modelo conceitual inicial e arquitetura candidata, sem iniciar implementacao.

Beneficios:
- desbloqueia analise arquitetural orientada ao problema real;
- permite organizar tarefas por dominio;
- permite iniciar contratos preliminares e riscos;
- mantem backend e frontend bloqueados ate aprovacao formal de arquitetura e contratos.

Trade-offs:
- ainda existem perguntas pendentes sobre volume, MySQL suportado, conectividade on-premise, retencao e secret manager;
- a arquitetura registrada e candidata, nao implementacao final.

Alternativas descartadas:
- Continuar tratando o projeto como sem escopo: descartado porque o problema agora foi descrito.
- Implementar scaffold imediatamente: descartado porque contratos e decisoes de seguranca ainda precisam refinamento.

## ADR-014: Arquitetura candidata como modular monolith hexagonal

Status: Proposta.

Decisao:
Adotar como candidata uma arquitetura de backend em modular monolith com Clean Architecture e Arquitetura Hexagonal, separando internamente os contextos de cadastro/governanca de customizacoes, verificacao de integridade, divergencias/notificacoes e seguranca/auditoria.

Motivo:
O dominio exige boundaries claros, testabilidade e isolamento de adapters para PostgreSQL, MySQL legado, secret manager, scheduler e REST legado. Ainda nao ha evidencia de escala ou autonomia operacional que justifique microservicos.

Beneficios:
- reduz complexidade inicial de deploy e operacao;
- mantem consistencia transacional simples para cadastro, verificacoes e divergencias;
- preserva portas/adapters para trocar estrategias de coleta, hashing e notificacao;
- facilita testes unitarios e de integracao.

Trade-offs:
- exige disciplina para manter modulos internos desacoplados;
- componentes de verificacao podem demandar isolamento futuro se o volume crescer muito.

Alternativas avaliadas:
- Microservicos por contexto: adiado por complexidade operacional prematura.
- Monolito sem boundaries hexagonais: descartado por risco de acoplamento ao legado e baixa testabilidade.

## ADR-015: Coleta hibrida para SaaS e on-premise

Status: Proposta.

Decisao:
Suportar coleta centralizada para ambientes SaaS e ambientes com conectividade controlada, e agente/conector local para clientes on-premise sem acesso direto de rede ou com restricoes de seguranca.

Motivo:
Clientes on-premise frequentemente nao expoem banco de dados para acesso inbound. Um agente local com comunicacao outbound reduz superficie de ataque e evita depender de abertura de rede para o sistema central.

Beneficios:
- atende SaaS e on-premise com estrategias adequadas;
- reduz exposicao de credenciais e bancos de clientes;
- permite operar mesmo sem acesso direto ao MySQL do cliente;
- reduz acoplamento operacional ao ambiente do cliente.

Trade-offs:
- agente local exige distribuicao, instalacao, versionamento e suporte;
- coleta central ainda precisa politicas fortes de rede, credenciais e auditoria.

Alternativas avaliadas:
- Sistema central sempre puxar dados: descartado como unica estrategia por risco operacional e de seguranca.
- Cliente sempre enviar manualmente os hashes: descartado como unica estrategia por fragilidade operacional e baixa rastreabilidade.

## ADR-016: Hash deterministico sobre representacao canonica versionada

Status: Proposta.

Decisao:
Calcular hashes de customizacoes sobre uma representacao canonica versionada do objeto SQL, registrando algoritmo de hash, versao da canonicalizacao e evidencias da verificacao.

Motivo:
Hash de texto bruto pode gerar falsos positivos por formatacao, comentarios, delimitadores, casing ou metadados volateis do MySQL. Ao mesmo tempo, normalizacao agressiva pode esconder mudancas semanticamente relevantes.

Beneficios:
- reduz falsos positivos;
- permite evoluir regras por tipo de objeto;
- torna resultados reproduziveis;
- permite auditoria tecnica de divergencias.

Trade-offs:
- exige suite de fixtures reais por versao de MySQL/MariaDB;
- exige governanca para evoluir canonicalizacao sem invalidar historico.

Alternativas avaliadas:
- Hash direto do SQL bruto: descartado como padrao principal por sensibilidade excessiva a formatacao.
- Comparacao puramente textual manual: descartada por baixa escalabilidade e rastreabilidade.

## ADR-017: Credenciais de clientes fora do PostgreSQL em texto claro

Status: Proposta.

Decisao:
Credenciais de bancos de clientes e segredos de integracao devem ser armazenados em secret manager/vault ou mecanismo equivalente, mantendo no PostgreSQL apenas referencias, metadados nao sensiveis e historico auditavel.

Motivo:
O sistema pode acessar bancos de clientes, inclusive on-premise. Vazamento de credenciais teria impacto critico de seguranca e confianca.

Beneficios:
- reduz impacto de comprometimento do banco do novo sistema;
- favorece rotacao e segregacao de segredos;
- melhora conformidade e auditoria;
- evita exposicao acidental em logs, dumps e backups.

Trade-offs:
- adiciona dependencia operacional de secret manager;
- desenvolvimento local precisara estrategia controlada para segredos fake ou vault local.

Alternativas avaliadas:
- Persistir credenciais criptografadas diretamente no PostgreSQL: possivel apenas como fallback bem justificado, mas nao recomendado como decisao principal.
- Guardar credenciais em arquivo de configuracao: descartado por risco operacional.

## ADR-018: Coordinator aprova continuidade do refinamento, mas bloqueia scaffold

Status: Aceita.

Decisao:
O `coordinator` valida a arquitetura candidata do `architect` como coerente para continuidade do refinamento, mas nao aprova scaffold, backend, frontend, schema de banco ou OpenAPI final nesta rodada.

Motivo:
A analise inicial responde ao problema real e aponta uma direcao tecnica adequada: modular monolith hexagonal, coleta hibrida, hashing deterministico sobre canonicalizacao versionada, credenciais fora do PostgreSQL em texto claro, auditoria e notificacao REST desacoplada. Ainda existem decisoes criticas abertas sobre canonicalizacao por tipo de objeto, conectividade on-premise, secret manager, autorizacao, retencao e contrato formal.

Beneficios:
- mantem avanco tecnico sem pular gates de arquitetura;
- evita scaffold prematuro que cristalize decisoes ainda abertas;
- preserva rastreabilidade da validacao do coordinator;
- mantem backend e frontend alinhados antes da implementacao.

Trade-offs:
- adia inicio de codigo de aplicacao;
- exige nova rodada do `architect` para fechar hashing, coleta, seguranca e contratos;
- lint, build e testes de aplicacao seguem nao aplicaveis enquanto nao houver scaffold.

Alternativas descartadas:
- Aprovar implementacao imediatamente: descartada por ausencia de OpenAPI final, politica de credenciais e canonicalizacao detalhada.
- Rejeitar a arquitetura candidata inteira: descartada porque a direcao proposta e tecnicamente coerente e alinhada ao dominio.

## ADR-019: Inicio controlado do scaffold apos aprovacao da arquitetura

Status: Aceita.

Decisao:
Iniciar o scaffold da solucao com um primeiro fluxo vertical de clientes, mantendo modular monolith hexagonal no backend, Angular feature-based no frontend e PostgreSQL com migracoes Flyway.

Motivo:
O usuario aprovou a arquitetura candidata em 2026-05-24. Um fluxo pequeno de clientes valida a estrutura de dominio, casos de uso, portas, adapters, API REST, migracao e UI sem antecipar decisoes ainda abertas de hashing, coleta on-premise, agente local ou contrato final do legado.

Beneficios:
- cria base executavel para evolucao incremental;
- valida stack Java 17, Spring Boot, PostgreSQL e Angular;
- reduz risco de arquitetura apenas documental;
- preserva boundaries para novos contextos.

Trade-offs:
- parte de seguranca usa perfil `local` permissivo apenas em Docker de desenvolvimento;
- OpenAPI completa ainda precisa ser formalizada;
- testes Angular dependem de Chrome/Chromium no ambiente local.

Alternativas descartadas:
- Implementar primeiro hashing/coleta MySQL: descartada nesta etapa por depender de regras de canonicalizacao ainda abertas.
- Criar todos os modulos de uma vez: descartada por aumentar risco e acoplamento prematuro.

## ADR-025: Verificacao manual auditavel antes de coletor MySQL e divergencias persistidas

Status: Aceita.

Decisao:
Materializar primeiro a verificacao manual auditavel como a proxima vertical funcional, comparando `currentHash` informado manualmente com `officialHash` ja registrado e persistindo `VerificationRun` e `VerificationResult` antes de implementar coletor MySQL, canonicalizacao automatica ou abertura automatica de `Divergence`.

Motivo:
Essa etapa valida o contexto de verificacao de integridade com baixo risco arquitetural. Ela exercita contrato, persistencia, historico, RBAC e UI sem antecipar decisoes ainda abertas de canonicalizacao, conectividade on-premise e tratamento operacional de falhas externas.

Beneficios:
- entrega um fluxo auditavel utilizavel mais cedo;
- desacopla a evolucao do frontend e do backend das incertezas do coletor MySQL;
- reduz risco de congelar contrato cedo demais em torno de erros de conectividade ainda nao modelados;
- cria base objetiva para evoluir depois para divergencias e notificacoes;
- reaproveita `CustomizationVersion` como referencia oficial sem exigir canonicalizacao automatica nesta fase.
- preserva o dashboard Angular atual com uma nova area funcional, evitando fragmentacao prematura de navegacao para uma vertical ainda pequena.

Trade-offs:
- o hash atual continua sendo informado manualmente nesta fase;
- resultados como `OBJECT_NOT_FOUND`, `UNREACHABLE` e `ERROR` ficam reservados para a fase com coletor real;
- a evidencia inicial fica limitada a `manual-entry`, sem artefato tecnico externo.

Alternativas avaliadas:
- Implementar coletor MySQL agora: descartada por depender de TASK-006 e TASK-007.
- Abrir divergencia automatica ja nesta fase: descartada porque ainda faltam regras de severidade, lifecycle e notificacao ao legado.

## ADR-026: Verificacao manual persiste um resultado por execucao nesta fase

Status: Aceita.

Decisao:
Enquanto a vertical permanecer manual e orientada a uma unica `CustomizationVersion` por chamada, cada `VerificationRun` persistira exatamente um `VerificationResult`.

Motivo:
O contrato atual de `POST /api/v1/verification-runs` recebe um unico `customizationVersionId` e um unico `currentHash`. Refletir isso no modelo e na persistencia simplifica auditoria, consulta historica e cobertura de testes sem antecipar a modelagem de verificacoes em lote ou coleta automatica.

Beneficios:
- mantem rastreabilidade simples entre execucao e resultado;
- reduz ambiguidade no contrato REST e no frontend;
- alinha dominio, JPA e schema com restricao unica em `verification_run_id`.

Trade-offs:
- verificacoes em lote exigirao evolucao futura do modelo;
- a consulta atual nao representa ainda uma execucao com multiplos objetos verificados.

Alternativas avaliadas:
- Permitir varios resultados por execucao ja nesta fase: descartada por adicionar complexidade sem caso de uso aprovado.
- Persistir somente `VerificationRun` e calcular o resultado em memoria: descartada por enfraquecer a auditoria.
