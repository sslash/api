package com.mikey.shredhub.api.domain;

public class ShredderLevelMapper {
	
	private final static String beginner = "Beginner";
	private final static String skilled = "Skilled";
	private final static String awesome = "Awesome";
	private final static String shredKing ="Shred king";
	private final static String wizard = "Wizard";
	
	public static String getShredderLevel(int currLvl) {
		
		if (  currLvl < 20 ) {
			return beginner;
		} else if (  currLvl < 40 ) {
			return skilled;
		} else if (  currLvl < 60 ) {
			return awesome;
		} else if (  currLvl < 80 ) {
			return shredKing;
		}else {
			return wizard;
		}
	}

}
