CREATE TABLE customization_versions (
    id UUID PRIMARY KEY,
    customization_id UUID NOT NULL,
    legacy_system_version VARCHAR(80) NOT NULL,
    official_hash VARCHAR(128) NOT NULL,
    hash_algorithm VARCHAR(32) NOT NULL,
    canonicalization_version VARCHAR(40) NOT NULL,
    content_signature VARCHAR(4000),
    registered_by VARCHAR(120),
    registered_at TIMESTAMPTZ NOT NULL,
    active_from TIMESTAMPTZ,
    active_until TIMESTAMPTZ,
    status VARCHAR(32) NOT NULL,
    CONSTRAINT fk_customization_versions_customization
        FOREIGN KEY (customization_id) REFERENCES customizations (id)
);

CREATE INDEX idx_customization_versions_customization_id
    ON customization_versions (customization_id);

CREATE INDEX idx_customization_versions_legacy_system_version
    ON customization_versions (legacy_system_version);

CREATE INDEX idx_customization_versions_status
    ON customization_versions (status);
