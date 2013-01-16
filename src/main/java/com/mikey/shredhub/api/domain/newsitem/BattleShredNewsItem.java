package com.mikey.shredhub.api.domain.newsitem;

import java.sql.Date;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.BattleShred;

public class BattleShredNewsItem extends ShredNewsItem {
	
	private BattleShred battleShred;
	
	private int battleId;

	public BattleShredNewsItem(BattleShred battleShred, Date timeCreated, int i) {
		super(timeCreated);
		this.battleShred = battleShred;
		battleId = i;
	}

	public BattleShred getBattleShred() {
		return battleShred;
	}

	public void setBattleShred(BattleShred battleShred) {
		this.battleShred = battleShred;
	}

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}	
	
	
}
