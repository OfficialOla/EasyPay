package com.example.EasyPay.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class Card {
@Id
private String id;
    private String cardType;
    private String cardNumber;
    private String cardHolderName;
    private String cardCvv;
    private LocalDate expiryDate;
    private String status;
}
