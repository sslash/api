package com.mikey.shredhub.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mikey.shredhub.api.domain.BattleShred;
import com.mikey.shredhub.api.domain.newsitem.BattleShredNewsItem;

public class BattleShredNewsItemMapper implements RowMapper <BattleShredNewsItem>{

	public BattleShredNewsItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		BattleShred shred = new BattleShred();
		shred.setId(rs.getInt("s_id"));
		shred.setDescription(rs.getString("s_description"));
		shred.setTimeCreated(rs.getDate("s_timeCreated"));
		shred.setVideoPath(rs.getString("VideoPath"));
		shred.setShredType(rs.getString("ShredType"));
		shred.setRound(rs.getInt("round"));
		shred.setOwner(new ShredderMapper().mapRow(rs, rowNum));
		
		BattleShredNewsItem newsItem = 
				new BattleShredNewsItem(shred, shred.getTimeCreated(), rs.getInt("battleid"));
		
		return newsItem;		
	}

}
