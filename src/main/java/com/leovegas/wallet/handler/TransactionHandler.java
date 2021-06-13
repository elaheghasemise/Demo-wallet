package com.leovegas.wallet.handler;

import com.leovegas.wallet.dto.TransactionDto;
import com.leovegas.wallet.entity.Transaction;
import com.leovegas.wallet.exception.WalletException;
import com.leovegas.wallet.service.TransactionService;
import com.leovegas.wallet.utility.Validator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class TransactionHandler {

    Log log = LogFactory.getLog(TransactionHandler.class);

    @Autowired
    TransactionService transactionService;

    @Autowired
    Validator validator;

    @PostMapping(value = "/debit/transaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Transaction createDebitTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = null;
        try {
            validator.validateInputData(transactionDto);
            transaction = transactionService.createDebit(transactionDto);
        } catch (WalletException e) {
            log.error("Failed to create debit transaction :" + e.getMessage());
        }
        return transaction;
    }

    @PostMapping(value = "/credit/transaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Transaction createCreditTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = null;
        try {
            validator.validateInputData(transactionDto);
            transaction = transactionService.createCredit(transactionDto);
        } catch (WalletException e) {
            log.error("Failed to create credit transaction :{}" + e.getMessage());
        }
        return transaction;
    }

    @GetMapping(value = "/list/transaction/{accountNumber}", consumes = APPLICATION_JSON_VALUE)
    public List<Transaction> transactionHistory(@PathVariable Integer accountNumber) {
        return transactionService.getTransactions(accountNumber);
    }

    @DeleteMapping(value = "/transaction/delete/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void removeAllTransactions() {
        transactionService.removeAllTransactions();
    }

}
