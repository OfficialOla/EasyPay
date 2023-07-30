package com.example.EasyPay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class EasyPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyPayApplication.class, args);
	}

}
