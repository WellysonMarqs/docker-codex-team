package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationRun;

public record VerificationExecution(VerificationRun run, VerificationResult result) {
}
