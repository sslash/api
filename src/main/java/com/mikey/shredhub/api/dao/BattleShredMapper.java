package com.mikey.shredhub.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mikey.shredhub.api.domain.BattleShred;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.ShredRating;
import com.mikey.shredhub.api.domain.Shredder;

public class BattleShredMapper extends ShredMapper {

	@Override
	protected Shred setConcreteShredder() {
		return new BattleShred();
	}
	
	@Override
	protected void addConcreteBehavior(ResultSet rs, Shred shred, int rowNum) throws SQLException {
		((BattleShred)shred).setRound(rs.getInt("round"));
		
		// Battle shreds doesn't need owners. They are in the battle object already
		shred.setOwner( new Shredder (rs.getInt("s_owner")));	
	}
}
