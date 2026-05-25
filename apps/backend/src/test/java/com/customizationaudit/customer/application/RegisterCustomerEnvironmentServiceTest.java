package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.Customer;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customer.domain.CustomerStatus;
import com.customizationaudit.customer.domain.EnvironmentStatus;
import com.customizationaudit.customer.domain.EnvironmentType;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegisterCustomerEnvironmentServiceTest {

    private final UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private final InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    private final InMemoryEnvironmentRepository environmentRepository = new InMemoryEnvironmentRepository();
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-24T12:00:00Z"), ZoneOffset.UTC);
    private final RegisterCustomerEnvironmentService service = new RegisterCustomerEnvironmentService(
            customerRepository,
            environmentRepository,
            clock
    );

    @Test
    void registersActiveEnvironmentForExistingCustomer() {
        customerRepository.save(Customer.restore(
                customerId,
                "Acme",
                "LEG-001",
                CustomerStatus.ACTIVE,
                clock.instant(),
                clock.instant()
        ));

        CustomerEnvironment environment = service.register(new RegisterCustomerEnvironmentCommand(
                customerId,
                "Producao SaaS",
                EnvironmentType.SAAS,
                CollectionMode.CENTRAL_PULL,
                "vault/customer/acme/prod"
        ));

        assertThat(environment.id()).isNotNull();
        assertThat(environment.customerId()).isEqualTo(customerId);
        assertThat(environment.name()).isEqualTo("Producao SaaS");
        assertThat(environment.type()).isEqualTo(EnvironmentType.SAAS);
        assertThat(environment.collectionMode()).isEqualTo(CollectionMode.CENTRAL_PULL);
        assertThat(environment.status()).isEqualTo(EnvironmentStatus.ACTIVE);
        assertThat(environment.credentialReferenceId()).isEqualTo("vault/customer/acme/prod");
        assertThat(environmentRepository.findByCustomerId(customerId)).hasSize(1);
    }

    @Test
    void rejectsEnvironmentWhenCustomerDoesNotExist() {
        RegisterCustomerEnvironmentCommand command = new RegisterCustomerEnvironmentCommand(
                customerId,
                "On-premise",
                EnvironmentType.ON_PREMISE,
                CollectionMode.LOCAL_AGENT_PUSH,
                null
        );

        assertThatThrownBy(() -> service.register(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("customer not found");
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

    private static final class InMemoryEnvironmentRepository implements CustomerEnvironmentRepository {

        private final List<CustomerEnvironment> environments = new ArrayList<>();

        @Override
        public CustomerEnvironment save(CustomerEnvironment environment) {
            environments.add(environment);
            return environment;
        }

        @Override
        public List<CustomerEnvironment> findByCustomerId(UUID id) {
            return environments.stream()
                    .filter(environment -> environment.customerId().equals(id))
                    .toList();
        }

        @Override
        public Optional<CustomerEnvironment> findById(UUID id) {
            return environments.stream()
                    .filter(environment -> environment.id().equals(id))
                    .findFirst();
        }
    }
}
