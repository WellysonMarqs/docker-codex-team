package com.customizationaudit.customization.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateCustomizationVersionRequest(
        @NotBlank
        @Size(max = 80)
        String legacySystemVersion,

        @NotBlank
        @Size(max = 128)
        String officialHash,

        @NotBlank
        @Size(max = 32)
        String hashAlgorithm,

        @NotBlank
        @Size(max = 40)
        String canonicalizationVersion,

        @Size(max = 4000)
        String contentSignature,

        @Size(max = 120)
        String registeredBy,

        Instant activeFrom,

        Instant activeUntil
) {
}
