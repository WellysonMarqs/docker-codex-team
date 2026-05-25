package com.customizationaudit.verification.domain;

public enum VerificationRunStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    COMPLETED_WITH_DIVERGENCE,
    FAILED,
    CANCELLED
}
