package com.example.EasyPay.services;


import com.example.EasyPay.data.models.Account;
import com.example.EasyPay.data.models.CurrencyType;
import com.example.EasyPay.data.repositories.AccountRepository;
import com.example.EasyPay.data.repositories.TransactionRepository;
import com.example.EasyPay.dtos.requests.*;
import com.example.EasyPay.dtos.response.UserRegistrationResponse;
import com.example.EasyPay.exceptions.AccountDoesNotExist;
import com.example.EasyPay.exceptions.CurrencyNotFoundException;
import com.example.EasyPay.exceptions.InsufficientFundException;
import com.example.EasyPay.exceptions.InvalidCredentialException;
import com.example.EasyPay.utils.CurrencyApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Objects;

import static com.example.EasyPay.utils.AppUtils.USER_ACCOUNT_CREATED_SUCCESSFUL;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserRegistrationResponse createAccount(CreateUserAccountRequest createUserAccountRequest) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountCurrencyType(generateAccountCurrentType(createUserAccountRequest.getAccountCurrencyType()));
        modelMapper.map(createUserAccountRequest, account);
         Account savedAccount = accountRepository.save(account);
        return buildUserRegistrationResponse(savedAccount.getAccountId());
    }
    private UserRegistrationResponse buildUserRegistrationResponse(String userId) {
        UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
        userRegistrationResponse.setMessage(USER_ACCOUNT_CREATED_SUCCESSFUL);
        userRegistrationResponse.setId(userId);
        return userRegistrationResponse;
    }

    @Override
    public TransactionReceipt deposit(DepositRequest depositRequest) throws AccountDoesNotExist, CurrencyNotFoundException {
        if(accountRepository.findByAccountNumber(depositRequest.getAccountNumber())== null){
            throw new AccountDoesNotExist("Account does not exist");}
        else {
            BigDecimal depositAmount;

            Account account = accountRepository.findByAccountNumber(depositRequest.getAccountNumber());
            depositAmount = convertDepositAmountToCurrencyType(depositRequest, account);
            account.setAccountBalance(account.getAccountBalance().add(depositAmount));
            accountRepository.save(account);

            TransactionReceipt transactionReceipt = getTransactionReceipt(depositRequest.getAccountNumber(), depositAmount);
            transactionReceipt.setTransactionType(depositRequest.getTransactionType());

            return transactionReceipt;
        }
    }

    @Override
    public void deleteAll() {
        accountRepository.deleteAll();
    }

    @Override
    public TransactionReceipt transfer(TransferRequest transferRequest) throws InvalidCredentialException, InsufficientFundException,  CurrencyNotFoundException {
        Account senderAccount = findByAccountNumber(transferRequest.getSenderAccountNumber());
        Account recipientAccount = findByAccountNumber(transferRequest.getRecipientAccountNumber());

        if (!senderAccount.getAccountPin().equals(transferRequest.getSenderAccountPin())) {
            throw new InvalidCredentialException("Invalid sender credentials");
        } else if (senderAccount.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new InsufficientFundException("Your account balance is too low for this transfer");
        } else {
            BigDecimal transferAmount = convertTransferAmountToRecipientCurrency(transferRequest, senderAccount, recipientAccount);

            senderAccount.setAccountBalance(senderAccount.getAccountBalance().subtract(transferRequest.getAmount()));
            accountRepository.save(senderAccount);

            recipientAccount.setAccountBalance(recipientAccount.getAccountBalance().add(transferAmount));
            accountRepository.save(recipientAccount);

            TransactionReceipt transactionReceipt = new TransactionReceipt();
            transactionReceipt.setStatus("Successful");
            transactionReceipt.setTransactionDate(LocalDate.now());
            transactionReceipt.setSenderAccountNumber(transferRequest.getSenderAccountNumber());
            transactionReceipt.setRecipientAccountNumber(transferRequest.getRecipientAccountNumber());
            transactionReceipt.setTransactionType(transferRequest.getTransactionType());
            transactionReceipt.setTransactionAmount(transferAmount);

            return transactionReceipt;
        }
    }

    private BigDecimal convertTransferAmountToRecipientCurrency(TransferRequest transferRequest, Account senderAccount, Account recipientAccount) throws CurrencyNotFoundException {
        if (senderAccount.getAccountCurrencyType().equals(recipientAccount.getAccountCurrencyType())) {
            return transferRequest.getAmount();
        }

        CurrencyApi senderCurrencyApi = new CurrencyApi(senderAccount.getAccountCurrencyType());
        CurrencyApi recipientCurrencyApi = new CurrencyApi(recipientAccount.getAccountCurrencyType());

        BigDecimal senderToBaseCurrencyRate = currencyConversion(transferRequest.getAmount(), senderAccount, senderCurrencyApi);
        BigDecimal baseToRecipientCurrencyRate = currencyConversion(BigDecimal.ONE, recipientAccount, recipientCurrencyApi);

        return senderToBaseCurrencyRate.multiply(baseToRecipientCurrencyRate);
    }

    private BigDecimal convertTransferAmountToCurrencyType(TransferRequest transferRequest, Account account) throws CurrencyNotFoundException {
        CurrencyApi currencyApi = new CurrencyApi(transferRequest.getCurrency());
        return currencyConversion(transferRequest.getAmount(), account, currencyApi);
    }

    private static TransactionReceipt getTransactionReceipt(String accountNumber, BigDecimal amount) {
        TransactionReceipt transactionReceipt = transactionReceiptPrimaryDetail(accountNumber);
        transactionReceipt.setTransactionAmount(amount);
        return transactionReceipt;
    }

    private static TransactionReceipt transactionReceiptPrimaryDetail(String accountNumber) {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setStatus("Successful");
        transactionReceipt.setTransactionDate(LocalDate.now());
        transactionReceipt.setAccountNumber(accountNumber);
        return transactionReceipt;
    }

    private static BigDecimal convertDepositAmountToCurrencyType(DepositRequest depositRequest, Account account) throws CurrencyNotFoundException {
        BigDecimal depositAmount;
        CurrencyApi currencyApi = new CurrencyApi(depositRequest.getCurrency());
        depositAmount = currencyConversion(depositRequest.getAmount(), account, currencyApi);
        return depositAmount;
    }

    private static BigDecimal currencyConversion(BigDecimal amount, Account account, CurrencyApi currencyApi) throws CurrencyNotFoundException {
        BigDecimal convertedAmount;
        if(Objects.equals(CurrencyType.USD, account.getAccountCurrencyType())){
            convertedAmount = currencyApi.dollarRate().multiply(amount);
        }
        else  if(Objects.equals(CurrencyType.GBP, account.getAccountCurrencyType())){
            convertedAmount = currencyApi.poundsRate().multiply(amount);
        }
        else  if(CurrencyType.CAD.equals(account.getAccountCurrencyType())){
            convertedAmount = currencyApi.canadianDollarRate().multiply(amount);
        }
        else  if(CurrencyType.EUR.equals(account.getAccountCurrencyType())){
            convertedAmount = currencyApi.eurosRate().multiply(amount);
        }
        else  if(CurrencyType.NGN.equals(account.getAccountCurrencyType())){
            convertedAmount = currencyApi.nairaRate().multiply(amount);
        }
        else throw new CurrencyNotFoundException("Currency not found");
        return convertedAmount;
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public TransactionReceipt withdraw(WithdrawalRequest withdrawalRequest) throws InvalidCredentialException, InsufficientFundException, CurrencyNotFoundException {
        Account foundAccount = findByAccountNumber(withdrawalRequest.getAccountNumber());
        if (!foundAccount.getAccountPin().equals(withdrawalRequest.getAccountPin()))
            throw new InvalidCredentialException("Invalid Credentials");
        else if (foundAccount.getAccountBalance().compareTo(withdrawalRequest.getAmount()) < 0)
            throw new InsufficientFundException("Your account is too low, kindly deposit before withdrawing");
        else {
            BigDecimal withdrawalAmount = convertWithdrawalAmountToCurrencyType(withdrawalRequest, foundAccount);
            foundAccount.setAccountBalance(foundAccount.getAccountBalance().subtract(withdrawalRequest.getAmount()));
            accountRepository.save(foundAccount);

            TransactionReceipt transactionReceipt = new TransactionReceipt();
            transactionReceipt.setStatus("Successful");
            transactionReceipt.setTransactionDate(LocalDate.now());
            transactionReceipt.setAccountNumber(withdrawalRequest.getAccountNumber());
            transactionReceipt.setTransactionType(withdrawalRequest.getTransactionType());
            transactionReceipt.setTransactionAmount(withdrawalAmount);

            return transactionReceipt;
        }

    }



    private static BigDecimal convertWithdrawalAmountToCurrencyType(WithdrawalRequest withdrawalRequest, Account foundAccount) throws CurrencyNotFoundException {

        CurrencyApi currencyApi = new CurrencyApi(foundAccount.getAccountCurrencyType());
        return currencyConversion(withdrawalRequest.getAmount(), foundAccount, currencyApi);
    }


    private CurrencyType generateAccountCurrentType(String currencyType) {
        if(currencyType.equalsIgnoreCase("Naira")){
            return CurrencyType.NGN;}
        else if (currencyType.equalsIgnoreCase("Pounds")) {
            return CurrencyType.GBP;}
        else if(currencyType.equalsIgnoreCase("Canadian Dollars")){
            return CurrencyType.CAD;}
        else if(currencyType.equalsIgnoreCase("Dollars")){
            return CurrencyType.USD;}
        else if(currencyType.equalsIgnoreCase("Euros")){
            return CurrencyType.EUR;}
        return null;
    }
    private String generateAccountNumber() {
        String accountNumber = "0";
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 9; i++) {
            int number =  random.nextInt(0, 9);
            accountNumber = accountNumber + ""+number+"";
        }

        return accountNumber;
    }

}
