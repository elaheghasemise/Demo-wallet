package com.leovegas.wallet.service;

import com.leovegas.wallet.dto.TransactionDto;
import com.leovegas.wallet.enmus.TransactionType;
import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Transaction;
import com.leovegas.wallet.entity.Wallet;
import com.leovegas.wallet.exception.WalletException;
import com.leovegas.wallet.repository.TransactionRepository;
import com.leovegas.wallet.utility.ErrorMassage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class TransactionService {

    @Autowired
    TransactionRepository repository;

    @Autowired
    WalletService walletService;

    public List<Transaction> getTransactions(Integer accountNumber) {
        return repository.getTransactions(accountNumber);
    }

    public Transaction createCredit(TransactionDto transactionDto) throws WalletException {
        var wallet = walletService.getWallet(transactionDto.getAccountNumber());
        verifyTransaction(transactionDto.getTransactionId());
        verifyCurrency(wallet, transactionDto);
        var transaction = toTransaction(transactionDto, TransactionType.CREDIT);
        repository.createTransaction(transaction);
        walletService.updateBalance(transaction.getAccountNumber(), transaction.getAmount().getValue());
        return transaction;
    }

    public Transaction createDebit(TransactionDto transactionDto) throws WalletException {
        var wallet = walletService.getWallet(transactionDto.getAccountNumber());
        verifyTransaction(transactionDto.getTransactionId());
        verifyCurrency(wallet, transactionDto);
        verifyBalance(wallet, transactionDto);
        var transaction = toTransaction(transactionDto, TransactionType.DEBIT);
        repository.createTransaction(transaction);
        walletService.updateBalance(transaction.getAccountNumber(), transaction.getAmount().getValue());
        return transaction;
    }

    public void removeAllTransactions() {
        repository.removeTransactions();
    }

    private Transaction toTransaction(TransactionDto transactionDto, TransactionType transactionType) {
        var transaction = new Transaction();
        transaction.setAccountNumber(transactionDto.getAccountNumber());
        transaction.setTransactionId(transactionDto.getTransactionId());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setCreated(LocalDateTime.now());
        transaction.setType(transactionType);
        if (transactionType.equals(TransactionType.DEBIT)) {
            transaction.setAmount(Amount.sek(transactionDto.getAmount().getValue().negate()));
        }
        return transaction;
    }



    private void verifyTransaction(Long transactionId) throws WalletException {
        var transaction = repository.getTransaction(transactionId);
        if (transaction != null) {
            throw new WalletException(String.format(ErrorMassage.DUPLICATE_TRANSACTION, transactionId));
        }
    }

    private void verifyCurrency(Wallet wallet, TransactionDto transaction) throws WalletException {
        if (!wallet.getBalance().getCurrencyCode().equals(transaction.getAmount().getCurrencyCode())) {
            throw new WalletException(ErrorMassage.INVALID_CURRENCY);
        }
    }

    private void verifyBalance(Wallet wallet, TransactionDto transaction) throws WalletException {
        if (wallet.getBalance().getValue().compareTo(transaction.getAmount().getValue()) < 0) {
            throw new WalletException(ErrorMassage.INSUFFICIENT_BALANCE);
        }
    }
}
