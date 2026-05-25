package com.customizationaudit.legacynotification.application;

import com.customizationaudit.legacynotification.domain.LegacyNotification;
import com.customizationaudit.legacynotification.domain.LegacyNotificationStatus;
import com.customizationaudit.legacynotification.domain.LegacyNotificationType;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListLegacyNotificationsQueryTest {

    private final InMemoryLegacyNotificationRepository legacyNotificationRepository = new InMemoryLegacyNotificationRepository();
    private final ListLegacyNotificationsQuery query = new ListLegacyNotificationsQuery(legacyNotificationRepository);

    @Test
    void listsNotificationsOrderedByCreatedAtDescending() {
        LegacyNotification older = save(
                UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                Instant.parse("2026-05-24T12:00:00Z")
        );
        LegacyNotification newer = save(
                UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                Instant.parse("2026-05-24T13:00:00Z")
        );

        List<LegacyNotification> notifications = query.list(null, null);

        assertThat(notifications.get(0).id()).isEqualTo(newer.id());
        assertThat(notifications.get(1).id()).isEqualTo(older.id());
    }

    @Test
    void filtersByCustomerAndEnvironment() {
        UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
        UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
        UUID otherEnvironmentId = UUID.fromString("22a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

        LegacyNotification matching = save(customerId, environmentId, Instant.parse("2026-05-24T12:00:00Z"));
        save(customerId, otherEnvironmentId, Instant.parse("2026-05-24T13:00:00Z"));

        List<LegacyNotification> notifications = query.list(customerId, environmentId);

        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).id()).isEqualTo(matching.id());
    }

    @Test
    void rejectsGetWhenNotificationDoesNotExist() {
        assertThatThrownBy(() -> query.get(UUID.fromString("1661c352-cfd8-4c37-88f0-353d2e470528")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("legacy notification not found");
    }

    private LegacyNotification save(UUID customerId, UUID environmentId, Instant createdAt) {
        return legacyNotificationRepository.save(LegacyNotification.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                customerId,
                environmentId,
                UUID.randomUUID(),
                LegacyNotificationType.DIVERGENCE_DETECTED,
                LegacyNotificationStatus.PENDING,
                "{\"status\":\"PENDING\"}",
                UUID.randomUUID().toString(),
                0,
                createdAt,
                null,
                null,
                null,
                "corr-001"
        ));
    }

    private static final class InMemoryLegacyNotificationRepository implements LegacyNotificationRepository {
        private final List<LegacyNotification> notifications = new ArrayList<>();
        @Override public LegacyNotification save(LegacyNotification legacyNotification) { notifications.add(legacyNotification); return legacyNotification; }
        @Override public Optional<LegacyNotification> findById(UUID id) { return notifications.stream().filter(notification -> notification.id().equals(id)).findFirst(); }
        @Override public List<LegacyNotification> findAll() { return List.copyOf(notifications); }
    }
}
