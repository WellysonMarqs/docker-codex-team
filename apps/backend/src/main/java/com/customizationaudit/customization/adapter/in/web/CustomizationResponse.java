package com.customizationaudit.customization.adapter.in.web;

import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationObjectType;
import com.customizationaudit.customization.domain.CustomizationStatus;

import java.time.Instant;
import java.util.UUID;

public record CustomizationResponse(
        UUID id,
        UUID customerId,
        UUID environmentId,
        String name,
        String description,
        CustomizationObjectType objectType,
        String objectIdentifier,
        CustomizationStatus status,
        String createdBy,
        Instant createdAt,
        Instant updatedAt
) {

    public static CustomizationResponse from(Customization customization) {
        return new CustomizationResponse(
                customization.id(),
                customization.customerId(),
                customization.environmentId(),
                customization.name(),
                customization.description(),
                customization.objectType(),
                customization.objectIdentifier(),
                customization.status(),
                customization.createdBy(),
                customization.createdAt(),
                customization.updatedAt()
        );
    }
}
