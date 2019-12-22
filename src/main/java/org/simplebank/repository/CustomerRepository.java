package org.simplebank.repository;

import org.simplebank.domain.Customer;
import org.simplebank.exception.CustomerException;

import java.util.Collection;

public interface CustomerRepository {

    Integer addCustomer(Customer customer);

    Collection<Customer> getCustomers() throws CustomerException;

    Customer getCustomerById(Integer id);

    Boolean deleteCustomer(Integer id);
}
