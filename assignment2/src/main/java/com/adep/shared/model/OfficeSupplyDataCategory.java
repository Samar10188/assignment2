package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class OfficeSupplyDataCategory implements Serializable{

	private static final long serialVersionUID = 2817847997847031226L;
	
	private List<DataSalesDiscountProfit> dataAppliances;
	private List<DataSalesDiscountProfit> dataArt;
	private List<DataSalesDiscountProfit> dataBinders;
	private List<DataSalesDiscountProfit> dataEnvelopes;
	private List<DataSalesDiscountProfit> dataFasteners;
	private List<DataSalesDiscountProfit> dataLabels;
	private List<DataSalesDiscountProfit> dataPaper;
	private List<DataSalesDiscountProfit> dataStorage;
	private List<DataSalesDiscountProfit> dataSupplies;

	public List<DataSalesDiscountProfit> getDataAppliances() {
		return dataAppliances;
	}

	public void setDataAppliances(List<DataSalesDiscountProfit> dataAppliances) {
		this.dataAppliances = dataAppliances;
	}

	public List<DataSalesDiscountProfit> getDataArt() {
		return dataArt;
	}

	public void setDataArt(List<DataSalesDiscountProfit> dataArt) {
		this.dataArt = dataArt;
	}

	public List<DataSalesDiscountProfit> getDataBinders() {
		return dataBinders;
	}

	public void setDataBinders(List<DataSalesDiscountProfit> dataBinders) {
		this.dataBinders = dataBinders;
	}

	public List<DataSalesDiscountProfit> getDataEnvelopes() {
		return dataEnvelopes;
	}

	public void setDataEnvelopes(List<DataSalesDiscountProfit> dataEnvelopes) {
		this.dataEnvelopes = dataEnvelopes;
	}

	public List<DataSalesDiscountProfit> getDataFasteners() {
		return dataFasteners;
	}

	public void setDataFasteners(List<DataSalesDiscountProfit> dataFasteners) {
		this.dataFasteners = dataFasteners;
	}

	public List<DataSalesDiscountProfit> getDataLabels() {
		return dataLabels;
	}

	public void setDataLabels(List<DataSalesDiscountProfit> dataLabels) {
		this.dataLabels = dataLabels;
	}

	public List<DataSalesDiscountProfit> getDataPaper() {
		return dataPaper;
	}

	public void setDataPaper(List<DataSalesDiscountProfit> dataPaper) {
		this.dataPaper = dataPaper;
	}

	public List<DataSalesDiscountProfit> getDataStorage() {
		return dataStorage;
	}

	public void setDataStorage(List<DataSalesDiscountProfit> dataStorage) {
		this.dataStorage = dataStorage;
	}

	public List<DataSalesDiscountProfit> getDataSupplies() {
		return dataSupplies;
	}

	public void setDataSupplies(List<DataSalesDiscountProfit> dataSupplies) {
		this.dataSupplies = dataSupplies;
	}

}
