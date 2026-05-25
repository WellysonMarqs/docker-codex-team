package com.customizationaudit.customization.adapter.in.web;

import com.customizationaudit.customer.adapter.in.web.TestSecurityConfig;
import com.customizationaudit.customization.application.ListCustomizationVersionsQuery;
import com.customizationaudit.customization.application.RegisterCustomizationVersionCommand;
import com.customizationaudit.customization.application.RegisterCustomizationVersionUseCase;
import com.customizationaudit.customization.domain.CustomizationVersion;
import com.customizationaudit.customization.domain.CustomizationVersionStatus;

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

@WebMvcTest(CustomizationVersionController.class)
@Import(TestSecurityConfig.class)
class CustomizationVersionControllerTest {

    private static final UUID CUSTOMIZATION_ID = UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterCustomizationVersionUseCase registerUseCase;

    @MockBean
    private ListCustomizationVersionsQuery listQuery;

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:write")
    void createsCustomizationVersion() throws Exception {
        when(registerUseCase.register(any(RegisterCustomizationVersionCommand.class)))
                .thenReturn(version());

        mockMvc.perform(post("/api/v1/customizations/{customizationId}/versions", CUSTOMIZATION_ID)
                        .contentType("application/json")
                        .content("""
                                {
                                  "legacySystemVersion": "2026.05",
                                  "officialHash": "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                                  "hashAlgorithm": "SHA-256",
                                  "canonicalizationVersion": "mysql-procedure-v1",
                                  "contentSignature": "CREATE PROCEDURE billing.sp_customer_tax",
                                  "registeredBy": "support"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.legacySystemVersion").value("2026.05"))
                .andExpect(jsonPath("$.hashAlgorithm").value("SHA-256"))
                .andExpect(jsonPath("$.canonicalizationVersion").value("mysql-procedure-v1"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void listsCustomizationVersions() throws Exception {
        when(listQuery.list(CUSTOMIZATION_ID)).thenReturn(List.of(version()));

        mockMvc.perform(get("/api/v1/customizations/{customizationId}/versions", CUSTOMIZATION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].legacySystemVersion").value("2026.05"));
    }

    private static CustomizationVersion version() {
        Instant now = Instant.parse("2026-05-24T12:00:00Z");
        return CustomizationVersion.restore(
                UUID.fromString("1d143e36-9265-4de0-b5c2-aad81ec1e355"),
                CUSTOMIZATION_ID,
                "2026.05",
                "2f1c0f9a8d3b4e6f7a8b9c0d1e2f3a4b",
                "SHA-256",
                "mysql-procedure-v1",
                "CREATE PROCEDURE billing.sp_customer_tax",
                "support",
                now,
                null,
                null,
                CustomizationVersionStatus.ACTIVE
        );
    }
}
