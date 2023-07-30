package com.example.EasyPay.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Transaction {
    @Id
    private String transactionId;
    private TransactionType transactionType;
    private LocalDate transactionDate;
    private BigDecimal transactionAmount;
    private String status;

}
