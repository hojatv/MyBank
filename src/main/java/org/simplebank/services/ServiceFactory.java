package org.simplebank.services;

public interface ServiceFactory {
    MoneyService makeMoneyService();
    CustomerService makeCustomerService();
}
