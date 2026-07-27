# Memoria Persistente dos Agentes

## Objetivo

Os agentes agora podem registrar e reaproveitar conhecimento operacional duravel em um banco SQL Server local.

Essa memoria existe para armazenar:

- contextos relevantes que precisam sobreviver entre sessoes;
- regras operacionais e arquiteturais aprendidas;
- licoes aprendidas apos erros, incidentes ou retrabalho;
- melhorias aprovadas para o fluxo dos agentes.
- conteudo relevante da documentacao obrigatoria.

## Banco e schema

- Banco padrao: `MemoriaAgentes` na instancia SQL Server local `localhost:1433`
- Schema principal: `memory/sqlserver/schema.sql`
- Implementacao: `tools/agent_memory.py`

## Estrutura fisica do banco

Tabelas:

- `memorias_agentes`
- `eventos_memorias_agentes`

Colunas principais:

- `nome_agente`
- `categoria`
- `escopo`
- `titulo`
- `conteudo`
- `tipo_origem`
- `referencia_origem`
- `situacao`
- `confianca`
- `criado_em`
- `atualizado_em`
- `ultimo_uso_em`

## Categorias suportadas

- `context`
- `rule`
- `lesson`
- `improvement`

## Quando consultar

Consulte a memoria antes de responder ou implementar quando:

- o pedido depender de decisoes anteriores do time;
- houver risco de repetir erro ja conhecido;
- existir regra operacional ou arquitetural que pode ter sido aprendida em execucoes anteriores;
- a tarefa for recorrente ou evolutiva.

## Quando persistir

Registre memoria somente quando o aprendizado for reutilizavel.

Persistir quando houver:

- nova regra validada;
- contexto recorrente de projeto;
- licao aprendida apos falha, bug, incidente ou retrabalho;
- melhoria aprovada no modo de operacao dos agentes.
- atualizacao relevante de documentacao que precise ser reaproveitada entre sessoes.

Nao persistir:

- pensamento temporario de uma unica resposta;
- informacao redundante que ja esta clara em `AGENTS.md` ou nos documentos obrigatorios;
- ruido operacional sem reaproveitamento futuro.

## Comandos

Inicializar banco:

```bash
python3 tools/agent_memory.py --sqlserver-host host.docker.internal --sqlserver-target-port 1433 init
```

Registrar ou atualizar memoria:

```bash
python3 tools/agent_memory.py --sqlserver-host host.docker.internal --sqlserver-target-port 1433 upsert \
  --agent root \
  --category rule \
  --title "Sempre validar documentacao obrigatoria" \
  --content "Toda alteracao com impacto em arquitetura, tarefas, decisoes ou contratos deve atualizar os quatro documentos obrigatorios." \
  --tags docs,governanca \
  --source-kind decision \
  --source-ref ADR-014 \
  --scope project \
  --confidence 5
```

Pesquisar memoria relevante:

```bash
python3 tools/agent_memory.py --sqlserver-host host.docker.internal --sqlserver-target-port 1433 search --agent root --query documentacao --limit 5
```

Listar memorias ativas:

```bash
python3 tools/agent_memory.py --sqlserver-host host.docker.internal --sqlserver-target-port 1433 list --agent root --category rule
```

Arquivar memoria obsoleta:

```bash
python3 tools/agent_memory.py --sqlserver-host host.docker.internal --sqlserver-target-port 1433 archive --id <entry-id> --actor root
```

## Regras operacionais

- Use `agent_name=shared` quando a memoria for util para todos os agentes.
- Prefira titulos curtos, especificos e estaveis.
- Use `source_kind` e `source_ref` para rastrear de onde veio o aprendizado.
- Ao reaproveitar uma memoria relevante, execute `touch` para atualizar `last_used_at`.
- Se a regra mudar, atualize o mesmo registro em vez de criar duplicata.
- O cliente `sqlcmd` usado pela CLI roda hoje no container `agent-memory-sqlserver`, mas o banco principal esta na instancia nativa do host.
