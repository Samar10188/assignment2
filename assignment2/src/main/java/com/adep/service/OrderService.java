package com.adep.service;

import com.adep.dto.OrderDto;

public interface OrderService {
	OrderDto getOderByOderId(String orderId) throws Exception;
}
