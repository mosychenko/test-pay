package com.job.testpaysandbox.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

/**
 * Created by max on 12/17/17.
 */
@Value
@Builder
public class PaymentPayload {
    String id;
    Date createDate;
    Payment payment;
    Account account;
}
