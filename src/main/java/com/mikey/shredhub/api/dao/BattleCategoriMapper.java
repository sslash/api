package com.mikey.shredhub.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mikey.shredhub.api.domain.BattleCategori;

public class BattleCategoriMapper implements RowMapper<BattleCategori> {

	public BattleCategori mapRow(ResultSet rs, int rowNum) throws SQLException {
		BattleCategori battleCategori = new BattleCategori();
		battleCategori.setId(rs.getInt("Id"));
		battleCategori.setCategoriText(rs.getString("CategoriText"));
		return battleCategori;
	}

}
