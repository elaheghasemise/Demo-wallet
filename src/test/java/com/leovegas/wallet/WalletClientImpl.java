package com.leovegas.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leovegas.wallet.dto.TransactionDto;
import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Transaction;
import com.leovegas.wallet.entity.Wallet;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WalletClientImpl implements WalletClient {

    static final Logger logger = Logger.getLogger(WalletClientImpl.class);

    public Optional<Wallet> createWallet(String accountHolder) {
        Wallet wallet = null;
        try {
            HttpRequestBase requestBase = createPostRequest("/wallet/create", new StringEntity(accountHolder));
            var response = send(requestBase);
            if (response == null) {
                return Optional.empty();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            wallet = objectMapper.readValue(response.getEntity().getContent(), Wallet.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(wallet);
    }

    public Optional<Transaction> debit(TransactionDto dto) {
        return createTransaction("debit", dto);
    }

    public Optional<Transaction> credit(TransactionDto dto) {
        return createTransaction("credit", dto);
    }

    public Optional<Wallet[]> getWallets() {
        Wallet[] wallet = null;
        try {
            HttpGet httpGet = createGetRequest("/wallets");
            var response = send(httpGet);
            if (response == null) {
                return Optional.empty();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            wallet = objectMapper.readValue(response.getEntity().getContent(), Wallet[].class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(wallet);
    }

    public List<Transaction> history(Integer accountNumber) {
        Transaction[] transactions = null;
        try {
            HttpGet httpGet = createGetRequest("/list/transaction/" + accountNumber);
            var response = send(httpGet);
            if (response == null) {
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            transactions = objectMapper.readValue(response.getEntity().getContent(), Transaction[].class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Arrays.asList(transactions);
    }

    public Optional<Wallet> getWallet(Integer accountNumber) {
        Wallet wallet = null;
        try {
            String path = "/wallet/" + accountNumber;
            HttpGet httpGet = createGetRequest(path);
            var response = send(httpGet);
            if (response == null) {
                return Optional.empty();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            wallet = objectMapper.readValue(response.getEntity().getContent(), Wallet.class);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(wallet);
    }

    public Optional<Amount> getBalance(Integer accountNumber) {
        Amount amount = null;
        try {
            String path = "/wallet/balance/" + accountNumber;
            HttpGet httpGet = createGetRequest(path);
            var response = send(httpGet);
            if (response == null) {
                return Optional.empty();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            amount = objectMapper.readValue(response.getEntity().getContent(), Amount.class);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(amount);
    }

    public Optional<Wallet> deleteAllWallets() {
        Wallet wallet = null;

        String url = "http://localhost:8080/wallet/delete/all";
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader("Content-Type", "application/json");
        var response = send(httpDelete);
        if (response == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(wallet);
    }

    public void deleteAllTransactions() {
        String url = "http://localhost:8080/transaction/delete/all";
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader("Content-Type", "application/json");
        var response = send(httpDelete);
    }

    private CloseableHttpResponse send(HttpRequestBase requestBase) {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(requestBase);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return response;
    }

    private HttpGet createGetRequest(String path) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http").setHost("localhost:8080").setPath(path);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Content-Type", "application/json");
        return httpGet;
    }

    private HttpPost createPostRequest(String path, HttpEntity httpEntity) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http").setHost("localhost:8080").setPath(path);
        URI uri = uriBuilder.build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(httpEntity);
        httpPost.setHeader("Content-Type", "application/json");
        return httpPost;
    }

    private Optional<Transaction> createTransaction(String transactionType, TransactionDto dto) {
        Transaction transaction = null;
        try {
            String jsonContent = new ObjectMapper().writeValueAsString(dto);
            HttpPost httpPost = createPostRequest("/" + transactionType + "/transaction", new StringEntity(jsonContent));
            var response = send(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                transaction = objectMapper.readValue(response.getEntity().getContent(), Transaction.class);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(transaction);
    }

}
