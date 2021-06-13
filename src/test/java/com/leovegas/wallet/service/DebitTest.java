package com.leovegas.wallet.service;

import com.leovegas.wallet.dto.TransactionDto;
import com.leovegas.wallet.enmus.TransactionType;
import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Transaction;
import com.leovegas.wallet.entity.Wallet;
import com.leovegas.wallet.exception.WalletException;
import com.leovegas.wallet.utility.ErrorMassage;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DebitTest {
    @Autowired
    TransactionService unitUnderTest;

    @Autowired
    WalletService walletService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreditTransaction() throws WalletException {
        walletService.removeWallets();
        unitUnderTest.removeAllTransactions();
        Wallet wallet = walletService.createWallet("LeoVegas");
        Assert.assertNotNull(wallet);
        Assert.assertEquals(Amount.zero("SEK"), wallet.getBalance());

        TransactionDto creditTrans = createTransaction(wallet.getAccountNumber(), Amount.sek(100.0), 1L);
        unitUnderTest.createCredit(creditTrans);
        Assert.assertEquals(Amount.sek(100.0), getBalance(wallet.getAccountNumber()));

        TransactionDto debitTrans = createTransaction(wallet.getAccountNumber(), Amount.sek(20.0), 2L);
        unitUnderTest.createDebit(debitTrans);

        Assert.assertEquals(Amount.sek(80.0), getBalance(wallet.getAccountNumber()));

        List<Transaction> transactionList = unitUnderTest.getTransactions(wallet.getAccountNumber());
        Assert.assertEquals(2, transactionList.size());
    }

    @Test
    public void testCreditTransactionWithInsufficientBalance() throws WalletException {
        walletService.removeWallets();
        unitUnderTest.removeAllTransactions();
        Wallet wallet = walletService.createWallet("LeoVegas");
        Assert.assertNotNull(wallet);
        Assert.assertEquals(Amount.zero("SEK"), wallet.getBalance());

        TransactionDto creditTrans = createTransaction(wallet.getAccountNumber(), Amount.sek(100.0), 1L);
        unitUnderTest.createCredit(creditTrans);

        TransactionDto debitTrans = createTransaction(wallet.getAccountNumber(), Amount.sek(110.0), 2L);
        expectedException.expect(WalletException.class);
        unitUnderTest.createDebit(debitTrans);

        expectedException.expectMessage(ErrorMassage.INSUFFICIENT_BALANCE);
    }

    private TransactionDto createTransaction(Integer accountNumber, Amount amount, Long transactionId) {
        TransactionDto transaction = new TransactionDto();
        transaction.setAmount(amount);
        transaction.setDescription("description");
        transaction.setTransactionId(transactionId);
        transaction.setAccountNumber(accountNumber);
        return transaction;
    }

    private Amount getBalance(Integer accountNumber) throws WalletException {
        Wallet wallet = walletService.getWallet(accountNumber);
        return wallet.getBalance();
    }

}
