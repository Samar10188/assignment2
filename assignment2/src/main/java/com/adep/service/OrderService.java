package com.adep.service;

import java.util.List;

import com.adep.dto.OrderDto;
import com.adep.shared.model.AllCountrySalesReturn;
import com.adep.shared.model.AllSalesDiscountProfitValue;
import com.adep.shared.model.AllYearSalesProfitDiscountQuantity;
import com.adep.shared.model.RegionSalesDiscountProfit;
import com.adep.shared.model.SeriesData;

public interface OrderService {
	OrderDto getOderByOderId(String orderId) throws Exception;

	List<SeriesData> getDataSalesDiscountProfit();

	AllSalesDiscountProfitValue getSalesProfitDiscountValueByCountry(String country);

	AllYearSalesProfitDiscountQuantity getByYear(String category, String subCategory, String region, String segment);

	RegionSalesDiscountProfit getDataSubCategorySalesDiscountProfit(int top);

	AllCountrySalesReturn findRetunedOrder(int top);
}
