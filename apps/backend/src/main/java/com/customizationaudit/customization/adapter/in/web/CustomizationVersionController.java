package com.customizationaudit.customization.adapter.in.web;

import com.customizationaudit.customization.application.ListCustomizationVersionsQuery;
import com.customizationaudit.customization.application.RegisterCustomizationVersionCommand;
import com.customizationaudit.customization.application.RegisterCustomizationVersionUseCase;
import com.customizationaudit.customization.domain.CustomizationVersion;

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
@RequestMapping("/api/v1/customizations/{customizationId}/versions")
public class CustomizationVersionController {

    private final RegisterCustomizationVersionUseCase registerUseCase;
    private final ListCustomizationVersionsQuery listQuery;

    public CustomizationVersionController(
            RegisterCustomizationVersionUseCase registerUseCase,
            ListCustomizationVersionsQuery listQuery
    ) {
        this.registerUseCase = registerUseCase;
        this.listQuery = listQuery;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_customizations:write')")
    public CustomizationVersionResponse create(
            @PathVariable UUID customizationId,
            @Valid @RequestBody CreateCustomizationVersionRequest request
    ) {
        CustomizationVersion version = registerUseCase.register(
                new RegisterCustomizationVersionCommand(
                        customizationId,
                        request.legacySystemVersion(),
                        request.officialHash(),
                        request.hashAlgorithm(),
                        request.canonicalizationVersion(),
                        request.contentSignature(),
                        request.registeredBy(),
                        request.activeFrom(),
                        request.activeUntil()
                )
        );
        return CustomizationVersionResponse.from(version);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_customizations:read')")
    public List<CustomizationVersionResponse> list(@PathVariable UUID customizationId) {
        return listQuery.list(customizationId).stream()
                .map(CustomizationVersionResponse::from)
                .toList();
    }
}
