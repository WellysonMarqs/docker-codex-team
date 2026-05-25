package com.customizationaudit.customization.adapter.in.web;

import com.customizationaudit.customization.domain.CustomizationVersion;
import com.customizationaudit.customization.domain.CustomizationVersionStatus;

import java.time.Instant;
import java.util.UUID;

public record CustomizationVersionResponse(
        UUID id,
        UUID customizationId,
        String legacySystemVersion,
        String officialHash,
        String hashAlgorithm,
        String canonicalizationVersion,
        String contentSignature,
        String registeredBy,
        Instant registeredAt,
        Instant activeFrom,
        Instant activeUntil,
        CustomizationVersionStatus status
) {
    static CustomizationVersionResponse from(CustomizationVersion version) {
        return new CustomizationVersionResponse(
                version.id(),
                version.customizationId(),
                version.legacySystemVersion(),
                version.officialHash(),
                version.hashAlgorithm(),
                version.canonicalizationVersion(),
                version.contentSignature(),
                version.registeredBy(),
                version.registeredAt(),
                version.activeFrom(),
                version.activeUntil(),
                version.status()
        );
    }
}
