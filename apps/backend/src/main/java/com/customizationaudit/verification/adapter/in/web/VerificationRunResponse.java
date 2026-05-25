package com.customizationaudit.verification.adapter.in.web;

import com.customizationaudit.verification.application.VerificationExecution;
import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationRunStatus;
import com.customizationaudit.verification.domain.VerificationTriggerType;

import java.time.Instant;
import java.util.UUID;

public record VerificationRunResponse(
        UUID id,
        UUID customerId,
        UUID environmentId,
        VerificationTriggerType triggerType,
        VerificationRunStatus status,
        Instant startedAt,
        Instant finishedAt,
        String requestedBy,
        String correlationId,
        VerificationResultResponse result
) {
    static VerificationRunResponse from(VerificationExecution execution) {
        return new VerificationRunResponse(
                execution.run().id(),
                execution.run().customerId(),
                execution.run().environmentId(),
                execution.run().triggerType(),
                execution.run().status(),
                execution.run().startedAt(),
                execution.run().finishedAt(),
                execution.run().requestedBy(),
                execution.run().correlationId(),
                VerificationResultResponse.from(execution.result())
        );
    }

    static VerificationRunResponse from(VerificationRun run, VerificationResultResponse result) {
        return new VerificationRunResponse(
                run.id(),
                run.customerId(),
                run.environmentId(),
                run.triggerType(),
                run.status(),
                run.startedAt(),
                run.finishedAt(),
                run.requestedBy(),
                run.correlationId(),
                result
        );
    }
}
