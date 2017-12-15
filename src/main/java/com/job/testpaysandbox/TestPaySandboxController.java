package com.job.testpaysandbox;

import com.job.testpaysandbox.model.Payment;
import com.job.testpaysandbox.service.OAuthService;
import com.job.testpaysandbox.service.TestPay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TestPaySandboxController {

    @Autowired
    private TestPay testPay;

    @Autowired
    private OAuthService oAuthService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @ApiOperation("Creates a payment \n" +
        "A Payment API call is asynchronous, which lets you show payout details at " +
        "a later time. After payment processing, you will receive webhook event notification with final " +
        "payment status")
    @RequestMapping(value = "/payments/payment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createPayment(@RequestHeader(name = "authorization") String token,
                                                @RequestBody Payment payment) {
        if (!validator.validate(payment).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}
