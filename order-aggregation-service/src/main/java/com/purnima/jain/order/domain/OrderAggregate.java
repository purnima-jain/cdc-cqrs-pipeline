package com.purnima.jain.order.domain;

import java.util.ArrayList;
import java.util.List;

import com.purnima.jain.order.dto.ItemDto;
import com.purnima.jain.order.dto.ShippingDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAggregate {

	private String orderId;
	private ShippingDetails shippingDetails;
	private List<Item> items;

	public OrderAggregate(ShippingDetailsDto shippingDetailsDto, ArrayList<ItemDto> itemDtoList) {
		this.setOrderId(shippingDetailsDto.getOrderId());

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setCustomerAddress(shippingDetailsDto.getCustomerAddress());
		shippingDetails.setCustomerName(shippingDetailsDto.getCustomerName());
		shippingDetails.setZipCode(shippingDetailsDto.getZipCode());
		this.setShippingDetails(shippingDetails);

		List<Item> items = new ArrayList<>();
		for (ItemDto itemDto : itemDtoList) {
			Item item = new Item();
			item.setItemId(itemDto.getItemId());
			item.setItemName(itemDto.getItemName());
			item.setPrice(itemDto.getPrice());
			item.setQuantity(itemDto.getQuantity());

			items.add(item);
		}
		this.setItems(items);
	}

}
