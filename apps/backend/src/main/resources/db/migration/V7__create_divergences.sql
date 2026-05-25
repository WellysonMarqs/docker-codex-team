CREATE TABLE divergences (
    id UUID PRIMARY KEY,
    verification_result_id UUID NOT NULL UNIQUE,
    customer_id UUID NOT NULL,
    environment_id UUID NOT NULL,
    customization_id UUID NOT NULL,
    severity VARCHAR(32) NOT NULL,
    status VARCHAR(40) NOT NULL,
    detected_at TIMESTAMPTZ NOT NULL,
    resolved_at TIMESTAMPTZ,
    correlation_id VARCHAR(120),
    CONSTRAINT fk_divergences_verification_result
        FOREIGN KEY (verification_result_id) REFERENCES verification_results (id),
    CONSTRAINT fk_divergences_customer
        FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_divergences_environment
        FOREIGN KEY (environment_id) REFERENCES customer_environments (id),
    CONSTRAINT fk_divergences_customization
        FOREIGN KEY (customization_id) REFERENCES customizations (id)
);

CREATE INDEX idx_divergences_customer_id
    ON divergences (customer_id);

CREATE INDEX idx_divergences_environment_id
    ON divergences (environment_id);

CREATE INDEX idx_divergences_customization_id
    ON divergences (customization_id);

CREATE INDEX idx_divergences_status
    ON divergences (status);
