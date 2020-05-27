package com.adep.rest.response;

import java.io.Serializable;

import com.adep.shared.model.SalesProfitDiscountValue;

public class SalesProfitDiscountValueResponse implements Serializable {

	private static final long serialVersionUID = -5805859752155691104L;

	private SalesProfitDiscountValue salesProfitDiscountValue;

	public SalesProfitDiscountValue getSalesProfitDiscountValue() {
		return salesProfitDiscountValue;
	}

	public void setSalesProfitDiscountValue(SalesProfitDiscountValue salesProfitDiscountValue) {
		this.salesProfitDiscountValue = salesProfitDiscountValue;
	}

}
