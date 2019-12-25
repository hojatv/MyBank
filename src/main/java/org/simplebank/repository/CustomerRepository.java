package org.simplebank.repository;

import org.simplebank.domain.Customer;
import org.simplebank.exception.UserException;

import java.util.Collection;

public interface CustomerRepository {

    Integer addCustomer(Customer customer);

    Collection<Customer> getCustomers() throws UserException;

    Customer getCustomerById(Integer id);

    Boolean deleteCustomer(Integer id);
}
