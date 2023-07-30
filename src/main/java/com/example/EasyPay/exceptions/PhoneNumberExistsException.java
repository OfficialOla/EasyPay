package com.example.EasyPay.exceptions;

public class PhoneNumberExistsException extends EasyPayException{
    public PhoneNumberExistsException(String message) {
        super(message);
    }
}
