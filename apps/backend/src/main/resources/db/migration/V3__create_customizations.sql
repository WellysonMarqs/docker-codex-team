CREATE TABLE customizations (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    environment_id UUID NOT NULL,
    name VARCHAR(160) NOT NULL,
    description VARCHAR(500),
    object_type VARCHAR(32) NOT NULL,
    object_identifier VARCHAR(240) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_by VARCHAR(120),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_customizations_customer
        FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_customizations_environment
        FOREIGN KEY (environment_id) REFERENCES customer_environments (id)
);

CREATE INDEX idx_customizations_customer_id ON customizations (customer_id);
CREATE INDEX idx_customizations_environment_id ON customizations (environment_id);
CREATE INDEX idx_customizations_object_type ON customizations (object_type);
CREATE INDEX idx_customizations_object_identifier ON customizations (object_identifier);
