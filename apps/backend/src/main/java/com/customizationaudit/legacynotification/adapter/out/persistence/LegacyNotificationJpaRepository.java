package com.customizationaudit.legacynotification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LegacyNotificationJpaRepository extends JpaRepository<LegacyNotificationJpaEntity, UUID> {

    List<LegacyNotificationJpaEntity> findByCustomerId(UUID customerId);

    List<LegacyNotificationJpaEntity> findByEnvironmentId(UUID environmentId);

    List<LegacyNotificationJpaEntity> findByCustomerIdAndEnvironmentId(UUID customerId, UUID environmentId);
}
