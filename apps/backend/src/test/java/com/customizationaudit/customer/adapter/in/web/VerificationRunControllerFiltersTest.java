package com.customizationaudit.verification.adapter.in.web;

import com.customizationaudit.customer.adapter.in.web.TestSecurityConfig;
import com.customizationaudit.verification.application.CreateVerificationRunUseCase;
import com.customizationaudit.verification.application.ListVerificationRunsQuery;
import com.customizationaudit.verification.application.VerificationExecution;
import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationResultStatus;
import com.customizationaudit.verification.domain.VerificationRun;
import com.customizationaudit.verification.domain.VerificationRunStatus;
import com.customizationaudit.verification.domain.VerificationTriggerType;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerificationRunController.class)
@Import(TestSecurityConfig.class)
class VerificationRunControllerFiltersTest {

    private static final UUID RUN_ID = UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443");
    private static final UUID CUSTOMER_ID = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private static final UUID ENVIRONMENT_ID = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");
    private static final UUID CUSTOMIZATION_VERSION_ID = UUID.fromString("1d143e36-9265-4de0-b5c2-aad81ec1e355");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateVerificationRunUseCase createVerificationRunUseCase;

    @MockBean
    private ListVerificationRunsQuery listVerificationRunsQuery;

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void listsVerificationRunsUsingCustomerAndEnvironmentFilters() throws Exception {
        when(listVerificationRunsQuery.list(CUSTOMER_ID, ENVIRONMENT_ID)).thenReturn(List.of(execution()));

        mockMvc.perform(get("/api/v1/verification-runs")
                        .queryParam("customerId", CUSTOMER_ID.toString())
                        .queryParam("environmentId", ENVIRONMENT_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(RUN_ID.toString()))
                .andExpect(jsonPath("$[0].result.status").value("MATCH"));

        verify(listVerificationRunsQuery).list(CUSTOMER_ID, ENVIRONMENT_ID);
    }

    private static VerificationExecution execution() {
        Instant startedAt = Instant.parse("2026-05-25T10:00:00Z");
        VerificationRun run = VerificationRun.restore(
                RUN_ID,
                CUSTOMER_ID,
                ENVIRONMENT_ID,
                VerificationTriggerType.MANUAL,
                VerificationRunStatus.COMPLETED,
                startedAt,
                startedAt,
                "support",
                "corr-001"
        );
        VerificationResult result = VerificationResult.restore(
                UUID.fromString("3aee51a0-17cb-45ec-bd44-80fc4fb620c7"),
                RUN_ID,
                CUSTOMIZATION_VERSION_ID,
                "ABC123",
                "ABC123",
                VerificationResultStatus.MATCH,
                "manual-entry",
                null,
                null,
                startedAt
        );
        return new VerificationExecution(run, result);
    }
}
