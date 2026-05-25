package com.customizationaudit.divergence.adapter.in.web;

import com.customizationaudit.divergence.application.ListDivergencesQuery;
import com.customizationaudit.divergence.application.UpdateDivergenceStatusCommand;
import com.customizationaudit.divergence.application.UpdateDivergenceStatusService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/divergences")
public class DivergenceController {

    private final ListDivergencesQuery listDivergencesQuery;
    private final UpdateDivergenceStatusService updateDivergenceStatusService;

    public DivergenceController(
            ListDivergencesQuery listDivergencesQuery,
            UpdateDivergenceStatusService updateDivergenceStatusService
    ) {
        this.listDivergencesQuery = listDivergencesQuery;
        this.updateDivergenceStatusService = updateDivergenceStatusService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public List<DivergenceResponse> list(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID environmentId
    ) {
        return listDivergencesQuery.list(customerId, environmentId).stream()
                .map(DivergenceResponse::from)
                .toList();
    }

    @GetMapping("/{divergenceId}")
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public DivergenceResponse get(@PathVariable UUID divergenceId) {
        return DivergenceResponse.from(listDivergencesQuery.get(divergenceId));
    }

    @PatchMapping("/{divergenceId}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_customizations:write')")
    public DivergenceResponse updateStatus(
            @PathVariable UUID divergenceId,
            @Valid @RequestBody UpdateDivergenceStatusRequest request
    ) {
        return DivergenceResponse.from(updateDivergenceStatusService.update(
                new UpdateDivergenceStatusCommand(divergenceId, request.status())
        ));
    }
}
