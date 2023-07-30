package com.example.EasyPay.exceptions;

public class UserNotFoundException extends EasyPayException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
