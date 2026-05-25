package com.customizationaudit.divergence.application;

import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.divergence.domain.DivergenceSeverity;
import com.customizationaudit.divergence.domain.DivergenceStatus;

import java.time.Instant;
import java.util.UUID;

public record DivergenceView(
        UUID id,
        UUID verificationResultId,
        UUID customerId,
        UUID environmentId,
        UUID customizationId,
        DivergenceSeverity severity,
        DivergenceStatus status,
        Instant detectedAt,
        Instant resolvedAt,
        String correlationId
) {
    public static DivergenceView from(Divergence divergence) {
        return new DivergenceView(
                divergence.id(),
                divergence.verificationResultId(),
                divergence.customerId(),
                divergence.environmentId(),
                divergence.customizationId(),
                divergence.severity(),
                divergence.status(),
                divergence.detectedAt(),
                divergence.resolvedAt(),
                divergence.correlationId()
        );
    }
}
