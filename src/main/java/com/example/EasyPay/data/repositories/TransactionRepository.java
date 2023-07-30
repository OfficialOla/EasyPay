package com.example.EasyPay.data.repositories;


import com.example.EasyPay.data.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
