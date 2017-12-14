package com.job.testpaysandbox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Token {
    @Expose
    public final String scope;
    @Expose
    @SerializedName("Access-Token")
    public final String value;
    @Expose
    @SerializedName("token-type")
    public final String type;
    @Expose
    @SerializedName("expires_in")
    public final long expiresInSec;
    public final Date createDate;

    private Token(String scope,
                 String value,
                 String type,
                 long expiresInSec,
                 Date createDate) {
        this.scope = scope;
        this.value = value;
        this.type = type;
        this.expiresInSec = expiresInSec;
        this.createDate = createDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isExpired() {
        Date currentTime = new Date();
        Date expiredTime = new Date(createDate.getTime() + expiresInSec * 1000);
        return currentTime.before(expiredTime);
    }

    public static class Builder {
        private String scope;
        private String value;
        private String type;
        private long expiresInSec;
        private Date createDate;

        public Builder() {
            createDate = new Date();
        }

        public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder expiresInSec(long expiresInSec) {
            this.expiresInSec = expiresInSec;
            return this;
        }

        public Token build() {
            return new Token(scope, value, type, expiresInSec, createDate);
        }
    }
}
