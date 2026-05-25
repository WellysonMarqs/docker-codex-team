package com.customizationaudit.customization.domain;

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
public class Customization {
    UUID id;
    UUID customerId;
    UUID environmentId;
    String name;
    String description;
    CustomizationObjectType objectType;
    String objectIdentifier;
    CustomizationStatus status;
    String createdBy;
    Instant createdAt;
    Instant updatedAt;

    public static Customization register(
            UUID customerId,
            UUID environmentId,
            String name,
            String description,
            CustomizationObjectType objectType,
            String objectIdentifier,
            String createdBy,
            Instant now
    ) {
        return restore(
                UUID.randomUUID(),
                customerId,
                environmentId,
                name,
                description,
                objectType,
                objectIdentifier,
                CustomizationStatus.ACTIVE,
                createdBy,
                now,
                now
        );
    }

    public static Customization restore(
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
        return new Customization(
                Objects.requireNonNull(id, "id is required"),
                Objects.requireNonNull(customerId, "customerId is required"),
                Objects.requireNonNull(environmentId, "environmentId is required"),
                requireText(name, "name is required"),
                normalizeOptional(description),
                Objects.requireNonNull(objectType, "objectType is required"),
                requireText(objectIdentifier, "objectIdentifier is required"),
                Objects.requireNonNull(status, "status is required"),
                normalizeOptional(createdBy),
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
