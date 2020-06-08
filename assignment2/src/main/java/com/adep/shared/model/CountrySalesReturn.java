package com.adep.shared.model;

public class CountrySalesReturn {

	private String country;
	private Double sales;
	private Double returnSales;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getSales() {
		return sales;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public Double getReturnSales() {
		return returnSales;
	}

	public void setReturnSales(Double returnSales) {
		this.returnSales = returnSales;
	}
}
