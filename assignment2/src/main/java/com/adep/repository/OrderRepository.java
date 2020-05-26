package com.adep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adep.entity.OrderEntity;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

	Optional<OrderEntity> findById(Long orderId);

	List<OrderEntity> findByCountry(String country);
}
