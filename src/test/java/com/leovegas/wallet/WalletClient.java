package com.leovegas.wallet;

import com.leovegas.wallet.dto.TransactionDto;
import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Transaction;
import com.leovegas.wallet.entity.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletClient {

    Optional<Wallet> createWallet(String accountHolder);

    Optional<Wallet[]> getWallets();

    Optional<Wallet> getWallet(Integer accountNumber);

    Optional<Amount> getBalance(Integer accountNumber);

    Optional<Transaction> debit(TransactionDto dto);

    Optional<Transaction> credit(TransactionDto dto);

    List<Transaction> history(Integer accountNumber);

    Optional<Wallet> deleteAllWallets();

    void deleteAllTransactions();


}
