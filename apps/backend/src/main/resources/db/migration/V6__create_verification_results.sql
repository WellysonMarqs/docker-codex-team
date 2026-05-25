CREATE TABLE verification_results (
    id UUID PRIMARY KEY,
    verification_run_id UUID NOT NULL UNIQUE,
    customization_version_id UUID NOT NULL,
    current_hash VARCHAR(128) NOT NULL,
    official_hash VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    evidence_reference VARCHAR(240),
    error_code VARCHAR(80),
    error_message VARCHAR(500),
    checked_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_verification_results_run
        FOREIGN KEY (verification_run_id) REFERENCES verification_runs (id),
    CONSTRAINT fk_verification_results_customization_version
        FOREIGN KEY (customization_version_id) REFERENCES customization_versions (id)
);

CREATE INDEX idx_verification_results_verification_run_id
    ON verification_results (verification_run_id);

CREATE INDEX idx_verification_results_customization_version_id
    ON verification_results (customization_version_id);

CREATE INDEX idx_verification_results_status
    ON verification_results (status);
