package com.purnima.jain.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class OrderWriteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderWriteServiceApplication.class, args);
		log.info("OrderWriteServiceApplication started.............");
	}

}
