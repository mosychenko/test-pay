package com.job.testpaysandbox.service;

import com.job.testpaysandbox.model.*;
import com.job.testpaysandbox.storage.ClientStore;
import com.job.testpaysandbox.storage.ScopeStore;
import com.job.testpaysandbox.storage.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public AuthResult createToken(String clientId,
                                  String password,
                                  String hostUrl) {
        Token token = tokenStore.findByClientId(clientId);
        if (token != null && !token.isExpired()) {
            return new AuthResult(AuthStatus.SUCCESS, token);
        }
        Client client = clientStore.getClientByClientId(clientId);
        if (client != null && client.password.equals(password)) {
            Token newToken = createNewToken(client, hostUrl);
            tokenStore.save(clientId, newToken);
            return new AuthResult(AuthStatus.SUCCESS, newToken);
        }
        return new AuthResult(AuthStatus.USER_NOT_FOUND);
    }

    private Token createNewToken(Client client, String hostUrl) {
        String tokenValue = OAuthUtils.generateRandomToken();
        String scope = scopeStore.getScopeForRole(client.role);
        return Token.builder()
                .scope(hostUrl + scope)
                .value(tokenValue)
                .type(TokenType.BEARER.getName())
                .expiresInSec(EXPIRE_TOKEN_SEC)
                .build();
    }
}