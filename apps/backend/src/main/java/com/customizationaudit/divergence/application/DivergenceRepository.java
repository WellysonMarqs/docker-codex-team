package com.customizationaudit.divergence.application;

import com.customizationaudit.divergence.domain.Divergence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DivergenceRepository {

    Divergence save(Divergence divergence);

    Optional<Divergence> findById(UUID id);

    Optional<Divergence> findByVerificationResultId(UUID verificationResultId);

    List<Divergence> findAll();

    default List<Divergence> findByCustomerId(UUID customerId) {
        return findAll().stream()
                .filter(divergence -> divergence.customerId().equals(customerId))
                .toList();
    }

    default List<Divergence> findByEnvironmentId(UUID environmentId) {
        return findAll().stream()
                .filter(divergence -> divergence.environmentId().equals(environmentId))
                .toList();
    }

    default List<Divergence> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId) {
        return findAll().stream()
                .filter(divergence -> divergence.customerId().equals(customerId))
                .filter(divergence -> divergence.environmentId().equals(environmentId))
                .toList();
    }
}
