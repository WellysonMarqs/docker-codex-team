package com.customizationaudit.legacynotification.adapter.out.persistence;

import com.customizationaudit.legacynotification.domain.LegacyNotification;
import com.customizationaudit.legacynotification.domain.LegacyNotificationStatus;
import com.customizationaudit.legacynotification.domain.LegacyNotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "legacy_notifications")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LegacyNotificationJpaEntity {

    @Id
    private UUID id;

    @Column(name = "divergence_id", nullable = false)
    private UUID divergenceId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "environment_id", nullable = false)
    private UUID environmentId;

    @Column(name = "customization_id", nullable = false)
    private UUID customizationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private LegacyNotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LegacyNotificationStatus status;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 120)
    private String idempotencyKey;

    @Column(nullable = false)
    private int attempts;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "last_error", length = 500)
    private String lastError;

    @Column(name = "correlation_id", length = 120)
    private String correlationId;

    static LegacyNotificationJpaEntity fromDomain(LegacyNotification legacyNotification) {
        return new LegacyNotificationJpaEntity(
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

    LegacyNotification toDomain() {
        return LegacyNotification.restore(
                id,
                divergenceId,
                customerId,
                environmentId,
                customizationId,
                type,
                status,
                payloadJson,
                idempotencyKey,
                attempts,
                createdAt,
                lastAttemptAt,
                sentAt,
                lastError,
                correlationId
        );
    }
}
