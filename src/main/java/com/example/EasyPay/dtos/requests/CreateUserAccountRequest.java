package com.example.EasyPay.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserAccountRequest {
    private String firstName;
    private String lastName;
    private String emailAddress;
    public  String DOB;
    private String password;
    private String phoneNumber;
    private String accountNumber;
    private String accountCurrencyType;
}
