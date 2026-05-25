package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.Customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class RegisterCustomerService implements RegisterCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final Clock clock;

    public RegisterCustomerService(CustomerRepository customerRepository, Clock clock) {
        this.customerRepository = customerRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Customer register(RegisterCustomerCommand command) {
        Customer customer = Customer.register(command.name(), command.externalReference(), clock.instant());
        return customerRepository.save(customer);
    }
}
