# Contrato de API

## Estado

Pendente.

Nenhum contrato de API deve ser definido antes do envio e analise do escopo detalhado do problema que o projeto ira resolver.

## Regra

Os contratos devem ser derivados do dominio, dos fluxos reais do produto e das necessidades de integracao entre os componentes da solucao aprovada.

Antes de criar endpoints, e obrigatorio definir:

- problema a resolver;
- usuarios;
- fluxos principais;
- regras de negocio;
- dados persistidos;
- requisitos de seguranca;
- requisitos de autorizacao;
- necessidades de auditoria;
- requisitos nao funcionais;
- criterios de aceite.

## Consideracoes Para Contratos

- O estilo de contrato depende da arquitetura aprovada e do tipo de integracao necessario.
- Quando houver API HTTP, o padrao de especificacao e a forma de exploracao devem ser definidos junto com a estrategia de testes e versionamento.
- Quando houver mensageria, jobs ou integracoes assincronas, os contratos tambem devem explicitar payloads, eventos, idempotencia e tratamento de falhas.

## Padrao Esperado Futuro

Quando o escopo for aprovado, este documento devera conter:

- recursos da API;
- endpoints;
- metodos HTTP;
- DTOs de request;
- DTOs de response;
- modelo padrao de erro;
- regras de autenticacao;
- regras de autorizacao;
- exemplos de request e response;
- estrategia de versionamento;
- requisitos de idempotencia quando aplicavel.

## Contrato Operacional Interno: Memoria Persistente dos Agentes

### Estado

Aprovado para uso interno do workspace.

### Persistencia principal

- instancia SQL Server nativa local em `localhost:1433`
- banco `MemoriaAgentes`
- schema principal em `memory/sqlserver/schema.sql`

### Tabelas fisicas

- `memorias_agentes`
- `eventos_memorias_agentes`

### Colunas fisicas de `memorias_agentes`

- `id`
- `nome_agente`
- `categoria`
- `escopo`
- `titulo`
- `conteudo`
- `tags`
- `tipo_origem`
- `referencia_origem`
- `situacao`
- `confianca`
- `criado_em`
- `atualizado_em`
- `ultimo_uso_em`

### Colunas fisicas de `eventos_memorias_agentes`

- `id`
- `memoria_id`
- `acao`
- `autor`
- `observacao_evento`
- `criado_em`

### Finalidade funcional

Esse contrato interno existe para que os agentes armazenem e consultem:

- documentacao operacional relevante;
- regras de trabalho;
- licoes aprendidas;
- melhorias aprovadas;
- contexto reutilizavel do projeto.

### Operacoes suportadas pela CLI

- `init`
- `upsert`
- `search`
- `list`
- `show`
- `touch`
- `archive`
- `stats`

### Regra de sincronizacao

Quando houver mudanca relevante em `TASKS.md`, `DECISIONS.md`, `ARCHITECTURE.md` ou `API_CONTRACT.md`, os agentes devem considerar se o conteudo precisa ser espelhado no banco como memoria reutilizavel.
