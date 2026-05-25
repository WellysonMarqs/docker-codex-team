package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationObjectType;
import com.customizationaudit.customization.domain.CustomizationStatus;
import com.customizationaudit.customization.domain.CustomizationVersion;
import com.customizationaudit.customization.domain.CustomizationVersionStatus;

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

class RegisterCustomizationVersionServiceTest {

    private final UUID customizationId = UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0");
    private final InMemoryCustomizationRepository customizationRepository = new InMemoryCustomizationRepository();
    private final InMemoryCustomizationVersionRepository versionRepository =
            new InMemoryCustomizationVersionRepository();
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-24T12:00:00Z"), ZoneOffset.UTC);
    private final RegisterCustomizationVersionService service = new RegisterCustomizationVersionService(
            customizationRepository,
            versionRepository,
            clock
    );

    @Test
    void registersVersionForExistingCustomization() {
        seedCustomization();

        CustomizationVersion version = service.register(new RegisterCustomizationVersionCommand(
                customizationId,
                "2026.05",
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                "sha-256",
                "mysql-procedure-v1",
                "CREATE PROCEDURE billing.sp_customer_tax",
                "support",
                Instant.parse("2026-05-24T00:00:00Z"),
                null
        ));

        assertThat(version.id()).isNotNull();
        assertThat(version.customizationId()).isEqualTo(customizationId);
        assertThat(version.hashAlgorithm()).isEqualTo("SHA-256");
        assertThat(version.status()).isEqualTo(CustomizationVersionStatus.ACTIVE);
        assertThat(versionRepository.findByCustomizationId(customizationId)).hasSize(1);
    }

    @Test
    void rejectsVersionWhenCustomizationDoesNotExist() {
        RegisterCustomizationVersionCommand command = new RegisterCustomizationVersionCommand(
                customizationId,
                "2026.05",
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                "SHA-256",
                "mysql-procedure-v1",
                null,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> service.register(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("customization not found");
    }

    private void seedCustomization() {
        customizationRepository.save(Customization.restore(
                customizationId,
                UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674"),
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                "Procedure fiscal",
                "Procedure customizada do cliente",
                CustomizationObjectType.PROCEDURE,
                "billing.sp_customer_tax",
                CustomizationStatus.ACTIVE,
                "support",
                clock.instant(),
                clock.instant()
        ));
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
            return customizations.stream()
                    .filter(customization -> customization.id().equals(id))
                    .findFirst();
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
        public List<CustomizationVersion> findByCustomizationId(UUID id) {
            return versions.stream()
                    .filter(version -> version.customizationId().equals(id))
                    .toList();
        }

        @Override
        public Optional<CustomizationVersion> findById(UUID id) {
            return versions.stream()
                    .filter(version -> version.id().equals(id))
                    .findFirst();
        }
    }
}
