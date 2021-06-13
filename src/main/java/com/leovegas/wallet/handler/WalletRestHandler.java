package com.leovegas.wallet.handler;

import com.leovegas.wallet.entity.Amount;
import com.leovegas.wallet.entity.Wallet;
import com.leovegas.wallet.exception.WalletException;
import com.leovegas.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class WalletRestHandler {

    @Autowired
    WalletService walletService;

    @PostMapping(value = "/wallet/create", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wallet createWallet(@RequestBody String accountHolder) {
        return walletService.createWallet(accountHolder);
    }

    @GetMapping(value = "/wallets", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Wallet> getWallets() {
        return walletService.getWallets();
    }

    @GetMapping(value = "/wallet/{accountNumber}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wallet getWallet(@PathVariable Integer accountNumber) throws WalletException {
        return walletService.getWallet(accountNumber);
    }

    @GetMapping(value = "/wallet/balance/{accountNumber}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Amount getBalance(@PathVariable Integer accountNumber) throws WalletException {
        return walletService.getWallet(accountNumber).getBalance();
    }

    @DeleteMapping(value = "/wallet/delete/all", consumes = APPLICATION_JSON_VALUE)
    public void deleteAllWallets() {
        walletService.removeWallets();
    }
}
