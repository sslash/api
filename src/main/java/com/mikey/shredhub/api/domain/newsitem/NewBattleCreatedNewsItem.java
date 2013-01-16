package com.mikey.shredhub.api.domain.newsitem;

import com.mikey.shredhub.api.domain.Battle;

public class NewBattleCreatedNewsItem extends ShredNewsItem {
	
	private Battle battle;

	public NewBattleCreatedNewsItem(Battle battle) {
		super(battle.getTimeCreated());
		this.battle = battle;
	}

	public Battle getBattle() {
		return battle;
	}

	public void setBattle(Battle battle) {
		this.battle = battle;
	}
}
