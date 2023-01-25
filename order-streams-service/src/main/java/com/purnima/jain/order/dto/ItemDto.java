package com.purnima.jain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

	private String itemId;
	private String itemName;
	private Double price;
	private Integer quantity;

}
