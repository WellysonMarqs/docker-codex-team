package com.customizationaudit.divergence.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DivergenceStatusTransitionTest {

    @Test
    void acknowledgesOpenDivergenceWithoutResolutionTimestamp() {
        Divergence updated = openDivergence().transitionTo(
                DivergenceStatus.ACKNOWLEDGED,
                Instant.parse("2026-05-25T12:30:00Z")
        );

        assertThat(updated.status()).isEqualTo(DivergenceStatus.ACKNOWLEDGED);
        assertThat(updated.resolvedAt()).isNull();
    }

    @Test
    void resolvesAcknowledgedDivergenceWithResolutionTimestamp() {
        Divergence acknowledged = openDivergence().transitionTo(
                DivergenceStatus.ACKNOWLEDGED,
                Instant.parse("2026-05-25T12:30:00Z")
        );

        Divergence resolved = acknowledged.transitionTo(
                DivergenceStatus.RESOLVED,
                Instant.parse("2026-05-25T13:00:00Z")
        );

        assertThat(resolved.status()).isEqualTo(DivergenceStatus.RESOLVED);
        assertThat(resolved.resolvedAt()).isEqualTo(Instant.parse("2026-05-25T13:00:00Z"));
    }

    @Test
    void rejectsUnsupportedTransition() {
        assertThatThrownBy(() -> openDivergence().transitionTo(
                DivergenceStatus.NOTIFIED,
                Instant.parse("2026-05-25T12:30:00Z")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("unsupported divergence status transition");
    }

    private static Divergence openDivergence() {
        return Divergence.restore(
                UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443"),
                UUID.fromString("3aee51a0-17cb-45ec-bd44-80fc4fb620c7"),
                UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0"),
                DivergenceSeverity.HIGH,
                DivergenceStatus.OPEN,
                Instant.parse("2026-05-25T12:00:00Z"),
                null,
                "corr-001"
        );
    }
}
