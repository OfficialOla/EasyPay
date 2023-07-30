package com.example.EasyPay.exceptions;

public class CurrencyNotFoundException extends  EasyPayException{
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
