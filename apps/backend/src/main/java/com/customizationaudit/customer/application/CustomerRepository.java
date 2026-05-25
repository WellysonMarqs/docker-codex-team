package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    List<Customer> findAll();

    Optional<Customer> findById(UUID id);
}
