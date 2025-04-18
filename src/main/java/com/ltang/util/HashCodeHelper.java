package com.ltang.util;

import com.ltang.entity.Transaction;
import com.ltang.exception.BusinessException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper
 *
 * @author tangliu
 */
public class HashCodeHelper {

    public static void main(String[] args) {
        System.out.println(calculateHashCode(new Transaction()));
    }

    public static void updateHashCode(Transaction transaction) {
        if (transaction == null) {
            throw new BusinessException("transaction should not be null");
        }
        transaction.setHashCode(calculateHashCode(transaction));
    }

    public static String calculateHashCode(Transaction transaction) {
        String input = String.format("%s|%s|%s|%s",
                transaction.getUserId(),
                transaction.getAmount().toString(),
                transaction.getType().name(),
                transaction.getDescription());

        return toMD5(input);
    }

    private static String toMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hex string
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
