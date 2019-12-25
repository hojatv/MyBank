package org.simplebank.repository;

import org.hibernate.SessionFactory;

import java.sql.SQLException;

public interface RepositorySessionFactory {
    public SessionFactory getSessionFactory();
}
