package com.customizationaudit.verification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface VerificationRunJpaRepository extends JpaRepository<VerificationRunJpaEntity, UUID> {
}
