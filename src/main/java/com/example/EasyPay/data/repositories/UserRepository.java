package com.example.EasyPay.data.repositories;

import com.example.EasyPay.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmailAddress(String emailAddress);

    User findByAccountNumber(String accountNumber);

}
