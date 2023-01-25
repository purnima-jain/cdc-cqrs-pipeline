package com.purnima.jain.order.jpa.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class OrderItemId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ORDER_ID")
	private String orderId;

	@Column(name = "ITEM_ID")
	private String itemId;

}
