package com.example.EasyPay.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class TransactionReceipt {
    private String transactionId;
    private String transactionType;
    private LocalDate transactionDate;
    private String accountNumber;
    private BigDecimal transactionAmount;
    private String status;
    private String senderAccountNumber;
    private String recipientAccountNumber;
}
