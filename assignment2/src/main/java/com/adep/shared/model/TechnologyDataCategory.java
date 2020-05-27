package com.adep.shared.model;

import java.util.List;

public class TechnologyDataCategory {

	private List<DataSalesDiscountProfit> dataAccessories;
	private List<DataSalesDiscountProfit> dataCopiers;
	private List<DataSalesDiscountProfit> dataMachines;
	private List<DataSalesDiscountProfit> dataPhones;

	public List<DataSalesDiscountProfit> getDataAccessories() {
		return dataAccessories;
	}

	public void setDataAccessories(List<DataSalesDiscountProfit> dataAccessories) {
		this.dataAccessories = dataAccessories;
	}

	public List<DataSalesDiscountProfit> getDataCopiers() {
		return dataCopiers;
	}

	public void setDataCopiers(List<DataSalesDiscountProfit> dataCopiers) {
		this.dataCopiers = dataCopiers;
	}

	public List<DataSalesDiscountProfit> getDataMachines() {
		return dataMachines;
	}

	public void setDataMachines(List<DataSalesDiscountProfit> dataMachines) {
		this.dataMachines = dataMachines;
	}

	public List<DataSalesDiscountProfit> getDataPhones() {
		return dataPhones;
	}

	public void setDataPhones(List<DataSalesDiscountProfit> dataPhones) {
		this.dataPhones = dataPhones;
	}

}
