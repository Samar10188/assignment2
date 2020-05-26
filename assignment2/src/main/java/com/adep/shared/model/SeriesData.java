package com.adep.shared.model;

import java.io.Serializable;
import java.util.List;

public class SeriesData implements Serializable {

	private static final long serialVersionUID = -186827153399105494L;

	private String name;
	private List<Double> data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Double> getData() {
		return data;
	}

	public void setData(List<Double> data) {
		this.data = data;
	}

}
