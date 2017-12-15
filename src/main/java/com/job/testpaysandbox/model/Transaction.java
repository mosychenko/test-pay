package com.job.testpaysandbox.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class Transaction {
    @SerializedName("external_id")
    String externalId;
    @NotNull
    @Valid
    Amount amount;
    String description;
}
