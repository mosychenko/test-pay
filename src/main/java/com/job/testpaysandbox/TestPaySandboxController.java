package com.job.testpaysandbox;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TestPaySandboxController {

    @Autowired
    private TestPay testPay;

    @ApiOperation("Get an access token \n " +
        "In response, the TestPay authorization server issues an access token. Re-use the access token " +
        "until it expires. When it expires, you can get a new token.")
    @RequestMapping(value = "/oauth2/token", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getAccessToken() {
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Creates a payment \n" +
        "A Payment API call is asynchronous, which lets you show payout details at " +
        "a later time. After payment processing, you will receive webhook event notification with final " +
        "payment status")
    @RequestMapping(value = "/payments/payment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createPayment() {
        return ResponseEntity.ok().build();
    }
}
