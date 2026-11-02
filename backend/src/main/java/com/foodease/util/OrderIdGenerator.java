package com.foodease.util;

import java.security.SecureRandom;

/**
 * Utility for generating clean, unique 12-character alphanumeric Order IDs.
 */
public final class OrderIdGenerator {

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    private OrderIdGenerator() {
        // Prevent instantiation of utility class (Encapsulation)
    }

    public static String generateOrderId() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
