package com.customizationaudit.divergence.adapter.in.web;

import com.customizationaudit.customer.adapter.in.web.TestSecurityConfig;
import com.customizationaudit.divergence.application.ListDivergencesQuery;
import com.customizationaudit.divergence.application.DivergenceView;
import com.customizationaudit.divergence.application.UpdateDivergenceStatusService;
import com.customizationaudit.divergence.domain.Divergence;
import com.customizationaudit.divergence.domain.DivergenceSeverity;
import com.customizationaudit.divergence.domain.DivergenceStatus;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DivergenceController.class)
@Import(TestSecurityConfig.class)
class DivergenceControllerTest {

    private static final UUID DIVERGENCE_ID = UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443");
    private static final UUID CUSTOMER_ID = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private static final UUID ENVIRONMENT_ID = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListDivergencesQuery listDivergencesQuery;

    @MockBean
    private UpdateDivergenceStatusService updateDivergenceStatusService;

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void listsDivergencesUsingFilters() throws Exception {
        when(listDivergencesQuery.list(CUSTOMER_ID, ENVIRONMENT_ID)).thenReturn(List.of(divergence()));

        mockMvc.perform(get("/api/v1/divergences")
                        .queryParam("customerId", CUSTOMER_ID.toString())
                        .queryParam("environmentId", ENVIRONMENT_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(DIVERGENCE_ID.toString()))
                .andExpect(jsonPath("$[0].severity").value("HIGH"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));

        verify(listDivergencesQuery).list(CUSTOMER_ID, ENVIRONMENT_ID);
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void getsDivergenceById() throws Exception {
        when(listDivergencesQuery.get(DIVERGENCE_ID)).thenReturn(divergence());

        mockMvc.perform(get("/api/v1/divergences/{divergenceId}", DIVERGENCE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DIVERGENCE_ID.toString()))
                .andExpect(jsonPath("$.correlationId").value("corr-001"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:write")
    void updatesDivergenceStatus() throws Exception {
        when(updateDivergenceStatusService.update(org.mockito.ArgumentMatchers.any()))
                .thenReturn(DivergenceView.from(divergence().transitionTo(
                        DivergenceStatus.RESOLVED,
                        Instant.parse("2026-05-25T11:30:00Z")
                )));

        mockMvc.perform(patch("/api/v1/divergences/{divergenceId}/status", DIVERGENCE_ID)
                        .contentType("application/json")
                        .content("""
                                {
                                  "status": "RESOLVED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"))
                .andExpect(jsonPath("$.resolvedAt").value("2026-05-25T11:30:00Z"));
    }

    private static Divergence divergence() {
        return Divergence.restore(
                DIVERGENCE_ID,
                UUID.fromString("3aee51a0-17cb-45ec-bd44-80fc4fb620c7"),
                CUSTOMER_ID,
                ENVIRONMENT_ID,
                UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0"),
                DivergenceSeverity.HIGH,
                DivergenceStatus.OPEN,
                Instant.parse("2026-05-25T10:00:00Z"),
                null,
                "corr-001"
        );
    }
}
