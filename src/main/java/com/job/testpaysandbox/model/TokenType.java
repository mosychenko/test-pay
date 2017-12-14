package com.job.testpaysandbox.model;

/**
 * Created by max on 12/14/17.
 */
public enum TokenType {
    BEARER("Bearer");

    private String name;

    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
