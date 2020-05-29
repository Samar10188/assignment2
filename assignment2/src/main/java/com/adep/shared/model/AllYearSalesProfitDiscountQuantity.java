package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class AllYearSalesProfitDiscountQuantity implements Serializable {

	private static final long serialVersionUID = 239980884715160036L;

	private List<YearSalesProfitDiscountQuantity> yearsSalesProfitDiscountQuantity;
	private SalesProfitDiscountQuantity totalSalesProfitDiscountQuantity;

	public List<YearSalesProfitDiscountQuantity> getYearsSalesProfitDiscountQuantity() {
		return yearsSalesProfitDiscountQuantity;
	}

	public void setYearsSalesProfitDiscountQuantity(
			List<YearSalesProfitDiscountQuantity> yearsSalesProfitDiscountQuantity) {
		this.yearsSalesProfitDiscountQuantity = yearsSalesProfitDiscountQuantity;
	}

	public SalesProfitDiscountQuantity getTotalSalesProfitDiscountQuantity() {
		return totalSalesProfitDiscountQuantity;
	}

	public void setTotalSalesProfitDiscountQuantity(SalesProfitDiscountQuantity totalSalesProfitDiscountQuantity) {
		this.totalSalesProfitDiscountQuantity = totalSalesProfitDiscountQuantity;
	}

}
