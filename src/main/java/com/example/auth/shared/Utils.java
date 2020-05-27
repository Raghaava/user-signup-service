package com.example.auth.shared;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {
    private static final Random random = new SecureRandom();
    private static final String ALPHABETS_DIGITS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generateUserId(int length) {
        return generateUniqueId(length);
    }

    private static String generateUniqueId(int length) {
        StringBuilder uniqueId = new StringBuilder();
        for(int i=0;i<length;i++) {
            int index = random.nextInt(ALPHABETS_DIGITS.length());
            uniqueId.append(ALPHABETS_DIGITS.charAt(index));
        }
        return uniqueId.toString();
    }
}
