package com.ltang.exception;

/**
 * Specific exception
 *
 * @author tangliu
 */
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
} 