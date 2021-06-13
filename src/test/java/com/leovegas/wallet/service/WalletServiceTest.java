package com.leovegas.wallet.service;

import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Wallet;
import com.leovegas.wallet.exception.WalletException;
import com.leovegas.wallet.utility.AccountNumberCreator;
import com.leovegas.wallet.utility.ErrorMassage;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletServiceTest {

    @Autowired
    WalletService unitUnderTest;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreateWallet() {
        String accountHolder = "Test User";

        Wallet wallet = unitUnderTest.createWallet(accountHolder);

        Assert.assertNotNull(wallet);
        Assert.assertNotNull(wallet.getAccountNumber());
        Assert.assertEquals(wallet.getUser(), accountHolder);
        Assert.assertEquals(wallet.getCreated().toLocalDate(), LocalDate.now());
        Assert.assertEquals(wallet.getBalance(), Amount.zero("SEK"));
    }

    @Test
    public void testFindWallet() throws WalletException {
        String user = "Test User";

        Wallet wallet = unitUnderTest.createWallet(user);

        Assert.assertNotNull(wallet);
        Assert.assertNotNull(wallet.getAccountNumber());
        Assert.assertEquals(wallet.getUser(), user);
        Assert.assertEquals(wallet.getCreated().toLocalDate(), LocalDate.now());
        Assert.assertEquals(wallet.getBalance(), Amount.zero("SEK"));

        wallet = unitUnderTest.getWallet(wallet.getAccountNumber());
        Assert.assertEquals(user, wallet.getUser());
    }


    @Test
    public void testGetWithNoneExistingWallet() throws WalletException {
        Integer accountNo = AccountNumberCreator.create();
        expectedException.expect(WalletException.class);

        unitUnderTest.getWallet(accountNo);

        expectedException.expectMessage(String.format(ErrorMassage.WALLET_NOT_FOUND, accountNo));
    }
}
