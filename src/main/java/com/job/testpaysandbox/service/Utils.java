package com.job.testpaysandbox.service;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {

    private static final String ALFABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String LOW_ALFABET = "abcdefghijklmnopqrstuvwxyz";
    private static final Random random = new SecureRandom();

    public static String generateRandomString(int length, boolean uppercase) {
        StringBuilder randomToken = new StringBuilder();
        String alfabet = uppercase ? ALFABET : ALFABET + LOW_ALFABET;
        for (int i = 0; i < length; i++) {
            randomToken.append(alfabet.charAt(random.nextInt(alfabet.length())));
        }
        return randomToken.toString();
    }
}
