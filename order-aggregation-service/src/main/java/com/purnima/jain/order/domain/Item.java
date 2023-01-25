package com.purnima.jain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

	private String itemId;
	private String itemName;
	private Double price;
	private Integer quantity;

}
