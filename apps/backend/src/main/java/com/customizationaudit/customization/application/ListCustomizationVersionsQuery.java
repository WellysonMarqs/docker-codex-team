package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.CustomizationVersion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListCustomizationVersionsQuery {

    private final CustomizationRepository customizationRepository;
    private final CustomizationVersionRepository versionRepository;

    public ListCustomizationVersionsQuery(
            CustomizationRepository customizationRepository,
            CustomizationVersionRepository versionRepository
    ) {
        this.customizationRepository = customizationRepository;
        this.versionRepository = versionRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomizationVersion> list(UUID customizationId) {
        customizationRepository.findById(customizationId)
                .orElseThrow(() -> new IllegalArgumentException("customization not found"));
        return versionRepository.findByCustomizationId(customizationId);
    }
}
