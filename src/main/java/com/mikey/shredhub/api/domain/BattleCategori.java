package com.mikey.shredhub.api.domain;

import java.io.Serializable;

public class BattleCategori implements Serializable {
	
	private String categoriText;

	private int id;
	
	
	public BattleCategori(int id) {
		this.id = id;
	}

	public BattleCategori() {
		// TODO Auto-generated constructor stub
	}

	public String getCategoriText() {
		return categoriText;
	}

	public void setCategoriText(String categoriText) {
		this.categoriText = categoriText;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
