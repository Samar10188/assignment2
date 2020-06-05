package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class AllCountrySalesReturn implements Serializable {

	private static final long serialVersionUID = 1624015313514516306L;

	private List<CountrySalesReturn> countrySalesReturns;
	private Double totalReturnedSales;
	private Double totalSales;
	private Double totalTopReturnedSales;
	private Double totalTopSales;

	public List<CountrySalesReturn> getCountrySalesReturns() {
		return countrySalesReturns;
	}

	public void setCountrySalesReturns(List<CountrySalesReturn> countrySalesReturns) {
		this.countrySalesReturns = countrySalesReturns;
	}

	public Double getTotalReturnedSales() {
		return totalReturnedSales;
	}

	public void setTotalReturnedSales(Double totalReturnedSales) {
		this.totalReturnedSales = totalReturnedSales;
	}

	public Double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(Double totalSales) {
		this.totalSales = totalSales;
	}

	public Double getTotalTopReturnedSales() {
		return totalTopReturnedSales;
	}

	public void setTotalTopReturnedSales(Double totalTopReturnedSales) {
		this.totalTopReturnedSales = totalTopReturnedSales;
	}

	public Double getTotalTopSales() {
		return totalTopSales;
	}

	public void setTotalTopSales(Double totalTopSales) {
		this.totalTopSales = totalTopSales;
	}

}
