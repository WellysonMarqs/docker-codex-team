package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationRun;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationRunRepository {

    VerificationRun save(VerificationRun verificationRun);

    List<VerificationRun> findAll();

    default List<VerificationRun> findByCustomerId(UUID customerId) {
        return findAll().stream()
                .filter(run -> run.customerId().equals(customerId))
                .toList();
    }

    default List<VerificationRun> findByEnvironmentId(UUID environmentId) {
        return findAll().stream()
                .filter(run -> run.environmentId().equals(environmentId))
                .toList();
    }

    default List<VerificationRun> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId) {
        return findAll().stream()
                .filter(run -> run.customerId().equals(customerId))
                .filter(run -> run.environmentId().equals(environmentId))
                .toList();
    }

    Optional<VerificationRun> findById(UUID id);
}
