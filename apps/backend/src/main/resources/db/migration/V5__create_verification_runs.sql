CREATE TABLE verification_runs (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    environment_id UUID NOT NULL,
    trigger_type VARCHAR(32) NOT NULL,
    status VARCHAR(40) NOT NULL,
    started_at TIMESTAMPTZ NOT NULL,
    finished_at TIMESTAMPTZ NOT NULL,
    requested_by VARCHAR(120),
    correlation_id VARCHAR(120),
    CONSTRAINT fk_verification_runs_customer
        FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_verification_runs_environment
        FOREIGN KEY (environment_id) REFERENCES customer_environments (id)
);

CREATE INDEX idx_verification_runs_customer_id ON verification_runs (customer_id);
CREATE INDEX idx_verification_runs_environment_id ON verification_runs (environment_id);
CREATE INDEX idx_verification_runs_trigger_type ON verification_runs (trigger_type);
CREATE INDEX idx_verification_runs_status ON verification_runs (status);
CREATE INDEX idx_verification_runs_started_at ON verification_runs (started_at DESC);
