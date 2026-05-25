package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.CustomizationVersion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class RegisterCustomizationVersionService implements RegisterCustomizationVersionUseCase {

    private final CustomizationRepository customizationRepository;
    private final CustomizationVersionRepository versionRepository;
    private final Clock clock;

    public RegisterCustomizationVersionService(
            CustomizationRepository customizationRepository,
            CustomizationVersionRepository versionRepository,
            Clock clock
    ) {
        this.customizationRepository = customizationRepository;
        this.versionRepository = versionRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public CustomizationVersion register(RegisterCustomizationVersionCommand command) {
        customizationRepository.findById(command.customizationId())
                .orElseThrow(() -> new IllegalArgumentException("customization not found"));

        CustomizationVersion version = CustomizationVersion.register(
                command.customizationId(),
                command.legacySystemVersion(),
                command.officialHash(),
                command.hashAlgorithm(),
                command.canonicalizationVersion(),
                command.contentSignature(),
                command.registeredBy(),
                command.activeFrom(),
                command.activeUntil(),
                clock.instant()
        );
        return versionRepository.save(version);
    }
}
