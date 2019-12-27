package org.simplebank.repository;

import org.hibernate.SessionFactory;

public interface RepositorySessionFactory {
    SessionFactory getSessionFactory();
}
