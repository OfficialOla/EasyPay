package com.example.EasyPay.services;

import com.example.EasyPay.data.models.Account;
import com.example.EasyPay.dtos.requests.*;
import com.example.EasyPay.dtos.response.ApiResponse;
import com.example.EasyPay.dtos.response.UserRegistrationResponse;
import com.example.EasyPay.exceptions.AccountDoesNotExist;
import com.example.EasyPay.exceptions.CurrencyNotFoundException;
import com.example.EasyPay.exceptions.InsufficientFundException;
import com.example.EasyPay.exceptions.InvalidCredentialException;


public interface AccountService {
    UserRegistrationResponse createAccount(CreateUserAccountRequest userRequest);

    TransactionReceipt deposit(DepositRequest depositRequest) throws AccountDoesNotExist, CurrencyNotFoundException;

    void deleteAll();

    Account findByAccountNumber(String accountNumber);

    TransactionReceipt withdraw(WithdrawalRequest withdrawalRequest) throws InvalidCredentialException, InsufficientFundException, CurrencyNotFoundException;

    TransactionReceipt transfer(TransferRequest transferRequest) throws AccountDoesNotExist, InvalidCredentialException, InsufficientFundException, CurrencyNotFoundException;

}
