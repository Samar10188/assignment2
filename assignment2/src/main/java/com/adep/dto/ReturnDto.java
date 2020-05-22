package com.adep.dto;

import java.io.Serializable;

public class ReturnDto implements Serializable{

	private static final long serialVersionUID = -2734475462647358741L;
	
	private String returned;
	private String orderId;
	private String market;
	
	public String getReturned() {
		return returned;
	}
	public void setReturned(String returned) {
		this.returned = returned;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
}
