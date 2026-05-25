package com.customizationaudit.customer.adapter.in.web;

import com.customizationaudit.customer.application.ListCustomerEnvironmentsQuery;
import com.customizationaudit.customer.application.RegisterCustomerEnvironmentCommand;
import com.customizationaudit.customer.application.RegisterCustomerEnvironmentUseCase;
import com.customizationaudit.customer.domain.CustomerEnvironment;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/environments")
public class CustomerEnvironmentController {

    private final RegisterCustomerEnvironmentUseCase registerEnvironmentUseCase;
    private final ListCustomerEnvironmentsQuery listEnvironmentsQuery;

    public CustomerEnvironmentController(
            RegisterCustomerEnvironmentUseCase registerEnvironmentUseCase,
            ListCustomerEnvironmentsQuery listEnvironmentsQuery
    ) {
        this.registerEnvironmentUseCase = registerEnvironmentUseCase;
        this.listEnvironmentsQuery = listEnvironmentsQuery;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_customizations:write')")
    public CustomerEnvironmentResponse create(
            @PathVariable UUID customerId,
            @Valid @RequestBody CreateCustomerEnvironmentRequest request
    ) {
        CustomerEnvironment environment = registerEnvironmentUseCase.register(
                new RegisterCustomerEnvironmentCommand(
                        customerId,
                        request.name(),
                        request.type(),
                        request.collectionMode(),
                        request.credentialReferenceId()
                )
        );
        return CustomerEnvironmentResponse.from(environment);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public List<CustomerEnvironmentResponse> list(@PathVariable UUID customerId) {
        return listEnvironmentsQuery.list(customerId).stream()
                .map(CustomerEnvironmentResponse::from)
                .toList();
    }
}
