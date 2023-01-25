package com.purnima.jain.order.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.purnima.jain.order.rest.dto.ItemDetailsDto;
import com.purnima.jain.order.rest.dto.ShippingDetailsDto;
import com.purnima.jain.order.service.ItemDetailsService;
import com.purnima.jain.order.service.ShippingDetailsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderRestController {

	@Autowired
	private ShippingDetailsService shippingDetailsService;

	@Autowired
	private ItemDetailsService itemDetailsService;

	@PostMapping(value = "/api/shipping-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShippingDetailsDto postShippingDetails(@RequestBody ShippingDetailsDto shippingDetailsDto) {
		log.info("Entering OrderRestController.postShippingDetails() with shippingDetailsDto :: {}", shippingDetailsDto);

		ShippingDetailsDto persistedShippingDetailsDto = shippingDetailsService.save(shippingDetailsDto);

		log.info("Leaving OrderRestController.postShippingDetails() with persistedShippingDetailsDto :: {}", persistedShippingDetailsDto);
		return persistedShippingDetailsDto;
	}

	@PostMapping(value = "/api/item-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemDetailsDto postItemDetails(@RequestBody ItemDetailsDto itemDetailsDto) {
		log.info("Entering OrderRestController.postItemDetails() with itemDetailsDto :: {}", itemDetailsDto);

		ItemDetailsDto persistedItemDetailsDto = itemDetailsService.save(itemDetailsDto);

		log.info("Leaving OrderRestController.postItemDetails() with persistedItemDetailsDto :: {}", persistedItemDetailsDto);
		return persistedItemDetailsDto;
	}

}
