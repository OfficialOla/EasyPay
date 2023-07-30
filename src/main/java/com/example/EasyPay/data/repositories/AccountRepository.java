package com.example.EasyPay.data.repositories;

import com.example.EasyPay.data.models.Account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByAccountNumber(String accountNumber);
}
