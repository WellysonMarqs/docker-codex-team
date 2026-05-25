# Contrato de API

## Estado

Preliminar, com endpoints de clientes, ambientes, customizacoes, versoes oficiais e verificacoes manuais implementados parcialmente no scaffold inicial.

O escopo inicial foi recebido em 2026-05-24 para o Sistema de Controle e Auditoria de Customizacoes. Este documento registra capacidades e recursos candidatos para orientar a modelagem. O scaffold inicial implementa clientes, ambientes, customizacoes oficiais, versoes oficiais com hash informado e a vertical de verificacao manual auditavel integrada ao dashboard Angular. Ainda nao ha OpenAPI final para todos os recursos.

Alteracao operacional registrada:
o ajuste para uso do Codex CLI local, sandbox Docker e orquestracao explicita de agentes nao altera contratos de API.

Alteracao operacional registrada em 2026-05-24:
o ajuste do modelo padrao dos agentes para `gpt-5.4` nao altera contratos de API.

Documentacao automatica:

- Swagger UI: `GET /swagger-ui.html`
- OpenAPI JSON: `GET /v3/api-docs`

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

## Capacidades Candidatas

- Gerenciar clientes.
- Gerenciar ambientes do cliente, incluindo tipo `SAAS` ou `ON_PREMISE`.
- Registrar customizacoes oficiais por cliente e ambiente.
- Versionar customizacoes por versao do legado.
- Registrar referencia do objeto customizado no MySQL.
- Registrar hash oficial, algoritmo de hash e versao da canonicalizacao.
- Executar verificacao manual de integridade.
- Agendar verificacoes periodicas.
- Consultar historico de verificacoes.
- Consultar divergencias.
- Notificar o sistema legado quando uma divergencia for detectada.
- Registrar auditoria de acoes administrativas e eventos de verificacao.

## Recursos REST Candidatos

Status dos recursos:

- `customers`: implementado inicialmente.
- `customer-environments`: implementado inicialmente.
- `legacy-system-versions`: versoes do sistema legado.
- `customizations`: implementado inicialmente.
- `customization-versions`: implementado inicialmente.
- `verification-runs`: implementado no backend.
- `verification-results`: persistido no backend e retornado de forma embutida em `verification-runs`; ainda sem endpoint REST dedicado.
- `divergences`: divergencias detectadas.
- `legacy-notifications`: tentativas de notificacao ao legado.
- `audit-events`: eventos de auditoria.

## Modelo de Dados de API Candidato

### Customer

- `id`
- `name`
- `externalReference`
- `status`
- `createdAt`
- `updatedAt`

### CustomerEnvironment

- `id`
- `customerId`
- `name`
- `type`: `SAAS` ou `ON_PREMISE`
- `collectionMode`: `CENTRAL_PULL`, `LOCAL_AGENT_PUSH` ou `MANUAL_SIGNATURE_UPLOAD`
- `status`
- `credentialReferenceId`
- `createdAt`
- `updatedAt`

### Customization

- `id`
- `customerId`
- `environmentId`
- `name`
- `description`
- `objectType`: `TABLE`, `FUNCTION`, `PROCEDURE`, `TRIGGER`, `SQL_SCRIPT` ou `OTHER`
- `objectIdentifier`
- `status`
- `createdBy`
- `createdAt`
- `updatedAt`

### CustomizationVersion

- `id`
- `customizationId`
- `legacySystemVersion`
- `officialHash`
- `hashAlgorithm`
- `canonicalizationVersion`
- `contentSignature`
- `registeredBy`
- `registeredAt`
- `activeFrom`
- `activeUntil`
- `status`

### VerificationRun

- `id`
- `customerId`
- `environmentId`
- `triggerType`: `MANUAL`, `SCHEDULED`, `LEGACY_EVENT` ou `AGENT_CALLBACK`
- `status`: `PENDING`, `RUNNING`, `COMPLETED`, `COMPLETED_WITH_DIVERGENCE`, `FAILED` ou `CANCELLED`
- `startedAt`
- `finishedAt`
- `requestedBy`
- `correlationId`
- `result`

### VerificationResult

