package com.mikey.shredhub.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.ShredderLevel;

public class ShredderMapper implements RowMapper <Shredder>{
	
	

	public Shredder mapRow(ResultSet rs, int rowNum) throws SQLException {
		Shredder shredder = new Shredder();
		shredder.setId(rs.getInt("sr_id"));
		shredder.setUsername(rs.getString("username"));
		shredder.setPassword(rs.getString("password"));
		shredder.setBirthdate(rs.getDate("BirthDate"));
		shredder.setCountry(rs.getString("Country"));
		shredder.setEmail(rs.getString("Email"));
		shredder.setDescription(rs.getString("sr_description"));
		shredder.setProfileImagePath(rs.getString("ProfileImage"));
		shredder.setLevel( new ShredderLevel(rs.getInt("ExperiencePoints"), rs.getInt("shredderLevel") ));		
		shredder.setTimeCreated(rs.getDate("sr_timeCreated"));
		
		try {
			shredder.addGuitar(rs.getString("guitar"));
			shredder.addEquiptment(rs.getString("equiptment"));	
			
		}catch(Exception e) {
			System.out.println("Error in shredder mapper: " + e.getMessage());
		}
		
		return shredder;
	}

	
}
