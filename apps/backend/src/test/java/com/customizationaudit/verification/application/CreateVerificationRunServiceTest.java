package com.customizationaudit.verification.application;

import com.customizationaudit.customer.application.CustomerEnvironmentRepository;
import com.customizationaudit.customer.application.CustomerRepository;
import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.Customer;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customer.domain.CustomerStatus;
import com.customizationaudit.customer.domain.EnvironmentStatus;
import com.customizationaudit.customer.domain.EnvironmentType;
import com.customizationaudit.customization.application.CustomizationRepository;
import com.customizationaudit.customization.application.CustomizationVersionRepository;
import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationObjectType;
import com.customizationaudit.customization.domain.CustomizationStatus;
import com.customizationaudit.customization.domain.CustomizationVersion;
import com.customizationaudit.customization.domain.CustomizationVersionStatus;
import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;
import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationRunStatus;
import com.customizationaudit.verification.domain.VerificationTriggerType;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateVerificationRunServiceTest {

    private final UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private final UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
    private final UUID customizationId = UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0");
    private final UUID versionId = UUID.fromString("1d143e36-9265-4de0-b5c2-aad81ec1e355");
    private final UUID otherCustomizationId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");
    private final UUID mismatchedVersionId = UUID.fromString("bbbbbbbb-cccc-dddd-eeee-ffffffffffff");

    private final InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    private final InMemoryEnvironmentRepository environmentRepository = new InMemoryEnvironmentRepository();
    private final InMemoryCustomizationRepository customizationRepository = new InMemoryCustomizationRepository();
    private final InMemoryCustomizationVersionRepository versionRepository = new InMemoryCustomizationVersionRepository();
    private final InMemoryVerificationRunRepository runRepository = new InMemoryVerificationRunRepository();
    private final InMemoryVerificationResultRepository resultRepository = new InMemoryVerificationResultRepository();
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-24T12:00:00Z"), ZoneOffset.UTC);
    private final CreateVerificationRunService service = new CreateVerificationRunService(
            customerRepository,
            environmentRepository,
            versionRepository,
            customizationRepository,
            runRepository,
            resultRepository,
            clock
    );

    @Test
    void createsCompletedVerificationWhenHashesMatch() {
        seedData();

        VerificationExecution execution = service.create(new CreateVerificationRunCommand(
                customerId,
                environmentId,
                versionId,
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                VerificationTriggerType.MANUAL,
                "support",
                "correlation-1"
        ));

        assertThat(execution.run().status()).isEqualTo(VerificationRunStatus.COMPLETED);
        assertThat(execution.result().status()).isEqualTo(VerificationResultStatus.MATCH);
        assertThat(execution.result().officialHash()).isEqualTo("2F1C0F9A8D3B4E6F7A8B9C0D1E2F3A4B");
        assertThat(runRepository.findAll()).hasSize(1);
        assertThat(resultRepository.findByVerificationRunId(execution.run().id())).hasSize(1);
    }

    @Test
    void createsDivergentVerificationWhenHashesDiffer() {
        seedData();

        VerificationExecution execution = service.create(new CreateVerificationRunCommand(
                customerId,
                environmentId,
                versionId,
                "deadbeef",
                VerificationTriggerType.MANUAL,
                null,
                null
        ));

        assertThat(execution.run().status()).isEqualTo(VerificationRunStatus.COMPLETED_WITH_DIVERGENCE);
        assertThat(execution.result().status()).isEqualTo(VerificationResultStatus.DIVERGENT);
    }

    @Test
    void rejectsVerificationWhenVersionDoesNotBelongToCustomerEnvironment() {
        seedData();
        customizationRepository.save(Customization.restore(
                otherCustomizationId,
                UUID.fromString("7fab5aaa-bdcc-4c08-a04d-54e98b5605ef"),
                environmentId,
                "Trigger auditoria",
                null,
                CustomizationObjectType.TRIGGER,
                "audit.tr_customer_change",
                CustomizationStatus.ACTIVE,
                "support",
                clock.instant(),
                clock.instant()
        ));
        versionRepository.save(CustomizationVersion.restore(
                mismatchedVersionId,
                otherCustomizationId,
                "2026.05",
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                "SHA-256",
                "mysql-trigger-v1",
                "CREATE TRIGGER audit.tr_customer_change",
                "support",
                clock.instant(),
                null,
                null,
                CustomizationVersionStatus.ACTIVE
        ));

        assertThatThrownBy(() -> service.create(new CreateVerificationRunCommand(
                customerId,
                environmentId,
                mismatchedVersionId,
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                VerificationTriggerType.MANUAL,
                null,
                null
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("customization does not belong to customer");
    }

    private void seedData() {
        customerRepository.save(Customer.restore(
                customerId,
                "Acme",
                "LEG-001",
                CustomerStatus.ACTIVE,
                clock.instant(),
                clock.instant()
        ));
        environmentRepository.save(CustomerEnvironment.restore(
                environmentId,
                customerId,
                "Producao",
                EnvironmentType.SAAS,
                CollectionMode.CENTRAL_PULL,
                EnvironmentStatus.ACTIVE,
                "vault/customer/acme/prod",
                clock.instant(),
                clock.instant()
        ));
        customizationRepository.save(Customization.restore(
                customizationId,
                customerId,
                environmentId,
                "Procedure fiscal",
                "Procedure customizada do cliente",
                CustomizationObjectType.PROCEDURE,
                "billing.sp_customer_tax",
                CustomizationStatus.ACTIVE,
                "support",
                clock.instant(),
                clock.instant()
        ));
        versionRepository.save(CustomizationVersion.restore(
                versionId,
                customizationId,
                "2026.05",
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                "SHA-256",
                "mysql-procedure-v1",
                "CREATE PROCEDURE billing.sp_customer_tax",
                "support",
                clock.instant(),
                null,
                null,
                CustomizationVersionStatus.ACTIVE
        ));
    }

    private static final class InMemoryCustomerRepository implements CustomerRepository {
        private final List<Customer> customers = new ArrayList<>();

        @Override
        public Customer save(Customer customer) {
            customers.add(customer);
            return customer;
        }

        @Override
        public List<Customer> findAll() {
            return List.copyOf(customers);
        }

        @Override
        public Optional<Customer> findById(UUID id) {
            return customers.stream().filter(customer -> customer.id().equals(id)).findFirst();
        }
    }

    private static final class InMemoryEnvironmentRepository implements CustomerEnvironmentRepository {
        private final List<CustomerEnvironment> environments = new ArrayList<>();

        @Override
        public CustomerEnvironment save(CustomerEnvironment environment) {
            environments.add(environment);
            return environment;
        }

        @Override
        public List<CustomerEnvironment> findByCustomerId(UUID id) {
            return environments.stream()
                    .filter(environment -> environment.customerId().equals(id))
                    .toList();
        }

        @Override
        public Optional<CustomerEnvironment> findById(UUID id) {
            return environments.stream().filter(environment -> environment.id().equals(id)).findFirst();
        }
    }

    private static final class InMemoryCustomizationRepository implements CustomizationRepository {
        private final List<Customization> customizations = new ArrayList<>();

        @Override
        public Customization save(Customization customization) {
            customizations.add(customization);
            return customization;
        }

        @Override
        public List<Customization> findAll() {
            return List.copyOf(customizations);
        }

        @Override
        public Optional<Customization> findById(UUID id) {
            return customizations.stream().filter(customization -> customization.id().equals(id)).findFirst();
        }
    }

    private static final class InMemoryCustomizationVersionRepository implements CustomizationVersionRepository {
        private final List<CustomizationVersion> versions = new ArrayList<>();

        @Override
        public CustomizationVersion save(CustomizationVersion version) {
            versions.add(version);
            return version;
        }

        @Override
        public List<CustomizationVersion> findByCustomizationId(UUID customizationId) {
            return versions.stream()
                    .filter(version -> version.customizationId().equals(customizationId))
                    .toList();
        }

        @Override
        public Optional<CustomizationVersion> findById(UUID id) {
            return versions.stream().filter(version -> version.id().equals(id)).findFirst();
        }
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
        public Optional<VerificationResult> findByVerificationRunIdAndCustomizationVersionId(UUID verificationRunId, UUID customizationVersionId) {
            return results.stream()
                    .filter(result -> result.verificationRunId().equals(verificationRunId))
                    .filter(result -> result.customizationVersionId().equals(customizationVersionId))
                    .findFirst();
        }
    }
}
