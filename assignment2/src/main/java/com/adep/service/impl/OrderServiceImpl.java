package com.adep.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adep.dto.OrderDto;
import com.adep.entity.OrderEntity;
import com.adep.repository.OrderRepository;
import com.adep.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public OrderDto getOderByOderId(String orderId) throws Exception {

		Optional<OrderEntity> storedOrderEntity = orderRepository.findById(Long.parseLong(orderId));
		if (!storedOrderEntity.isPresent()) {
			throw new Exception("Order does not exit with id " + orderId);
		}
		OrderDto orderDto = new OrderDto();
		BeanUtils.copyProperties(storedOrderEntity.get(), orderDto);

		return orderDto;
	}

}