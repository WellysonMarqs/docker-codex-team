package com.customizationaudit.customer.adapter.out.persistence;

import com.customizationaudit.customer.application.CustomerRepository;
import com.customizationaudit.customer.domain.Customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresCustomerRepository implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public PostgresCustomerRepository(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return customerJpaRepository.save(CustomerJpaEntity.fromDomain(customer)).toDomain();
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll().stream()
                .map(CustomerJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerJpaRepository.findById(id).map(CustomerJpaEntity::toDomain);
    }
}
