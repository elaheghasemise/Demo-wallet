package com.leovegas.wallet.entity;

import com.leovegas.wallet.utility.AccountNumberCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Wallet {
    private Integer accountNumber;
    private String user;
    private Amount balance;
    private LocalDateTime created;
    private LocalDateTime lastUpdate;

    public Wallet() {
    }

    public Wallet(String user) {
        this.user = user;
        balance = Amount.zero("SEK");
        created = LocalDateTime.now();
        lastUpdate = LocalDateTime.now();
        accountNumber = AccountNumberCreator.create();
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
