package com.customizationaudit.customer.adapter.out.persistence;

import com.customizationaudit.customer.application.CustomerEnvironmentRepository;
import com.customizationaudit.customer.domain.CustomerEnvironment;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresCustomerEnvironmentRepository implements CustomerEnvironmentRepository {

    private final CustomerEnvironmentJpaRepository environmentJpaRepository;

    public PostgresCustomerEnvironmentRepository(CustomerEnvironmentJpaRepository environmentJpaRepository) {
        this.environmentJpaRepository = environmentJpaRepository;
    }

    @Override
    public CustomerEnvironment save(CustomerEnvironment environment) {
        return environmentJpaRepository.save(CustomerEnvironmentJpaEntity.fromDomain(environment)).toDomain();
    }

    @Override
    public List<CustomerEnvironment> findByCustomerId(UUID customerId) {
        return environmentJpaRepository.findByCustomerIdOrderByNameAsc(customerId).stream()
                .map(CustomerEnvironmentJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<CustomerEnvironment> findById(UUID id) {
        return environmentJpaRepository.findById(id).map(CustomerEnvironmentJpaEntity::toDomain);
    }
}