- `id`
- `verificationRunId`
- `customizationVersionId`
- `currentHash`
- `officialHash`
- `status`: `MATCH`, `DIVERGENT`, `OBJECT_NOT_FOUND`, `UNREACHABLE` ou `ERROR`
- `evidenceReference`
- `errorCode`
- `errorMessage`
- `checkedAt`

Observacao da fase atual:

- cada `VerificationRun` retorna exatamente um `VerificationResult`;
- os estados `OBJECT_NOT_FOUND`, `UNREACHABLE` e `ERROR` permanecem reservados para a fase com coletor real.

### Divergence

- `id`
- `verificationResultId`
- `customerId`
- `environmentId`
- `customizationId`
- `severity`
- `status`: `OPEN`, `NOTIFIED`, `ACKNOWLEDGED`, `RESOLVED` ou `IGNORED_WITH_JUSTIFICATION`
- `detectedAt`
- `resolvedAt`
- `correlationId`

### LegacyNotification

- `id`
- `divergenceId`
- `targetSystem`
- `idempotencyKey`
- `status`: `PENDING`, `SENT`, `FAILED` ou `EXHAUSTED`
- `attempts`
- `lastAttemptAt`
- `nextRetryAt`
- `correlationId`

## Endpoints Candidatos

Endpoints administrativos:

- `POST /api/v1/customers`: implementado inicialmente.
- `GET /api/v1/customers`: implementado inicialmente.
- `GET /api/v1/customers/{customerId}`
- `POST /api/v1/customers/{customerId}/environments`: implementado inicialmente.
- `GET /api/v1/customers/{customerId}/environments`: implementado inicialmente.
- `POST /api/v1/customizations`: implementado inicialmente.
- `GET /api/v1/customizations`: implementado inicialmente.
- `GET /api/v1/customizations/{customizationId}`
- `POST /api/v1/customizations/{customizationId}/versions`: implementado inicialmente.
- `GET /api/v1/customizations/{customizationId}/versions`: implementado inicialmente.
- `POST /api/v1/verification-runs`: implementado no backend.
- `GET /api/v1/verification-runs`: implementado no backend.
- `GET /api/v1/verification-runs/{verificationRunId}`: implementado no backend.
- `GET /api/v1/divergences`
- `GET /api/v1/divergences/{divergenceId}`
- `PATCH /api/v1/divergences/{divergenceId}`

Endpoints candidatos para agente on-premise:

- `POST /api/v1/agent/verification-results`
- `POST /api/v1/agent/heartbeats`

Endpoints candidatos para consulta pelo legado:

- `GET /api/v1/legacy/customization-integrity-status`
- `GET /api/v1/legacy/divergences`

Notificacao outbound candidata para o legado:

- `POST {legacyBaseUrl}/api/customization-divergences`

## Endpoints Implementados Inicialmente

### POST /api/v1/customers

Cria um cliente.

Request:

```json
{
  "name": "Cliente Piloto",
  "externalReference": "LEG-001"
}
```

Response `201`:

```json
{
  "id": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
  "name": "Cliente Piloto",
  "externalReference": "LEG-001",
  "status": "ACTIVE",
  "createdAt": "2026-05-24T22:16:49.113509Z",
  "updatedAt": "2026-05-24T22:16:49.113509Z"
}
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:write`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### GET /api/v1/customers

Lista clientes.

Response `200`:

```json
[
  {
    "id": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
    "name": "Cliente Piloto",
    "externalReference": "LEG-001",
    "status": "ACTIVE",
    "createdAt": "2026-05-24T22:16:49.113509Z",
    "updatedAt": "2026-05-24T22:16:49.113509Z"
  }
]
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:read`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### POST /api/v1/customers/{customerId}/environments

Cria um ambiente do cliente.

Request:

```json
{
  "name": "Producao SaaS",
  "type": "SAAS",
  "collectionMode": "CENTRAL_PULL",
  "credentialReferenceId": "vault/customer/acme/prod"
}
```

Response `201`:

