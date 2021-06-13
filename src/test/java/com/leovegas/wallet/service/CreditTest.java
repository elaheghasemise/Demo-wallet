package com.leovegas.wallet.service;

import com.leovegas.wallet.dto.TransactionDto;
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
public class CreditTest {
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

        TransactionDto transaction = createTransaction(wallet.getAccountNumber(), Amount.sek(100.0));
        unitUnderTest.createCredit(transaction);

        List<Transaction> transactionList = unitUnderTest.getTransactions(wallet.getAccountNumber());
        Assert.assertFalse(transactionList.isEmpty());
        Assert.assertEquals(1, transactionList.size());

        wallet = walletService.getWallet(wallet.getAccountNumber());
        Assert.assertEquals(1, walletService.getWallets().size());
        Assert.assertEquals(Amount.sek(100.0), wallet.getBalance());
    }

    @Test
    public void testCreditTransactionWithDifferentCurrency() throws WalletException {
        walletService.removeWallets();
        Wallet wallet = walletService.createWallet("LeoVegas");
        Assert.assertNotNull(wallet);
        Assert.assertEquals(Amount.zero("SEK"), wallet.getBalance());

        TransactionDto transactionDto = createTransaction(wallet.getAccountNumber(), Amount.of("USD", 100.0));
        expectedException.expect(WalletException.class);
        unitUnderTest.createCredit(transactionDto);

        expectedException.expectMessage(ErrorMassage.INVALID_CURRENCY);
    }

    @Test
    public void testCreditTransactionWithDuplicateTransactionId() throws WalletException {
        walletService.removeWallets();
        Wallet wallet = walletService.createWallet("LeoVegas");
        Assert.assertNotNull(wallet);
        Assert.assertEquals(Amount.zero("SEK"), wallet.getBalance());

        TransactionDto transactionDto = createTransaction(wallet.getAccountNumber(), Amount.sek(100.0));
        unitUnderTest.createCredit(transactionDto);

        transactionDto = createTransaction(wallet.getAccountNumber(), Amount.sek(100.0));
        expectedException.expect(WalletException.class);
        unitUnderTest.createCredit(transactionDto);

        expectedException.expectMessage(String.format(ErrorMassage.DUPLICATE_TRANSACTION, 1));
    }

    private TransactionDto createTransaction(Integer accountNumber, Amount amount) {
        TransactionDto dto = new TransactionDto();
        dto.setAmount(amount);
        dto.setDescription("100 sek");
        dto.setTransactionId(1L);
        dto.setAccountNumber(accountNumber);
        return dto;
    }
}
