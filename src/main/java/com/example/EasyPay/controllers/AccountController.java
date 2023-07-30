package com.example.EasyPay.controllers;

import com.example.EasyPay.dtos.requests.CreateUserAccountRequest;
import com.example.EasyPay.dtos.response.UserRegistrationResponse;
import com.example.EasyPay.exceptions.EasyPayException;
import com.example.EasyPay.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;


    @PostMapping
    public ResponseEntity<UserRegistrationResponse> createAccount(@RequestBody CreateUserAccountRequest createUserAccountRequest ){
        var response = accountService.createAccount(createUserAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
