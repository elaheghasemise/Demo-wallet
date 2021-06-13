package com.leovegas.wallet.repository;

import com.leovegas.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class WalletRepository {

    @Autowired
    MongoTemplate template;

    public Wallet create(String user) {
        var wallet = new Wallet(user);
        template.save(wallet);
        return wallet;
    }

    public Wallet find(Integer accountNumber) {
        var query = new Query();
        query.addCriteria(where("accountNumber").is(accountNumber));
        return template.findOne(query, Wallet.class);
    }

    public List<Wallet> findAll() {
        return template.findAll(Wallet.class);
    }

    public void removeAll() {
        template.remove(new Query(), Wallet.class);
    }


}
