package com.leovegas.wallet.utility;

import com.leovegas.wallet.dto.TransactionDto;
import com.leovegas.wallet.exception.WalletException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Component
public class Validator {

    public void validateInputData(@NotNull TransactionDto transactionDto) throws WalletException {
        if (transactionDto.getTransactionId() == null || transactionDto.getAccountNumber() == null || transactionDto.getAmount() == null) {
            throw new WalletException(ErrorMassage.INVALID_INPUT_DATA);
        }
    }
}
