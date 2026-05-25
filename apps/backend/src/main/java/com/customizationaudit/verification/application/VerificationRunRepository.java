package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationRun;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationRunRepository {

    VerificationRun save(VerificationRun verificationRun);

    List<VerificationRun> findAll();

    Optional<VerificationRun> findById(UUID id);
}
