package com.job.testpaysandbox.storage;

import com.job.testpaysandbox.model.Account;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by max on 12/17/17.
 */
@Component
public class MerchantAccountStore {
    private final Map<String, Account> accountsByEmail = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        accountsByEmail.put("test@example.com", new Account("USD", new BigDecimal(100), "secret_world"));
        accountsByEmail.put("test2@example.com", new Account("RUB", new BigDecimal(200), "test2@example.com"));
    }

    public Account getAccountByEmail(String email) {
        return accountsByEmail.get(email);
    }

    public void updateAccount(String email, Account account) {
        accountsByEmail.put(email, account);
    }
}
