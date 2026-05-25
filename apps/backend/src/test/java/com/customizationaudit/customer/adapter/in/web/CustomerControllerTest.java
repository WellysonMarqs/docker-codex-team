package com.customizationaudit.customer.adapter.in.web;

import com.customizationaudit.customer.application.ListCustomersQuery;
import com.customizationaudit.customer.application.RegisterCustomerCommand;
import com.customizationaudit.customer.application.RegisterCustomerUseCase;
import com.customizationaudit.customer.domain.Customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(TestSecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterCustomerUseCase registerCustomerUseCase;

    @MockBean
    private ListCustomersQuery listCustomersQuery;

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:write")
    void createsCustomer() throws Exception {
        Customer customer = Customer.restore(
                UUID.fromString("4a3d9f6e-c624-4e51-9f38-4c676b94be99"),
                "Acme",
                "LEG-001",
                com.customizationaudit.customer.domain.CustomerStatus.ACTIVE,
                Instant.parse("2026-05-24T12:00:00Z"),
                Instant.parse("2026-05-24T12:00:00Z")
        );
        when(registerCustomerUseCase.register(any(RegisterCustomerCommand.class))).thenReturn(customer);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content("{\"name\":\"Acme\",\"externalReference\":\"LEG-001\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Acme"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void listsCustomers() throws Exception {
        when(listCustomersQuery.list()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
