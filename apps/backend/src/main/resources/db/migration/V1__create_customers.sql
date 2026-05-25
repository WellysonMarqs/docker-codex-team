CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    external_reference VARCHAR(80),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_customers_status ON customers (status);
CREATE INDEX idx_customers_external_reference ON customers (external_reference);
