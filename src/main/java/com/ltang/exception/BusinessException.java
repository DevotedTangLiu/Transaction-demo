package com.ltang.exception;

/**
 * Generic simplified Business Exception
 *
 * @author tangliu
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
