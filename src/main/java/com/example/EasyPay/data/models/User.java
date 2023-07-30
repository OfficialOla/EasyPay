package com.example.EasyPay.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String DOB;
    private LocalDate DateOfRegistration;
    private String accountNumber;
    private int age;
    private String password;
    private List<Card> cards = new ArrayList<>();


}
