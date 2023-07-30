package com.example.EasyPay.exceptions;

public class InsufficientFundException extends EasyPayException {
    public InsufficientFundException(String message) {
        super(message);
    }
}
