package org.simplebank.services;

import org.simplebank.domain.Balance;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.domain.TransferDetail;
import org.simplebank.exception.UserException;

import java.util.List;

public interface MoneyService {
    TransferDetail transfer(MoneyTransferDTO moneyTransferDTO) throws UserException;

    List<Balance> getBalancesByAccountId(String accountId) throws UserException;
}
