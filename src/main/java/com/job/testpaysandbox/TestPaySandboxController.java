package com.job.testpaysandbox;

import com.job.testpaysandbox.service.OAuthService;
import com.job.testpaysandbox.service.OAuthUtils;
import com.job.testpaysandbox.service.TestPay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class TestPaySandboxController {

    @Autowired
    private TestPay testPay;

    @Autowired
    private OAuthService oAuthService;

    @ApiOperation("Get an access token \n " +
        "In response, the TestPay authorization server issues an access token. Re-use the access token " +
        "until it expiresInSec. When it expiresInSec, you can get a new token.")
    @RequestMapping(value = "/oauth2/token", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getAccessToken(@RequestHeader(name = "authorization") String authInfo,
                                                 @RequestParam(name = "grant_type") String grantType,
                                                 HttpServletRequest request) {
        if (!OAuthUtils.validateGrant(grantType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Grant Type Incorrect");
        }
        Pair<String, String> clientIdAndPassword = OAuthUtils.parseBase64Credentials(authInfo);
        StringBuffer requestURL = request.getRequestURL();
        String hostPath = requestURL.delete(requestURL.indexOf(request.getRequestURI()), requestURL.length()).toString();
        oAuthService.createToken(clientIdAndPassword.getKey(), clientIdAndPassword.getValue(), hostPath);
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
