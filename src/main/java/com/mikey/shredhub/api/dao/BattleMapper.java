package com.mikey.shredhub.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.BattleCategori;
import com.mikey.shredhub.api.domain.ShredBattler;
import com.mikey.shredhub.api.domain.Shredder;


public class BattleMapper implements RowMapper<Battle> {


	public Battle mapRow(ResultSet rs, int rowNum) throws SQLException {
		Battle battle = new Battle();
		battle.setId(rs.getInt("Id"));
		
		// Set battlers
		ShredBattler battler = new ShredBattler( new Shredder(rs.getInt("Shredder1")) );
		ShredBattler battlee = new ShredBattler( new Shredder(rs.getInt("Shredder2")) );		
		battle.setBattler(battler);
		battle.setBattlee(battlee);
	
		BattleCategori categori = new BattleCategori(rs.getInt("BattleCategori"));
		battle.setBattleCategori(categori);
		
		battle.setStatus(rs.getString("Status"));
		battle.setTimeCreated(rs.getDate("TimeCreated"));
		battle.setRound(rs.getInt("Round"));
		return battle;
	}

}
