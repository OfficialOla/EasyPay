package com.example.EasyPay.dtos.requests;

import com.example.EasyPay.data.models.CurrencyType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DepositRequest {
    private String transactionType;
    private CurrencyType currency;
    private BigDecimal amount = BigDecimal.ZERO;
    private String accountNumber;
}