```json
{
  "id": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
  "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
  "name": "Producao SaaS",
  "type": "SAAS",
  "collectionMode": "CENTRAL_PULL",
  "status": "ACTIVE",
  "credentialReferenceId": "vault/customer/acme/prod",
  "createdAt": "2026-05-25T01:23:18.575507Z",
  "updatedAt": "2026-05-25T01:23:18.575507Z"
}
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:write`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### GET /api/v1/customers/{customerId}/environments

Lista ambientes do cliente.

Response `200`:

```json
[
  {
    "id": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
    "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
    "name": "Producao SaaS",
    "type": "SAAS",
    "collectionMode": "CENTRAL_PULL",
    "status": "ACTIVE",
    "credentialReferenceId": "vault/customer/acme/prod",
    "createdAt": "2026-05-25T01:23:18.575507Z",
    "updatedAt": "2026-05-25T01:23:18.575507Z"
  }
]
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:read`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### POST /api/v1/customizations

Cria o registro conceitual de uma customizacao oficial por cliente e ambiente.

Request:

```json
{
  "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
  "environmentId": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
  "name": "Procedure fiscal",
  "description": "Procedure customizada do cliente",
  "objectType": "PROCEDURE",
  "objectIdentifier": "billing.sp_customer_tax",
  "createdBy": "support"
}
```

Response `201`:

```json
{
  "id": "64255850-95f8-4c3d-b5e7-c6cba76e9810",
  "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
  "environmentId": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
  "name": "Procedure fiscal",
  "description": "Procedure customizada do cliente",
  "objectType": "PROCEDURE",
  "objectIdentifier": "billing.sp_customer_tax",
  "status": "ACTIVE",
  "createdBy": "support",
  "createdAt": "2026-05-25T01:57:11.189666251Z",
  "updatedAt": "2026-05-25T01:57:11.189666251Z"
}
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:write`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### GET /api/v1/customizations

Lista customizacoes cadastradas.

Response `200`:

```json
[
  {
    "id": "64255850-95f8-4c3d-b5e7-c6cba76e9810",
    "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
    "environmentId": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
    "name": "Procedure fiscal",
    "description": "Procedure customizada do cliente",
    "objectType": "PROCEDURE",
    "objectIdentifier": "billing.sp_customer_tax",
    "status": "ACTIVE",
    "createdBy": "support",
    "createdAt": "2026-05-25T01:57:11.189666251Z",
    "updatedAt": "2026-05-25T01:57:11.189666251Z"
  }
]
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:read`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### POST /api/v1/customizations/{customizationId}/versions

Cria uma versao oficial para uma customizacao. Nesta etapa o hash e a assinatura canonica sao informados pela administracao; a coleta MySQL e o calculo automatico de hash permanecem fora do escopo.

Request:

```json
{
  "legacySystemVersion": "2026.05",
  "officialHash": "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
  "hashAlgorithm": "SHA-256",
  "canonicalizationVersion": "mysql-procedure-v1",
  "contentSignature": "CREATE PROCEDURE billing.sp_customer_tax",
  "registeredBy": "support"
}
```

Response `201`:

```json
{
  "id": "4f631ef1-65f5-4831-9bf6-580d3dfb7040",
  "customizationId": "64255850-95f8-4c3d-b5e7-c6cba76e9810",
  "legacySystemVersion": "2026.05",
  "officialHash": "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
  "hashAlgorithm": "SHA-256",
  "canonicalizationVersion": "mysql-procedure-v1",
  "contentSignature": "CREATE PROCEDURE billing.sp_customer_tax",
  "registeredBy": "support",
  "registeredAt": "2026-05-25T02:05:23.846395567Z",
  "activeFrom": null,
  "activeUntil": null,
  "status": "ACTIVE"
}
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:write`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### GET /api/v1/customizations/{customizationId}/versions

Lista versoes oficiais de uma customizacao.

Response `200`:

