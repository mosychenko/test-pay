package com.job.testpaysandbox.model;

public class User {
    public final String clientId;
    public final String password;
    public final String email;
    public Token token;

    public User(String clientId, String password, String email) {
        this.clientId = clientId;
        this.password = password;
        this.email = email;
    }
}
