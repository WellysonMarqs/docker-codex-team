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

## ADR-002: Nao fixar stack de backend antes da analise do escopo

Status: Aceita.

Decisao:
Nao definir stack de backend como obrigatoria antes da analise do escopo detalhado, dos requisitos nao funcionais, das integracoes e das restricoes operacionais.

Motivo:
A stack de backend precisa ser consequencia do problema, nao premissa fixa. Sem entendimento validado do dominio e das restricoes, a decisao seria prematura e poderia induzir acoplamento tecnico desnecessario.

Beneficios:
- reduz risco de escolha prematura;
- melhora aderencia entre tecnologia e necessidade real;
- preserva flexibilidade para comparar alternativas;
- evita contaminar a arquitetura com decisoes ainda nao justificadas.

Trade-offs:
- adia definicoes de scaffold e pipeline;
- exige disciplina para nao assumir tecnologias por habito;
- pode aumentar o tempo inicial de descoberta.

Alternativas descartadas:
- Fixar backend agora: descartado por ausencia de escopo aprovado.

## ADR-002A: Nao fixar tecnologia de documentacao de API antes de confirmar o estilo de integracao

Status: Aceita.

Decisao:
Definir a tecnologia de documentacao e exploracao de contratos somente depois de confirmar se o produto tera API HTTP, quais integracoes existirao e como esses contratos precisarao ser validados.

Motivo:
Nem todo problema exige os mesmos mecanismos de contrato. O formato e a ferramenta devem ser escolhidos de acordo com o estilo de integracao e com a estrategia de testes e operacao.

Beneficios:
- evita impor ferramentas antes da necessidade;
- permite escolher o mecanismo de contrato mais aderente ao produto;
- preserva coerencia entre integracao, testes e operacao.

Trade-offs:
- a documentacao contratual permanece pendente por mais tempo;
- algumas decisoes de integracao so poderao ser fechadas depois da modelagem.

Alternativas descartadas:
- Fixar tecnologia de contrato antes do escopo: descartado por prematuridade.

## ADR-002B: Nao antecipar bibliotecas auxiliares de produtividade

Status: Aceita.

Decisao:
Bibliotecas auxiliares de produtividade so devem ser avaliadas depois da definicao da stack principal e das convencoes do projeto.

Motivo:
Escolher bibliotecas auxiliares antes da stack principal cria ruido decisorio e antecipa debates que ainda nao agregam valor.

Beneficios:
- reduz decisoes acessorias prematuras;
- melhora foco na modelagem, arquitetura e requisitos;
- evita acoplamento antecipado a ferramentas especificas.

Trade-offs:
- essa avaliacao fica para uma etapa posterior;
- o scaffold nao pode assumir atalhos de produtividade ainda nao aprovados.

Alternativas descartadas:
- Escolher bibliotecas auxiliares agora: descartado por ausencia de definicao de stack.

## ADR-003: Nao fixar tecnologia de persistencia antes da modelagem

Status: Aceita.

Decisao:
Nao definir banco de dados ou estrategia de persistencia como obrigatorios antes da analise do dominio, dos tipos de dado, das consultas e das integracoes necessarias.

Motivo:
A escolha de persistencia precisa refletir consistencia, volume, auditoria, consulta, latencia operacional e integracoes externas.

Beneficios:
- evita amarracao prematura;
- permite avaliar melhor requisitos de consistencia e operacao;
- reduz risco de incompatibilidade com o problema real.

Trade-offs:
- as decisoes de ambiente local e integracao ficam dependentes da etapa arquitetural;
- o scaffold de persistencia nao pode ser preparado ainda.

Alternativas descartadas:
- Fixar banco agora: descartado por ausencia de modelagem validada.

## ADR-004: Nao fixar stack de frontend antes da analise do escopo

Status: Aceita.

Decisao:
Nao definir stack de frontend como obrigatoria antes de entender jornadas, interacoes, acessibilidade, requisitos de operacao e necessidades de integracao.

Motivo:
A camada de interface precisa ser escolhida em funcao do produto e da experiencia esperada, nao por preferencia previa.

Beneficios:
- melhora aderencia entre tecnologia e experiencia do usuario;
- evita boilerplate desnecessario;
- permite avaliar melhor trade-offs de produtividade, manutencao e UX.

Trade-offs:
- parte das decisoes de UX engineering fica pendente por mais tempo;
- o scaffold frontend nao pode ser fechado ainda.

Alternativas descartadas:
- Fixar frontend agora: descartado por ausencia de escopo aprovado.

