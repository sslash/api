package com.mikey.shredhub.api.domain;

import java.io.Serializable;

public class BattleShred extends Shred implements Serializable{ 
	
	private int round;
	

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	@Override
	public String toString() {
		return "BattleShred [round=" + round + ", getShredType()="
				+ getShredType() + ", getId()=" + getId()
				+ ", getDescription()=" + getDescription() + ", getOwner()="
				+ getOwner() + ", getTimeCreated()=" + getTimeCreated()
				+ ", getVideoPath()=" + getVideoPath() + ", getTags()="
				+ getTags() + ", getShredComments()=" + getShredComments()
				+ ", getRating()=" + getRating() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}
}
