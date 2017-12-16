package com.job.testpaysandbox.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class Payment {
    @NotNull
    Intent intent;
    @NotEmpty
    @SerializedName("notification_url")
    String notificationUrl;
    @NotNull
    @Valid
    Payer payer;
    @NotNull
    @Valid
    Transaction transaction;
}
