package com.purnima.jain.order.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.purnima.jain.order.domain.Order;
import com.purnima.jain.order.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderRestController {

	@Autowired
	private OrderService orderService;

	// Read Order by Order Id
	@GetMapping("/api/order/{orderId}")
	public ResponseEntity<Order> findOrderByOrderId(@PathVariable("orderId") String orderId) {
		log.info("Entering OrderRestController.findOrderByOrderId() with orderId :: {}", orderId);
		Order order = orderService.findOrderByOrderId(orderId);
		if (order != null) {
			return new ResponseEntity<>(order, HttpStatus.OK);
		}

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

//	
//	
//	
//	
//	
//	
//
//	@Autowired
//	private ShippingDetailsService shippingDetailsService;
//
//	@Autowired
//	private ItemDetailsService itemDetailsService;
//
//	@PostMapping(value = "/api/shipping-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ShippingDetailsDto postShippingDetails(@RequestBody ShippingDetailsDto shippingDetailsDto) {
//		log.info("Entering OrderRestController.postShippingDetails() with shippingDetailsDto :: {}", shippingDetailsDto);
//
//		ShippingDetailsDto persistedShippingDetailsDto = shippingDetailsService.save(shippingDetailsDto);
//
//		log.info("Leaving OrderRestController.postShippingDetails() with persistedShippingDetailsDto :: {}", persistedShippingDetailsDto);
//		return persistedShippingDetailsDto;
//	}
//
//	@PostMapping(value = "/api/item-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ItemDetailsDto postItemDetails(@RequestBody ItemDetailsDto itemDetailsDto) {
//		log.info("Entering OrderRestController.postItemDetails() with itemDetailsDto :: {}", itemDetailsDto);
//
//		ItemDetailsDto persistedItemDetailsDto = itemDetailsService.save(itemDetailsDto);
//
//		log.info("Leaving OrderRestController.postItemDetails() with persistedItemDetailsDto :: {}", persistedItemDetailsDto);
//		return persistedItemDetailsDto;
//	}

}
