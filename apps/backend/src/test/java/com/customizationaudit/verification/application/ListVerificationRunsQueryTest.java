package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;
import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationRunStatus;
import com.customizationaudit.verification.domain.VerificationTriggerType;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListVerificationRunsQueryTest {

    private final InMemoryVerificationRunRepository runRepository = new InMemoryVerificationRunRepository();
    private final InMemoryVerificationResultRepository resultRepository = new InMemoryVerificationResultRepository();
    private final ListVerificationRunsQuery query = new ListVerificationRunsQuery(runRepository, resultRepository);

    @Test
    void listsRunsOrderedByStartedAtDescending() {
        VerificationRun olderRun = runRepository.save(VerificationRun.restore(
                UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443"),
                UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                VerificationTriggerType.MANUAL,
                VerificationRunStatus.COMPLETED,
                Instant.parse("2026-05-24T12:00:00Z"),
                Instant.parse("2026-05-24T12:00:30Z"),
                "support",
                "corr-older"
        ));
        VerificationRun newerRun = runRepository.save(VerificationRun.restore(
                UUID.fromString("ac4dc6bf-d566-4d4e-baf8-b1fe6269907b"),
                UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                VerificationTriggerType.MANUAL,
                VerificationRunStatus.COMPLETED_WITH_DIVERGENCE,
                Instant.parse("2026-05-24T13:00:00Z"),
                Instant.parse("2026-05-24T13:00:30Z"),
                "support",
                "corr-newer"
        ));

        resultRepository.save(VerificationResult.restore(
                UUID.fromString("3aee51a0-17cb-45ec-bd44-80fc4fb620c7"),
                olderRun.id(),
                UUID.fromString("1d143e36-9265-4de0-b5c2-aad81ec1e355"),
                "ABC123",
                "ABC123",
                VerificationResultStatus.MATCH,
                "manual-entry",
                null,
                null,
                Instant.parse("2026-05-24T12:00:30Z")
        ));
        resultRepository.save(VerificationResult.restore(
                UUID.fromString("d3b2b5b7-75ab-4412-9267-0cc2f4f59e80"),
                newerRun.id(),
                UUID.fromString("2d143e36-9265-4de0-b5c2-aad81ec1e355"),
                "DEADBEEF",
                "ABC123",
                VerificationResultStatus.DIVERGENT,
                "manual-entry",
                null,
                null,
                Instant.parse("2026-05-24T13:00:30Z")
        ));

        List<VerificationExecution> runs = query.list();

        assertThat(runs).hasSize(2);
        assertThat(runs.get(0).run().id()).isEqualTo(newerRun.id());
        assertThat(runs.get(0).result().status()).isEqualTo(VerificationResultStatus.DIVERGENT);
        assertThat(runs.get(1).run().id()).isEqualTo(olderRun.id());
        assertThat(runs.get(1).result().status()).isEqualTo(VerificationResultStatus.MATCH);
    }

    @Test
    void rejectsGetWhenRunDoesNotExist() {
        assertThatThrownBy(() -> query.get(UUID.fromString("1661c352-cfd8-4c37-88f0-353d2e470528")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("verification run not found");
    }

    private static final class InMemoryVerificationRunRepository implements VerificationRunRepository {
        private final List<VerificationRun> runs = new ArrayList<>();

        @Override
        public VerificationRun save(VerificationRun verificationRun) {
            runs.add(verificationRun);
            return verificationRun;
        }

        @Override
        public List<VerificationRun> findAll() {
            return List.copyOf(runs);
        }

        @Override
        public Optional<VerificationRun> findById(UUID id) {
            return runs.stream().filter(run -> run.id().equals(id)).findFirst();
        }
    }

    private static final class InMemoryVerificationResultRepository implements VerificationResultRepository {
        private final List<VerificationResult> results = new ArrayList<>();

        @Override
        public VerificationResult save(VerificationResult verificationResult) {
            results.add(verificationResult);
            return verificationResult;
        }

        @Override
        public List<VerificationResult> findByVerificationRunId(UUID verificationRunId) {
            return results.stream()
                    .filter(result -> result.verificationRunId().equals(verificationRunId))
                    .toList();
        }

        @Override
        public Optional<VerificationResult> findByVerificationRunIdAndCustomizationVersionId(
                UUID verificationRunId,
                UUID customizationVersionId
        ) {
            return results.stream()
                    .filter(result -> result.verificationRunId().equals(verificationRunId))
                    .filter(result -> result.customizationVersionId().equals(customizationVersionId))
                    .findFirst();
        }
    }
}
