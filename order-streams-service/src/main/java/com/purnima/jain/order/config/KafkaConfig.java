package com.purnima.jain.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.stream.input.shipping-details.topic-name}")
	private String shippingDetailsTopicName;

	@Value("${spring.kafka.stream.input.item-details.topic-name}")
	private String itemDetailsTopicName;

	@Value("${spring.kafka.stream.input.order-aggregate.topic-name}")
	private String orderAggregateTopicName;

	@Bean
	public String shippingDetailsTopicName() {
		return shippingDetailsTopicName;
	}

	@Bean
	public String itemDetailsTopicName() {
		return itemDetailsTopicName;
	}

	@Bean
	public String orderAggregateTopicName() {
		return orderAggregateTopicName;
	}

}
