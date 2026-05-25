package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.Customer;

public interface RegisterCustomerUseCase {

    Customer register(RegisterCustomerCommand command);
}
