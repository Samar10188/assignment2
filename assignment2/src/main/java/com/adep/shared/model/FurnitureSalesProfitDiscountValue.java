package com.adep.shared.model;

import java.util.List;

public class FurnitureSalesProfitDiscountValue {
	
	private List<DataValueNameColor> salesData;
	private List<DataValueNameColor> profitData;
	private List<DataValueNameColor> discountData;
	private List<String> subCategory;

	public List<String> getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(List<String> subCategory) {
		this.subCategory = subCategory;
	}

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
