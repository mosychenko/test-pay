package com.job.testpaysandbox.model;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

@Value
@Builder
public class Amount {
    @NotEmpty
    String value;
    @NotEmpty
    String currency;
}