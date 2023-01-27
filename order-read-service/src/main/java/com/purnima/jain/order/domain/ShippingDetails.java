package com.purnima.jain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetails {

	private String customerName;
	private String customerAddress;
	private String zipCode;

}
