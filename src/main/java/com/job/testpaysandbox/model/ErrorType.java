package com.job.testpaysandbox.model;

/**
 * Created by max on 12/16/17.
 */
public enum ErrorType {
    INVALID_REQUEST(400, "Request is not well-formatted, syntactically incorrect or violates schema"),
    AUTHENTIFICATION_FAILURE(401, "Authentication failed due to invalid authentication credentials"),
    UNSUPPORTED_MEDIA_TYPE(415, "The server does not support the request payload media type"),
    INTERNAL_SERVER_ERROR(500, "An internal server error has occurred");

    private int httpCode;
    private String description;

    ErrorType(int httpCode, String description) {
        this.httpCode = httpCode;
        this.description = description;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getDescription() {
        return description;
    }
}
