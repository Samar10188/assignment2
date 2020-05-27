package com.adep.rest.response;

import java.io.Serializable;

import com.adep.shared.model.AllSalesDiscountProfitValue;

public class SalesProfitDiscountValueResponse implements Serializable {

	private static final long serialVersionUID = -5805859752155691104L;

	private AllSalesDiscountProfitValue salesProfitDiscountValue;

	public AllSalesDiscountProfitValue getSalesProfitDiscountValue() {
		return salesProfitDiscountValue;
	}

	public void setSalesProfitDiscountValue(AllSalesDiscountProfitValue salesProfitDiscountValue) {
		this.salesProfitDiscountValue = salesProfitDiscountValue;
	}

}
