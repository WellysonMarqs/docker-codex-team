package com.customizationaudit.legacynotification.adapter.in.web;

import com.customizationaudit.legacynotification.application.ListLegacyNotificationsQuery;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/legacy-notifications")
public class LegacyNotificationController {

    private final ListLegacyNotificationsQuery listLegacyNotificationsQuery;

    public LegacyNotificationController(ListLegacyNotificationsQuery listLegacyNotificationsQuery) {
        this.listLegacyNotificationsQuery = listLegacyNotificationsQuery;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public List<LegacyNotificationResponse> list(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID environmentId
    ) {
        return listLegacyNotificationsQuery.list(customerId, environmentId).stream()
                .map(LegacyNotificationResponse::from)
                .toList();
    }

    @GetMapping("/{legacyNotificationId}")
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public LegacyNotificationResponse get(@PathVariable UUID legacyNotificationId) {
        return LegacyNotificationResponse.from(listLegacyNotificationsQuery.get(legacyNotificationId));
    }
}
