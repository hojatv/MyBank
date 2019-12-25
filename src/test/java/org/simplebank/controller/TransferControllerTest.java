package org.simplebank.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.junit.*;
import org.simplebank.domain.Account;
import org.simplebank.domain.Balance;
import org.simplebank.domain.Customer;

import java.util.List;
import java.util.Properties;

import static org.simplebank.domain.Currency.GBP;
import static org.simplebank.util.Configs.getProperty;
import static org.simplebank.domain.Currency.EUR;


public class TransferControllerTest {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeClass
    public static void beforeTests() {
        Properties settings = new Properties();

        Configuration configuration = new Configuration();
        settings.put(Environment.DRIVER, getProperty("driver"));
        settings.put(Environment.URL, getProperty("url"));
        settings.put(Environment.DIALECT, getProperty("dialect"));
        settings.put(Environment.SHOW_SQL, getProperty("show_sql"));
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, getProperty("current_session_content_class"));
        settings.put(Environment.HBM2DDL_AUTO, getProperty("hbm2ddl_auto"));
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Account.class);
        configuration.addAnnotatedClass(Balance.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Before
    public void setUp() {
        session = sessionFactory.openSession();
        //session.beginTransaction();
    }

    @Test
    public void test(){
        session.beginTransaction();
        Account account = new Account();
        account.setAssignedIBAN("IBAN");
        account.setSourceIBAN("SOURCE_IBAN");

        Balance balance1 = new Balance();
        balance1.setAmount(10.F);
        balance1.setCurrency(EUR);
        balance1.setETag(System.nanoTime());


        Customer customer = new Customer();
        customer.setName("Reza");
        customer.setPhone("+49152211221");
        session.saveOrUpdate(customer);
        account.setCustomer(customer);
        session.saveOrUpdate(account);

        balance1.setAccount(account);
        session.saveOrUpdate(balance1);

        Balance balance2 = new Balance();
        balance2.setAmount(10.F);
        balance2.setCurrency(GBP);
        balance2.setETag(System.nanoTime());
        balance2.setAccount(account);
        session.saveOrUpdate(balance2);

        session.getTransaction().commit();

        List customers = session.createQuery("FROM Customer").list();
        List Accounts = session.createQuery("FROM Account").list();
        List Balances = session.createQuery("FROM Balance").list();


        System.out.println("customer = " + customer);



    }



    @After
    public void tearDown() {
        //session.getTransaction().commit();
        session.close();
    }

    @AfterClass
    public static void afterTests() {
        sessionFactory.close();
    }
}