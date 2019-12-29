package org.simplebank.services.impl;

import org.simplebank.domain.Customer;
import org.simplebank.exception.UserException;
import org.simplebank.repository.CustomerRepository;
import org.simplebank.repository.impl.CustomerRepositoryImpl;
import org.simplebank.services.CustomerService;

import java.util.Collection;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl() {
        customerRepository = new CustomerRepositoryImpl();
    }

    @Override
    public Collection<Customer> getCustomers() throws UserException {
        return customerRepository.getCustomers();
    }
}
