package com.customizationaudit.customer.adapter.out.persistence;

import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customer.domain.EnvironmentStatus;
import com.customizationaudit.customer.domain.EnvironmentType;

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
@Table(name = "customer_environments")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerEnvironmentJpaEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EnvironmentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_mode", nullable = false, length = 40)
    private CollectionMode collectionMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EnvironmentStatus status;

    @Column(name = "credential_reference_id", length = 160)
    private String credentialReferenceId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    static CustomerEnvironmentJpaEntity fromDomain(CustomerEnvironment environment) {
        return new CustomerEnvironmentJpaEntity(
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

    CustomerEnvironment toDomain() {
        return CustomerEnvironment.restore(
                id,
                customerId,
                name,
                type,
                collectionMode,
                status,
                credentialReferenceId,
                createdAt,
                updatedAt
        );
    }
}
