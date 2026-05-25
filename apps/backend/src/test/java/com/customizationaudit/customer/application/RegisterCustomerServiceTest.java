package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.Customer;
import com.customizationaudit.customer.domain.CustomerStatus;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterCustomerServiceTest {

    private final InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-24T12:00:00Z"), ZoneOffset.UTC);
    private final RegisterCustomerService service = new RegisterCustomerService(repository, clock);

    @Test
    void registersActiveCustomer() {
        Customer customer = service.register(new RegisterCustomerCommand("Acme", "LEG-001"));

        assertThat(customer.id()).isNotNull();
        assertThat(customer.name()).isEqualTo("Acme");
        assertThat(customer.externalReference()).isEqualTo("LEG-001");
        assertThat(customer.status()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(customer.createdAt()).isEqualTo(clock.instant());
        assertThat(repository.findAll()).hasSize(1);
    }

    private static final class InMemoryCustomerRepository implements CustomerRepository {

        private final List<Customer> customers = new ArrayList<>();

        @Override
        public Customer save(Customer customer) {
            customers.add(customer);
            return customer;
        }

        @Override
        public List<Customer> findAll() {
            return List.copyOf(customers);
        }

        @Override
        public Optional<Customer> findById(UUID id) {
            return customers.stream().filter(customer -> customer.id().equals(id)).findFirst();
        }
    }
}
