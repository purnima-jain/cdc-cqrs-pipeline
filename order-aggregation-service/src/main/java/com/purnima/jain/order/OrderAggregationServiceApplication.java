package com.purnima.jain.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class OrderAggregationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderAggregationServiceApplication.class, args);

//		try {
//			FileUtils.cleanDirectory(new File("C:/Users/jain_/AppData/Local/Temp/kafka-streams"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		log.info("OrderAggregationServiceApplication started.............");
	}
}
