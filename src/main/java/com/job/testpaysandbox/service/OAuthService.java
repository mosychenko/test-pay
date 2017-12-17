package com.job.testpaysandbox.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.job.testpaysandbox.model.*;
import com.job.testpaysandbox.storage.ClientStore;
import com.job.testpaysandbox.storage.ScopeStore;
import com.job.testpaysandbox.storage.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by max on 12/14/17.
 */
@Service
public class OAuthService {

    private static final int EXPIRE_TOKEN_SEC = 32398;

    @Autowired
    private ClientStore clientStore;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ScopeStore scopeStore;

    private Gson gson;

    @PostConstruct
    public void init() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();
    }

    public AuthResult createToken(String clientId,
                                  String password,
                                  String hostUrl) {
        Token token = tokenStore.findByClientId(clientId);
        if (token != null && !token.isExpired()) {
            return new AuthResult(AuthStatus.SUCCESS, gson.toJson(token));
        }
        Client client = clientStore.getClientByClientId(clientId);
        if (client != null && client.password.equals(password)) {
            Token newToken = createNewToken(client, hostUrl);
            tokenStore.save(clientId, newToken);
            return new AuthResult(AuthStatus.SUCCESS, gson.toJson(newToken));
        }
        return new AuthResult(AuthStatus.USER_NOT_FOUND);
    }

    private Token createNewToken(Client client, String hostUrl) {
        String tokenValue = Utils.generateRandomString(OAuthUtils.TOKEN_LENGTH, false);
        String scope = scopeStore.getScopeForRole(client.role);
        return Token.builder()
                .scope(hostUrl + scope)
                .value(tokenValue)
                .type(TokenType.BEARER.getName())
                .expiresInSec(EXPIRE_TOKEN_SEC)
                .build();
    }
}
