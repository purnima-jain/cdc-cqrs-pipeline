package com.purnima.jain.order.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "SHIPPING_DETAILS")
@Data
public class ShippingDetailsEntity {

	@Id
	@Column(name = "ORDER_ID")
	private String orderId;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "CUSTOMER_ADDRESS")
	private String customerAddress;

	@Column(name = "ZIPCODE")
	private String zipCode;

}