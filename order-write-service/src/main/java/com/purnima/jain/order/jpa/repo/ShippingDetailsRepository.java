package com.purnima.jain.order.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.purnima.jain.order.jpa.entity.ShippingDetailsEntity;

@Repository
public interface ShippingDetailsRepository extends JpaRepository<ShippingDetailsEntity, String> {

}