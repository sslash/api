package com.mikey.shredhub.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A shredder in a battle
 * 
 * @author michaekg
 *
 */
public class ShredBattler implements Serializable {

	private Shredder shredder;
	
	private List <BattleShred> shreds;
	
	private boolean currentBattler = false;
	
	private int battlePoints;
	

	public ShredBattler(Shredder shredder) {
		this.shredder = shredder;
	}

	public boolean isCurrentBattler() {
		return currentBattler;
	}

	public void setCurrentBattler(boolean currentBattler) {
		this.currentBattler = currentBattler;
	}

	public Shredder getShredder() {
		return shredder;
	}

	public void setShredder(Shredder shredder) {
		this.shredder = shredder;
	}

	public List<BattleShred> getShreds() {
		return shreds;
	}

	public void setShreds(List<BattleShred> shreds) {
		this.shreds = shreds;
	}

	public int getBattlePoints() {
		return battlePoints;
	}

	public void setBattlePoints(int battlePoints) {
		this.battlePoints = battlePoints;
	}

	public int getShredderId() {
		return shredder.getId();
	}

	public void addBattleShred(Shred s) {
		if( shreds == null ) {
			shreds = new ArrayList<BattleShred>();
		}
		// Just to be sure..
		if ( s instanceof BattleShred)
			shreds.add((BattleShred)s);
	}

	public void addBattlePoints(int currentRating) {
		this.battlePoints += currentRating;		
	}
}
