package com.customizationaudit.customer.adapter.out.persistence;

import com.customizationaudit.customer.domain.Customer;
import com.customizationaudit.customer.domain.CustomerStatus;

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
@Table(name = "customers")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(name = "external_reference", length = 80)
    private String externalReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CustomerStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    static CustomerJpaEntity fromDomain(Customer customer) {
        return new CustomerJpaEntity(
                customer.id(),
                customer.name(),
                customer.externalReference(),
                customer.status(),
                customer.createdAt(),
                customer.updatedAt()
        );
    }

    Customer toDomain() {
        return Customer.restore(id, name, externalReference, status, createdAt, updatedAt);
    }
}
