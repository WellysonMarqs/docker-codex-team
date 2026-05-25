package com.customizationaudit.customer.adapter.in.web;

import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.EnvironmentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCustomerEnvironmentRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull EnvironmentType type,
        @NotNull CollectionMode collectionMode,
        @Size(max = 160) String credentialReferenceId
) {
}
