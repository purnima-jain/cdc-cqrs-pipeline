package com.purnima.jain.order.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ITEM_DETAILS")
@IdClass(OrderItemId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailsEntity {

	@Id
	@Column(name = "ORDER_ID")
	private String orderId;

	@Id
	@Column(name = "ITEM_ID")
	private String itemId;

	@Column(name = "ITEM_NAME")
	private String itemName;

	@Column(name = "PRICE")
	private Double price;

	@Column(name = "QUANTITY")
	private Integer quantity;
}
