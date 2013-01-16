package com.mikey.shredhub.api.domain;

import java.io.Serializable;

public class ShredderLevel implements Serializable {
	
	private double xp;
	private int level;
	public ShredderLevel(int xp, int level) {
		this.xp = xp;
		this.level = level;
	}
	
	public double getXp() {
		return xp;
	}
	public void setXp(double xp) {
		this.xp = xp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getLevelTitle() {
		return ShredderLevelMapper.getShredderLevel(level);
	}
	
	
}
