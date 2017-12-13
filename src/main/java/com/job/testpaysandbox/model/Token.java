package com.job.testpaysandbox.model;

public class Token {
    public final String scope;
    public final String value;
    public final String type;
    public final long expires;

    public Token(String scope, String value, String type, long expires) {
        this.scope = scope;
        this.value = value;
        this.type = type;
        this.expires = expires;
    }
}
