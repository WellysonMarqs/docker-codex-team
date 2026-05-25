package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.EnvironmentType;

import java.util.UUID;

public record RegisterCustomerEnvironmentCommand(
        UUID customerId,
        String name,
        EnvironmentType type,
        CollectionMode collectionMode,
        String credentialReferenceId
) {
}
