package com.customizationaudit.verification.application;

import com.customizationaudit.divergence.application.DivergenceRepository;
import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.divergence.domain.DivergenceSeverity;
import com.customizationaudit.customer.application.CustomerEnvironmentRepository;
import com.customizationaudit.customer.application.CustomerRepository;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customization.application.CustomizationRepository;
import com.customizationaudit.customization.application.CustomizationVersionRepository;
import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationVersion;
import com.customizationaudit.legacynotification.application.LegacyNotificationRepository;
import com.customizationaudit.legacynotification.domain.LegacyNotification;
import com.customizationaudit.legacynotification.domain.LegacyNotificationType;
import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;
import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationRunStatus;
import com.customizationaudit.verification.domain.VerificationTriggerType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

@Service
public class CreateVerificationRunService implements CreateVerificationRunUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerEnvironmentRepository environmentRepository;
    private final CustomizationVersionRepository customizationVersionRepository;
    private final CustomizationRepository customizationRepository;
    private final VerificationRunRepository verificationRunRepository;
    private final VerificationResultRepository verificationResultRepository;
    private final DivergenceRepository divergenceRepository;
    private final LegacyNotificationRepository legacyNotificationRepository;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    CreateVerificationRunService(
            CustomerRepository customerRepository,
            CustomerEnvironmentRepository environmentRepository,
            CustomizationVersionRepository customizationVersionRepository,
            CustomizationRepository customizationRepository,
            VerificationRunRepository verificationRunRepository,
            VerificationResultRepository verificationResultRepository,
            DivergenceRepository divergenceRepository,
            Clock clock
    ) {
        this(
                customerRepository,
                environmentRepository,
                customizationVersionRepository,
                customizationRepository,
                verificationRunRepository,
                verificationResultRepository,
                divergenceRepository,
                new NoOpLegacyNotificationRepository(),
                new ObjectMapper(),
                clock
        );
    }

    CreateVerificationRunService(
            CustomerRepository customerRepository,
            CustomerEnvironmentRepository environmentRepository,
            CustomizationVersionRepository customizationVersionRepository,
            CustomizationRepository customizationRepository,
            VerificationRunRepository verificationRunRepository,
            VerificationResultRepository verificationResultRepository,
            Clock clock
    ) {
        this(
                customerRepository,
                environmentRepository,
                customizationVersionRepository,
                customizationRepository,
                verificationRunRepository,
                verificationResultRepository,
                new NoOpDivergenceRepository(),
                new NoOpLegacyNotificationRepository(),
                new ObjectMapper(),
                clock
            );
    }

    @Autowired
    public CreateVerificationRunService(
            CustomerRepository customerRepository,
            CustomerEnvironmentRepository environmentRepository,
            CustomizationVersionRepository customizationVersionRepository,
            CustomizationRepository customizationRepository,
            VerificationRunRepository verificationRunRepository,
            VerificationResultRepository verificationResultRepository,
            DivergenceRepository divergenceRepository,
            LegacyNotificationRepository legacyNotificationRepository,
            ObjectMapper objectMapper,
            Clock clock
    ) {
        this.customerRepository = customerRepository;
        this.environmentRepository = environmentRepository;
        this.customizationVersionRepository = customizationVersionRepository;
        this.customizationRepository = customizationRepository;
        this.verificationRunRepository = verificationRunRepository;
        this.verificationResultRepository = verificationResultRepository;
        this.divergenceRepository = divergenceRepository;
        this.legacyNotificationRepository = legacyNotificationRepository;
        this.objectMapper = configureObjectMapper(objectMapper);
        this.clock = clock;
    }

    @Override
    @Transactional
    public VerificationExecution create(CreateVerificationRunCommand command) {
        customerRepository.findById(command.customerId())
                .orElseThrow(() -> new IllegalArgumentException("customer not found"));

        CustomerEnvironment environment = environmentRepository.findById(command.environmentId())
                .orElseThrow(() -> new IllegalArgumentException("environment not found"));
        if (!Objects.equals(environment.customerId(), command.customerId())) {
            throw new IllegalArgumentException("environment does not belong to customer");
        }

        CustomizationVersion version = customizationVersionRepository.findById(command.customizationVersionId())
                .orElseThrow(() -> new IllegalArgumentException("customization version not found"));

        Customization customization = customizationRepository.findById(version.customizationId())
                .orElseThrow(() -> new IllegalArgumentException("customization not found"));
        if (!Objects.equals(customization.customerId(), command.customerId())) {
            throw new IllegalArgumentException("customization does not belong to customer");
        }
        if (!Objects.equals(customization.environmentId(), command.environmentId())) {
            throw new IllegalArgumentException("customization does not belong to environment");
        }

        Instant now = clock.instant();
        String normalizedCurrentHash = normalizeHash(command.currentHash(), "currentHash");
        String normalizedOfficialHash = normalizeHash(version.officialHash(), "officialHash");
        VerificationResultStatus resultStatus = normalizedCurrentHash.equalsIgnoreCase(normalizedOfficialHash)
                ? VerificationResultStatus.MATCH
                : VerificationResultStatus.DIVERGENT;
        VerificationRunStatus runStatus = resultStatus == VerificationResultStatus.MATCH
                ? VerificationRunStatus.COMPLETED
                : VerificationRunStatus.COMPLETED_WITH_DIVERGENCE;

        VerificationRun run = verificationRunRepository.save(VerificationRun.register(
                command.customerId(),
                command.environmentId(),
                command.triggerType() == null ? VerificationTriggerType.MANUAL : command.triggerType(),
                runStatus,
                command.requestedBy(),
                normalizeCorrelationId(command.correlationId()),
                now,
                now
        ));

        VerificationResult result = verificationResultRepository.save(VerificationResult.register(
                run.id(),
                version.id(),
                normalizedCurrentHash,
                normalizedOfficialHash,
                resultStatus,
                "manual-entry",
                null,
                null,
                now
        ));

        if (resultStatus == VerificationResultStatus.DIVERGENT) {
            Divergence divergence = divergenceRepository.save(Divergence.open(
                    result.id(),
                    command.customerId(),
                    command.environmentId(),
                    customization.id(),
                    DivergenceSeverity.HIGH,
                    now,
                    run.correlationId()
            ));
            legacyNotificationRepository.save(LegacyNotification.pending(
                    divergence.id(),
                    command.customerId(),
                    command.environmentId(),
                    customization.id(),
                    LegacyNotificationType.DIVERGENCE_DETECTED,
                    serializePayload(new LegacyNotificationPayload(
                            divergence.id(),
                            command.customerId(),
                            command.environmentId(),
                            customization.id(),
                            customization.name(),
                            customization.objectType().name(),
                            customization.objectIdentifier(),
                            version.legacySystemVersion(),
                            normalizedOfficialHash,
                            normalizedCurrentHash,
                            now,
                            DivergenceSeverity.HIGH.name(),
                            run.correlationId()
                    )),
                    divergence.id().toString(),
                    now,
                    run.correlationId()
            ));
        }

        return new VerificationExecution(run, result);
    }

    private String normalizeHash(String hash, String fieldName) {
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return hash.trim().toUpperCase();
    }

    private String normalizeCorrelationId(String value) {
        if (value == null || value.isBlank()) {
            return java.util.UUID.randomUUID().toString();
        }
        return value.trim();
    }

    private String serializePayload(LegacyNotificationPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("failed to serialize legacy notification payload", exception);
        }
    }

    private ObjectMapper configureObjectMapper(ObjectMapper mapper) {
        return mapper.copy().findAndRegisterModules();
    }

    private static final class NoOpDivergenceRepository implements DivergenceRepository {

        @Override
        public Divergence save(Divergence divergence) {
            return divergence;
        }

        @Override
        public java.util.Optional<Divergence> findById(java.util.UUID id) {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.Optional<Divergence> findByVerificationResultId(java.util.UUID verificationResultId) {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.List<Divergence> findAll() {
            return java.util.List.of();
        }
    }

    private static final class NoOpLegacyNotificationRepository implements LegacyNotificationRepository {

        @Override
        public LegacyNotification save(LegacyNotification legacyNotification) {
            return legacyNotification;
        }

        @Override
        public java.util.Optional<LegacyNotification> findById(java.util.UUID id) {
            return java.util.Optional.empty();
        }
    }

    private record LegacyNotificationPayload(
            java.util.UUID divergenceId,
            java.util.UUID customerId,
            java.util.UUID environmentId,
            java.util.UUID customizationId,
            String customizationName,
            String objectType,
            String objectIdentifier,
            String legacySystemVersion,
            String officialHash,
            String currentHash,
            java.time.Instant checkedAt,
            String severity,
            String correlationId
    ) {
    }
}
