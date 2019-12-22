package org.simplebank;

import org.simplebank.controller.MybankApi;
import org.simplebank.controller.impl.MybankApiImpl;

public class Application {
    public static void main(String[] args) {
        Application application = new Application();
        MybankApi mybankApi = new MybankApiImpl();
        application.lunchMybankApiHandlers(mybankApi);
    }
    public void lunchMybankApiHandlers(MybankApi mybankApi){
        mybankApi.balanceHandler();
        mybankApi.getAllCustomers();
    }
}
