package com.purnima.jain.order.jpa.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.purnima.jain.order.jpa.entity.ShippingDetailsEntity;

@Repository
public interface ShippingDetailsRepository extends CrudRepository<ShippingDetailsEntity, String> {
	
}