## ADR-005: Contratos devem nascer apos entendimento do dominio

Status: Aceita.

Decisao:
Os contratos de integracao serao definidos apenas apos o escopo detalhado e a modelagem inicial do dominio.

Motivo:
Contratos devem expressar capacidades de negocio e fluxos reais. Definir interfaces antes de entender o problema aumenta risco de integracoes procedurais, acopladas ou incompletas.

Beneficios:
- contratos mais alinhados ao dominio;
- menos retrabalho;
- melhor integracao entre componentes;
- testes de contrato mais relevantes.

Trade-offs:
- `API_CONTRACT.md` fica pendente ate haver escopo;
- as frentes de implementacao ainda nao podem ser integradas.

Alternativas descartadas:
- Criar contratos genericos antecipados: descartado por nao resolver necessariamente o problema real.

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

## ADR-009: Docker Agent oficial com root como orquestrador

Status: Aceita.

Decisao:
Usar o Docker Agent oficial com `agents.yml` como configuracao do time multi-agent. O agente `root` atua como orquestrador principal e delega tarefas para os agentes especializados.

Motivo:
O Docker Agent e o framework oficial da Docker para definir e executar times de agentes especializados por YAML, com delegacao via `sub_agents`.

Beneficios:
- segue a documentacao oficial da Docker;
- usa `docker agent run agents.yml` como comando principal;
- preserva `agents.yml` como fonte primaria;
- permite delegacao hierarquica para architect, backend_dev, frontend_dev e qa;
- evita runtime customizado desnecessario;
- permite gerar imagem com `docker agent build agents.yml`.

Trade-offs:
- depende do Docker Agent instalado no ambiente;
- a execucao real depende do provider configurado; no modo atual, depende de `OPENAI_API_KEY` valido no ambiente;
- o agente padrao precisa se chamar `root`, concentrando a coordenacao nesse agente.

Alternativas descartadas:
- Runtime customizado com Dockerfile e Docker Compose: descartado porque o objetivo e usar o Docker Agent oficial.
- Criar quatro imagens diferentes: descartado porque Docker Agent ja suporta time multi-agent em um unico YAML.
- Implementar codigo de aplicacao junto com configuracao de agentes: descartado para manter separacao de responsabilidades.

## ADR-020: Memoria persistente dos agentes em SQL Server com schema em portugues

Status: Aceita.

Decisao:
Usar a instancia SQL Server nativa local em `localhost:1433` como persistencia principal da memoria operacional dos agentes, com banco `MemoriaAgentes`, schema fisico em portugues e acesso por `tools/agent_memory.py`.

Motivo:
Os agentes precisam preservar documentacao relevante, regras, licoes aprendidas e melhorias aprovadas entre sessoes. Como ha uma instancia SQL Server local disponivel, faz mais sentido usar essa persistencia principal do que manter a memoria operacional apenas em arquivos ou em SQLite.

Beneficios:

- centraliza a memoria persistente em banco relacional aderente ao ambiente do usuario;
- permite que documentacao e aprendizagem sejam consultadas de forma estruturada;
- reduz perda de contexto entre sessoes;
- melhora rastreabilidade por eventos;
- mantem nomenclatura fisica do banco em portugues.

Trade-offs:

- a CLI ainda depende de um cliente `sqlcmd` executado em container auxiliar, porque o ambiente atual nao expõe cliente nativo local;
- os arquivos obrigatorios continuam existindo no repositorio e precisam ser mantidos em sincronia conceitual com o banco;
- os testes automatizados passam a depender de uma instancia SQL Server acessivel no ambiente.

Alternativas descartadas:

- manter memoria apenas em Markdown: descartado por baixa estrutura para busca e rastreabilidade;
- usar SQLite como persistencia principal ou de suporte: descartado porque a instancia SQL Server local ja existe e atende melhor ao objetivo operacional;
- migrar para MongoDB: descartado por nao haver necessidade forte de modelo documental flexivel neste momento.

Diretriz complementar:
Os nomes fisicos de tabelas e colunas do banco devem permanecer em portugues do Brasil.

## ADR-010: OpenAI como provider principal do Docker Agent

Status: Aceita.

Decisao:
Usar OpenAI como provider principal do Docker Agent por meio da variavel de ambiente `OPENAI_API_KEY`.

Motivo:
O objetivo e usar um provider cloud suportado oficialmente e de configuracao simples no Docker Agent, mantendo o Docker Agent como orquestrador multi-agent.

