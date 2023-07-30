package com.example.EasyPay.exceptions;

public class EmailExistsException extends EasyPayException{
    public EmailExistsException(String message) {
        super(message);
    }
}
