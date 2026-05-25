package com.customizationaudit.legacynotification.application;

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
import com.customizationaudit.divergence.application.DivergenceRepository;
import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.verification.application.CreateVerificationRunCommand;
import com.customizationaudit.verification.application.CreateVerificationRunService;
import com.customizationaudit.verification.application.VerificationExecution;
import com.customizationaudit.verification.application.VerificationResultRepository;
import com.customizationaudit.verification.application.VerificationRunRepository;
import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;
import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationTriggerType;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LegacyNotificationFromVerificationTest {

    private final UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private final UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
    private final UUID customizationId = UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0");
    private final UUID versionId = UUID.fromString("1d143e36-9265-4de0-b5c2-aad81ec1e355");

    private final InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    private final InMemoryEnvironmentRepository environmentRepository = new InMemoryEnvironmentRepository();
    private final InMemoryCustomizationRepository customizationRepository = new InMemoryCustomizationRepository();
    private final InMemoryCustomizationVersionRepository versionRepository = new InMemoryCustomizationVersionRepository();
    private final InMemoryVerificationRunRepository runRepository = new InMemoryVerificationRunRepository();
    private final InMemoryVerificationResultRepository resultRepository = new InMemoryVerificationResultRepository();
    private final InMemoryDivergenceRepository divergenceRepository = new InMemoryDivergenceRepository();
    private final InMemoryLegacyNotificationRepository legacyNotificationRepository = new InMemoryLegacyNotificationRepository();
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-24T12:00:00Z"), ZoneOffset.UTC);
    private final CreateVerificationRunService service = new CreateVerificationRunService(
            customerRepository,
            environmentRepository,
            versionRepository,
            customizationRepository,
            runRepository,
            resultRepository,
            divergenceRepository,
            legacyNotificationRepository,
            new ObjectMapper(),
            clock
    );

    @Test
    void createsPendingLegacyNotificationWhenVerificationIsDivergent() {
        seedData();

        VerificationExecution execution = service.create(new CreateVerificationRunCommand(
                customerId,
                environmentId,
                versionId,
                "deadbeef",
                VerificationTriggerType.MANUAL,
                "support",
                "corr-notify"
        ));

        assertThat(execution.result().status()).isEqualTo(VerificationResultStatus.DIVERGENT);
        assertThat(legacyNotificationRepository.findAll()).hasSize(1);
        var notification = legacyNotificationRepository.findAll().get(0);
        assertThat(notification.divergenceId()).isEqualTo(divergenceRepository.findAll().get(0).id());
        assertThat(notification.customerId()).isEqualTo(customerId);
        assertThat(notification.environmentId()).isEqualTo(environmentId);
        assertThat(notification.customizationId()).isEqualTo(customizationId);
        assertThat(notification.status().name()).isEqualTo("PENDING");
        assertThat(notification.type().name()).isEqualTo("DIVERGENCE_DETECTED");
        assertThat(notification.idempotencyKey()).isEqualTo(notification.divergenceId().toString());
        assertThat(notification.payloadJson()).contains("deadbeef".toUpperCase());
        assertThat(notification.correlationId()).isEqualTo("corr-notify");
    }

    @Test
    void doesNotCreateLegacyNotificationWhenVerificationMatches() {
        seedData();

        service.create(new CreateVerificationRunCommand(
                customerId,
                environmentId,
                versionId,
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                VerificationTriggerType.MANUAL,
                "support",
                "corr-match"
        ));

        assertThat(legacyNotificationRepository.findAll()).isEmpty();
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
        @Override public Customer save(Customer customer) { customers.add(customer); return customer; }
        @Override public List<Customer> findAll() { return List.copyOf(customers); }
        @Override public Optional<Customer> findById(UUID id) { return customers.stream().filter(customer -> customer.id().equals(id)).findFirst(); }
    }

    private static final class InMemoryEnvironmentRepository implements CustomerEnvironmentRepository {
        private final List<CustomerEnvironment> environments = new ArrayList<>();
        @Override public CustomerEnvironment save(CustomerEnvironment environment) { environments.add(environment); return environment; }
        @Override public List<CustomerEnvironment> findByCustomerId(UUID id) { return environments.stream().filter(environment -> environment.customerId().equals(id)).toList(); }
        @Override public Optional<CustomerEnvironment> findById(UUID id) { return environments.stream().filter(environment -> environment.id().equals(id)).findFirst(); }
    }

    private static final class InMemoryCustomizationRepository implements CustomizationRepository {
        private final List<Customization> customizations = new ArrayList<>();
        @Override public Customization save(Customization customization) { customizations.add(customization); return customization; }
        @Override public List<Customization> findAll() { return List.copyOf(customizations); }
        @Override public Optional<Customization> findById(UUID id) { return customizations.stream().filter(customization -> customization.id().equals(id)).findFirst(); }
    }

    private static final class InMemoryCustomizationVersionRepository implements CustomizationVersionRepository {
        private final List<CustomizationVersion> versions = new ArrayList<>();
        @Override public CustomizationVersion save(CustomizationVersion version) { versions.add(version); return version; }
        @Override public List<CustomizationVersion> findByCustomizationId(UUID customizationId) { return versions.stream().filter(version -> version.customizationId().equals(customizationId)).toList(); }
        @Override public Optional<CustomizationVersion> findById(UUID id) { return versions.stream().filter(version -> version.id().equals(id)).findFirst(); }
    }

    private static final class InMemoryVerificationRunRepository implements VerificationRunRepository {
        private final List<VerificationRun> runs = new ArrayList<>();
        @Override public VerificationRun save(VerificationRun run) { runs.add(run); return run; }
        @Override public Optional<VerificationRun> findById(UUID id) { return runs.stream().filter(run -> run.id().equals(id)).findFirst(); }
        @Override public List<VerificationRun> findAll() { return List.copyOf(runs); }
    }

    private static final class InMemoryVerificationResultRepository implements VerificationResultRepository {
        private final List<VerificationResult> results = new ArrayList<>();
        @Override public VerificationResult save(VerificationResult result) { results.add(result); return result; }
        @Override public List<VerificationResult> findByVerificationRunId(UUID verificationRunId) { return results.stream().filter(result -> result.verificationRunId().equals(verificationRunId)).toList(); }
        @Override public Optional<VerificationResult> findByVerificationRunIdAndCustomizationVersionId(UUID verificationRunId, UUID customizationVersionId) { return results.stream().filter(result -> result.verificationRunId().equals(verificationRunId)).filter(result -> result.customizationVersionId().equals(customizationVersionId)).findFirst(); }
    }

    private static final class InMemoryDivergenceRepository implements DivergenceRepository {
        private final List<Divergence> divergences = new ArrayList<>();
        @Override public Divergence save(Divergence divergence) { divergences.add(divergence); return divergence; }
        @Override public Optional<Divergence> findById(UUID id) { return divergences.stream().filter(divergence -> divergence.id().equals(id)).findFirst(); }
        @Override public Optional<Divergence> findByVerificationResultId(UUID verificationResultId) { return divergences.stream().filter(divergence -> divergence.verificationResultId().equals(verificationResultId)).findFirst(); }
        @Override public List<Divergence> findAll() { return List.copyOf(divergences); }
    }

    private static final class InMemoryLegacyNotificationRepository implements LegacyNotificationRepository {
        private final Map<UUID, com.customizationaudit.legacynotification.domain.LegacyNotification> notifications = new LinkedHashMap<>();
        @Override public com.customizationaudit.legacynotification.domain.LegacyNotification save(com.customizationaudit.legacynotification.domain.LegacyNotification legacyNotification) { notifications.put(legacyNotification.id(), legacyNotification); return legacyNotification; }
        @Override public Optional<com.customizationaudit.legacynotification.domain.LegacyNotification> findById(UUID id) { return Optional.ofNullable(notifications.get(id)); }
        @Override public List<com.customizationaudit.legacynotification.domain.LegacyNotification> findAll() { return List.copyOf(notifications.values()); }
    }
}
