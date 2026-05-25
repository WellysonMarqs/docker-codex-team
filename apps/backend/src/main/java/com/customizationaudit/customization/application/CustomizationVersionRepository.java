package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.CustomizationVersion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomizationVersionRepository {

    CustomizationVersion save(CustomizationVersion version);

    List<CustomizationVersion> findByCustomizationId(UUID customizationId);

    Optional<CustomizationVersion> findById(UUID id);
}
