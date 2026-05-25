package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.CustomerEnvironment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListCustomerEnvironmentsQuery {

    private final CustomerEnvironmentRepository environmentRepository;

    public ListCustomerEnvironmentsQuery(CustomerEnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerEnvironment> list(UUID customerId) {
        return environmentRepository.findByCustomerId(customerId);
    }
}
