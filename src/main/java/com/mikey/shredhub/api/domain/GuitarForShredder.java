package com.mikey.shredhub.api.domain;

public class GuitarForShredder {
	
	private String name;
	
	private int diggs;
	
	private String imgPath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDiggs() {
		return diggs;
	}

	public void setDiggs(int diggs) {
		this.diggs = diggs;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
}
