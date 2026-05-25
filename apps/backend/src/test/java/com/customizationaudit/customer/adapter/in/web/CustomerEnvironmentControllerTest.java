package com.customizationaudit.customer.adapter.in.web;

import com.customizationaudit.customer.application.ListCustomerEnvironmentsQuery;
import com.customizationaudit.customer.application.RegisterCustomerEnvironmentCommand;
import com.customizationaudit.customer.application.RegisterCustomerEnvironmentUseCase;
import com.customizationaudit.customer.domain.CollectionMode;
import com.customizationaudit.customer.domain.CustomerEnvironment;
import com.customizationaudit.customer.domain.EnvironmentStatus;
import com.customizationaudit.customer.domain.EnvironmentType;

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

@WebMvcTest(CustomerEnvironmentController.class)
@Import(TestSecurityConfig.class)
class CustomerEnvironmentControllerTest {

    private static final UUID CUSTOMER_ID = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterCustomerEnvironmentUseCase registerEnvironmentUseCase;

    @MockBean
    private ListCustomerEnvironmentsQuery listEnvironmentsQuery;

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:write")
    void createsEnvironment() throws Exception {
        CustomerEnvironment environment = environment();
        when(registerEnvironmentUseCase.register(any(RegisterCustomerEnvironmentCommand.class)))
                .thenReturn(environment);

        mockMvc.perform(post("/api/v1/customers/{customerId}/environments", CUSTOMER_ID)
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Producao SaaS",
                                  "type": "SAAS",
                                  "collectionMode": "CENTRAL_PULL",
                                  "credentialReferenceId": "vault/customer/acme/prod"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID.toString()))
                .andExpect(jsonPath("$.name").value("Producao SaaS"))
                .andExpect(jsonPath("$.type").value("SAAS"))
                .andExpect(jsonPath("$.collectionMode").value("CENTRAL_PULL"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void listsEnvironments() throws Exception {
        when(listEnvironmentsQuery.list(CUSTOMER_ID)).thenReturn(List.of(environment()));

        mockMvc.perform(get("/api/v1/customers/{customerId}/environments", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Producao SaaS"));
    }

    private static CustomerEnvironment environment() {
        Instant now = Instant.parse("2026-05-24T12:00:00Z");
        return CustomerEnvironment.restore(
                UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1"),
                CUSTOMER_ID,
                "Producao SaaS",
                EnvironmentType.SAAS,
                CollectionMode.CENTRAL_PULL,
                EnvironmentStatus.ACTIVE,
                "vault/customer/acme/prod",
                now,
                now
        );
    }
}
