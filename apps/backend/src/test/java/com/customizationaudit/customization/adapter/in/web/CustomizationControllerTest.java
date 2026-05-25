package com.customizationaudit.customization.adapter.in.web;

import com.customizationaudit.customer.adapter.in.web.TestSecurityConfig;
import com.customizationaudit.customization.application.ListCustomizationsQuery;
import com.customizationaudit.customization.application.RegisterCustomizationCommand;
import com.customizationaudit.customization.application.RegisterCustomizationUseCase;
import com.customizationaudit.customization.domain.Customization;
import com.customizationaudit.customization.domain.CustomizationObjectType;
import com.customizationaudit.customization.domain.CustomizationStatus;

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

@WebMvcTest(CustomizationController.class)
@Import(TestSecurityConfig.class)
class CustomizationControllerTest {

    private static final UUID CUSTOMER_ID = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private static final UUID ENVIRONMENT_ID = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterCustomizationUseCase registerCustomizationUseCase;

    @MockBean
    private ListCustomizationsQuery listCustomizationsQuery;

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:write")
    void createsCustomization() throws Exception {
        when(registerCustomizationUseCase.register(any(RegisterCustomizationCommand.class)))
                .thenReturn(customization());

        mockMvc.perform(post("/api/v1/customizations")
                        .contentType("application/json")
                        .content("""
                                {
                                  "customerId": "5b94d976-2f57-4d45-b6e3-426246d01674",
                                  "environmentId": "11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1",
                                  "name": "Procedure fiscal",
                                  "description": "Procedure customizada do cliente",
                                  "objectType": "PROCEDURE",
                                  "objectIdentifier": "billing.sp_customer_tax",
                                  "createdBy": "support"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Procedure fiscal"))
                .andExpect(jsonPath("$.objectType").value("PROCEDURE"))
                .andExpect(jsonPath("$.objectIdentifier").value("billing.sp_customer_tax"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void listsCustomizations() throws Exception {
        when(listCustomizationsQuery.list()).thenReturn(List.of(customization()));

        mockMvc.perform(get("/api/v1/customizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Procedure fiscal"));
    }

    private static Customization customization() {
        Instant now = Instant.parse("2026-05-24T12:00:00Z");
        return Customization.restore(
                UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0"),
                CUSTOMER_ID,
                ENVIRONMENT_ID,
                "Procedure fiscal",
                "Procedure customizada do cliente",
                CustomizationObjectType.PROCEDURE,
                "billing.sp_customer_tax",
                CustomizationStatus.ACTIVE,
                "support",
                now,
                now
        );
    }
}
