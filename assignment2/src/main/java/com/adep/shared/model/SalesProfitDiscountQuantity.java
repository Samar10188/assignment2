package com.adep.shared.model;

import java.io.Serializable;

public class SalesProfitDiscountQuantity implements Serializable {

	private static final long serialVersionUID = -8358702367827623685L;

	private Double sales;
	private Double quantity;
	private Double discount;
	private Double profit;

	public Double getSales() {
		return sales;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

}
