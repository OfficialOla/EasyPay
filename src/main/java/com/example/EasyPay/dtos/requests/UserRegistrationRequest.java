package com.example.EasyPay.dtos.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationRequest {
    private String firstName;
    private String lastName;
    private String EmailAddress;
    private String DOB;
    private String password;
    private String phoneNumber;
    private String accountCurrencyType;
    private String accountPin;

}
