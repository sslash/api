package com.mikey.shredhub.api.dao;

import java.sql.Date;
import java.util.List;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.newsitem.BattleShredNewsItem;

public interface BattleDAO {
	public int addBattleRequest(final int shredderId,final Date now, final int shredeeId, final String battleStyle);
	
	public void persistBattleShred(int battleId, int shredId, int round);
	
	public List<Battle> getOngoingBattlesForShredderWithId(int id);

	public List<Battle> getBattleRequestsForShredderWithId(int id);

	public Battle getBattleWithId(int id);

	public void persistBattle(Battle battle);
	
	public List<Shred> getShredsForBattleWithId (int id);
	
	public List<BattleShredNewsItem> getBattleShredsFromFanees(Shredder shredder);
	
	public List <Battle> getNewestBattlesFromFanees(int shredderId);
}
