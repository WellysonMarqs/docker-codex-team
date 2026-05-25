package com.customizationaudit.customer.adapter.in.web;

import com.customizationaudit.customer.application.ListCustomersQuery;
import com.customizationaudit.customer.application.RegisterCustomerCommand;
import com.customizationaudit.customer.application.RegisterCustomerUseCase;
import com.customizationaudit.customer.domain.Customer;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final ListCustomersQuery listCustomersQuery;

    public CustomerController(
            RegisterCustomerUseCase registerCustomerUseCase,
            ListCustomersQuery listCustomersQuery
    ) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.listCustomersQuery = listCustomersQuery;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_customizations:write')")
    public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = registerCustomerUseCase.register(
                new RegisterCustomerCommand(request.name(), request.externalReference())
        );
        return CustomerResponse.from(customer);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public List<CustomerResponse> list() {
        return listCustomersQuery.list().stream()
                .map(CustomerResponse::from)
                .toList();
    }
}
