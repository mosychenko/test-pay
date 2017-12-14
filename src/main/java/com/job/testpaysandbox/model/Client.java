package com.job.testpaysandbox.model;

public class Client {
    public final String password;
    public final String email;
    public Token token;
    public Role role;

    public Client(String password, String email, Role role) {
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
