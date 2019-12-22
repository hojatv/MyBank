package org.simplebank.repository;

import org.hibernate.SessionFactory;

public interface RepositorySessionFactory {
    public SessionFactory getSessionFactory();
    public void populateData();
}