Beneficios:
- mantem o Docker Agent oficial como runtime de orquestracao;
- usa um modelo OpenAI suportado nativamente pelo Docker Agent;
- simplifica a inicializacao em ambientes onde o Docker Model Runner nao esta habilitado;
- preserva o controle do coordinator sobre delegacao, arquitetura, tarefas e documentacao.

Trade-offs:
- depende de `OPENAI_API_KEY` valido no ambiente de execucao;
- pode gerar custo de consumo de API conforme uso.

Alternativas descartadas:
- Usar Docker Model Runner local como caminho principal: descartado para esta configuracao porque a prioridade atual e simplificar a execucao com provider cloud suportado oficialmente.

## ADR-011: Agente dedicado de QA para quality gates e validacao de testes

Status: Aceita.

Decisao:
Adicionar um sub-agent `qa` dedicado a estrategia, execucao e validacao dos testes obrigatorios do projeto.

Motivo:
Os testes descritos na documentacao nao devem ficar diluidos apenas entre implementacao backend e frontend. Um agente especializado em QA melhora a independencia da validacao, aumenta a rastreabilidade dos quality gates e reduz o risco de entregas com cobertura insuficiente.

Beneficios:
- explicita ownership de validacao;
- melhora a disciplina de quality gates;
- separa implementacao de verificacao;
- favorece cobertura de testes unitarios, integracao, contrato, E2E e acessibilidade;
- aumenta a clareza do workflow multi-agent.

Trade-offs:
- adiciona mais um agente para coordenar;
- exige delimitacao clara entre implementar testes e validar testes;
- pode aumentar o tempo total de execucao em fluxos completos.

Alternativas descartadas:
- Deixar todos os testes somente com backend_dev e frontend_dev: descartado por reduzir independencia na validacao.
- Concentrar toda validacao apenas no root: descartado por misturar orquestracao com execucao especializada de QA.

Complementos operacionais aceitos para o agente `qa`:
- o agente deve produzir evidencias objetivas, nao apenas parecer textual;
- o agente deve emitir parecer final no formato aprovado, aprovado com ressalvas ou bloqueado;
- na ausencia de codigo, ambiente ou dados, o agente deve entregar plano de testes, premissas, riscos e criterios de pronto para teste;
- o agente deve incluir testes exploratorios guiados por risco e smoke de seguranca basica quando aplicavel;
- defeitos devem ser registrados com passos, esperado versus atual, severidade e evidencia.

## ADR-012: Root deve consolidar com julgamento tecnico e comunicacao natural

Status: Aceita.

Decisao:
O agente `root` deve continuar como coordinator principal, mas com instrucao explicita para atuar tambem como sintetizador tecnico, revisor critico e comunicador principal com o usuario.

Motivo:
Um coordinator que apenas delega e repassa respostas tende a produzir saidas superficiais, mecanicas e pouco uteis para tomada de decisao. O valor do `root` aumenta quando ele filtra ruido, identifica conflitos, prioriza achados e entrega conclusoes acionaveis em linguagem natural.

Beneficios:
- melhora a qualidade percebida das respostas;
- reduz respostas burocraticas ou excessivamente genericas;
- aumenta a clareza para o usuario final;
- preserva a especializacao dos sub-agentes sem perder unidade tecnica;
- fortalece o papel do coordinator como dono da entrega.

Trade-offs:
- o `root` passa a consumir mais contexto antes de responder;
- respostas podem ficar um pouco mais longas em temas complexos;
- exige maior disciplina para distinguir consolidacao de invencao.

Alternativas descartadas:
- Manter o `root` apenas como roteador de tarefas: descartado por baixo valor analitico.
- Transferir a responsabilidade de comunicacao principal para um sub-agent: descartado para nao fragmentar a interface com o usuario.

## ADR-013: Comandos do root devem exigir saida analitica e nao burocratica

Status: Aceita.

Decisao:
Os comandos embutidos do agente `root`, como `status`, `prepare_scope` e `validate_docs`, devem instruir explicitamente o coordinator a responder com sintese executiva, priorizacao, julgamento tecnico e recomendacoes acionaveis.

Motivo:
Mesmo com um bom prompt principal, comandos curtos e genericos tendem a induzir respostas mecanicas, superficiais ou excessivamente checklist-driven. Ajustar os comandos melhora a qualidade da interacao nos fluxos mais frequentes sem alterar o papel do agente.

Beneficios:
- aumenta consistencia de tom e profundidade;
- reduz respostas burocraticas;
- melhora a utilidade pratica dos comandos prontos;
- reforca o papel do `root` como dono da analise final.

