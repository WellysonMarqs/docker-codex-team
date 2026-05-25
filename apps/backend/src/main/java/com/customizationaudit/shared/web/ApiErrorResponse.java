package com.customizationaudit.shared.web;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String code,
        String message,
        List<ApiErrorDetail> details,
        String correlationId
) {
    public record ApiErrorDetail(String field, String message) {
    }
}
