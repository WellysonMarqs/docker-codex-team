package com.customizationaudit.divergence.application;

import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.divergence.domain.DivergenceSeverity;
import com.customizationaudit.divergence.domain.DivergenceStatus;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListDivergencesQueryTest {

    private final InMemoryDivergenceRepository divergenceRepository = new InMemoryDivergenceRepository();
    private final ListDivergencesQuery query = new ListDivergencesQuery(divergenceRepository);

    @Test
    void listsDivergencesOrderedByDetectedAtDescending() {
        Divergence older = save(
                UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                Instant.parse("2026-05-24T12:00:00Z")
        );
        Divergence newer = save(
                UUID.fromString("ac4dc6bf-d566-4d4e-baf8-b1fe6269907b"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                Instant.parse("2026-05-24T13:00:00Z")
        );

        List<Divergence> divergences = query.list();

        assertThat(divergences).hasSize(2);
        assertThat(divergences.get(0).id()).isEqualTo(newer.id());
        assertThat(divergences.get(1).id()).isEqualTo(older.id());
    }

    @Test
    void filtersByCustomerAndEnvironment() {
        UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
        UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
        UUID otherEnvironmentId = UUID.fromString("22a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

        Divergence matching = save(customerId, environmentId, Instant.parse("2026-05-24T12:00:00Z"));
        save(customerId, otherEnvironmentId, Instant.parse("2026-05-24T13:00:00Z"));

        List<Divergence> divergences = query.list(customerId, environmentId);

        assertThat(divergences).hasSize(1);
        assertThat(divergences.get(0).id()).isEqualTo(matching.id());
    }

    @Test
    void rejectsGetWhenDivergenceDoesNotExist() {
        assertThatThrownBy(() -> query.get(UUID.fromString("1661c352-cfd8-4c37-88f0-353d2e470528")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("divergence not found");
    }

    private Divergence save(UUID customerId, UUID environmentId, Instant detectedAt) {
        return divergenceRepository.save(Divergence.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                customerId,
                environmentId,
                UUID.randomUUID(),
                DivergenceSeverity.HIGH,
                DivergenceStatus.OPEN,
                detectedAt,
                null,
                "corr-001"
        ));
    }

    private static final class InMemoryDivergenceRepository implements DivergenceRepository {
        private final List<Divergence> divergences = new ArrayList<>();

        @Override
        public Divergence save(Divergence divergence) {
            divergences.add(divergence);
            return divergence;
        }

        @Override
        public Optional<Divergence> findById(UUID id) {
            return divergences.stream().filter(divergence -> divergence.id().equals(id)).findFirst();
        }

        @Override
        public Optional<Divergence> findByVerificationResultId(UUID verificationResultId) {
            return divergences.stream()
                    .filter(divergence -> divergence.verificationResultId().equals(verificationResultId))
                    .findFirst();
        }

        @Override
        public List<Divergence> findAll() {
            return List.copyOf(divergences);
        }
    }
}
