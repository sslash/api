package com.mikey.shredhub.api.domain;

import java.io.Serializable;

public class Tag implements Serializable {

	private String label;
	
	private int id;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
