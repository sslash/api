package com.mikey.shredhub.api.domain;

import java.io.Serializable;

public class ShredRating implements Serializable {
	
	private int currentRating;
	
	private int numberOfRaters;

	public int getCurrentRating() {
		return currentRating;
	}

	public void setCurrentRating(int currentRating) {
		this.currentRating = currentRating;
	}

	public int getNumberOfRaters() {
		return numberOfRaters;
	}

	public void setNumberOfRaters(int numberOfRaters) {
		this.numberOfRaters = numberOfRaters;
	}
	
	public int getRating() {
		if ( currentRating == 0 ) return 0;
		else {
			return currentRating/numberOfRaters;
		}
	}
	
	

}
