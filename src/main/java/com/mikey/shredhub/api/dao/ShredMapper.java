package com.mikey.shredhub.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.ShredRating;
import com.mikey.shredhub.api.domain.Shredder;

/**
 * This is the supertype for a shredmapper.
 * Its not that flexible, considering that I am using
 * inheritance. Another approach would be to use the strategy
 * pattern to delegate (populate) behavior..
 * 
 * @author michaekg
 *
 */
public class ShredMapper implements RowMapper <Shred>{

	public Shred mapRow(ResultSet rs, int rowNum) throws SQLException {
		// Template method!
		Shred shred = this.setConcreteShredder();
		shred.setId(rs.getInt("s_id"));
		shred.setDescription(rs.getString("s_description"));
		shred.setTimeCreated(rs.getDate("s_timeCreated"));
		shred.setThumbnailpath(rs.getString("thumbnailpath"));
		shred.setVideoPath(rs.getString("VideoPath"));
		shred.setShredType(rs.getString("ShredType"));
		// add rating 
		ShredRating sr = new ShredRating();
		sr.setCurrentRating(rs.getInt("currentRating"));
		sr.setNumberOfRaters(rs.getInt("numberOfRaters"));
		shred.setRating(sr);
		this.addConcreteBehavior(rs, shred, rowNum);
		return shred;
	}

	protected Shred setConcreteShredder() {
		return new Shred();
	}

	// Normal shreds needs owners
	protected void addConcreteBehavior(ResultSet rs, Shred shred, int rowNum) throws SQLException {
		shred.setOwner( new ShredderMapper().mapRow(rs, rowNum));		
	}

}
