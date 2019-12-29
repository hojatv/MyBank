package org.simplebank.services.impl;

import org.simplebank.services.CustomerService;
import org.simplebank.services.MoneyService;
import org.simplebank.services.ServiceFactory;

public class ServiceFactoryImpl implements ServiceFactory {
    @Override
    public MoneyService makeMoneyService() {
        return new MoneyServiceImpl();
    }

    @Override
    public CustomerService makeCustomerService() {
        return new CustomerServiceImpl();
    }
}
