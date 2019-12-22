package org.simplebank.repository;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.simplebank.domain.Account;
import org.simplebank.domain.Balance;
import org.simplebank.domain.Customer;

import static org.simplebank.common.Configs.getProperty;

public class HibernateH2SessionFactory implements RepositorySessionFactory {
    private static Logger log = Logger.getLogger(HibernateH2SessionFactory.class);
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
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
            } catch (Exception ex) {
                log.error("Problem while getting session. more info {}", ex);
            }
        }
        return sessionFactory;
    }

    @Override
    public void populateData() {

    }
}