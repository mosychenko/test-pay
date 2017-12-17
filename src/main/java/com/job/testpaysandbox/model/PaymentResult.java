package com.job.testpaysandbox.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

/**
 * Created by max on 12/17/17.
 */
@Value
@Builder
public class PaymentResult {
    String id;
    @SerializedName("create_time")
    String createTime;
    String state;
}