```json
[
  {
    "id": "4f631ef1-65f5-4831-9bf6-580d3dfb7040",
    "customizationId": "64255850-95f8-4c3d-b5e7-c6cba76e9810",
    "legacySystemVersion": "2026.05",
    "officialHash": "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
    "hashAlgorithm": "SHA-256",
    "canonicalizationVersion": "mysql-procedure-v1",
    "contentSignature": "CREATE PROCEDURE billing.sp_customer_tax",
    "registeredBy": "support",
    "registeredAt": "2026-05-25T02:05:23.846396Z",
    "activeFrom": null,
    "activeUntil": null,
    "status": "ACTIVE"
  }
]
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:read`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

Semantica de `collectionMode` no recurso de ambiente:

- `CENTRAL_PULL`: o sistema central sera o responsavel por iniciar a coleta quando o coletor automatico existir.
- `LOCAL_AGENT_PUSH`: um agente local no ambiente do cliente sera o responsavel por iniciar o envio da evidencia quando o contrato do agente for implementado.
- `MANUAL_SIGNATURE_UPLOAD`: o ambiente admite fluxo manual auditavel, sem coletor automatico.

Observacao de UX:
o frontend foi reorganizado com menu lateral por etapas dentro da mesma rota para reduzir densidade horizontal, sem alterar payloads, recursos ou semantica REST.

### POST /api/v1/verification-runs

Cria uma verificacao manual auditavel comparando o hash informado manualmente com o hash oficial da versao selecionada. Nesta etapa nao ha coleta MySQL nem canonicalizacao automatica.

Relacao com `collectionMode` nesta fase:

- o endpoint nao executa `CENTRAL_PULL`;
- o endpoint nao recebe eventos de `LOCAL_AGENT_PUSH`;
- o endpoint representa o fluxo manual que mais se aproxima de `MANUAL_SIGNATURE_UPLOAD`, embora ainda aceite ambientes cadastrados com qualquer modo de coleta.

Request:

```json
{
  "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
  "environmentId": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
  "customizationVersionId": "4f631ef1-65f5-4831-9bf6-580d3dfb7040",
  "currentHash": "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
  "triggerType": "MANUAL",
  "requestedBy": "support",
  "correlationId": "manual-check-2026-05-25-001"
}
```

Response `201`:

```json
{
  "id": "5fcd67d2-4a11-42bf-b8de-18f80fb9fd8e",
  "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
  "environmentId": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
  "triggerType": "MANUAL",
  "status": "COMPLETED",
  "startedAt": "2026-05-25T02:20:00Z",
  "finishedAt": "2026-05-25T02:20:00Z",
  "requestedBy": "support",
  "correlationId": "manual-check-2026-05-25-001",
  "result": {
    "id": "2d69427c-9d56-44dd-99f9-5fe4b752fcc2",
    "verificationRunId": "5fcd67d2-4a11-42bf-b8de-18f80fb9fd8e",
    "customizationVersionId": "4f631ef1-65f5-4831-9bf6-580d3dfb7040",
    "currentHash": "2F1C0F9A8D3B4E6F7A8B9C0D1E2F3A4B",
    "officialHash": "2F1C0F9A8D3B4E6F7A8B9C0D1E2F3A4B",
    "status": "MATCH",
    "evidenceReference": "manual-entry",
    "errorCode": null,
    "errorMessage": null,
    "checkedAt": "2026-05-25T02:20:00Z"
  }
}
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:write`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### GET /api/v1/verification-runs

Lista o historico de verificacoes manuais registradas.

Response `200`:

```json
[
  {
    "id": "5fcd67d2-4a11-42bf-b8de-18f80fb9fd8e",
    "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
    "environmentId": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
    "triggerType": "MANUAL",
    "status": "COMPLETED_WITH_DIVERGENCE",
    "startedAt": "2026-05-25T02:21:00Z",
    "finishedAt": "2026-05-25T02:21:00Z",
    "requestedBy": "support",
    "correlationId": "manual-check-2026-05-25-002",
    "result": {
      "id": "7a1a0f0a-7db5-49f4-98d7-d53f13fd67f1",
      "verificationRunId": "5fcd67d2-4a11-42bf-b8de-18f80fb9fd8e",
      "customizationVersionId": "4f631ef1-65f5-4831-9bf6-580d3dfb7040",
      "currentHash": "DEADBEEF",
      "officialHash": "2F1C0F9A8D3B4E6F7A8B9C0D1E2F3A4B",
      "status": "DIVERGENT",
      "evidenceReference": "manual-entry",
      "errorCode": null,
      "errorMessage": null,
      "checkedAt": "2026-05-25T02:21:00Z"
    }
  }
]
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:read`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

