package com.job.testpaysandbox.controller;

import com.job.testpaysandbox.model.AuthResult;
import com.job.testpaysandbox.model.Token;
import com.job.testpaysandbox.service.OAuthService;
import com.job.testpaysandbox.service.OAuthUtils;
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
public class AuthController {

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
        AuthResult result = oAuthService.createToken(clientIdAndPassword.getKey(), clientIdAndPassword.getValue(), hostPath);
        switch (result.status) {
            case SUCCESS:
                return ResponseEntity.ok(result.jsonToken);
            case USER_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
