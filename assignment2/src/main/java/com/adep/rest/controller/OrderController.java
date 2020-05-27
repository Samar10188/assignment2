package com.adep.rest.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adep.dto.OrderDto;
import com.adep.rest.response.OrderModelResponse;
import com.adep.rest.response.SalesProfitDiscountValueResponse;
import com.adep.rest.response.SeriesDataResponse;
import com.adep.service.OrderService;
import com.adep.shared.model.SalesProfitDiscountValue;
import com.adep.shared.model.SeriesData;

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

	@GetMapping("/getDataSalesDiscountProfit")
	public SeriesDataResponse getDataSalesDiscountProfit() {

		List<SeriesData> seriesData = orderService.getDataSalesDiscountProfit();

		SeriesDataResponse seriesDataResponse = new SeriesDataResponse();
		seriesDataResponse.setSeriesData(seriesData);

		return seriesDataResponse;
	}

	@GetMapping("/salesProfitDiscountValue")
	public SalesProfitDiscountValueResponse getSalesProfitDiscountValueByCountry(
			@RequestParam(value = "country", defaultValue = "world", required = false) String country) {

		SalesProfitDiscountValue salesProfitDiscountValue = orderService.getSalesProfitDiscountValueByCountry(country);
		SalesProfitDiscountValueResponse salesProfitDiscountValueResponse = new SalesProfitDiscountValueResponse();
		salesProfitDiscountValueResponse.setSalesProfitDiscountValue(salesProfitDiscountValue);

		return salesProfitDiscountValueResponse;
	}

}
