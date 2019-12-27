package org.simplebank;

import org.apache.log4j.Logger;
import org.simplebank.controller.MybankApi;
import org.simplebank.controller.impl.MybankApiImpl;
import org.simplebank.repository.impl.HibernateH2SessionFactory;
import spark.Spark;

public class Application {
    private static final Logger log = Logger.getLogger(HibernateH2SessionFactory.class);

    public static void main(String[] args) {
        Application application = new Application();
        MybankApi mybankApi = new MybankApiImpl();
        application.lunchMybank(mybankApi);
    }

    private void lunchMybank(MybankApi mybankApi) {
        try {
            HibernateH2SessionFactory.populateData();
            mybankApi.getBalances();
            mybankApi.transferMoney();
            mybankApi.getAllCustomers();
        } catch (Exception ex) {
            log.error("\n\nProblem lunching Mybank application. More info : ", ex.getCause());
            Spark.stop();
            System.exit(100);
        }
    }
}
