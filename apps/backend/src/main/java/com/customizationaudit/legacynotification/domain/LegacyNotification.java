package com.customizationaudit.legacynotification.domain;

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
public class LegacyNotification {
    UUID id;
    UUID divergenceId;
    UUID customerId;
    UUID environmentId;
    UUID customizationId;
    LegacyNotificationType type;
    LegacyNotificationStatus status;
    String payloadJson;
    String idempotencyKey;
    int attempts;
    Instant createdAt;
    Instant lastAttemptAt;
    Instant sentAt;
    String lastError;
    String correlationId;

    public static LegacyNotification pending(
            UUID divergenceId,
            UUID customerId,
            UUID environmentId,
            UUID customizationId,
            LegacyNotificationType type,
            String payloadJson,
            String idempotencyKey,
            Instant createdAt,
            String correlationId
    ) {
        return restore(
                UUID.randomUUID(),
                divergenceId,
                customerId,
                environmentId,
                customizationId,
                type,
                LegacyNotificationStatus.PENDING,
                payloadJson,
                idempotencyKey,
                0,
                createdAt,
                null,
                null,
                null,
                correlationId
        );
    }

    public static LegacyNotification restore(
            UUID id,
            UUID divergenceId,
            UUID customerId,
            UUID environmentId,
            UUID customizationId,
            LegacyNotificationType type,
            LegacyNotificationStatus status,
            String payloadJson,
            String idempotencyKey,
            int attempts,
            Instant createdAt,
            Instant lastAttemptAt,
            Instant sentAt,
            String lastError,
            String correlationId
    ) {
        if (attempts < 0) {
            throw new IllegalArgumentException("attempts must be non-negative");
        }
        return new LegacyNotification(
                Objects.requireNonNull(id, "id is required"),
                Objects.requireNonNull(divergenceId, "divergenceId is required"),
                Objects.requireNonNull(customerId, "customerId is required"),
                Objects.requireNonNull(environmentId, "environmentId is required"),
                Objects.requireNonNull(customizationId, "customizationId is required"),
                Objects.requireNonNull(type, "type is required"),
                Objects.requireNonNull(status, "status is required"),
                requireText(payloadJson, "payloadJson is required"),
                requireText(idempotencyKey, "idempotencyKey is required"),
                attempts,
                Objects.requireNonNull(createdAt, "createdAt is required"),
                lastAttemptAt,
                sentAt,
                normalizeOptional(lastError),
                normalizeOptional(correlationId)
        );
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private static String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
