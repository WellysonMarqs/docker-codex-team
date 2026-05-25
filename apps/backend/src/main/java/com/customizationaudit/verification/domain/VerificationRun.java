package com.customizationaudit.verification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Value
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationRun {
    UUID id;
    UUID customerId;
    UUID environmentId;
    VerificationTriggerType triggerType;
    VerificationRunStatus status;
    Instant startedAt;
    Instant finishedAt;
    String requestedBy;
    String correlationId;

    public static VerificationRun register(
            UUID customerId,
            UUID environmentId,
            VerificationTriggerType triggerType,
            VerificationRunStatus status,
            String requestedBy,
            String correlationId,
            Instant startedAt,
            Instant finishedAt
    ) {
        return new VerificationRun(
                UUID.randomUUID(),
                Objects.requireNonNull(customerId, "customerId is required"),
                Objects.requireNonNull(environmentId, "environmentId is required"),
                Objects.requireNonNull(triggerType, "triggerType is required"),
                Objects.requireNonNull(status, "status is required"),
                Objects.requireNonNull(startedAt, "startedAt is required"),
                Objects.requireNonNull(finishedAt, "finishedAt is required"),
                normalizeOptional(requestedBy),
                normalizeOptional(correlationId)
        );
    }

    public static VerificationRun restore(
            UUID id,
            UUID customerId,
            UUID environmentId,
            VerificationTriggerType triggerType,
            VerificationRunStatus status,
            Instant startedAt,
            Instant finishedAt,
            String requestedBy,
            String correlationId
    ) {
        return new VerificationRun(
                Objects.requireNonNull(id, "id is required"),
                Objects.requireNonNull(customerId, "customerId is required"),
                Objects.requireNonNull(environmentId, "environmentId is required"),
                Objects.requireNonNull(triggerType, "triggerType is required"),
                Objects.requireNonNull(status, "status is required"),
                Objects.requireNonNull(startedAt, "startedAt is required"),
                Objects.requireNonNull(finishedAt, "finishedAt is required"),
                normalizeOptional(requestedBy),
                normalizeOptional(correlationId)
        );
    }

    private static String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
