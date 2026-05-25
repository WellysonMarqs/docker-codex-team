CREATE TABLE legacy_notifications (
    id UUID PRIMARY KEY,
    divergence_id UUID NOT NULL REFERENCES divergences (id),
    customer_id UUID NOT NULL REFERENCES customers (id),
    environment_id UUID NOT NULL REFERENCES customer_environments (id),
    customization_id UUID NOT NULL REFERENCES customizations (id),
    type VARCHAR(40) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payload_json TEXT NOT NULL,
    idempotency_key VARCHAR(120) NOT NULL UNIQUE,
    attempts INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_attempt_at TIMESTAMP WITH TIME ZONE NULL,
    sent_at TIMESTAMP WITH TIME ZONE NULL,
    last_error VARCHAR(500) NULL,
    correlation_id VARCHAR(120) NULL
);

CREATE INDEX idx_legacy_notifications_customer_id ON legacy_notifications (customer_id);
CREATE INDEX idx_legacy_notifications_environment_id ON legacy_notifications (environment_id);
CREATE INDEX idx_legacy_notifications_divergence_id ON legacy_notifications (divergence_id);
