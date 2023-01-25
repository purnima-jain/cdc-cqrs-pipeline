package com.purnima.jain.order.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class ItemDetailsDto {

	private String orderId;
	private List<ItemDto> items;
}