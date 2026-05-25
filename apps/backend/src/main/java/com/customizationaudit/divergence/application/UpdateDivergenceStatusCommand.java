package com.customizationaudit.divergence.application;

import com.customizationaudit.divergence.domain.DivergenceStatus;

import java.util.UUID;

public record UpdateDivergenceStatusCommand(
        UUID divergenceId,
        DivergenceStatus status
) {
}
