package org.simplebank.repository.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.simplebank.domain.Balance;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.exception.UserException;
import org.simplebank.repository.BalanceRepository;
import org.simplebank.repository.RepositorySessionFactory;

import java.util.List;

import static java.util.Objects.nonNull;

public class BalanceRepositoryImpl implements BalanceRepository {
    private final SessionFactory sessionFactory;
    private static final Logger log = Logger.getLogger(CustomerRepositoryImpl.class);

    public BalanceRepositoryImpl() {
        RepositorySessionFactory hibernateH2SessionFactory = new HibernateH2SessionFactory();
        sessionFactory = hibernateH2SessionFactory.getSessionFactory();
    }

    @Override
    public List getBalancesByAccountId(String accountId) throws UserException {
        Session session = sessionFactory.openSession();
        List balances;
        try {
            balances = session.createQuery("from Balance b where b.account.id = :accountId")
                    .setParameter("accountId", Integer.valueOf(accountId))
                    .list();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UserException("Problem getting Balances for accountId " + accountId + ". More info : " + ex);
        } finally {
            session.close();
        }
        return balances;
    }

    @Override
    public boolean transfer(MoneyTransferDTO moneyTransferDTO) throws UserException {
        Session session = sessionFactory.openSession();
        boolean committed = false;
        try {
            session.beginTransaction();
            Balance source = checkAndFindSender(session, moneyTransferDTO);

            if (nonNull(source)) {
                source.setAmount(source.getAmount() - moneyTransferDTO.getAmount());
                source.setETag(System.nanoTime());
                session.saveOrUpdate(source);

                Balance destination = checkAndFindDestination(session, moneyTransferDTO);

                if (nonNull(destination)) {
                    destination.setAmount(destination.getAmount() + moneyTransferDTO.getAmount());
                    destination.setETag(System.nanoTime());
                    session.saveOrUpdate(destination);
                } else {
                    //TODO shall we allow this?
                    throw new UserException("Receiver has not balance in " + moneyTransferDTO.getCurrency());
                }
            } else {
                throw new UserException("Transfer incomplete. Please check accountNumber, balance and eTag.");
            }
            session.getTransaction().commit();
            committed = true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UserException("Problem transferring money from account " + moneyTransferDTO.getSourceAccountId()
                    + " to account: " + moneyTransferDTO.getDestinationAccountId() + ". More info : " + ex);
        } finally {
            if (!committed) {
                session.getTransaction().rollback();
            }
            session.close();
        }

        return committed;
    }

    private Balance checkAndFindSender(Session session, MoneyTransferDTO moneyTransferDTO) {
        Balance sender = null;

        /* checking following criterias :
         * accountId and currency and etag of balance should match and amount shouldn't be greater than current balance amount */
        Query query = session.createQuery("from Balance b where b.account.id = :accountId and b.currency = :currency" +
                " and b.eTag= :etag and b.amount >= :amount");
        query.setParameter("accountId", moneyTransferDTO.getSourceAccountId());
        query.setParameter("currency", moneyTransferDTO.getCurrency());
        query.setParameter("etag", moneyTransferDTO.getEtag());
        query.setParameter("amount", moneyTransferDTO.getAmount());
        List balanceList = query.list();
        if (balanceList.size() > 0) {
            sender = (Balance) balanceList.get(0);
        }

        return sender;
    }

    private Balance checkAndFindDestination(Session session, MoneyTransferDTO moneyTransferDTO) {
        Balance receiver = null;
        Query receiverBalanceQuery = session.createQuery("from Balance b where b.account.id = :accountId and b.currency = :currency");
        receiverBalanceQuery.setParameter("accountId", moneyTransferDTO.getDestinationAccountId());
        receiverBalanceQuery.setParameter("currency", moneyTransferDTO.getCurrency());
        if (receiverBalanceQuery.list().size() > 0) {
            receiver = (Balance) receiverBalanceQuery.list().get(0);
        }
        return receiver;
    }
}
