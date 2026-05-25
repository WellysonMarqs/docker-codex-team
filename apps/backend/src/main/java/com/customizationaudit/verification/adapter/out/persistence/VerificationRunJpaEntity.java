package com.customizationaudit.verification.adapter.out.persistence;

import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationRunStatus;
import com.customizationaudit.verification.domain.VerificationTriggerType;

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
@Table(name = "verification_runs")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationRunJpaEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "environment_id", nullable = false)
    private UUID environmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false, length = 32)
    private VerificationTriggerType triggerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private VerificationRunStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at", nullable = false)
    private Instant finishedAt;

    @Column(name = "requested_by", length = 120)
    private String requestedBy;

    @Column(name = "correlation_id", length = 120)
    private String correlationId;

    static VerificationRunJpaEntity fromDomain(VerificationRun run) {
        return new VerificationRunJpaEntity(
                run.id(),
                run.customerId(),
                run.environmentId(),
                run.triggerType(),
                run.status(),
                run.startedAt(),
                run.finishedAt(),
                run.requestedBy(),
                run.correlationId()
        );
    }

    VerificationRun toDomain() {
        return VerificationRun.restore(
                id,
                customerId,
                environmentId,
                triggerType,
                status,
                startedAt,
                finishedAt,
                requestedBy,
                correlationId
        );
    }
}
