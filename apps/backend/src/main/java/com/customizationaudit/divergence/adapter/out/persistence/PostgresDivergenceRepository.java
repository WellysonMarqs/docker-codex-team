package com.customizationaudit.divergence.adapter.out.persistence;

import com.customizationaudit.divergence.application.DivergenceRepository;
import com.customizationaudit.divergence.domain.Divergence;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresDivergenceRepository implements DivergenceRepository {

    private final DivergenceJpaRepository divergenceJpaRepository;

    public PostgresDivergenceRepository(DivergenceJpaRepository divergenceJpaRepository) {
        this.divergenceJpaRepository = divergenceJpaRepository;
    }

    @Override
    public Divergence save(Divergence divergence) {
        return divergenceJpaRepository.save(DivergenceJpaEntity.fromDomain(divergence)).toDomain();
    }

    @Override
    public Optional<Divergence> findById(UUID id) {
        return divergenceJpaRepository.findById(id).map(DivergenceJpaEntity::toDomain);
    }

    @Override
    public Optional<Divergence> findByVerificationResultId(UUID verificationResultId) {
        return divergenceJpaRepository.findByVerificationResultId(verificationResultId).map(DivergenceJpaEntity::toDomain);
    }

    @Override
    public List<Divergence> findAll() {
        return divergenceJpaRepository.findAll().stream()
                .map(DivergenceJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Divergence> findByCustomerId(UUID customerId) {
        return divergenceJpaRepository.findByCustomerId(customerId).stream()
                .map(DivergenceJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Divergence> findByEnvironmentId(UUID environmentId) {
        return divergenceJpaRepository.findByEnvironmentId(environmentId).stream()
                .map(DivergenceJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Divergence> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId) {
        return divergenceJpaRepository.findByCustomerIdAndEnvironmentId(customerId, environmentId).stream()
                .map(DivergenceJpaEntity::toDomain)
                .toList();
    }
}
