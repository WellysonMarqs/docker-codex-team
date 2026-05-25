package com.customizationaudit.verification.adapter.in.web;

import com.customizationaudit.verification.application.CreateVerificationRunCommand;
import com.customizationaudit.verification.application.CreateVerificationRunUseCase;
import com.customizationaudit.verification.application.ListVerificationRunsQuery;
import com.customizationaudit.verification.application.VerificationExecution;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/verification-runs")
public class VerificationRunController {

    private final CreateVerificationRunUseCase createVerificationRunUseCase;
    private final ListVerificationRunsQuery listVerificationRunsQuery;

    public VerificationRunController(
            CreateVerificationRunUseCase createVerificationRunUseCase,
            ListVerificationRunsQuery listVerificationRunsQuery
    ) {
        this.createVerificationRunUseCase = createVerificationRunUseCase;
        this.listVerificationRunsQuery = listVerificationRunsQuery;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_customizations:write')")
    public VerificationRunResponse create(@Valid @RequestBody CreateVerificationRunRequest request) {
        VerificationExecution execution = createVerificationRunUseCase.create(
                new CreateVerificationRunCommand(
                        request.customerId(),
                        request.environmentId(),
                        request.customizationVersionId(),
                        request.currentHash(),
                        request.triggerType(),
                        request.requestedBy(),
                        request.correlationId()
                )
        );
        return VerificationRunResponse.from(execution);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public List<VerificationRunResponse> list(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID environmentId
    ) {
        List<VerificationExecution> executions = customerId == null && environmentId == null
                ? listVerificationRunsQuery.list()
                : listVerificationRunsQuery.list(customerId, environmentId);
        return executions.stream()
                .map(VerificationRunResponse::from)
                .toList();
    }

    @GetMapping("/{verificationRunId}")
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public VerificationRunResponse get(@PathVariable UUID verificationRunId) {
        return VerificationRunResponse.from(listVerificationRunsQuery.get(verificationRunId));
    }
}
