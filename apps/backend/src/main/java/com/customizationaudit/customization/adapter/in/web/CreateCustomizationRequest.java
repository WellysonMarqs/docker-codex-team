package com.customizationaudit.customization.adapter.in.web;

import com.customizationaudit.customization.domain.CustomizationObjectType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCustomizationRequest(
        @NotNull UUID customerId,
        @NotNull UUID environmentId,
        @NotBlank @Size(max = 160) String name,
        @Size(max = 500) String description,
        @NotNull CustomizationObjectType objectType,
        @NotBlank @Size(max = 240) String objectIdentifier,
        @Size(max = 120) String createdBy
) {
}
