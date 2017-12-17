package com.job.testpaysandbox.model;

import com.google.gson.annotations.SerializedName;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by max on 12/17/17.
 */
public class WebhookNotificationMessage {
    public final String currency;
    public final String amount;
    public final String id;
    @SerializedName("external_id")
    public final String externalId;
    public final String status;
    public final String sha2sig;

    public WebhookNotificationMessage(String currency,
                                      String amount,
                                      String id,
                                      String externalId,
                                      String status,
                                      String sha2sig) {
        this.currency = currency;
        this.amount = amount;
        this.id = id;
        this.externalId = externalId;
        this.status = status;
        this.sha2sig = sha2sig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String currency;
        private String amount;
        private String id;
        private String externalId;
        private String status;
        private String sha2sig;

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder externalId(String externalId) {
            this.externalId = externalId;
            return this;
        }

        public Builder status(PaymentProcessingStatus status) {
            this.status = status.name().toLowerCase();
            return this;
        }

        public WebhookNotificationMessage build(String shaSecretUpperCase) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String text = currency + amount + shaSecretUpperCase + id + externalId + status;
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.US_ASCII));
            return new WebhookNotificationMessage(currency, amount, id, externalId, status, new String(hash));
        }
    }
}
