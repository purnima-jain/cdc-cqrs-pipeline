package com.purnima.jain.order.util.serde;

import java.util.ArrayList;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.purnima.jain.order.domain.OrderAggregate;
import com.purnima.jain.order.dto.ItemDto;
import com.purnima.jain.order.dto.ShippingDetailsDto;

public class StreamsSerdes extends Serdes {

	public static Serde<ItemDto> ItemDtoSerde() {
		return new ItemDtoSerde();
	}

	public static final class ItemDtoSerde extends WrapperSerde<ItemDto> {
		public ItemDtoSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>(ItemDto.class, false));
		}
	}

	public static Serde<ArrayList<ItemDto>> ItemDtoArrayListSerde() {
		return new ItemDtoArrayListSerde();
	}

	public static final class ItemDtoArrayListSerde extends WrapperSerde<ArrayList<ItemDto>> {
		public ItemDtoArrayListSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>(new TypeReference<ArrayList<ItemDto>>() {}, false));
		}
	}

	public static Serde<ShippingDetailsDto> ShippingDetailsDtoSerde() {
		return new ShippingDetailsDtoSerde();
	}

	public static final class ShippingDetailsDtoSerde extends WrapperSerde<ShippingDetailsDto> {
		public ShippingDetailsDtoSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>(ShippingDetailsDto.class, false));
		}
	}

	public static Serde<OrderAggregate> OrderAggregateSerde() {
		return new OrderAggregateSerde();
	}

	public static final class OrderAggregateSerde extends WrapperSerde<OrderAggregate> {
		public OrderAggregateSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>(OrderAggregate.class, false));
		}
	}

}
