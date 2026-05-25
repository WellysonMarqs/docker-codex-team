package com.customizationaudit.customer.adapter.in.web;

import com.customizationaudit.customer.domain.Customer;
import com.customizationaudit.customer.domain.CustomerStatus;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String externalReference,
        CustomerStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.id(),
                customer.name(),
                customer.externalReference(),
                customer.status(),
                customer.createdAt(),
                customer.updatedAt()
        );
    }
}
