package com.customizationaudit.customization.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface CustomizationVersionJpaRepository extends JpaRepository<CustomizationVersionJpaEntity, UUID> {

    List<CustomizationVersionJpaEntity> findByCustomizationIdOrderByRegisteredAtDesc(UUID customizationId);
}