### GET /api/v1/verification-runs/{verificationRunId}

Consulta o detalhe de uma verificacao manual registrada.

Response `200`:

```json
{
  "id": "5fcd67d2-4a11-42bf-b8de-18f80fb9fd8e",
  "customerId": "be6d92a0-c868-4bd7-a072-7a45a4f9772a",
  "environmentId": "8877233b-e62f-428a-9f04-fb4a2c6ae86e",
  "triggerType": "MANUAL",
  "status": "COMPLETED",
  "startedAt": "2026-05-25T02:20:00Z",
  "finishedAt": "2026-05-25T02:20:00Z",
  "requestedBy": "support",
  "correlationId": "manual-check-2026-05-25-001",
  "result": {
    "id": "2d69427c-9d56-44dd-99f9-5fe4b752fcc2",
    "verificationRunId": "5fcd67d2-4a11-42bf-b8de-18f80fb9fd8e",
    "customizationVersionId": "4f631ef1-65f5-4831-9bf6-580d3dfb7040",
    "currentHash": "2F1C0F9A8D3B4E6F7A8B9C0D1E2F3A4B",
    "officialHash": "2F1C0F9A8D3B4E6F7A8B9C0D1E2F3A4B",
    "status": "MATCH",
    "evidenceReference": "manual-entry",
    "errorCode": null,
    "errorMessage": null,
    "checkedAt": "2026-05-25T02:20:00Z"
  }
}
```

Autorizacao:

- Perfil padrao: exige autoridade `SCOPE_customizations:read`.
- Perfil `local`: liberado apenas para desenvolvimento Docker local.

## Modelo de Erro Candidato

```json
{
  "timestamp": "2026-05-24T00:00:00Z",
  "status": 400,
  "code": "CUSTOMIZATION_INVALID_OBJECT_TYPE",
  "message": "Tipo de objeto customizado invalido.",
  "details": [
    {
      "field": "objectType",
      "message": "Valor nao suportado."
    }
  ],
  "correlationId": "9f6f0c0c-0000-4000-8000-000000000000"
}
```

## Seguranca de API Candidata

- APIs administrativas protegidas por autenticacao e autorizacao RBAC.
- APIs sistema-sistema protegidas por credenciais tecnicas rotacionaveis, mTLS ou OAuth2 Client Credentials conforme infraestrutura disponivel.
- Endpoints de agente devem validar identidade do ambiente, escopo permitido e idempotencia.
- Todas as respostas devem evitar segredos, connection strings e conteudo sensivel de customizacoes quando nao houver permissao explicita.

## Regras de Idempotencia Candidatas

- Criacao de verificacao manual pode aceitar `Idempotency-Key`.
- Callback de agente deve ser idempotente por `verificationRunId`, `customizationVersionId` e hash atual.
- Notificacao ao legado deve usar chave idempotente baseada em `divergenceId`.

## Stack Considerada Para Contratos

- Backend: Java 17 com Spring Boot estavel.
- Banco: PostgreSQL.
- Frontend: Angular.
- Especificacao: OpenAPI 3.1 quando houver API REST.

## Padrao Esperado Futuro

Quando o contrato final for aprovado, este documento devera conter:

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

## Pendencias Para Contrato Final

- Confirmar politica de autenticacao e autorizacao.
- Confirmar se o legado recebera notificacoes com idempotency key.
- Confirmar payload aceito pelo legado.
- Confirmar se agentes on-premise serao obrigatorios, opcionais ou faseados.
- Confirmar se conteudo completo da customizacao pode trafegar pela API ou apenas hash/assinatura.
- Definir se `verification-results` continuara apenas embutido em `verification-runs` ou ganhara endpoint dedicado.
- Definir paginacao, filtros e ordenacao padrao.
- Gerar especificacao OpenAPI 3.1 apos aprovacao da arquitetura.
