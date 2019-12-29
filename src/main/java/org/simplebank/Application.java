package org.simplebank;

import org.apache.log4j.Logger;
import org.simplebank.controller.ControllerFactory;
import org.simplebank.controller.MybankApi;
import org.simplebank.controller.impl.ControllerFactoryImpl;
import org.simplebank.util.DataSetup;
import spark.Spark;

public class Application {
    private static final Logger log = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        Application application = new Application();
        ControllerFactory controllerFactory = new ControllerFactoryImpl();
        MybankApi mybankApi = controllerFactory.makeMyBank();
        application.lunchMybank(mybankApi);
    }

    private void lunchMybank(MybankApi mybankApi) {
        try {
            DataSetup.populateData();
            setupEndpoints(mybankApi);
        } catch (Exception ex) {
            log.error("\n\nProblem lunching Mybank application. More info : ", ex.getCause());
            Spark.stop();
            System.exit(100);
        }
    }

    private void setupEndpoints(MybankApi mybankApi) {
        mybankApi.findBalanceForAccountId();
        mybankApi.transfer();
        mybankApi.getCustomers();
    }
}
