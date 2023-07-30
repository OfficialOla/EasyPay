package com.example.EasyPay.data.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data

public class Account {
    @Id
    private String accountId;
    private String accountFirstName;
    private String accountLastName;
    private BigDecimal accountBalance = BigDecimal.ZERO;
    private String accountNumber;
    private String accountPin;
    private CurrencyType accountCurrencyType;
    private String accountStatus;
    private String transactionHistory;

}
