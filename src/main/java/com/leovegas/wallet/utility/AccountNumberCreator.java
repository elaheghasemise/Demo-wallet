package com.leovegas.wallet.utility;

import java.security.SecureRandom;

public class AccountNumberCreator {
    private static final  SecureRandom RAND = new SecureRandom();

    private AccountNumberCreator() {
    }

    public static final Integer create() {
        return RAND.nextInt(899999999);
    }
}
