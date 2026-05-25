package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.CustomerEnvironment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerEnvironmentRepository {

    CustomerEnvironment save(CustomerEnvironment environment);

    List<CustomerEnvironment> findByCustomerId(UUID customerId);

    Optional<CustomerEnvironment> findById(UUID id);
}
