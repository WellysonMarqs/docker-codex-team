# Prompt: Sistema de Controle e Auditoria de Customizações

Contexto do problema:

Temos um sistema legado que roda em dois tipos de ambiente:

1. SaaS, hospedado e controlado pela nossa empresa.
2. On-premise, instalado no ambiente do próprio cliente.

O sistema legado permite customizações em algumas telas e em funções de cálculo armazenadas no banco MySQL. Cada cliente pode ter sua própria customização.

Exemplo relevante:
O sistema legado possui funções no banco que calculam juros e recálculo de dívidas. Cada cliente pode ter uma fórmula própria de cálculo. O processamento do valor da dívida acontece no banco de dados.

Situação atual:

- Para clientes SaaS, a equipe de suporte controla e aplica essas customizações.
- Para clientes on-premise, o cliente precisa solicitar à equipe de suporte que aplique as customizações no banco de dados dele.
- Alguns clientes on-premise alteram as customizações por conta própria, sem solicitar suporte.
- Essas alterações não controladas podem gerar bugs e problemas quando o sistema legado é atualizado para uma nova versão.

Problema a resolver:

Precisamos criar um novo sistema para controlar quais customizações existem, a quais clientes pertencem e se a customização instalada no banco do cliente continua igual à versão registrada oficialmente.

Objetivo do novo sistema:

Criar um sistema de controle e auditoria de customizações do sistema legado, capaz de:

- registrar customizações oficiais por cliente;
- armazenar um hash do conteúdo da customização;
- identificar qual tabela, função, procedure, trigger ou objeto customizado pertence a qual cliente;
- consultar periodicamente ou sob demanda o banco do cliente;
- gerar um hash do conteúdo atual da customização no banco do cliente;
- comparar o hash atual com o hash oficial registrado;
- detectar alterações não autorizadas ou divergentes;
- notificar o sistema legado quando houver divergência;
- permitir integração por API REST.

Exemplo de funcionamento esperado:

1. A equipe registra no novo sistema que o cliente X possui uma customização na função de cálculo de juros.
2. O novo sistema armazena:
   - cliente;
   - ambiente: SaaS ou on-premise;
   - tipo de objeto customizado;
   - identificador do objeto, por exemplo nome da tabela, função, procedure ou trigger;
   - conteúdo ou assinatura da customização;
   - hash oficial da customização;
   - versão do sistema legado relacionada;
   - data de registro;
   - responsável pelo registro.
3. Periodicamente, ou quando houver atualização da tabela/função/procedure, o novo sistema consulta o banco do cliente.
4. O sistema gera um novo hash do conteúdo encontrado.
5. Se o hash atual for igual ao hash oficial, não houve alteração.
6. Se o hash atual for diferente, houve alteração não registrada ou divergente.
7. A divergência deve ser registrada e notificada ao sistema legado via API REST.

Requisitos funcionais iniciais:

- Cadastrar clientes.
- Cadastrar ambientes do cliente: SaaS ou on-premise.
- Cadastrar customizações oficiais por cliente.
- Registrar hash oficial da customização.
- Registrar tipo de customização: tabela, função, procedure, trigger, script SQL ou outro objeto.
- Consultar customizações registradas.
- Executar verificação de integridade das customizações.
- Comparar hash oficial com hash atual encontrado no banco do cliente.
- Registrar histórico de verificações.
- Registrar divergências.
- Notificar o sistema legado via API REST quando houver divergência.
- Permitir execução manual de verificação.
- Permitir execução periódica/agendada de verificação.

Requisitos não funcionais:

- Segurança é crítica, pois o sistema pode acessar bancos de clientes.
- Credenciais de banco dos clientes não podem ficar expostas.
- Deve haver auditoria de ações.
- Deve haver rastreabilidade das verificações.
- Deve haver logs estruturados.
- Deve ser possível entender facilmente qual cliente possui qual customização.
- Deve ser robusto para ambientes SaaS e on-premise.
- Deve ser extensível para novos tipos de customização no futuro.
- Deve evitar acoplamento forte com detalhes internos do legado.
- Deve considerar performance, pois alguns clientes podem ter muitas customizações.
- Deve considerar falhas de conexão com ambientes on-premise.
- Deve considerar retry, timeout e tratamento de erro.
- Deve considerar versionamento das customizações.

Stack obrigatória:

- Backend: Java 17 com Spring Boot estável.
- Banco do novo sistema: PostgreSQL.
- Frontend: Angular.
- Sistema legado: usa MySQL.
- Integração de notificação: API REST.

Restrições importantes:

- Não implementar código ainda.
- Primeiro, o architect deve analisar o problema.
- A arquitetura final só deve ser proposta após a análise do problema.
- O coordinator deve validar a proposta antes de qualquer implementação.
- Devem ser atualizados, quando aplicável:
  - ARCHITECTURE.md
  - DECISIONS.md
  - TASKS.md
  - API_CONTRACT.md
  - README.md

Tarefas esperadas do multi-agent neste momento:

1. O root/coordinator deve entender o problema e organizar o trabalho.
2. O coordinator deve acionar o architect para:
   - analisar o domínio;
   - identificar bounded contexts candidatos;
   - identificar riscos;
   - identificar requisitos funcionais e não funcionais;
   - propor alternativas arquiteturais;
   - explicar trade-offs;
   - sugerir modelo inicial de dados;
   - sugerir estratégia de hashing;
   - sugerir estratégia de coleta/verificação em ambientes SaaS e on-premise;
   - sugerir estratégia de notificação via API REST;
   - sugerir estratégia de segurança para credenciais e acesso aos bancos dos clientes;
   - sugerir estratégia de auditoria e observabilidade.
3. O coordinator deve validar a proposta do architect.
4. Nenhum backend_dev ou frontend_dev deve implementar código ainda.
5. O resultado esperado é documentação técnica inicial e organização das próximas tarefas.

Perguntas que o multi-agent deve responder:

- Qual é o domínio principal do sistema?
- Quais entidades iniciais parecem necessárias?
- Como representar cliente, ambiente, customização, hash oficial, verificação e divergência?
- Como calcular hash de forma determinística para objetos SQL?
- Como evitar falsos positivos por diferença de formatação SQL?
- Como acessar bancos MySQL de clientes on-premise com segurança?
- O sistema deve puxar dados dos clientes ou os clientes devem enviar dados?
- Como lidar com clientes on-premise sem acesso direto de rede?
- Como versionar customizações por versão do legado?
- Como notificar o legado via REST?
- Quais riscos existem?
- Quais decisões precisam ser registradas em DECISIONS.md?
- Quais tarefas devem ser criadas em TASKS.md?

Critério de sucesso desta rodada:

O multi-agent deve produzir uma análise clara do problema, levantar riscos e perguntas pendentes, propor uma arquitetura inicial candidata com trade-offs, e atualizar a documentação obrigatória sem implementar código.
