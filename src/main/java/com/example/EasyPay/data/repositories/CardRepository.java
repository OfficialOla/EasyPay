package com.example.EasyPay.data.repositories;

import com.example.EasyPay.data.models.Card;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String> {
}
