package com.customizationaudit.customer.adapter.in.web;

import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customer.domain.EnvironmentStatus;
import com.customizationaudit.customer.domain.EnvironmentType;

import java.time.Instant;
import java.util.UUID;

public record CustomerEnvironmentResponse(
        UUID id,
        UUID customerId,
        String name,
        EnvironmentType type,
        CollectionMode collectionMode,
        EnvironmentStatus status,
        String credentialReferenceId,
        Instant createdAt,
        Instant updatedAt
) {

    public static CustomerEnvironmentResponse from(CustomerEnvironment environment) {
        return new CustomerEnvironmentResponse(
                environment.id(),
                environment.customerId(),
                environment.name(),
                environment.type(),
                environment.collectionMode(),
                environment.status(),
                environment.credentialReferenceId(),
                environment.createdAt(),
                environment.updatedAt()
        );
    }
}
