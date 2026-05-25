package com.customizationaudit.verification.adapter.in.web;

import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;

import java.time.Instant;
import java.util.UUID;

public record VerificationResultResponse(
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
    static VerificationResultResponse from(VerificationResult result) {
        return new VerificationResultResponse(
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
}
