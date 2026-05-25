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
public class Customer {
    UUID id;
    String name;
    String externalReference;
    CustomerStatus status;
    Instant createdAt;
    Instant updatedAt;

    public static Customer register(String name, String externalReference, Instant now) {
        return restore(UUID.randomUUID(), name, externalReference, CustomerStatus.ACTIVE, now, now);
    }

    public static Customer restore(
            UUID id,
            String name,
            String externalReference,
            CustomerStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Customer(
                Objects.requireNonNull(id, "id is required"),
                requireText(name, "name is required"),
                externalReference,
                Objects.requireNonNull(status, "status is required"),
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
}
