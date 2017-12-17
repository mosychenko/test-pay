package com.job.testpaysandbox.controller;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.job.testpaysandbox.model.ErrorType;
import com.job.testpaysandbox.model.Payment;
import com.job.testpaysandbox.model.PaymentProcessingStatus;
import com.job.testpaysandbox.model.PaymentResult;
import com.job.testpaysandbox.service.OAuthService;
import com.job.testpaysandbox.service.OAuthUtils;
import com.job.testpaysandbox.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TestPaySandboxController {

    @Autowired
    private PayService payService;

    @Autowired
    private OAuthService oAuthService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Gson gson = new Gson();

    @ApiOperation("Creates a payment \n" +
        "A Payment API call is asynchronous, which lets you show payout details at " +
        "a later time. After payment processing, you will receive webhook event notification with final " +
        "payment status")
    @RequestMapping(value = "/payments/payment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createPayment(@RequestHeader(name = "Authorization") String token,
                                                @RequestBody Payment payment,
                                                HttpServletRequest request) {
        if (!oAuthService.isValidToken(OAuthUtils.getToken(token), request.getRequestURL().toString())) {
            return createErrorResult(ErrorType.AUTHENTIFICATION_FAILURE);
        }
        if (!validator.validate(payment).isEmpty()) {
            return createErrorResult(ErrorType.INVALID_REQUEST);
        }
        PaymentResult result = payService.addPaymentToProcessing(payment);
        if (PaymentProcessingStatus.FAILED.name().toLowerCase().equals(result.getState())) {
            return createErrorResult(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(gson.toJson(result));
    }

    private ResponseEntity<String> createErrorResult(ErrorType errorType) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.error = errorType.name();
        errorResult.description = errorType.getDescription();
        return ResponseEntity.status(errorType.getHttpCode()).body(gson.toJson(errorResult));
    }

    private class ErrorResult {
        public String error;
        @SerializedName("error_description")
        public String description;
    }
}
