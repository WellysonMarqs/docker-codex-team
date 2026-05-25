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
public class VerificationResult {
    UUID id;
    UUID verificationRunId;
    UUID customizationVersionId;
    String currentHash;
    String officialHash;
    VerificationResultStatus status;
    String evidenceReference;
    String errorCode;
    String errorMessage;
    Instant checkedAt;

    public static VerificationResult register(
            UUID verificationRunId,
            UUID customizationVersionId,
            String currentHash,
            String officialHash,
            VerificationResultStatus status,
            String evidenceReference,
            String errorCode,
            String errorMessage,
            Instant checkedAt
    ) {
        return new VerificationResult(
                UUID.randomUUID(),
                Objects.requireNonNull(verificationRunId, "verificationRunId is required"),
                Objects.requireNonNull(customizationVersionId, "customizationVersionId is required"),
                requireText(currentHash, "currentHash is required"),
                requireText(officialHash, "officialHash is required"),
                Objects.requireNonNull(status, "status is required"),
                normalizeOptional(evidenceReference),
                normalizeOptional(errorCode),
                normalizeOptional(errorMessage),
                Objects.requireNonNull(checkedAt, "checkedAt is required")
        );
    }

    public static VerificationResult restore(
            UUID id,
            UUID verificationRunId,
            UUID customizationVersionId,
            String currentHash,
            String officialHash,
            VerificationResultStatus status,
            String evidenceReference,
            String errorCode,
            String errorMessage,
            Instant checkedAt
    ) {
        return new VerificationResult(
                Objects.requireNonNull(id, "id is required"),
                Objects.requireNonNull(verificationRunId, "verificationRunId is required"),
                Objects.requireNonNull(customizationVersionId, "customizationVersionId is required"),
                requireText(currentHash, "currentHash is required"),
                requireText(officialHash, "officialHash is required"),
                Objects.requireNonNull(status, "status is required"),
                normalizeOptional(evidenceReference),
                normalizeOptional(errorCode),
                normalizeOptional(errorMessage),
                Objects.requireNonNull(checkedAt, "checkedAt is required")
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
