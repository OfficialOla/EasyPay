package com.example.EasyPay.exceptions;

public class AccountDoesNotExist extends EasyPayException {
    public AccountDoesNotExist(String message) {
        super(message);
    }
}
