package org.simplebank.controller;

import org.simplebank.domain.Customer;
import org.simplebank.exception.UserException;

import java.util.Collection;

public interface MybankApi {
    Collection<Customer> getCustomers() throws UserException;
    void findBalanceForAccountId();
    void transfer();
}
