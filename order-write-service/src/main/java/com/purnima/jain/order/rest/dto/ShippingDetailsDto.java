package com.purnima.jain.order.rest.dto;

import lombok.Data;

@Data
public class ShippingDetailsDto {

	private String orderId;
	private String customerName;
	private String customerAddress;
	private String zipCode;

}
