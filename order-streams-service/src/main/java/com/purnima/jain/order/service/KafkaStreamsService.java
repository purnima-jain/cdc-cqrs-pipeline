package com.purnima.jain.order.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KGroupedTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.purnima.jain.order.domain.Item;
import com.purnima.jain.order.domain.OrderAggregate;
import com.purnima.jain.order.domain.ShippingDetails;
import com.purnima.jain.order.dto.ItemDetailsDto;
import com.purnima.jain.order.dto.ItemDto;
import com.purnima.jain.order.dto.ShippingDetailsDto;
import com.purnima.jain.order.util.serde.StreamsSerdes;

import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Joined;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaStreamsService {
	
	@Value("#{kafkaConfig.shippingDetailsTopicName()}")
	private String shippingDetailsTopicName;
	
	@Value("#{kafkaConfig.itemDetailsTopicName()}")
	private String itemDetailsTopicName;
	
	@Value("#{kafkaConfig.orderAggregateTopicName()}")
	private String orderAggregateTopicName;
	
	private static final Serde<String> STRING_SERDE = Serdes.String();
	private static final Serde<ItemDto> ITEM_DTO_SERDE = StreamsSerdes.ItemDtoSerde();
	// private static final Serde<ItemDetailsDto> ITEM_DETAILS_DTO_SERDE = StreamsSerdes.ItemDetailsDtoSerde();
	private static final Serde<ArrayList<ItemDto>> ITEM_DTO_ARRAYLIST_SERDE = StreamsSerdes.ItemDtoArrayListSerde();
	private static final Serde<ShippingDetailsDto> SHIPPING_DETAILS_DTO_SERDE = StreamsSerdes.ShippingDetailsDtoSerde();
	private static final Serde<OrderAggregate> ORDER_AGGREGATE_SERDE = StreamsSerdes.OrderAggregateSerde();
	
	// private static final String SHIPPING_DETAILS_KSTREAM_TO_KTABLE_MATERIALIZED_TABLE = "shipping-details-kstream-to-ktable-materialized-table";
	private static final String ITEM_DTO_STATE_STORE = "item-dto-state-store";
	private static final String SHIPPING_DETAILS_DTO_STATE_STORE = "shipping-details-dto-state-store";
	private static final String ORDER_AGGREGATE_STATE_STORE = "order-aggregate-state-store";
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	public void processMessage(StreamsBuilder streamsBuilder) {
		
		// Shipping Details Read
		KStream<String, String> shippingDetailsSourceInputKStream = streamsBuilder.stream(shippingDetailsTopicName, Consumed.with(STRING_SERDE, STRING_SERDE));
		
		// Print shippingDetailsSourceInputKStream
		shippingDetailsSourceInputKStream.foreach((k, v) -> log.info("shippingDetailsSourceInputKStream :: Key :: {}, Value :: {}", k, v));
		
		// Change the Json value of the message to ShippingDetailsDto
		KStream<String, ShippingDetailsDto> shippingDetailsDtoWithKeyAsOrderIdKStream = shippingDetailsSourceInputKStream.map((orderIdJson, shippingDetailsJson) ->
        							new KeyValue<>(parseOrderId(orderIdJson), parseShippingDetails(shippingDetailsJson)));
		
		// Print shippingDetailsDtoWithKeyAsOrderIdKStream
		shippingDetailsDtoWithKeyAsOrderIdKStream.foreach((k, v) -> log.info("shippingDetailsDtoWithKeyAsOrderIdKStream :: Key :: {}, Value :: {}", k, v));
		
		// Convert KStream to KTable
		KTable<String, ShippingDetailsDto> shippingDetailsDtoWithKeyAsOrderIdKTable = shippingDetailsDtoWithKeyAsOrderIdKStream
				.toTable(Materialized.<String, ShippingDetailsDto, KeyValueStore<Bytes, byte[]>>as(SHIPPING_DETAILS_DTO_STATE_STORE).withKeySerde(STRING_SERDE).withValueSerde(SHIPPING_DETAILS_DTO_SERDE));
		
		// Print shippingDetailsDtoWithKeyAsOrderIdKTable
		shippingDetailsDtoWithKeyAsOrderIdKTable.toStream().foreach((k, v) -> log.info("shippingDetailsDtoWithKeyAsOrderIdKTable :: Key :: {}, Value :: {}", k, v));
		
		
		
		
		// Item Details Read
		KStream<String, String> itemDetailsSourceInputKStream = streamsBuilder.stream(itemDetailsTopicName, Consumed.with(STRING_SERDE, STRING_SERDE));
		
		// Print itemDetailsSourceInputKStream
		itemDetailsSourceInputKStream.foreach((k, v) -> log.info("itemDetailsSourceInputKStream :: Key :: {}, Value :: {}", k, v));	
		
		// Change the Key of the message from ItemId + OrderId to only OrderId and parse the Json value to ItemDto
		KStream<String, ItemDto> itemDtoWithKeyAsOrderIdKStream = itemDetailsSourceInputKStream.map((itemIdOrderIdJson, itemDetailsJson) ->
        							new KeyValue<>(parseOrderId(itemIdOrderIdJson), parseItemDetails(itemDetailsJson)));
		
		// Print itemDtoWithKeyAsOrderIdKStream
		itemDtoWithKeyAsOrderIdKStream.foreach((k, v) -> log.info("itemDtoWithKeyAsOrderIdKStream :: Key :: {}, Value :: {}", k, v));
		
		// Group all the ItemDtos for each OrderId
		KGroupedStream<String, ItemDto> itemDtoWithKeyAsOrderIdKGroupedStream = itemDtoWithKeyAsOrderIdKStream
																						.groupByKey(Grouped.with(STRING_SERDE, ITEM_DTO_SERDE));
																						
		// Aggregate all the ItemDtos pertaining to each OrderId in a list
		KTable<String, ArrayList<ItemDto>> itemDtoListWithKeyAsOrderIdKTable = itemDtoWithKeyAsOrderIdKGroupedStream
				.aggregate((Initializer<ArrayList<ItemDto>>) ArrayList::new, 
						(orderId, itemDto, itemDtoList) -> addItemToList(itemDtoList, itemDto),
						Materialized.<String, ArrayList<ItemDto>, KeyValueStore<Bytes, byte[]>>as(ITEM_DTO_STATE_STORE).withKeySerde(STRING_SERDE).withValueSerde(ITEM_DTO_ARRAYLIST_SERDE));
		
		// Print itemDtoListWithKeyAsOrderIdKTable
		itemDtoListWithKeyAsOrderIdKTable.toStream().foreach((k, v) -> log.info("itemDtoListWithKeyAsOrderIdKTable :: Key :: {}, Value :: {}", k, v));
		
		
		
		// Joining the two tables: shippingDetailsDtoWithKeyAsOrderIdKTable and itemDtoListWithKeyAsOrderIdKTable
		ValueJoiner<ShippingDetailsDto, ArrayList<ItemDto>, OrderAggregate> shippingDetailsAndItemListJoiner = (shippingDetailsDto, itemDtoList) -> instantiateOrderAggregate(shippingDetailsDto, itemDtoList);
		
		
		// ValueJoiner<ShippingDetailsDto, ArrayList<ItemDto>, OrderAggregate> shippingDetailsAndItemListJoiner = (shippingDetailsDto, itemDtoList) -> new OrderAggregate(shippingDetailsDto, itemDtoList);
		
//		KTable<String, OrderAggregate> orderAggregateKTable = shippingDetailsDtoWithKeyAsOrderIdKTable.join(itemDtoListWithKeyAsOrderIdKTable, shippingDetailsAndItemListJoiner, Materialized.<String, OrderAggregate, KeyValueStore<Bytes, byte[]>>
//																									        as(ORDER_AGGREGATE_STATE_STORE)
//																									        .withKeySerde(STRING_SERDE)
//																									        .withValueSerde(ORDER_AGGREGATE_SERDE));
		KTable<String, OrderAggregate> orderAggregateKTable = shippingDetailsDtoWithKeyAsOrderIdKTable.join(itemDtoListWithKeyAsOrderIdKTable, shippingDetailsAndItemListJoiner);		
		
		// Printing orderAggregateKTable
		orderAggregateKTable.toStream().foreach((k, v) -> log.info("orderAggregateKTable :: Key :: {}, Value :: {}", k, v));
		
		// Outputting to Kafka Topic
		log.info("OUTPUT TOPIC NAME :: {}", orderAggregateTopicName);
		orderAggregateKTable.toStream().to(orderAggregateTopicName, Produced.with(STRING_SERDE, ORDER_AGGREGATE_SERDE));
	}
	
	private OrderAggregate instantiateOrderAggregate(ShippingDetailsDto shippingDetailsDto, ArrayList<ItemDto> itemDtoList) {
		OrderAggregate orderAggregate = new OrderAggregate();
		orderAggregate.setOrderId(shippingDetailsDto.getOrderId());
		
		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setCustomerAddress(shippingDetailsDto.getCustomerAddress());
		shippingDetails.setCustomerName(shippingDetailsDto.getCustomerName());
		shippingDetails.setZipCode(shippingDetailsDto.getZipCode());
		orderAggregate.setShippingDetails(shippingDetails);
		
		List<Item> items = new ArrayList<>();
		for(ItemDto itemDto : itemDtoList) {
			Item item = new Item();
			item.setItemId(itemDto.getItemId());
			item.setItemName(itemDto.getItemName());
			item.setPrice(itemDto.getPrice());
			item.setQuantity(itemDto.getQuantity());
			
			items.add(item);
		}
		orderAggregate.setItems(items);
		
		return orderAggregate;
	}
	
//	private ItemDetailsDto addItemToItemDetailsDto(ItemDetailsDto itemDetailsDto, ItemDto itemDto) {
//		itemDetailsDto.getItems().add(itemDto);
//		return itemDetailsDto;
//	}

	private ShippingDetailsDto parseShippingDetails(String shippingDetailsJson) {
		ShippingDetailsDto shippingDetailsDto = null;
		
		try {
			JsonNode shippingDetailsJsonNode = objectMapper.readTree(shippingDetailsJson);
			
			JsonNode payloadJsonNode = shippingDetailsJsonNode.get("payload");
			
			// String itemId = payloadJsonNode.get("item_id").asText();
			String orderId = payloadJsonNode.get("order_id").asText();
			String customerAddress = payloadJsonNode.get("customer_address").asText();
			String customerName = payloadJsonNode.get("customer_name").asText();
			String zipCode = payloadJsonNode.get("zipcode").asText();
			
			shippingDetailsDto = new ShippingDetailsDto();
			shippingDetailsDto.setOrderId(orderId);
			shippingDetailsDto.setCustomerAddress(customerAddress);
			shippingDetailsDto.setCustomerName(customerName);
			shippingDetailsDto.setZipCode(zipCode);
	
		} catch (JsonMappingException e) {
			// TODO
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO
			e.printStackTrace();
		}

		return shippingDetailsDto;
	}

	private ArrayList<ItemDto> addItemToList(ArrayList<ItemDto> itemDtoList, ItemDto itemDto) {
		itemDtoList.add(itemDto);
		return itemDtoList;
	}

	private String parseOrderId(String primaryKeyJson) {
		String orderId = null;
		
		try {
			JsonNode primaryKeyJsonNode = objectMapper.readTree(primaryKeyJson);
		
			JsonNode orderIdJsonNode = primaryKeyJsonNode.at("/payload/order_id");			
			orderId = orderIdJsonNode.asText();
		} catch (JsonMappingException e) {
			// TODO
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO
			e.printStackTrace();
		}

		return orderId;
	}

	private ItemDto parseItemDetails(String itemDetailsJson) {	
		ItemDto itemDto = null;
		
		try {
			JsonNode itemDetailsJsonNode = objectMapper.readTree(itemDetailsJson);
			
			JsonNode payloadJsonNode = itemDetailsJsonNode.get("payload");
			
			String itemId = payloadJsonNode.get("item_id").asText();
			// String orderId = payloadJsonNode.get("order_id").asText();
			String itemName = payloadJsonNode.get("item_name").asText();
			Double price = payloadJsonNode.get("price").asDouble(0);
			Integer quantity = payloadJsonNode.get("quantity").asInt(0);
			
//			JsonNode itemIdJsonNode = itemDetailsJsonNode.at("/payload/item_id");
//			String itemId = itemIdJsonNode.asText();
//			
//			JsonNode orderIdJsonNode = itemDetailsJsonNode.at("/payload/order_id");
//			String orderId = orderIdJsonNode.asText();
//			
//			JsonNode itemNameJsonNode = itemDetailsJsonNode.at("/payload/item_name");
//			String itemName = itemNameJsonNode.asText();
//			
//			JsonNode priceJsonNode = itemDetailsJsonNode.at("/payload/price");
//			Double price = priceJsonNode.asDouble(0);
//			
//			JsonNode quantityJsonNode = itemDetailsJsonNode.at("/payload/quantity");
//			Integer quantity = quantityJsonNode.asInt(0);			
			
			itemDto = new ItemDto();
			itemDto.setItemId(itemId);
			itemDto.setItemName(itemName);
			itemDto.setPrice(price);
			itemDto.setQuantity(quantity);
	
		} catch (JsonMappingException e) {
			// TODO
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO
			e.printStackTrace();
		}

		return itemDto;
	}
	
	
	
	
	
	
//	@Autowired
//	public void processMessage(StreamsBuilder streamsBuilder) {
//		
//		// Shipping Details Read
//		KStream<String, String> shippingDetailsSourceInputKStream = streamsBuilder.stream(shippingDetailsTopicName, Consumed.with(STRING_SERDE, STRING_SERDE));
//		
//		// Print shippingDetailsSourceInputKStream
//		shippingDetailsSourceInputKStream.foreach((k, v) -> log.info("shippingDetailsSourceInputKStream :: Key :: {}, Value :: {}", k, v));
//		
//		// Item Details Read
//		KStream<String, String> itemDetailsSourceInputKStream = streamsBuilder.stream(itemDetailsTopicName, Consumed.with(STRING_SERDE, STRING_SERDE));
//		
//		// Print itemDetailsSourceInputKStream
//		itemDetailsSourceInputKStream.foreach((k, v) -> log.info("itemDetailsSourceInputKStream :: Key :: {}, Value :: {}", k, v));		
//		
//		// itemDetailsSourceInputKStream.foreach((k, v) -> log.info("Item Id AFTER :: {}", parseJson(v)));
//	}
//
//	private String parseJson(String itemDetailsJson) {
//		String result = null;
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			JsonNode jsonNode = objectMapper.readTree(itemDetailsJson);
//			log.info("itemDetailsJson :: {}", itemDetailsJson);
//			log.info("jsonNode :: {}", objectMapper.writeValueAsString(jsonNode));
//			JsonNode jsonNode2 = jsonNode.get("payload");
//			result = objectMapper.writeValueAsString(jsonNode2);
//			log.info("result :: {}", result);
//			
//			JsonNode orderId = jsonNode2.at("/after/order_id");
//			
//			String orderIdAsText = orderId.asText();
//			log.info("orderIdAsText :: {}", orderIdAsText);
//			
//			String orderIdAswriteValueAsString = objectMapper.writeValueAsString(orderId);
//			log.info("orderIdAswriteValueAsString :: {}", orderIdAswriteValueAsString);
//			
//			
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//
//		return result;
//	}
}
