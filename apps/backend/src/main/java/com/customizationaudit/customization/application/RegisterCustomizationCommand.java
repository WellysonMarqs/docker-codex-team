package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.CustomizationObjectType;

import java.util.UUID;

public record RegisterCustomizationCommand(
        UUID customerId,
        UUID environmentId,
        String name,
        String description,
        CustomizationObjectType objectType,
        String objectIdentifier,
        String createdBy
) {
}
