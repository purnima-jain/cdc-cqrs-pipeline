package com.purnima.jain.order.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.purnima.jain.order.exception.OrderAlreadyExistsException;
import com.purnima.jain.order.jpa.entity.ShippingDetailsEntity;
import com.purnima.jain.order.jpa.repo.ShippingDetailsRepository;
import com.purnima.jain.order.rest.dto.ShippingDetailsDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShippingDetailsService {

	@Autowired
	private ShippingDetailsRepository shippingDetailsRepository;

	public ShippingDetailsDto save(ShippingDetailsDto shippingDetailsDto) {
		log.info("Entering ShippingDetailsService.save() with shippingDetailsDto :: {}", shippingDetailsDto);

		if (shippingDetailsDto.getOrderId() == null || shippingDetailsDto.getOrderId().isBlank()) {
			shippingDetailsDto.setOrderId(UUID.randomUUID().toString());
		} else if (shippingDetailsRepository.existsById(shippingDetailsDto.getOrderId())) {
			log.error("Order Id already exists in the database. Order Id: {}", shippingDetailsDto.getOrderId());
			throw new OrderAlreadyExistsException("Order Id already exists in the database. Order Id: " + shippingDetailsDto.getOrderId());
		}

		ShippingDetailsEntity shippingDetailsEntity = convertDtoToEntity(shippingDetailsDto);

		ShippingDetailsEntity persistedShippingDetailsEntity = shippingDetailsRepository.save(shippingDetailsEntity);

		ShippingDetailsDto persistedShippingDetailsDto = convertEntityToDto(persistedShippingDetailsEntity);

		log.info("Leaving ShippingDetailsService.save() with persistedShippingDetailsDto :: {}", persistedShippingDetailsDto);
		return persistedShippingDetailsDto;
	}

	private ShippingDetailsEntity convertDtoToEntity(ShippingDetailsDto shippingDetailsDto) {
		ShippingDetailsEntity shippingDetailsEntity = new ShippingDetailsEntity();

		shippingDetailsEntity.setOrderId(shippingDetailsDto.getOrderId());
		shippingDetailsEntity.setCustomerName(shippingDetailsDto.getCustomerName());
		shippingDetailsEntity.setCustomerAddress(shippingDetailsDto.getCustomerAddress());
		shippingDetailsEntity.setZipCode(shippingDetailsDto.getZipCode());

		return shippingDetailsEntity;
	}

	private ShippingDetailsDto convertEntityToDto(ShippingDetailsEntity shippingDetailsEntity) {
		ShippingDetailsDto shippingDetailsDto = new ShippingDetailsDto();

		shippingDetailsDto.setOrderId(shippingDetailsEntity.getOrderId());
		shippingDetailsDto.setCustomerName(shippingDetailsEntity.getCustomerName());
		shippingDetailsDto.setCustomerAddress(shippingDetailsEntity.getCustomerAddress());
		shippingDetailsDto.setZipCode(shippingDetailsEntity.getZipCode());

		return shippingDetailsDto;
	}

}
