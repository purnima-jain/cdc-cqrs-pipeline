package com.purnima.jain.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.purnima.jain.order.domain.Order;
import com.purnima.jain.order.domain.OrderWrapper;
import com.purnima.jain.order.repo.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order findOrderByOrderId(String orderId) {
		log.info("Entering OrderService.findOrderByOrderId() with orderId :: {}", orderId);
		Order order = null;
		OrderWrapper orderWrapper = orderRepository.findById(orderId).orElseGet(null);
		if (orderWrapper.getOrder() != null) {
			order = orderWrapper.getOrder();
			log.info("Order retrieved from db: {}", order);
		}

		return order;
	}

}
