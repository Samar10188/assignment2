package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class FurnitureDataCategory implements Serializable {

	private static final long serialVersionUID = 566364862699898444L;

	private List<DataSalesDiscountProfit> dataBookcases;
	private List<DataSalesDiscountProfit> dataChairs;
	private List<DataSalesDiscountProfit> dataFurnishings;
	private List<DataSalesDiscountProfit> dataTables;

	public List<DataSalesDiscountProfit> getDataBookcases() {
		return dataBookcases;
	}

	public void setDataBookcases(List<DataSalesDiscountProfit> dataBookcases) {
		this.dataBookcases = dataBookcases;
	}

	public List<DataSalesDiscountProfit> getDataChairs() {
		return dataChairs;
	}

	public void setDataChairs(List<DataSalesDiscountProfit> dataChairs) {
		this.dataChairs = dataChairs;
	}

	public List<DataSalesDiscountProfit> getDataFurnishings() {
		return dataFurnishings;
	}

	public void setDataFurnishings(List<DataSalesDiscountProfit> dataFurnishings) {
		this.dataFurnishings = dataFurnishings;
	}

	public List<DataSalesDiscountProfit> getDataTables() {
		return dataTables;
	}

	public void setDataTables(List<DataSalesDiscountProfit> dataTables) {
		this.dataTables = dataTables;
	}

}
