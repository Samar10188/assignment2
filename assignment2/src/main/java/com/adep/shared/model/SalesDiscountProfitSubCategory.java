package com.adep.shared.model;

import java.io.Serializable;

public class SalesDiscountProfitSubCategory implements Serializable {

	private static final long serialVersionUID = -6333085102396177065L;

	private Double sales;
	private Double profit;
	private Double discount;
	private String subCategory;

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

}
