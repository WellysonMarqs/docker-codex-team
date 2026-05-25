package com.customizationaudit.customer.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID> {
}
