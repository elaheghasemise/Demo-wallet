package com.leovegas.wallet.config;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;


@Configuration
public class MongoConfig {

    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "embeded_db";

    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        var mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp(MONGO_DB_URL);
        var mongoClient = mongo.getObject();
        return new MongoTemplate(mongoClient, MONGO_DB_NAME);
    }
}