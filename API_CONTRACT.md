# Contrato de API

## Estado

Pendente.

Nenhum contrato de API deve ser definido antes do envio e analise do escopo detalhado do problema que o projeto ira resolver.

## Regra

Os contratos devem ser derivados do dominio, dos fluxos reais do produto e das necessidades de integracao entre Angular e Spring Boot.

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

## Stack Considerada Para Contratos

- Backend: Java 17 com Spring Boot estavel.
- Banco: PostgreSQL.
- Frontend: Angular.
- Especificacao: OpenAPI 3.1 quando houver API REST.

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