Trade-offs:
- respostas desses comandos podem ficar um pouco maiores;
- exige cuidado para nao repetir contexto desnecessario.

Alternativas descartadas:
- Deixar apenas o prompt principal carregar esse comportamento: descartado por baixa garantia de aderencia nos comandos embutidos.

## ADR-014: Todo o time deve seguir padrao de clareza, rigor e sintese acionavel

Status: Aceita.

Decisao:
Refinar os prompts de `architect`, `backend_dev`, `frontend_dev` e `qa` para que todos os agentes entreguem respostas mais claras, humanas, tecnicamente densas e orientadas a decisao, sem perder suas especializacoes.

Motivo:
Melhorar apenas o `root` nao resolve o problema por completo quando os sub-agentes continuam respondendo de forma superficial, burocratica ou pouco acionavel. A qualidade final do coordinator depende da qualidade do material que ele recebe.

Beneficios:
- melhora a qualidade media das delegacoes;
- reduz necessidade de retrabalho do `root` para reinterpretar respostas fracas;
- aumenta consistencia de comunicacao em todo o time;
- reforca cultura de evidencias, trade-offs e conclusoes acionaveis.

Trade-offs:
- prompts mais longos consomem mais contexto;
- agentes podem responder com mais detalhe em tarefas complexas;
- exige disciplina para manter densidade sem prolixidade.

Alternativas descartadas:
- Melhorar apenas o `root`: descartado por atuar apenas na consolidacao final.
- Criar agentes novos apenas para comunicacao: descartado por aumentar complexidade operacional sem necessidade.

## ADR-015: Separar modelo generalista e modelo de coding por papel

Status: Aceita.

Decisao:
Adotar estrategia de modelos por responsabilidade no `agents.yml`: `root`, `architect` e `qa` passam a usar `openai-general` apontando para `gpt-5.4`, enquanto `backend_dev` e `frontend_dev` passam a usar `openai-coding` apontando para `gpt-5.2-codex`.

Motivo:
O coordinator e os agentes de analise e validacao dependem mais de julgamento amplo, sintese e comparacao de alternativas. Ja os agentes de implementacao tendem a se beneficiar mais de um modelo otimizado para coding agentic. Separar por papel melhora aderencia tecnica sem misturar expectativas de comportamento em um unico modelo.

Beneficios:
- melhora especializacao por agente;
- aproxima implementacao do comportamento de coding agent;
- preserva um modelo generalista forte para coordenacao, arquitetura e QA;
- reduz dependencia exclusiva de prompt para extrair comportamento adequado.

Trade-offs:
- aumenta dependencia da disponibilidade de mais de um deployment no provider;
- pode elevar custo operacional;
- adiciona mais uma dimensao de configuracao para manter.

Risco operacional:
- em ambientes Azure OpenAI ou gateways compativeis, o valor do campo `model` pode precisar corresponder ao nome do deployment exposto no ambiente, e nao apenas ao identificador canonico do modelo;
- se `gpt-5.4` ou `gpt-5.2-codex` nao estiverem publicados no endpoint atual, a execucao falhara ate o deployment ser ajustado.

Alternativas descartadas:
- Manter um unico modelo para todos os agentes: descartado por menor especializacao.
- Trocar apenas o `root`: descartado por gerar ganho parcial.

## ADR-016: Reduzir overhead operacional de think, todo e delegacao

Status: Aceita.

Decisao:
Refinar os prompts principalmente de `root`, `architect` e `qa` para reduzir uso desnecessario de `think`, `todo`, releitura de contexto e delegacao excessiva em tarefas simples ou analises pontuais.

Motivo:
Os testes mostraram melhora clara na qualidade textual, mas tambem evidenciaram overhead operacional acima do necessario. Em especial, houve criacao de todos e uso de raciocinio interno em perguntas que poderiam ser respondidas de forma mais direta.

Beneficios:
- respostas mais rapidas;
- menor consumo de tokens;
- menor poluicao operacional durante execucao;
- mais foco em resultado do que em processo interno;
- melhor equilibrio entre profundidade e eficiencia.

Trade-offs:
- reduz espaco para rastreamento interno em tarefas pequenas;
- exige julgamento melhor sobre quando planejar e quando responder diretamente.

Alternativas descartadas:
- Manter o comportamento atual: descartado por custo e latencia desnecessarios.
- Remover `think` e `todo` por completo: descartado porque eles ainda sao uteis em tarefas complexas.
