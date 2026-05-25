package com.customizationaudit.customization.adapter.out.persistence;

import com.customizationaudit.customization.application.CustomizationRepository;
import com.customizationaudit.customization.domain.Customization;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresCustomizationRepository implements CustomizationRepository {

    private final CustomizationJpaRepository customizationJpaRepository;

    public PostgresCustomizationRepository(CustomizationJpaRepository customizationJpaRepository) {
        this.customizationJpaRepository = customizationJpaRepository;
    }

    @Override
    public Customization save(Customization customization) {
        return customizationJpaRepository.save(CustomizationJpaEntity.fromDomain(customization)).toDomain();
    }

    @Override
    public List<Customization> findAll() {
        return customizationJpaRepository.findAll().stream()
                .map(CustomizationJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Customization> findById(UUID id) {
        return customizationJpaRepository.findById(id)
                .map(CustomizationJpaEntity::toDomain);
    }
}
