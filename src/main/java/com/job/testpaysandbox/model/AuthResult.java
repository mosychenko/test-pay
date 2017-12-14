package com.job.testpaysandbox.model;

/**
 * Created by max on 12/14/17.
 */
public class AuthResult {
    public final Token token;
    public final AuthStatus status;

    public AuthResult(AuthStatus status) {
        this(status, null);
    }

    public AuthResult(AuthStatus status, Token token) {
        this.token = token;
        this.status = status;
    }
}
