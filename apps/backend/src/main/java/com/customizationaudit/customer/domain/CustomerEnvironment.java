package com.customizationaudit.customer.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Value
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerEnvironment {
    UUID id;
    UUID customerId;
    String name;
    EnvironmentType type;
    CollectionMode collectionMode;
    EnvironmentStatus status;
    String credentialReferenceId;
    Instant createdAt;
    Instant updatedAt;

    public static CustomerEnvironment register(
            UUID customerId,
            String name,
            EnvironmentType type,
            CollectionMode collectionMode,
            String credentialReferenceId,
            Instant now
    ) {
        return restore(
                UUID.randomUUID(),
                customerId,
                name,
                type,
                collectionMode,
                EnvironmentStatus.ACTIVE,
                credentialReferenceId,
                now,
                now
        );
    }

    public static CustomerEnvironment restore(
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
        return new CustomerEnvironment(
                Objects.requireNonNull(id, "id is required"),
                Objects.requireNonNull(customerId, "customerId is required"),
                requireText(name, "name is required"),
                Objects.requireNonNull(type, "type is required"),
                Objects.requireNonNull(collectionMode, "collectionMode is required"),
                Objects.requireNonNull(status, "status is required"),
                normalizeOptional(credentialReferenceId),
                Objects.requireNonNull(createdAt, "createdAt is required"),
                Objects.requireNonNull(updatedAt, "updatedAt is required")
        );
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private static String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
