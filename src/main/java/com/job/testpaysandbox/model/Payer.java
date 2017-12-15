package com.job.testpaysandbox.model;

import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Value
public class Payer {
    @NotEmpty
    String email;
}
