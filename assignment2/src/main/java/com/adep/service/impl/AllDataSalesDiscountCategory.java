package com.adep.service.impl;

import java.io.Serializable;
import java.util.List;

import com.adep.shared.model.DataSalesDiscountProfit;

public class AllDataSalesDiscountCategory implements Serializable {

	private static final long serialVersionUID = 1997150490552405300L;

	private List<DataSalesDiscountProfit> allDataSalesDiscountProfit;
	private List<DataSalesDiscountProfit> technologyDataSalesDiscountProfit;
	private List<DataSalesDiscountProfit> furnitureDataSalesDiscountProfit;
	private List<DataSalesDiscountProfit> officeSupplyDataSalesDiscountProfit;

	public List<DataSalesDiscountProfit> getAllDataSalesDiscountProfit() {
		return allDataSalesDiscountProfit;
	}

	public void setAllDataSalesDiscountProfit(List<DataSalesDiscountProfit> allDataSalesDiscountProfit) {
		this.allDataSalesDiscountProfit = allDataSalesDiscountProfit;
	}

	public List<DataSalesDiscountProfit> getTechnologyDataSalesDiscountProfit() {
		return technologyDataSalesDiscountProfit;
	}

	public void setTechnologyDataSalesDiscountProfit(List<DataSalesDiscountProfit> technologyDataSalesDiscountProfit) {
		this.technologyDataSalesDiscountProfit = technologyDataSalesDiscountProfit;
	}

	public List<DataSalesDiscountProfit> getFurnitureDataSalesDiscountProfit() {
		return furnitureDataSalesDiscountProfit;
	}

	public void setFurnitureDataSalesDiscountProfit(List<DataSalesDiscountProfit> furnitureDataSalesDiscountProfit) {
		this.furnitureDataSalesDiscountProfit = furnitureDataSalesDiscountProfit;
	}

	public List<DataSalesDiscountProfit> getOfficeSupplyDataSalesDiscountProfit() {
		return officeSupplyDataSalesDiscountProfit;
	}

	public void setOfficeSupplyDataSalesDiscountProfit(
			List<DataSalesDiscountProfit> officeSupplyDataSalesDiscountProfit) {
		this.officeSupplyDataSalesDiscountProfit = officeSupplyDataSalesDiscountProfit;
	}

}
