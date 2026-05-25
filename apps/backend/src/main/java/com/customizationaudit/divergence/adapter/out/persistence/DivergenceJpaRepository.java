package com.customizationaudit.divergence.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface DivergenceJpaRepository extends JpaRepository<DivergenceJpaEntity, UUID> {

    Optional<DivergenceJpaEntity> findByVerificationResultId(UUID verificationResultId);

    List<DivergenceJpaEntity> findByCustomerId(UUID customerId);

    List<DivergenceJpaEntity> findByEnvironmentId(UUID environmentId);

    List<DivergenceJpaEntity> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId);
}
