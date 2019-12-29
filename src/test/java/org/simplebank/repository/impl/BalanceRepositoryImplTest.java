package org.simplebank.repository.impl;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.domain.TransferDetail;
import org.simplebank.exception.UserException;
import org.simplebank.util.DataSetup;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.simplebank.domain.Currency.*;
import static org.simplebank.domain.Status.ERROR;
import static org.simplebank.domain.Status.SUCCESS;
import static org.simplebank.util.DataSetup.clearDataSet;

@RunWith(MockitoJUnitRunner.class)
public class BalanceRepositoryImplTest {

    private static final Integer SOURCE_ACCOUNT_ID = 1001;
    private static final Integer DESTINATION_ACCOUNT_ID = 1002;
    private static final Long CUSTOMER1TO_CUSTOMER2_ETAG = 82734923487L;
    private static final Logger log = Logger.getLogger(BalanceRepositoryImplTest.class);

    @InjectMocks
    BalanceRepositoryImpl balanceRepository;
    private int successfulTransaction = 0;

    @Before
    public void populateData() {
        DataSetup.populateData();
    }

    @Test
    public void getBalancesByAccountId() throws UserException {
        List balances = balanceRepository.getBalancesByAccountId(String.valueOf(SOURCE_ACCOUNT_ID));
        assertThat(balances.size(), is(2));

    }

    @Test
    public void testSuccessfulTransfer() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(SUCCESS));
    }

    @Test
    public void testLowBalanceTransactionShouldFail() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setAmount(1000f);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }

    @Test
    public void testIncorrectBalanceShouldFail() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setCurrency(JPY);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }

    @Test
    public void testIncorrectETagShouldFail() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setEtag(123123123L);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }

    @Test
    public void testIncorrectReceiptCurrencyShouldFail() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setCurrency(EUR);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }


    @Test
    public void testOnlyOneTransactionSuccessWhenRaceCondition() {
        for (int j = 0; j < 100; ++j) {
            concurrentUsers();
        }
        log.info("successfulTransaction = " + successfulTransaction);
        assertThat(successfulTransaction, is(1));
    }


    public void concurrentUsers() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 50; ++i) {
            Runnable runner = new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        testMethod();
                    } catch (InterruptedException ie) {
                        log.error("Exception when testing concurrentUsers. More info: " + ie);
                    }
                }

                private void testMethod() {
                    MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
                    TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
                    if (transferDetail.getStatus().getStatus().equals(SUCCESS.getStatus())) {
                        successfulTransaction++;
                    }
                }
            };
            new Thread(runner, "TestThread" + i).start();
        }
        countDownLatch.countDown();
    }


    private MoneyTransferDTO createMoneyTransferDTO() {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(10F);
        moneyTransferDTO.setCurrency(GBP);
        moneyTransferDTO.setSourceAccountId(SOURCE_ACCOUNT_ID);
        moneyTransferDTO.setDestinationAccountId(DESTINATION_ACCOUNT_ID);
        moneyTransferDTO.setEtag(CUSTOMER1TO_CUSTOMER2_ETAG);
        return moneyTransferDTO;
    }

    @After
    public void cleanUp(){
        clearDataSet();
    }
}