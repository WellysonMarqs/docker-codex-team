package com.customizationaudit.customization.application;

import com.customizationaudit.customer.application.CustomerEnvironmentRepository;
import com.customizationaudit.customer.application.CustomerRepository;
import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.Customer;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customer.domain.CustomerStatus;
import com.customizationaudit.customer.domain.EnvironmentStatus;
import com.customizationaudit.customer.domain.EnvironmentType;
import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationObjectType;
import com.customizationaudit.customization.domain.CustomizationStatus;

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

class RegisterCustomizationServiceTest {

    private final UUID customerId = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private final UUID environmentId = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
    private final InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    private final InMemoryEnvironmentRepository environmentRepository = new InMemoryEnvironmentRepository();
    private final InMemoryCustomizationRepository customizationRepository = new InMemoryCustomizationRepository();
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-24T12:00:00Z"), ZoneOffset.UTC);
    private final RegisterCustomizationService service = new RegisterCustomizationService(
            customerRepository,
            environmentRepository,
            customizationRepository,
            clock
    );

    @Test
    void registersCustomizationForExistingCustomerEnvironment() {
        seedCustomerAndEnvironment(customerId, environmentId);

        Customization customization = service.register(new RegisterCustomizationCommand(
                customerId,
                environmentId,
                "Procedure fiscal",
                "Procedure customizada do cliente",
                CustomizationObjectType.PROCEDURE,
                "billing.sp_customer_tax",
                "support"
        ));

        assertThat(customization.id()).isNotNull();
        assertThat(customization.customerId()).isEqualTo(customerId);
        assertThat(customization.environmentId()).isEqualTo(environmentId);
        assertThat(customization.objectType()).isEqualTo(CustomizationObjectType.PROCEDURE);
        assertThat(customization.objectIdentifier()).isEqualTo("billing.sp_customer_tax");
        assertThat(customization.status()).isEqualTo(CustomizationStatus.ACTIVE);
        assertThat(customizationRepository.findAll()).hasSize(1);
    }

    @Test
    void rejectsCustomizationWhenEnvironmentBelongsToAnotherCustomer() {
        UUID anotherCustomerId = UUID.fromString("7fab5aaa-bdcc-4c08-a04d-54e98b5605ef");
        seedCustomerAndEnvironment(customerId, environmentId);
        customerRepository.save(Customer.restore(
                anotherCustomerId,
                "Globex",
                "LEG-002",
                CustomerStatus.ACTIVE,
                clock.instant(),
                clock.instant()
        ));

        RegisterCustomizationCommand command = new RegisterCustomizationCommand(
                anotherCustomerId,
                environmentId,
                "Trigger auditoria",
                null,
                CustomizationObjectType.TRIGGER,
                "audit.tr_customer_change",
                null
        );

        assertThatThrownBy(() -> service.register(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("environment does not belong to customer");
    }

    private void seedCustomerAndEnvironment(UUID seedCustomerId, UUID seedEnvironmentId) {
        customerRepository.save(Customer.restore(
                seedCustomerId,
                "Acme",
                "LEG-001",
                CustomerStatus.ACTIVE,
                clock.instant(),
                clock.instant()
        ));
        environmentRepository.save(CustomerEnvironment.restore(
                seedEnvironmentId,
                seedCustomerId,
                "Producao",
                EnvironmentType.SAAS,
                CollectionMode.CENTRAL_PULL,
                EnvironmentStatus.ACTIVE,
                "vault/customer/acme/prod",
                clock.instant(),
                clock.instant()
        ));
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

    private static final class InMemoryCustomizationRepository implements CustomizationRepository {

        private final List<Customization> customizations = new ArrayList<>();

        @Override
        public Customization save(Customization customization) {
            customizations.add(customization);
            return customization;
        }

        @Override
        public List<Customization> findAll() {
            return List.copyOf(customizations);
        }

        @Override
        public Optional<Customization> findById(UUID id) {
            return customizations.stream()
                    .filter(customization -> customization.id().equals(id))
                    .findFirst();
        }
    }
}
