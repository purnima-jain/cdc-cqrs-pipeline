package com.purnima.jain.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableMongoRepositories
public class OrderReadServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderReadServiceApplication.class, args);
		log.info("OrderReadServiceApplication started.............");
	}

}
