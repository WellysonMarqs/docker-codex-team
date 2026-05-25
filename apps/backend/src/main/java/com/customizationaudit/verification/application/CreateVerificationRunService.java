package com.customizationaudit.verification.application;

import com.customizationaudit.customer.application.CustomerEnvironmentRepository;
import com.customizationaudit.customer.application.CustomerRepository;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customization.application.CustomizationRepository;
import com.customizationaudit.customization.application.CustomizationVersionRepository;
import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationVersion;
import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;
import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationRunStatus;
import com.customizationaudit.verification.domain.VerificationTriggerType;

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
    private final Clock clock;

    public CreateVerificationRunService(
            CustomerRepository customerRepository,
            CustomerEnvironmentRepository environmentRepository,
            CustomizationVersionRepository customizationVersionRepository,
            CustomizationRepository customizationRepository,
            VerificationRunRepository verificationRunRepository,
            VerificationResultRepository verificationResultRepository,
            Clock clock
    ) {
        this.customerRepository = customerRepository;
        this.environmentRepository = environmentRepository;
        this.customizationVersionRepository = customizationVersionRepository;
        this.customizationRepository = customizationRepository;
        this.verificationRunRepository = verificationRunRepository;
        this.verificationResultRepository = verificationResultRepository;
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
}
