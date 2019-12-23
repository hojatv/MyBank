package org.simplebank.repository;

import org.simplebank.domain.Balance;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.exception.UserException;

import java.util.List;

public interface BalanceRepository {
    public List<Balance> getBalancesByAccountId(String accountId) throws UserException;

    boolean transfer(MoneyTransferDTO moneyTransferDTO) throws UserException;
}
