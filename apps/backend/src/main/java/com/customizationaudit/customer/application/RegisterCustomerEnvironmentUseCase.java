package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.CustomerEnvironment;

public interface RegisterCustomerEnvironmentUseCase {

    CustomerEnvironment register(RegisterCustomerEnvironmentCommand command);
}
