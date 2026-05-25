package com.customizationaudit.customer.application;

import com.customizationaudit.customer.domain.Customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListCustomersQuery {

    private final CustomerRepository customerRepository;

    public ListCustomersQuery(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<Customer> list() {
        return customerRepository.findAll();
    }
}
