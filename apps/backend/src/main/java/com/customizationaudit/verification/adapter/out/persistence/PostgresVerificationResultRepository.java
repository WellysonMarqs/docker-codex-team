package com.customizationaudit.verification.adapter.out.persistence;

import com.customizationaudit.verification.application.VerificationResultRepository;
import com.customizationaudit.verification.domain.VerificationResult;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresVerificationResultRepository implements VerificationResultRepository {

    private final VerificationResultJpaRepository verificationResultJpaRepository;

    public PostgresVerificationResultRepository(VerificationResultJpaRepository verificationResultJpaRepository) {
        this.verificationResultJpaRepository = verificationResultJpaRepository;
    }

    @Override
    public VerificationResult save(VerificationResult verificationResult) {
        return verificationResultJpaRepository.save(VerificationResultJpaEntity.fromDomain(verificationResult)).toDomain();
    }

    @Override
    public List<VerificationResult> findByVerificationRunId(UUID verificationRunId) {
        return verificationResultJpaRepository.findByVerificationRunId(verificationRunId).stream()
                .map(VerificationResultJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<VerificationResult> findByVerificationRunIdAndCustomizationVersionId(UUID verificationRunId, UUID customizationVersionId) {
        return verificationResultJpaRepository.findByVerificationRunIdAndCustomizationVersionId(verificationRunId, customizationVersionId)
                .map(VerificationResultJpaEntity::toDomain);
    }
}
