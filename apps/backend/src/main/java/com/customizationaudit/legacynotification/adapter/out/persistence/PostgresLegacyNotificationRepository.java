package com.customizationaudit.legacynotification.adapter.out.persistence;

import com.customizationaudit.legacynotification.application.LegacyNotificationRepository;
import com.customizationaudit.legacynotification.domain.LegacyNotification;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresLegacyNotificationRepository implements LegacyNotificationRepository {

    private final LegacyNotificationJpaRepository legacyNotificationJpaRepository;

    public PostgresLegacyNotificationRepository(LegacyNotificationJpaRepository legacyNotificationJpaRepository) {
        this.legacyNotificationJpaRepository = legacyNotificationJpaRepository;
    }

    @Override
    public LegacyNotification save(LegacyNotification legacyNotification) {
        return legacyNotificationJpaRepository.save(LegacyNotificationJpaEntity.fromDomain(legacyNotification)).toDomain();
    }

    @Override
    public Optional<LegacyNotification> findById(UUID id) {
        return legacyNotificationJpaRepository.findById(id).map(LegacyNotificationJpaEntity::toDomain);
    }

    @Override
    public List<LegacyNotification> findAll() {
        return legacyNotificationJpaRepository.findAll().stream()
                .map(LegacyNotificationJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<LegacyNotification> findByCustomerId(UUID customerId) {
        return legacyNotificationJpaRepository.findByCustomerId(customerId).stream()
                .map(LegacyNotificationJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<LegacyNotification> findByEnvironmentId(UUID environmentId) {
        return legacyNotificationJpaRepository.findByEnvironmentId(environmentId).stream()
                .map(LegacyNotificationJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<LegacyNotification> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId) {
        return legacyNotificationJpaRepository.findByCustomerIdAndEnvironmentId(customerId, environmentId).stream()
                .map(LegacyNotificationJpaEntity::toDomain)
                .toList();
    }
}
