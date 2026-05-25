package com.customizationaudit.divergence.adapter.in.web;

import com.customizationaudit.divergence.domain.DivergenceStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateDivergenceStatusRequest(
        @NotNull DivergenceStatus status
) {
}
