package com.job.testpaysandbox.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

/**
 * Created by max on 12/17/17.
 */
@Data
@Builder
public class WebhookPayload {
    private String notificationUrl;
    private WebhookNotificationMessage message;
    @Setter(AccessLevel.NONE)
    private int totalTryCount;
    @Setter(AccessLevel.NONE)
    private int todayTryCount;

    public void incTry() {
        todayTryCount++;
        totalTryCount++;
    }
}
