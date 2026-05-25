package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.CustomerEnvironment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class RegisterCustomerEnvironmentService implements RegisterCustomerEnvironmentUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerEnvironmentRepository environmentRepository;
    private final Clock clock;

    public RegisterCustomerEnvironmentService(
            CustomerRepository customerRepository,
            CustomerEnvironmentRepository environmentRepository,
            Clock clock
    ) {
        this.customerRepository = customerRepository;
        this.environmentRepository = environmentRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public CustomerEnvironment register(RegisterCustomerEnvironmentCommand command) {
        customerRepository.findById(command.customerId())
                .orElseThrow(() -> new IllegalArgumentException("customer not found"));

        CustomerEnvironment environment = CustomerEnvironment.register(
                command.customerId(),
                command.name(),
                command.type(),
                command.collectionMode(),
                command.credentialReferenceId(),
                clock.instant()
        );
        return environmentRepository.save(environment);
    }
}
