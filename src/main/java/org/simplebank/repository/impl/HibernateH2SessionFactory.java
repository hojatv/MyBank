package org.simplebank.repository.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.h2.tools.RunScript;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.simplebank.domain.Account;
import org.simplebank.domain.Balance;
import org.simplebank.domain.Customer;
import org.simplebank.repository.RepositorySessionFactory;

import static org.simplebank.util.Configs.getProperty;

public class HibernateH2SessionFactory implements RepositorySessionFactory {
    private static final Logger log = Logger.getLogger(HibernateH2SessionFactory.class);
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

    public static void populateData() throws SQLException {
        log.info("Populating Test User Table and data ..... ");
        try (Connection conn = DriverManager.getConnection(getProperty("url"), "", "")) {
            String filePath = new File("").getAbsolutePath();
            String SQL_FILE_PATH = "/src/main/resources/demo.sql";
            RunScript.execute(conn, new FileReader(filePath + SQL_FILE_PATH));
        } catch (SQLException e) {
            log.error("Error populating user data: ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            log.error("Error finding test script file ", e);
            throw new RuntimeException(e);
        }

    }
}