package com.customizationaudit.verification.application;

public interface CreateVerificationRunUseCase {

    VerificationExecution create(CreateVerificationRunCommand command);
}
