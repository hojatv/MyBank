package org.simplebank.repository.impl;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.domain.TransferDetail;
import org.simplebank.exception.UserException;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.simplebank.domain.Currency.*;
import static org.simplebank.domain.Status.ERROR;
import static org.simplebank.domain.Status.SUCCESS;

@RunWith(MockitoJUnitRunner.class)
public class BalanceRepositoryImplTest {

    private static final Integer SOURCE_ACCOUNT_ID = 1001;
    private static final Integer DESTINATION_ACCOUNT_ID = 1002;
    private static final Long CUSTOMER1TO_CUSTOMER2_ETAG = 82734923487L;
    private static final Logger log = Logger.getLogger(BalanceRepositoryImplTest.class);

    @InjectMocks
    BalanceRepositoryImpl balanceRepository;
    private int failedTransaction = 0;
    private int sucessfulltransaction = 0;

    @Before
    public void populateData() throws SQLException {
        HibernateH2SessionFactory.populateData();
    }

    @Test
    public void getBalancesByAccountId() throws UserException {
        List balances = balanceRepository.getBalancesByAccountId(String.valueOf(SOURCE_ACCOUNT_ID));
        assertThat(balances.size(),is(2));

    }

    @Test
    public void successfulTransfer() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(SUCCESS));
    }

    @Test
    public void lowBalanceFails() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setAmount(1000f);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }

    @Test
    public void incorrectBalanceFails() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setCurrency(JPY);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }

    @Test
    public void incorrectETagFails() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setEtag(123123123l);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }

    @Test
    public void incorrectReceiptCurrencyFails() {
        MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();
        moneyTransferDTO.setCurrency(EUR);
        TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);
        assertThat(transferDetail.getStatus(), is(ERROR));
        assertThat(transferDetail.getErrorMessage(), is("Transfer incomplete. Please check accountNumber, balance and eTag."));
    }


    @Test
    public void test() {
        for (int j = 0; j < 100; ++j) {
            concurrentUsers();
        }
        log.info("failedTransaction = " + failedTransaction);
        log.info("sucessfulltransaction = " + sucessfulltransaction);
        assertThat(sucessfulltransaction, is(1));
    }


    public void concurrentUsers() {
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 50; ++i) {
            Runnable runner = new Runnable() {
                public void run() {
                    try {
                        latch.await();
                        testMethod();
                    } catch (InterruptedException ie) {
                        System.out.println("ie = " + ie);

                    }
                }

                private void testMethod() {
                    //GIVEN
                    MoneyTransferDTO moneyTransferDTO = createMoneyTransferDTO();

                    //WHEN
                    TransferDetail transferDetail = balanceRepository.transfer(moneyTransferDTO);

                    //THEN
                    if (transferDetail.getStatus().getStatus().equals(ERROR.getStatus())) {
                        failedTransaction++;
                    }
                    if (transferDetail.getStatus().getStatus().equals(SUCCESS.getStatus())) {
                        sucessfulltransaction++;
                    }


                }
            };
            new Thread(runner, "TestThread" + i).start();
        }
        // all threads are waiting on the latch.
        latch.countDown(); // release the latch
        // all threads are now running concurrently.
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
}