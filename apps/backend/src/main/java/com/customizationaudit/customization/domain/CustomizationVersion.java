package com.customizationaudit.customization.domain;

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
public class CustomizationVersion {
    UUID id;
    UUID customizationId;
    String legacySystemVersion;
    String officialHash;
    String hashAlgorithm;
    String canonicalizationVersion;
    String contentSignature;
    String registeredBy;
    Instant registeredAt;
    Instant activeFrom;
    Instant activeUntil;
    CustomizationVersionStatus status;

    public static CustomizationVersion register(
            UUID customizationId,
            String legacySystemVersion,
            String officialHash,
            String hashAlgorithm,
            String canonicalizationVersion,
            String contentSignature,
            String registeredBy,
            Instant activeFrom,
            Instant activeUntil,
            Instant now
    ) {
        return restore(
                UUID.randomUUID(),
                customizationId,
                legacySystemVersion,
                officialHash,
                hashAlgorithm,
                canonicalizationVersion,
                contentSignature,
                registeredBy,
                now,
                activeFrom,
                activeUntil,
                CustomizationVersionStatus.ACTIVE
        );
    }

    public static CustomizationVersion restore(
            UUID id,
            UUID customizationId,
            String legacySystemVersion,
            String officialHash,
            String hashAlgorithm,
            String canonicalizationVersion,
            String contentSignature,
            String registeredBy,
            Instant registeredAt,
            Instant activeFrom,
            Instant activeUntil,
            CustomizationVersionStatus status
    ) {
        return new CustomizationVersion(
                Objects.requireNonNull(id, "id is required"),
                Objects.requireNonNull(customizationId, "customizationId is required"),
                requireText(legacySystemVersion, "legacySystemVersion is required"),
                requireText(officialHash, "officialHash is required"),
                requireText(hashAlgorithm, "hashAlgorithm is required").toUpperCase(),
                requireText(canonicalizationVersion, "canonicalizationVersion is required"),
                normalizeOptional(contentSignature),
                normalizeOptional(registeredBy),
                Objects.requireNonNull(registeredAt, "registeredAt is required"),
                activeFrom,
                activeUntil,
                Objects.requireNonNull(status, "status is required")
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
