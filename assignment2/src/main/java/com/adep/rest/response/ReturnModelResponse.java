package com.adep.rest.response;

import java.io.Serializable;

public class ReturnModelResponse implements Serializable{

	private static final long serialVersionUID = 7577934692743810025L;

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
