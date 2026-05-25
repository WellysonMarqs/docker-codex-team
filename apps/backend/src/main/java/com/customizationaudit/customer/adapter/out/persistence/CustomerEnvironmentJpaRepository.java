package com.customizationaudit.customer.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface CustomerEnvironmentJpaRepository extends JpaRepository<CustomerEnvironmentJpaEntity, UUID> {

    List<CustomerEnvironmentJpaEntity> findByCustomerIdOrderByNameAsc(UUID customerId);
}
