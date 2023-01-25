package com.purnima.jain.order.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ItemDetailsDto {

	private String orderId;
	private List<ItemDto> items = new ArrayList<>();
}