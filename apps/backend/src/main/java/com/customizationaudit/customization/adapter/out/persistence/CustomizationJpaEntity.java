package com.customizationaudit.customization.adapter.out.persistence;

import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationObjectType;
import com.customizationaudit.customization.domain.CustomizationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customizations")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomizationJpaEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "environment_id", nullable = false)
    private UUID environmentId;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type", nullable = false, length = 32)
    private CustomizationObjectType objectType;

    @Column(name = "object_identifier", nullable = false, length = 240)
    private String objectIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CustomizationStatus status;

    @Column(name = "created_by", length = 120)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    static CustomizationJpaEntity fromDomain(Customization customization) {
        return new CustomizationJpaEntity(
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

    Customization toDomain() {
        return Customization.restore(
                id,
                customerId,
                environmentId,
                name,
                description,
                objectType,
                objectIdentifier,
                status,
                createdBy,
                createdAt,
                updatedAt
        );
    }
}
