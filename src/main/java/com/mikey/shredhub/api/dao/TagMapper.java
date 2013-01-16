package com.mikey.shredhub.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mikey.shredhub.api.domain.Tag;

public class TagMapper implements RowMapper<Tag>{

	public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
		Tag t = new Tag();
		t.setId(rs.getInt("id"));
		t.setLabel(rs.getString("label"));
		return t;
	}

}
