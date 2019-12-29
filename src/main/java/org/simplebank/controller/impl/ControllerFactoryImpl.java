package org.simplebank.controller.impl;

import org.simplebank.controller.ControllerFactory;
import org.simplebank.controller.MybankApi;

public class ControllerFactoryImpl implements ControllerFactory {
    @Override
    public MybankApi makeMyBank() {
        return new MybankApiImpl();
    }
}
