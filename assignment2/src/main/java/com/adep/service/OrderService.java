package com.adep.service;

import java.util.List;

import com.adep.dto.OrderDto;
import com.adep.shared.model.SalesProfitDiscountValue;
import com.adep.shared.model.SeriesData;

public interface OrderService {
	OrderDto getOderByOderId(String orderId) throws Exception;

	List<SeriesData> getDataSalesDiscountProfit();
	
	SalesProfitDiscountValue getSalesProfitDiscountValueByCountry(String country);

}
