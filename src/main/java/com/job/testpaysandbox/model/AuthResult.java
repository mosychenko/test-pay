package com.job.testpaysandbox.model;

/**
 * Created by max on 12/14/17.
 */
public class AuthResult {
    public final String jsonToken;
    public final AuthStatus status;

    public AuthResult(AuthStatus status) {
        this(status, null);
    }

    public AuthResult(AuthStatus status, String jsonToken) {
        this.jsonToken = jsonToken;
        this.status = status;
    }
}
