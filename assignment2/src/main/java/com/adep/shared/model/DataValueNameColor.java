package com.adep.shared.model;

import java.io.Serializable;

public class DataValueNameColor implements Serializable {

	private static final long serialVersionUID = -3175406547260558814L;

	private Double y;
	private String name;
	private String color;

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
