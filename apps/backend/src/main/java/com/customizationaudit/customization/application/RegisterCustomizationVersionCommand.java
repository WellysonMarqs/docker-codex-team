package com.customizationaudit.customization.application;

import java.time.Instant;
import java.util.UUID;

public record RegisterCustomizationVersionCommand(
        UUID customizationId,
        String legacySystemVersion,
        String officialHash,
        String hashAlgorithm,
        String canonicalizationVersion,
        String contentSignature,
        String registeredBy,
        Instant activeFrom,
        Instant activeUntil
) {
}
