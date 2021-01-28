package com.demo.interceptor.exception;

/**
 * com.demo.exception.exception
 * create by admin nihui
 * create time 2021/1/28
 * version 1.0
 **/
public class SystemException extends RuntimeException {
    private String message;

    public SystemException() {
    }

    public SystemException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
