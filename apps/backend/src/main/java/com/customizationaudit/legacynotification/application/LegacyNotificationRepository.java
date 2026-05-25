package com.customizationaudit.legacynotification.application;

import com.customizationaudit.legacynotification.domain.LegacyNotification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LegacyNotificationRepository {

    LegacyNotification save(LegacyNotification legacyNotification);

    Optional<LegacyNotification> findById(UUID id);

    default List<LegacyNotification> findAll() {
        return List.of();
    }

    default List<LegacyNotification> findByCustomerId(UUID customerId) {
        return findAll().stream()
                .filter(notification -> notification.customerId().equals(customerId))
                .toList();
    }

    default List<LegacyNotification> findByEnvironmentId(UUID environmentId) {
        return findAll().stream()
                .filter(notification -> notification.environmentId().equals(environmentId))
                .toList();
    }

    default List<LegacyNotification> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId) {
        return findAll().stream()
                .filter(notification -> notification.customerId().equals(customerId))
                .filter(notification -> notification.environmentId().equals(environmentId))
                .toList();
    }
}
