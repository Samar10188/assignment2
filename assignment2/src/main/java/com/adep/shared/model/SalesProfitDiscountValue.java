package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class SalesProfitDiscountValue implements Serializable {

	private static final long serialVersionUID = -8361672472982019562L;

	private List<DataValueNameColor> salesData;
	private List<DataValueNameColor> profitData;
	private List<DataValueNameColor> discountData;

	public List<DataValueNameColor> getSalesData() {
		return salesData;
	}

	public void setSalesData(List<DataValueNameColor> salesData) {
		this.salesData = salesData;
	}

	public List<DataValueNameColor> getProfitData() {
		return profitData;
	}

	public void setProfitData(List<DataValueNameColor> profitData) {
		this.profitData = profitData;
	}

	public List<DataValueNameColor> getDiscountData() {
		return discountData;
	}

	public void setDiscountData(List<DataValueNameColor> discountData) {
		this.discountData = discountData;
	}

}
