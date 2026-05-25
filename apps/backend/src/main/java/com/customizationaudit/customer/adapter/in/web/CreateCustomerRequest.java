package com.customizationaudit.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
        @NotBlank @Size(max = 160) String name,
        @Size(max = 80) String externalReference
) {
}
