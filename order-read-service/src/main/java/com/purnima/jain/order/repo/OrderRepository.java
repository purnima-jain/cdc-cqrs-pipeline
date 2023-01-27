package com.purnima.jain.order.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.purnima.jain.order.domain.OrderWrapper;

@Repository
public interface OrderRepository extends MongoRepository<OrderWrapper, String> {

}
