package com.customizationaudit.customization.application;

import com.customizationaudit.customer.application.CustomerEnvironmentRepository;
import com.customizationaudit.customer.application.CustomerRepository;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customization.domain.Customization;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class RegisterCustomizationService implements RegisterCustomizationUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerEnvironmentRepository environmentRepository;
    private final CustomizationRepository customizationRepository;
    private final Clock clock;

    public RegisterCustomizationService(
            CustomerRepository customerRepository,
            CustomerEnvironmentRepository environmentRepository,
            CustomizationRepository customizationRepository,
            Clock clock
    ) {
        this.customerRepository = customerRepository;
        this.environmentRepository = environmentRepository;
        this.customizationRepository = customizationRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Customization register(RegisterCustomizationCommand command) {
        customerRepository.findById(command.customerId())
                .orElseThrow(() -> new IllegalArgumentException("customer not found"));

        CustomerEnvironment environment = environmentRepository.findById(command.environmentId())
                .orElseThrow(() -> new IllegalArgumentException("environment not found"));
        if (!environment.customerId().equals(command.customerId())) {
            throw new IllegalArgumentException("environment does not belong to customer");
        }

        Customization customization = Customization.register(
                command.customerId(),
                command.environmentId(),
                command.name(),
                command.description(),
                command.objectType(),
                command.objectIdentifier(),
                command.createdBy(),
                clock.instant()
        );
        return customizationRepository.save(customization);
    }
}
