package com.customizationaudit.verification.adapter.out.persistence;

import com.customizationaudit.verification.application.VerificationRunRepository;
import com.customizationaudit.verification.domain.VerificationRun;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresVerificationRunRepository implements VerificationRunRepository {

    private final VerificationRunJpaRepository verificationRunJpaRepository;

    public PostgresVerificationRunRepository(VerificationRunJpaRepository verificationRunJpaRepository) {
        this.verificationRunJpaRepository = verificationRunJpaRepository;
    }

    @Override
    public VerificationRun save(VerificationRun verificationRun) {
        return verificationRunJpaRepository.save(VerificationRunJpaEntity.fromDomain(verificationRun)).toDomain();
    }

    @Override
    public List<VerificationRun> findAll() {
        return verificationRunJpaRepository.findAll().stream()
                .map(VerificationRunJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<VerificationRun> findById(UUID id) {
        return verificationRunJpaRepository.findById(id).map(VerificationRunJpaEntity::toDomain);
    }
}
