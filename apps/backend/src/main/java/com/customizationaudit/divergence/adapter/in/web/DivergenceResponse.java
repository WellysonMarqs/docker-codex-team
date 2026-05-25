package com.customizationaudit.divergence.adapter.in.web;

import com.customizationaudit.divergence.application.DivergenceView;
import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.divergence.domain.DivergenceSeverity;
import com.customizationaudit.divergence.domain.DivergenceStatus;

import java.time.Instant;
import java.util.UUID;

public record DivergenceResponse(
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
    static DivergenceResponse from(Divergence divergence) {
        return new DivergenceResponse(
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

    static DivergenceResponse from(DivergenceView divergence) {
        return new DivergenceResponse(
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
