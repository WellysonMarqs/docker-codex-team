package com.customizationaudit.customization.adapter.out.persistence;

import com.customizationaudit.customization.domain.CustomizationVersion;
import com.customizationaudit.customization.domain.CustomizationVersionStatus;

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
@Table(name = "customization_versions")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomizationVersionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "customization_id", nullable = false)
    private UUID customizationId;

    @Column(name = "legacy_system_version", nullable = false, length = 80)
    private String legacySystemVersion;

    @Column(name = "official_hash", nullable = false, length = 128)
    private String officialHash;

    @Column(name = "hash_algorithm", nullable = false, length = 32)
    private String hashAlgorithm;

    @Column(name = "canonicalization_version", nullable = false, length = 40)
    private String canonicalizationVersion;

    @Column(name = "content_signature", length = 4000)
    private String contentSignature;

    @Column(name = "registered_by", length = 120)
    private String registeredBy;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    @Column(name = "active_from")
    private Instant activeFrom;

    @Column(name = "active_until")
    private Instant activeUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CustomizationVersionStatus status;

    static CustomizationVersionJpaEntity fromDomain(CustomizationVersion version) {
        return new CustomizationVersionJpaEntity(
                version.id(),
                version.customizationId(),
                version.legacySystemVersion(),
                version.officialHash(),
                version.hashAlgorithm(),
                version.canonicalizationVersion(),
                version.contentSignature(),
                version.registeredBy(),
                version.registeredAt(),
                version.activeFrom(),
                version.activeUntil(),
                version.status()
        );
    }

    CustomizationVersion toDomain() {
        return CustomizationVersion.restore(
                id,
                customizationId,
                legacySystemVersion,
                officialHash,
                hashAlgorithm,
                canonicalizationVersion,
                contentSignature,
                registeredBy,
                registeredAt,
                activeFrom,
                activeUntil,
                status
        );
    }
}
