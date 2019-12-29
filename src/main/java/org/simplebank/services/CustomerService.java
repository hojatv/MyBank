package org.simplebank.services;

import org.simplebank.domain.Customer;
import org.simplebank.exception.UserException;

import java.util.Collection;

public interface CustomerService {
    Collection<Customer> getCustomers() throws UserException;
}
