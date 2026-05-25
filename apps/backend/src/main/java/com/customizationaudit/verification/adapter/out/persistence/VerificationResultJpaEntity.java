package com.customizationaudit.verification.adapter.out.persistence;

import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;

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
@Table(name = "verification_results")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationResultJpaEntity {

    @Id
    private UUID id;

    @Column(name = "verification_run_id", nullable = false, unique = true)
    private UUID verificationRunId;

    @Column(name = "customization_version_id", nullable = false)
    private UUID customizationVersionId;

    @Column(name = "current_hash", nullable = false, length = 128)
    private String currentHash;

    @Column(name = "official_hash", nullable = false, length = 128)
    private String officialHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private VerificationResultStatus status;

    @Column(name = "evidence_reference", length = 240)
    private String evidenceReference;

    @Column(name = "error_code", length = 80)
    private String errorCode;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt;

    static VerificationResultJpaEntity fromDomain(VerificationResult result) {
        return new VerificationResultJpaEntity(
                result.id(),
                result.verificationRunId(),
                result.customizationVersionId(),
                result.currentHash(),
                result.officialHash(),
                result.status(),
                result.evidenceReference(),
                result.errorCode(),
                result.errorMessage(),
                result.checkedAt()
        );
    }

    VerificationResult toDomain() {
        return VerificationResult.restore(
                id,
                verificationRunId,
                customizationVersionId,
                currentHash,
                officialHash,
                status,
                evidenceReference,
                errorCode,
                errorMessage,
                checkedAt
        );
    }
}
