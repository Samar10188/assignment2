package com.adep.shared.model;

import java.io.Serializable;

public class DataSalesDiscountProfit implements Serializable {

	private static final long serialVersionUID = -7049071664505274116L;

	private Double sales;
	private Double profit;
	private Double discount;
	private String subCategory;
	private String category;

	public Double getSales() {
		return sales;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
