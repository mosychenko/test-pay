package com.job.testpaysandbox.model;

/**
 * Created by max on 12/14/17.
 */
public enum GrantType {

    CLIENT_CREDENTIALS,
    EMPTY;

    public static GrantType parse(String grantType) {
        switch (grantType) {
            case "client_credentials" : return CLIENT_CREDENTIALS;
            default: return EMPTY;
        }
    }
}
