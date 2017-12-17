package com.job.testpaysandbox.storage;

import com.job.testpaysandbox.model.Token;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenStore {

    private final Map<String, Token> tokenByClientId = new HashMap<>();

    public synchronized void save(String clientId, Token newToken) {
        tokenByClientId.putIfAbsent(clientId, newToken);
    }

    public synchronized Token findByClientId(String clientId) {
        return tokenByClientId.get(clientId);
    }

    public synchronized Token findByTokenValue(String tokenValue) {
        for(Token token: tokenByClientId.values()) {
            if (token.value.equals(tokenValue)) {
                return token;
            }
        }
        return null;
    }
}
