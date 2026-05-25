CREATE TABLE customer_environments (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    name VARCHAR(120) NOT NULL,
    type VARCHAR(32) NOT NULL,
    collection_mode VARCHAR(40) NOT NULL,
    status VARCHAR(32) NOT NULL,
    credential_reference_id VARCHAR(160),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_customer_environments_customer
        FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE INDEX idx_customer_environments_customer_id ON customer_environments (customer_id);
CREATE INDEX idx_customer_environments_type ON customer_environments (type);
CREATE INDEX idx_customer_environments_collection_mode ON customer_environments (collection_mode);
