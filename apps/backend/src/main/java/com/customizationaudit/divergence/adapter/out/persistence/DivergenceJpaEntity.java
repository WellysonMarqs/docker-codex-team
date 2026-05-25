package com.customizationaudit.divergence.adapter.out.persistence;

import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.divergence.domain.DivergenceSeverity;
import com.customizationaudit.divergence.domain.DivergenceStatus;

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
@Table(name = "divergences")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DivergenceJpaEntity {

    @Id
    private UUID id;

    @Column(name = "verification_result_id", nullable = false, unique = true)
    private UUID verificationResultId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "environment_id", nullable = false)
    private UUID environmentId;

    @Column(name = "customization_id", nullable = false)
    private UUID customizationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DivergenceSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private DivergenceStatus status;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "correlation_id", length = 120)
    private String correlationId;

    static DivergenceJpaEntity fromDomain(Divergence divergence) {
        return new DivergenceJpaEntity(
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

    Divergence toDomain() {
        return Divergence.restore(
                id,
                verificationResultId,
                customerId,
                environmentId,
                customizationId,
                severity,
                status,
                detectedAt,
                resolvedAt,
                correlationId
        );
    }
}
