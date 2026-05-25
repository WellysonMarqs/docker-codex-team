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

class ListVerificationRunsFiltersTest {

    private final InMemoryVerificationRunRepository runRepository = new InMemoryVerificationRunRepository();
    private final InMemoryVerificationResultRepository resultRepository = new InMemoryVerificationResultRepository();
    private final ListVerificationRunsQuery query = new ListVerificationRunsQuery(runRepository, resultRepository);

    @Test
    void filtersRunsByCustomerAndEnvironment() {
        UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
        UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
        UUID otherEnvironmentId = UUID.fromString("22a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

        VerificationRun matchingRun = saveRun(
                UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443"),
                customerId,
                environmentId,
                "corr-match"
        );
        VerificationRun otherRun = saveRun(
                UUID.fromString("ac4dc6bf-d566-4d4e-baf8-b1fe6269907b"),
                customerId,
                otherEnvironmentId,
                "corr-other-env"
        );

        resultRepository.save(resultFor(matchingRun.id(), VerificationResultStatus.MATCH));
        resultRepository.save(resultFor(otherRun.id(), VerificationResultStatus.DIVERGENT));

        List<VerificationExecution> runs = query.list(customerId, environmentId);

        assertThat(runs).hasSize(1);
        assertThat(runs.get(0).run().id()).isEqualTo(matchingRun.id());
    }

    @Test
    void filtersRunsByCustomerOnly() {
        UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
        UUID otherCustomerId = UUID.fromString("2222d976-2f57-4d45-b6e3-426246d01674");
        UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

        VerificationRun matchingRun = saveRun(
                UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443"),
                customerId,
                environmentId,
                "corr-customer"
        );
        VerificationRun otherRun = saveRun(
                UUID.fromString("ac4dc6bf-d566-4d4e-baf8-b1fe6269907b"),
                otherCustomerId,
                environmentId,
                "corr-other-customer"
        );

        resultRepository.save(resultFor(matchingRun.id(), VerificationResultStatus.MATCH));
        resultRepository.save(resultFor(otherRun.id(), VerificationResultStatus.DIVERGENT));

        List<VerificationExecution> runs = query.list(customerId, null);

        assertThat(runs).hasSize(1);
        assertThat(runs.get(0).run().id()).isEqualTo(matchingRun.id());
    }

    @Test
    void filtersRunsByEnvironmentOnly() {
        UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
        UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
        UUID otherEnvironmentId = UUID.fromString("22a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

        VerificationRun matchingRun = saveRun(
                UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443"),
                customerId,
                environmentId,
                "corr-environment"
        );
        VerificationRun otherRun = saveRun(
                UUID.fromString("ac4dc6bf-d566-4d4e-baf8-b1fe6269907b"),
                customerId,
                otherEnvironmentId,
                "corr-other-environment"
        );

        resultRepository.save(resultFor(matchingRun.id(), VerificationResultStatus.MATCH));
        resultRepository.save(resultFor(otherRun.id(), VerificationResultStatus.DIVERGENT));

        List<VerificationExecution> runs = query.list(null, environmentId);

        assertThat(runs).hasSize(1);
        assertThat(runs.get(0).run().id()).isEqualTo(matchingRun.id());
    }

    private VerificationRun saveRun(UUID runId, UUID customerId, UUID environmentId, String correlationId) {
        return runRepository.save(VerificationRun.restore(
                runId,
                customerId,
                environmentId,
                VerificationTriggerType.MANUAL,
                VerificationRunStatus.COMPLETED,
                Instant.parse("2026-05-24T12:00:00Z"),
                Instant.parse("2026-05-24T12:00:30Z"),
                "support",
                correlationId
        ));
    }

    private static VerificationResult resultFor(UUID verificationRunId, VerificationResultStatus status) {
        return VerificationResult.restore(
                UUID.randomUUID(),
                verificationRunId,
                UUID.fromString("1d143e36-9265-4de0-b5c2-aad81ec1e355"),
                "ABC123",
                "ABC123",
                status,
                "manual-entry",
                null,
                null,
                Instant.parse("2026-05-24T12:00:30Z")
        );
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
