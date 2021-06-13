package com.leovegas.wallet.service;

import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Wallet;
import com.leovegas.wallet.exception.WalletException;
import com.leovegas.wallet.repository.WalletRepository;
import com.leovegas.wallet.utility.ErrorMassage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class WalletService {

    @Autowired
    WalletRepository repository;

    @Autowired
    MongoOperations operations;

    public Wallet createWallet(String user) {
        return repository.create(user);
    }

    public Wallet getWallet(Integer accountNumber) throws WalletException {
        var wallet = repository.find(accountNumber);
        if (wallet == null) {
            throw new WalletException(String.format(ErrorMassage.WALLET_NOT_FOUND, accountNumber));
        }
        return wallet;
    }

    public List<Wallet> getWallets() {
        return repository.findAll();
    }

    public synchronized void updateBalance(@NotNull Integer accountNumber, @NotNull BigDecimal transactionAmount) throws WalletException {
        var wallet = getWallet(accountNumber);
        BigDecimal newBalance = wallet.getBalance().getValue().add(transactionAmount);
        wallet.setBalance(Amount.sek(newBalance));
        operations.updateFirst(query(where("accountNumber").is(accountNumber)), Update.update("balance", Amount.sek(newBalance)), Wallet.class);
    }

    public void removeWallets() {
        repository.removeAll();
    }

}
