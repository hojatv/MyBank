package org.simplebank.services;

import org.simplebank.domain.Balance;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.exception.UserException;
import org.simplebank.repository.BalanceRepository;
import org.simplebank.repository.impl.BalanceRepositoryImpl;

import java.util.List;

public class MoneyService {
    private BalanceRepository balanceRepository;

    public MoneyService() {
        balanceRepository = new BalanceRepositoryImpl();
    }

    public boolean transfer(MoneyTransferDTO moneyTransferDTO) throws UserException {
        return balanceRepository.transfer(moneyTransferDTO);
    }

    public List<Balance> getBalancesByAccountId(String accountId) throws UserException {
        return balanceRepository.getBalancesByAccountId(accountId);
    }
}
