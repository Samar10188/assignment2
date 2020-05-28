package com.adep.shared.model;

import java.io.Serializable;

public class DataCategoryProfitSalesDiscount implements Serializable {

	private static final long serialVersionUID = -1848292523159674125L;

	private DataCategory dataCategory;
	private TechnologyDataCategory technologyDataCategory;
	private FurnitureDataCategory furnitureDataCategory;
	private OfficeSupplyDataCategory officeSupplyDataCategory;

	public DataCategory getDataCategory() {
		return dataCategory;
	}

	public void setDataCategory(DataCategory dataCategory) {
		this.dataCategory = dataCategory;
	}

	public TechnologyDataCategory getTechnologyDataCategory() {
		return technologyDataCategory;
	}

	public void setTechnologyDataCategory(TechnologyDataCategory technologyDataCategory) {
		this.technologyDataCategory = technologyDataCategory;
	}

	public FurnitureDataCategory getFurnitureDataCategory() {
		return furnitureDataCategory;
	}

	public void setFurnitureDataCategory(FurnitureDataCategory furnitureDataCategory) {
		this.furnitureDataCategory = furnitureDataCategory;
	}

	public OfficeSupplyDataCategory getOfficeSupplyDataCategory() {
		return officeSupplyDataCategory;
	}

	public void setOfficeSupplyDataCategory(OfficeSupplyDataCategory officeSupplyDataCategory) {
		this.officeSupplyDataCategory = officeSupplyDataCategory;
	}

}
