package com.example.EasyPay.dtos.requests;


import com.example.EasyPay.data.models.CurrencyType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferRequest {
    private String senderAccountNumber;
    private String senderAccountPin;
    private String recipientAccountNumber;
    private BigDecimal amount;
    private String transactionType;
    private CurrencyType currency;
    private String transferDescription;

}
