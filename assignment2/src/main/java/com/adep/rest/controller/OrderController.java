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
import com.adep.shared.model.AllCountrySalesReturn;
import com.adep.shared.model.AllSalesDiscountProfitValue;
import com.adep.shared.model.AllYearSalesProfitDiscountQuantity;
import com.adep.shared.model.RegionSalesDiscountProfit;
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

		if (country.contains("world")) {
			country = ".*";
		}

		AllSalesDiscountProfitValue salesProfitDiscountValue = orderService
				.getSalesProfitDiscountValueByCountry(country);
		SalesProfitDiscountValueResponse salesProfitDiscountValueResponse = new SalesProfitDiscountValueResponse();
		salesProfitDiscountValueResponse.setSalesProfitDiscountValue(salesProfitDiscountValue);

		return salesProfitDiscountValueResponse;
	}

	@GetMapping("/year/salesProfitDiscountValue")
	public AllYearSalesProfitDiscountQuantity getYearSalesProfitDiscountValueByCountry(
			@RequestParam(value = "category", defaultValue = "all", required = false) String category,
			@RequestParam(value = "subCategory", defaultValue = "all", required = false) String subCategory,
			@RequestParam(value = "region", defaultValue = "all", required = false) String region,
			@RequestParam(value = "segment", defaultValue = "all", required = false) String segment) {
		if (category.contains("all")) {
			category = ".*";
		}
		if (subCategory.contains("all")) {
			subCategory = ".*";
		}
		if (region.contains("all")) {
			region = ".*";
		}
		if (segment.contains("all")) {
			segment = ".*";
		}

		AllYearSalesProfitDiscountQuantity allYearSalesProfitDiscountQuantity = orderService.getByYear(category,
				subCategory, region, segment);

		return allYearSalesProfitDiscountQuantity;
	}

	@GetMapping("/getProfitByRegion")
	public RegionSalesDiscountProfit getDataSalesDiscountProfitByRegion(
			@RequestParam(value = "top", defaultValue = "13", required = false) String top) {

		RegionSalesDiscountProfit regionSalesDiscountProfit = orderService
				.getDataSubCategorySalesDiscountProfit(Integer.parseInt(top));

		return regionSalesDiscountProfit;
	}

	@GetMapping("/getDataCountryReturned")
	public AllCountrySalesReturn getDataCountryReturned(
			@RequestParam(value = "top", defaultValue = "15", required = false) String top) {

		AllCountrySalesReturn allCountrySalesReturn = orderService.findRetunedOrder(Integer.parseInt(top));

		return allCountrySalesReturn;
	}

}
