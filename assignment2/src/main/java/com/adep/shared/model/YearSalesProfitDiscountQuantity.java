package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class YearSalesProfitDiscountQuantity implements Serializable {

	private static final long serialVersionUID = 8730011370725408560L;

	private List<SalesProfitDiscountQuantity> dataYear;
	private SalesProfitDiscountQuantity totalData;

	public List<SalesProfitDiscountQuantity> getDataYear() {
		return dataYear;
	}

	public void setDataYear(List<SalesProfitDiscountQuantity> dataYear) {
		this.dataYear = dataYear;
	}

	public SalesProfitDiscountQuantity getTotalData() {
		return totalData;
	}

	public void setTotalData(SalesProfitDiscountQuantity totalData) {
		this.totalData = totalData;
	}

}
