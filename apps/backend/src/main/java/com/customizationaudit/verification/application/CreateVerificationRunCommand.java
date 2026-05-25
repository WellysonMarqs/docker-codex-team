package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationTriggerType;

import java.util.UUID;

public record CreateVerificationRunCommand(
        UUID customerId,
        UUID environmentId,
        UUID customizationVersionId,
        String currentHash,
        VerificationTriggerType triggerType,
        String requestedBy,
        String correlationId
) {
}
