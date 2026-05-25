package com.customizationaudit.legacynotification.adapter.in.web;

import com.customizationaudit.customer.adapter.in.web.TestSecurityConfig;
import com.customizationaudit.legacynotification.application.ListLegacyNotificationsQuery;
import com.customizationaudit.legacynotification.domain.LegacyNotification;
import com.customizationaudit.legacynotification.domain.LegacyNotificationStatus;
import com.customizationaudit.legacynotification.domain.LegacyNotificationType;

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

@WebMvcTest(LegacyNotificationController.class)
@Import(TestSecurityConfig.class)
class LegacyNotificationControllerTest {

    private static final UUID NOTIFICATION_ID = UUID.fromString("4aa6cdb1-60e2-4dc5-a2a8-d9dd39f10d56");
    private static final UUID CUSTOMER_ID = UUID.fromString("5b94d976-2f57-4d45-b6e3-426246d01674");
    private static final UUID ENVIRONMENT_ID = UUID.fromString("11a0a2bd-44db-4d1e-90d8-8c392b4a9ac1");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListLegacyNotificationsQuery listLegacyNotificationsQuery;

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void listsNotificationsUsingFilters() throws Exception {
        when(listLegacyNotificationsQuery.list(CUSTOMER_ID, ENVIRONMENT_ID)).thenReturn(List.of(notification()));

        mockMvc.perform(get("/api/v1/legacy-notifications")
                        .queryParam("customerId", CUSTOMER_ID.toString())
                        .queryParam("environmentId", ENVIRONMENT_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(NOTIFICATION_ID.toString()))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].type").value("DIVERGENCE_DETECTED"));

        verify(listLegacyNotificationsQuery).list(CUSTOMER_ID, ENVIRONMENT_ID);
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customizations:read")
    void getsNotificationById() throws Exception {
        when(listLegacyNotificationsQuery.get(NOTIFICATION_ID)).thenReturn(notification());

        mockMvc.perform(get("/api/v1/legacy-notifications/{legacyNotificationId}", NOTIFICATION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(NOTIFICATION_ID.toString()))
                .andExpect(jsonPath("$.idempotencyKey").value("key-001"));
    }

    private static LegacyNotification notification() {
        return LegacyNotification.restore(
                NOTIFICATION_ID,
                UUID.fromString("f15aa535-01b5-4b59-a1cf-b39990eb7443"),
                CUSTOMER_ID,
                ENVIRONMENT_ID,
                UUID.fromString("6c57d84e-70e3-48b1-b82c-fc7ac0370fd0"),
                LegacyNotificationType.DIVERGENCE_DETECTED,
                LegacyNotificationStatus.PENDING,
                "{\"status\":\"PENDING\"}",
                "key-001",
                0,
                Instant.parse("2026-05-25T10:00:00Z"),
                null,
                null,
                null,
                "corr-001"
        );
    }
}
