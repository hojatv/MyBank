package org.simplebank.repository;

import org.simplebank.domain.Customer;
import org.simplebank.exception.UserException;

import java.util.Collection;

public interface CustomerRepository {
    Collection<Customer> getCustomers() throws UserException;
}
