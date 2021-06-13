package com.leovegas.wallet.repository;

import com.leovegas.wallet.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {

    @Autowired
    MongoTemplate template;

    public Transaction getTransaction(Long transactionId) {
        var query = new Query();
        query.addCriteria(Criteria.where("transactionId").is(transactionId));
        return template.findOne(query, Transaction.class);
    }

    public List<Transaction> getTransactions(Integer accountNumber) {
        var query = new Query();
        query.addCriteria(Criteria.where("accountNumber").is(accountNumber)).with(new Sort(Sort.Direction.DESC,"created"));
        return template.find(query, Transaction.class);
    }

    public void createTransaction(Transaction transaction) {
        transaction.setId(UUID.randomUUID());
        template.save(transaction);
    }

    public void removeTransactions() {
        template.remove(new Query(), Transaction.class);
    }


}
