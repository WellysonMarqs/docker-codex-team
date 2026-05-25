package com.customizationaudit.verification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface VerificationRunJpaRepository extends JpaRepository<VerificationRunJpaEntity, UUID> {

    List<VerificationRunJpaEntity> findByCustomerId(UUID customerId);

    List<VerificationRunJpaEntity> findByEnvironmentId(UUID environmentId);

    List<VerificationRunJpaEntity> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId);
}
