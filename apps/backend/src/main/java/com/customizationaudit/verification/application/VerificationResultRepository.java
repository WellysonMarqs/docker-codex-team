package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationResultRepository {

    VerificationResult save(VerificationResult verificationResult);

    List<VerificationResult> findByVerificationRunId(UUID verificationRunId);

    Optional<VerificationResult> findByVerificationRunIdAndCustomizationVersionId(UUID verificationRunId, UUID customizationVersionId);
}
