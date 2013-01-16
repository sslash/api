package com.mikey.shredhub.api.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Battle implements Serializable {
	
	public static final String STATUS_ACCEPTED = "accepted";
	
	public static final String STATUS_DECLINED = "declined";
	
	public static final String STATUS_AWAITING = "awaiting";
	
	private int id;
	
	private ShredBattler battler;
	
	private ShredBattler battlee;
	
	private Date timeCreated;
	
	private BattleCategori battleCategori;
	
	private String status;
	
	private int round;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public BattleCategori getBattleCategori() {
		return battleCategori;
	}

	public void setBattleCategori(BattleCategori battleCategori) {
		this.battleCategori = battleCategori;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRound(int battleRound) {
		this.round = battleRound;		
	}
	
	public int getRound() {
		return this.round;
	}

	public ShredBattler getBattler() {
		return battler;
	}

	public void setBattler(ShredBattler battler) {
		this.battler = battler;
	}

	public ShredBattler getBattlee() {
		return battlee;
	}

	public void setBattlee(ShredBattler battlee) {
		this.battlee = battlee;
	}

	public void initBattle() {
		this.battler.setCurrentBattler(true);		
	}

	public void populateBattlesRoundAndPoints(List<Shred> shredBattles) {
		for ( Shred s: shredBattles ) {
			ShredBattler owner = null;
			if ( s.getOwner().equals(battler.getShredder())) {
				owner = battler;	
			} else if ( s.getOwner().equals( battlee.getShredder())) {
				owner = battlee;
			} else {
				continue;
			}
			owner.addBattleShred(s);
			owner.addBattlePoints(s.getRating().getCurrentRating());
		}
		
		setCurrentBattler(shredBattles.size());
	}
	
private void setCurrentBattler(int size) {
		if ( size % 2 == 0 ) {
			System.out.println("current: battler");
			battler.setCurrentBattler(true);
			battlee.setCurrentBattler(false);
		} else {
			System.out.println("current: battlee, id = " + battlee.getShredderId());
			battlee.setCurrentBattler(true);
			battler.setCurrentBattler(false);
		}		
	}

	public ShredBattler getLeader() {
		if ( battler.getBattlePoints() > battlee.getBattlePoints() ) {
			return battler;
		} else if  ( battlee.getBattlePoints() > battler.getBattlePoints()) {
			return battlee;
		} else {
		// even. TODO: Generate null object implementation
		return null;
		}
	}
	
	public Shredder getCurrentBattler() {
		if ( battler.isCurrentBattler() ) {
			return battler.getShredder();
		} else {
			return battlee.getShredder();
		}
	}
}
