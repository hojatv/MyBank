package org.simplebank.repository;

import org.simplebank.domain.Balance;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.domain.TransferDetail;
import org.simplebank.exception.UserException;

import java.util.List;

public interface BalanceRepository {
    List<Balance> getBalancesByAccountId(String accountId) throws UserException;

    TransferDetail transfer(MoneyTransferDTO moneyTransferDTO);
}
