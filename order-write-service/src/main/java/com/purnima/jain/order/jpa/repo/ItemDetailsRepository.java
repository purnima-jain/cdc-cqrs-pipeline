package com.purnima.jain.order.jpa.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.purnima.jain.order.jpa.entity.ItemDetailsEntity;
import com.purnima.jain.order.jpa.entity.OrderItemId;

@Repository
public interface ItemDetailsRepository extends CrudRepository<ItemDetailsEntity, OrderItemId> {
	
}