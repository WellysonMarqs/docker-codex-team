package com.customizationaudit.verification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface VerificationResultJpaRepository extends JpaRepository<VerificationResultJpaEntity, UUID> {

    List<VerificationResultJpaEntity> findByVerificationRunId(UUID verificationRunId);

    Optional<VerificationResultJpaEntity> findByVerificationRunIdAndCustomizationVersionId(UUID verificationRunId, UUID customizationVersionId);
}
