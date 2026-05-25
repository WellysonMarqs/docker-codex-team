package com.customizationaudit.legacynotification.adapter.in.web;

import com.customizationaudit.legacynotification.domain.LegacyNotification;
import com.customizationaudit.legacynotification.domain.LegacyNotificationStatus;
import com.customizationaudit.legacynotification.domain.LegacyNotificationType;

import java.time.Instant;
import java.util.UUID;

public record LegacyNotificationResponse(
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
    static LegacyNotificationResponse from(LegacyNotification legacyNotification) {
        return new LegacyNotificationResponse(
                legacyNotification.id(),
                legacyNotification.divergenceId(),
                legacyNotification.customerId(),
                legacyNotification.environmentId(),
                legacyNotification.customizationId(),
                legacyNotification.type(),
                legacyNotification.status(),
                legacyNotification.payloadJson(),
                legacyNotification.idempotencyKey(),
                legacyNotification.attempts(),
                legacyNotification.createdAt(),
                legacyNotification.lastAttemptAt(),
                legacyNotification.sentAt(),
                legacyNotification.lastError(),
                legacyNotification.correlationId()
        );
    }
}
