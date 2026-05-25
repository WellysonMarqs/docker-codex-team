package com.customizationaudit.divergence.application;

import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.divergence.domain.DivergenceSeverity;
import com.customizationaudit.divergence.domain.DivergenceStatus;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateDivergenceStatusServiceTest {

    private static final UUID DIVERGENCE_ID = UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443");
    private final InMemoryDivergenceRepository divergenceRepository = new InMemoryDivergenceRepository();
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-25T13:00:00Z"), ZoneOffset.UTC);
    private final UpdateDivergenceStatusService service = new UpdateDivergenceStatusService(divergenceRepository, clock);

    @Test
    void acknowledgesOpenDivergence() {
        divergenceRepository.save(openDivergence());

        DivergenceView updated = service.update(new UpdateDivergenceStatusCommand(DIVERGENCE_ID, DivergenceStatus.ACKNOWLEDGED));

        assertThat(updated.status()).isEqualTo(DivergenceStatus.ACKNOWLEDGED);
        assertThat(updated.resolvedAt()).isNull();
    }

    @Test
    void resolvesOpenDivergence() {
        divergenceRepository.save(openDivergence());

        DivergenceView updated = service.update(new UpdateDivergenceStatusCommand(DIVERGENCE_ID, DivergenceStatus.RESOLVED));

        assertThat(updated.status()).isEqualTo(DivergenceStatus.RESOLVED);
        assertThat(updated.resolvedAt()).isEqualTo(clock.instant());
    }

    @Test
    void rejectsUnknownDivergence() {
        assertThatThrownBy(() -> service.update(new UpdateDivergenceStatusCommand(DIVERGENCE_ID, DivergenceStatus.RESOLVED)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("divergence not found");
    }

    private static Divergence openDivergence() {
        return Divergence.restore(
                DIVERGENCE_ID,
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

    private static final class InMemoryDivergenceRepository implements DivergenceRepository {
        private final Map<UUID, Divergence> divergences = new LinkedHashMap<>();

        @Override
        public Divergence save(Divergence divergence) {
            divergences.put(divergence.id(), divergence);
            return divergence;
        }

        @Override
        public Optional<Divergence> findById(UUID id) {
            return Optional.ofNullable(divergences.get(id));
        }

        @Override
        public Optional<Divergence> findByVerificationResultId(UUID verificationResultId) {
            return divergences.values().stream()
                    .filter(divergence -> divergence.verificationResultId().equals(verificationResultId))
                    .findFirst();
        }

        @Override
        public List<Divergence> findAll() {
            return List.copyOf(divergences.values());
        }
    }
}
