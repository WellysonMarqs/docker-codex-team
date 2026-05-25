package com.customizationaudit.divergence.domain;

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
public class Divergence {
    UUID id;
    UUID verificationResultId;
    UUID customerId;
    UUID environmentId;
    UUID customizationId;
    DivergenceSeverity severity;
    DivergenceStatus status;
    Instant detectedAt;
    Instant resolvedAt;
    String correlationId;

    public static Divergence open(
            UUID verificationResultId,
            UUID customerId,
            UUID environmentId,
            UUID customizationId,
            DivergenceSeverity severity,
            Instant detectedAt,
            String correlationId
    ) {
        return restore(
                UUID.randomUUID(),
                verificationResultId,
                customerId,
                environmentId,
                customizationId,
                severity,
                DivergenceStatus.OPEN,
                detectedAt,
                null,
                correlationId
        );
    }

    public static Divergence restore(
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
        return new Divergence(
                Objects.requireNonNull(id, "id is required"),
                Objects.requireNonNull(verificationResultId, "verificationResultId is required"),
                Objects.requireNonNull(customerId, "customerId is required"),
                Objects.requireNonNull(environmentId, "environmentId is required"),
                Objects.requireNonNull(customizationId, "customizationId is required"),
                Objects.requireNonNull(severity, "severity is required"),
                Objects.requireNonNull(status, "status is required"),
                Objects.requireNonNull(detectedAt, "detectedAt is required"),
                resolvedAt,
                normalizeOptional(correlationId)
        );
    }

    public Divergence transitionTo(DivergenceStatus targetStatus, Instant occurredAt) {
        Objects.requireNonNull(targetStatus, "targetStatus is required");
        Objects.requireNonNull(occurredAt, "occurredAt is required");

        if (status == targetStatus) {
            return this;
        }

        return switch (targetStatus) {
            case ACKNOWLEDGED -> acknowledge();
            case RESOLVED -> resolve(occurredAt);
            default -> throw new IllegalArgumentException("unsupported divergence status transition");
        };
    }

    private Divergence acknowledge() {
        if (status == DivergenceStatus.RESOLVED) {
            throw new IllegalArgumentException("resolved divergence cannot be acknowledged");
        }
        return restore(
                id,
                verificationResultId,
                customerId,
                environmentId,
                customizationId,
                severity,
                DivergenceStatus.ACKNOWLEDGED,
                detectedAt,
                resolvedAt,
                correlationId
        );
    }

    private Divergence resolve(Instant occurredAt) {
        if (status == DivergenceStatus.RESOLVED) {
            return this;
        }
        if (status != DivergenceStatus.OPEN && status != DivergenceStatus.ACKNOWLEDGED) {
            throw new IllegalArgumentException("only open or acknowledged divergence can be resolved");
        }
        return restore(
                id,
                verificationResultId,
                customerId,
                environmentId,
                customizationId,
                severity,
                DivergenceStatus.RESOLVED,
                detectedAt,
                occurredAt,
                correlationId
        );
    }

    private static String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
