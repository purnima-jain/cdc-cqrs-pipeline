package com.purnima.jain.order.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.purnima.jain.order.jpa.entity.ItemDetailsEntity;
import com.purnima.jain.order.jpa.entity.OrderItemId;

@Repository
public interface ItemDetailsRepository extends JpaRepository<ItemDetailsEntity, OrderItemId> {

	List<ItemDetailsEntity> findByOrderId(String orderId);

}