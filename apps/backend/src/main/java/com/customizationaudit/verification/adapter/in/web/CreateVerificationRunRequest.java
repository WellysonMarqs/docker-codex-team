package com.customizationaudit.verification.adapter.in.web;

import com.customizationaudit.verification.domain.VerificationTriggerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateVerificationRunRequest(
        @NotNull
        UUID customerId,

        @NotNull
        UUID environmentId,

        @NotNull
        UUID customizationVersionId,

        @NotBlank
        @Size(max = 128)
        String currentHash,

        VerificationTriggerType triggerType,

        @Size(max = 120)
        String requestedBy,

        @Size(max = 120)
        String correlationId
) {
}
