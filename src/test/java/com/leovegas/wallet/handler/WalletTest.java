package com.leovegas.wallet.handler;

import com.leovegas.wallet.WalletClientImpl;
import com.leovegas.wallet.dto.TransactionDto;
import com.leovegas.wallet.enmus.TransactionType;
import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Transaction;
import com.leovegas.wallet.entity.Wallet;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/*
 First run DemoWallet to run the handler tests
*/
public class WalletTest {

    WalletClientImpl client = new WalletClientImpl();

    @Test
    public void createAndGetWallets() {
        client.deleteAllWallets();
        Optional<Wallet> newWallet = client.createWallet("Test User");
        Assert.assertTrue(newWallet.isPresent());

        Optional<Wallet[]> optionalWallets = client.getWallets();
        Assert.assertTrue(optionalWallets.isPresent());
        Assert.assertEquals(1, optionalWallets.get().length);

        Optional<Wallet> optWallet = client.getWallet(newWallet.get().getAccountNumber());
        Assert.assertTrue(optWallet.isPresent());
    }

    @Test
    public void getWallets() {
        client.deleteAllWallets();
        Optional<Wallet> newWallet = client.createWallet("Test User");
        Assert.assertTrue(newWallet.isPresent());

        Optional<Wallet> optWallet = client.getWallet(newWallet.get().getAccountNumber());
        Assert.assertTrue(optWallet.isPresent());
    }

    @Test
    public void createDebitCreditTransactions() {
        client.deleteAllWallets();
        client.deleteAllTransactions();
        Optional<Wallet> wallet = client.createWallet("Test User");
        Assert.assertTrue(wallet.isPresent());
        Integer accountNumber = wallet.get().getAccountNumber();

        Optional<Transaction> creditTrans = client.credit(create10SekTransaction(accountNumber, 20L));
        Assert.assertTrue(creditTrans.isPresent());
        Assert.assertEquals(TransactionType.CREDIT, creditTrans.get().getType());

        Optional<Amount> optAmount = client.getBalance(accountNumber);
        Assert.assertTrue(optAmount.isPresent());

        Optional<Transaction> debitTrans = client.debit(create10SekTransaction(accountNumber, 21L));
        Assert.assertTrue(debitTrans.isPresent());
        Assert.assertEquals(TransactionType.DEBIT, debitTrans.get().getType());

        wallet = client.getWallet(accountNumber);
        Assert.assertEquals(BigDecimal.valueOf(0.0), wallet.get().getBalance().getValue());

        List<Transaction> transactionList = client.history(accountNumber);
        Assert.assertEquals(2, transactionList.size());
    }

    private TransactionDto create10SekTransaction(Integer accountNumber, Long transactionId) {
        TransactionDto dto = new TransactionDto();
        dto.setAmount(Amount.sek(10d));
        dto.setTransactionId(transactionId);
        dto.setAccountNumber(accountNumber);
        return dto;
    }
}

