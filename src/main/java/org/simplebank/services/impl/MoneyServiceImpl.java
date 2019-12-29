package org.simplebank.services.impl;

import org.simplebank.domain.Balance;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.domain.TransferDetail;
import org.simplebank.exception.UserException;
import org.simplebank.repository.BalanceRepository;
import org.simplebank.repository.impl.BalanceRepositoryImpl;
import org.simplebank.services.MoneyService;

import java.util.List;

public class MoneyServiceImpl implements MoneyService {
    private final BalanceRepository balanceRepository;

    public MoneyServiceImpl() {
        balanceRepository = new BalanceRepositoryImpl();
    }

    @Override
    public TransferDetail transfer(MoneyTransferDTO moneyTransferDTO) throws UserException {
        return balanceRepository.transfer(moneyTransferDTO);
    }

    @Override
    public List<Balance> getBalancesByAccountId(String accountId) throws UserException {
        return balanceRepository.getBalancesByAccountId(accountId);
    }
}
