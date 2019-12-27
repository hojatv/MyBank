package org.simplebank.services;

import org.simplebank.domain.Customer;
import org.simplebank.exception.UserException;
import org.simplebank.repository.CustomerRepository;
import org.simplebank.repository.impl.CustomerRepositoryImpl;

import java.util.Collection;

public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(){
        customerRepository = new CustomerRepositoryImpl();
    }

    public void addCustomer(Customer customer) {
        customerRepository.addCustomer(customer);
    }

    public Collection<Customer> getCustomers() throws UserException {
        return customerRepository.getCustomers();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.getCustomerById(id);
    }


}
