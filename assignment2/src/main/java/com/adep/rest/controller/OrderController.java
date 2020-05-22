package com.adep.rest.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adep.dto.OrderDto;
import com.adep.rest.response.OrderModelResponse;
import com.adep.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public OrderModelResponse getOderByOderId(@RequestParam String orderId) throws Exception {

		OrderDto orderDto = orderService.getOderByOderId(orderId);

		OrderModelResponse orderModelResponse = new OrderModelResponse();

		BeanUtils.copyProperties(orderDto, orderModelResponse);

		return orderModelResponse;
	}

}
