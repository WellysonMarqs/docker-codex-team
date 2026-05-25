package com.customizationaudit.customization.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface CustomizationJpaRepository extends JpaRepository<CustomizationJpaEntity, UUID> {
}
