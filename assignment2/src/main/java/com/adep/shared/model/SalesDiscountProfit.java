package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class SalesDiscountProfit implements Serializable{

	private static final long serialVersionUID = 6577358539758589946L;
	
	private List<Double> dataSales;
	private List<Double> dataDiscount;
	private List<Double> dataProfit;

	public List<Double> getDataSales() {
		return dataSales;
	}

	public void setDataSales(List<Double> dataSales) {
		this.dataSales = dataSales;
	}

	public List<Double> getDataDiscount() {
		return dataDiscount;
	}

	public void setDataDiscount(List<Double> dataDiscount) {
		this.dataDiscount = dataDiscount;
	}

	public List<Double> getDataProfit() {
		return dataProfit;
	}

	public void setDataProfit(List<Double> dataProfit) {
		this.dataProfit = dataProfit;
	}

}
