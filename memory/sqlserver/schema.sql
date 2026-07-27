IF OBJECT_ID(N'dbo.eventos_memorias_agentes', N'U') IS NOT NULL
BEGIN
    ALTER TABLE dbo.eventos_memorias_agentes DROP CONSTRAINT FK_eventos_memorias_agentes_memoria_id;
END;

IF OBJECT_ID(N'dbo.eventos_memorias_agentes', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.eventos_memorias_agentes (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        memoria_id UNIQUEIDENTIFIER NOT NULL,
        acao NVARCHAR(32) NOT NULL,
        autor NVARCHAR(128) NOT NULL,
        observacao_evento NVARCHAR(1000) NULL,
        criado_em DATETIME2(0) NOT NULL
    );
END;

IF OBJECT_ID(N'dbo.memorias_agentes', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.memorias_agentes (
        id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
        nome_agente NVARCHAR(128) NOT NULL,
        categoria NVARCHAR(32) NOT NULL,
        escopo NVARCHAR(32) NOT NULL CONSTRAINT DF_memorias_agentes_escopo DEFAULT N'project',
        titulo NVARCHAR(255) NOT NULL,
        conteudo NVARCHAR(MAX) NOT NULL,
        tags NVARCHAR(1000) NOT NULL CONSTRAINT DF_knowledge_entries_tags DEFAULT N'',
        tipo_origem NVARCHAR(64) NOT NULL CONSTRAINT DF_memorias_agentes_tipo_origem DEFAULT N'manual',
        referencia_origem NVARCHAR(255) NULL,
        situacao NVARCHAR(32) NOT NULL CONSTRAINT DF_memorias_agentes_situacao DEFAULT N'active',
        confianca INT NOT NULL CONSTRAINT DF_memorias_agentes_confianca DEFAULT 3,
        criado_em DATETIME2(0) NOT NULL,
        atualizado_em DATETIME2(0) NOT NULL,
        ultimo_uso_em DATETIME2(0) NULL,
        CONSTRAINT UQ_memorias_agentes_nome_categoria_titulo UNIQUE (nome_agente, categoria, titulo),
        CONSTRAINT CK_memorias_agentes_categoria CHECK (categoria IN (N'context', N'rule', N'lesson', N'improvement')),
        CONSTRAINT CK_memorias_agentes_escopo CHECK (escopo IN (N'global', N'project', N'task')),
        CONSTRAINT CK_memorias_agentes_situacao CHECK (situacao IN (N'active', N'archived')),
        CONSTRAINT CK_memorias_agentes_confianca CHECK (confianca BETWEEN 1 AND 5)
    );
END;

IF OBJECT_ID(N'idx_memorias_agentes_nome_categoria_situacao', N'IX') IS NULL
BEGIN
    CREATE INDEX idx_memorias_agentes_nome_categoria_situacao
        ON dbo.memorias_agentes (nome_agente, categoria, situacao);
END;

IF OBJECT_ID(N'idx_memorias_agentes_escopo_situacao', N'IX') IS NULL
BEGIN
    CREATE INDEX idx_memorias_agentes_escopo_situacao
        ON dbo.memorias_agentes (escopo, situacao);
END;

IF OBJECT_ID(N'idx_memorias_agentes_ultimo_uso_em', N'IX') IS NULL
BEGIN
    CREATE INDEX idx_memorias_agentes_ultimo_uso_em
        ON dbo.memorias_agentes (ultimo_uso_em);
END;

IF OBJECT_ID(N'idx_eventos_memorias_agentes_memoria_criado_em', N'IX') IS NULL
BEGIN
    CREATE INDEX idx_eventos_memorias_agentes_memoria_criado_em
        ON dbo.eventos_memorias_agentes (memoria_id, criado_em);
END;

IF NOT EXISTS (
    SELECT 1
    FROM sys.foreign_keys
    WHERE name = N'FK_eventos_memorias_agentes_memoria_id'
)
BEGIN
    ALTER TABLE dbo.eventos_memorias_agentes
    ADD CONSTRAINT FK_eventos_memorias_agentes_memoria_id
        FOREIGN KEY (memoria_id) REFERENCES dbo.memorias_agentes (id);
END;
