package com.customizationaudit.customization.adapter.in.web;

import com.customizationaudit.customization.application.ListCustomizationsQuery;
import com.customizationaudit.customization.application.RegisterCustomizationCommand;
import com.customizationaudit.customization.application.RegisterCustomizationUseCase;
import com.customizationaudit.customization.domain.Customization;

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
@RequestMapping("/api/v1/customizations")
public class CustomizationController {

    private final RegisterCustomizationUseCase registerCustomizationUseCase;
    private final ListCustomizationsQuery listCustomizationsQuery;

    public CustomizationController(
            RegisterCustomizationUseCase registerCustomizationUseCase,
            ListCustomizationsQuery listCustomizationsQuery
    ) {
        this.registerCustomizationUseCase = registerCustomizationUseCase;
        this.listCustomizationsQuery = listCustomizationsQuery;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_customizations:write')")
    public CustomizationResponse create(@Valid @RequestBody CreateCustomizationRequest request) {
        Customization customization = registerCustomizationUseCase.register(
                new RegisterCustomizationCommand(
                        request.customerId(),
                        request.environmentId(),
                        request.name(),
                        request.description(),
                        request.objectType(),
                        request.objectIdentifier(),
                        request.createdBy()
                )
        );
        return CustomizationResponse.from(customization);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public List<CustomizationResponse> list() {
        return listCustomizationsQuery.list().stream()
                .map(CustomizationResponse::from)
                .toList();
    }
}
