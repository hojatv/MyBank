package org.simplebank.repository.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.simplebank.domain.Customer;
import org.simplebank.exception.CustomerException;
import org.simplebank.common.HibernateUtil;
import org.simplebank.repository.CustomerRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    private final SessionFactory sessionFactory;
    private static final Logger log = Logger.getLogger(CustomerRepositoryImpl.class);

    public CustomerRepositoryImpl() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Collection<Customer> getCustomers() throws CustomerException {
        Session session = sessionFactory.openSession();
        List<Customer> customers = new ArrayList<>();
        try {
            customers = session.createQuery("FROM Customer").list();

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CustomerException("Problem getting all customers info");
        } finally {
            session.close();
        }
        return customers;
    }

    @Override
    public Integer addCustomer(Customer customer) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public Customer getCustomerById(Integer id) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public Boolean deleteCustomer(Integer id) {
        //TODO
        throw new NotImplementedException();
    }
}
