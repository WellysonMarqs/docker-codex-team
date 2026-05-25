package com.customizationaudit.customization.adapter.out.persistence;

import com.customizationaudit.customization.application.CustomizationVersionRepository;
import com.customizationaudit.customization.domain.CustomizationVersion;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresCustomizationVersionRepository implements CustomizationVersionRepository {

    private final CustomizationVersionJpaRepository versionJpaRepository;

    public PostgresCustomizationVersionRepository(CustomizationVersionJpaRepository versionJpaRepository) {
        this.versionJpaRepository = versionJpaRepository;
    }

    @Override
    public CustomizationVersion save(CustomizationVersion version) {
        return versionJpaRepository.save(CustomizationVersionJpaEntity.fromDomain(version)).toDomain();
    }

    @Override
    public List<CustomizationVersion> findByCustomizationId(UUID customizationId) {
        return versionJpaRepository.findByCustomizationIdOrderByRegisteredAtDesc(customizationId).stream()
                .map(CustomizationVersionJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<CustomizationVersion> findById(UUID id) {
        return versionJpaRepository.findById(id).map(CustomizationVersionJpaEntity::toDomain);
    }
}
