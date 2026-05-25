package com.customizationaudit.legacynotification.application;

import com.customizationaudit.legacynotification.domain.LegacyNotification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ListLegacyNotificationsQuery {

    private final LegacyNotificationRepository legacyNotificationRepository;

    public ListLegacyNotificationsQuery(LegacyNotificationRepository legacyNotificationRepository) {
        this.legacyNotificationRepository = legacyNotificationRepository;
    }

    @Transactional(readOnly = true)
    public List<LegacyNotification> list(UUID customerId, UUID environmentId) {
        return loadNotifications(customerId, environmentId).stream()
                .sorted(Comparator.comparing(LegacyNotification::createdAt, Comparator.reverseOrder()))
                .toList();
    }

    @Transactional(readOnly = true)
    public LegacyNotification get(UUID legacyNotificationId) {
        return legacyNotificationRepository.findById(legacyNotificationId)
                .orElseThrow(() -> new IllegalArgumentException("legacy notification not found"));
    }

    private List<LegacyNotification> loadNotifications(UUID customerId, UUID environmentId) {
        if (customerId != null && environmentId != null) {
            return legacyNotificationRepository.findByCustomerIdAndEnvironmentId(customerId, environmentId);
        }
        if (customerId != null) {
            return legacyNotificationRepository.findByCustomerId(customerId);
        }
        if (environmentId != null) {
            return legacyNotificationRepository.findByEnvironmentId(environmentId);
        }
        return legacyNotificationRepository.findAll();
    }
}
