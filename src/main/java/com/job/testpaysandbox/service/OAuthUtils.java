package com.job.testpaysandbox.service;

import com.job.testpaysandbox.model.GrantType;
import javafx.util.Pair;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class OAuthUtils {

    private static final int TOKEN_LENGTH = 16;
    private static final String alfabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new SecureRandom();

    public static Pair<String, String> parseBase64Credentials(String authInfo) {
        String[] authHeaderParts = authInfo.split(" ");
        if (authHeaderParts.length < 2) {
            throw new IllegalArgumentException();
        }
        String authCredentials = new String(Base64.getDecoder().decode(authHeaderParts[1]));
        String[] splittedAuthCredentials = authCredentials.split(":");
        if (splittedAuthCredentials.length < 2) {
            throw new IllegalArgumentException();
        }
        return new Pair<>(splittedAuthCredentials[0], splittedAuthCredentials[1]);
    }

    public static String generateRandomToken() {
        StringBuilder randomToken = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            randomToken.append(alfabet.charAt(random.nextInt(alfabet.length())));
        }
        return randomToken.toString();
    }

    public static boolean validateGrant(String grantType) {
        return GrantType.CLIENT_CREDENTIALS.equals(GrantType.parse(grantType));
    }
}