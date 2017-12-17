package com.job.testpaysandbox.model;

import java.math.BigDecimal;

/**
 * Created by max on 12/17/17.
 */
public class Account {
    public final String currency;
    public final BigDecimal totalMoney;
    public final String secret;

    public Account(String currency, BigDecimal totalMoney, String secret) {
        this.currency = currency;
        this.totalMoney = totalMoney;
        this.secret = secret;
    }
}
