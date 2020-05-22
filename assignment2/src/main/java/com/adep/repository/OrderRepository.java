package com.adep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adep.entity.OrderEntity;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

}
