package com.purnima.jain.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.purnima.jain.order.exception.OrderAlreadyExistsException;
import com.purnima.jain.order.jpa.entity.ItemDetailsEntity;
import com.purnima.jain.order.jpa.repo.ItemDetailsRepository;
import com.purnima.jain.order.rest.dto.ItemDetailsDto;
import com.purnima.jain.order.rest.dto.ItemDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItemDetailsService {

	@Autowired
	private ItemDetailsRepository itemDetailsRepository;

	public ItemDetailsDto save(ItemDetailsDto itemDetailsDto) {
		log.info("Entering ItemDetailsService.save() with itemDetailsDto :: {}", itemDetailsDto);

		if (itemDetailsDto.getOrderId() == null || itemDetailsDto.getOrderId().isBlank()) {
			itemDetailsDto.setOrderId(UUID.randomUUID().toString());
		} else if (!itemDetailsRepository.findByOrderId(itemDetailsDto.getOrderId()).isEmpty()) {
			log.error("Order Id already exists in the database. Order Id: {}", itemDetailsDto.getOrderId());
			throw new OrderAlreadyExistsException("Order Id already exists in the database. Order Id: " + itemDetailsDto.getOrderId());
		}

		List<ItemDetailsEntity> itemDetailsEntityList = convertDtoToEntity(itemDetailsDto);

		List<ItemDetailsEntity> persistedItemDetailsEntityList = (List<ItemDetailsEntity>) itemDetailsRepository.saveAll(itemDetailsEntityList);

		ItemDetailsDto persistedItemDetailsDto = convertEntityToDto(persistedItemDetailsEntityList);

		log.info("Leaving ItemDetailsService.save() with persistedItemDetailsDto :: {}", persistedItemDetailsDto);
		return persistedItemDetailsDto;
	}

	private List<ItemDetailsEntity> convertDtoToEntity(ItemDetailsDto itemDetailsDto) {
		List<ItemDetailsEntity> itemDetailsEntityList = new ArrayList<>();

		if (itemDetailsDto != null) {
			itemDetailsEntityList = itemDetailsDto.getItems().stream()
					.map(itemDto -> new ItemDetailsEntity(itemDetailsDto.getOrderId(), itemDto.getItemId(), itemDto.getItemName(), itemDto.getPrice(), itemDto.getQuantity()))
					.collect(Collectors.toList());
		}

		return itemDetailsEntityList;
	}

	private ItemDetailsDto convertEntityToDto(List<ItemDetailsEntity> itemDetailsEntityList) {
		ItemDetailsDto itemDetailsDto = null;

		if (itemDetailsEntityList != null) {
			itemDetailsDto = new ItemDetailsDto();
			itemDetailsDto.setOrderId(itemDetailsEntityList.get(0).getOrderId());
			List<ItemDto> items = itemDetailsEntityList.stream()
					.map(itemDetailsEntity -> new ItemDto(itemDetailsEntity.getItemId(), itemDetailsEntity.getItemName(), itemDetailsEntity.getPrice(), itemDetailsEntity.getQuantity()))
					.collect(Collectors.toList());
			itemDetailsDto.setItems(items);
		}

		return itemDetailsDto;
	}

}
