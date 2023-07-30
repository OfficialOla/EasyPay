package com.example.EasyPay.dtos.requests;

import com.example.EasyPay.data.models.CurrencyType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WithdrawalRequest {
    private String transactionType;
    private CurrencyType currencyType;
    private BigDecimal amount;
    private String accountNumber;
    private String accountPin;
}
