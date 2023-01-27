package com.purnima.jain.order.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	private String orderId;

	private ShippingDetails shippingDetails;

	private List<Item> items;

}
