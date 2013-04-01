package com.mikey.shredhub.api.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mikey.shredhub.api.domain.GuitarForShredder;
import com.mikey.shredhub.api.domain.Shredder;

@Service
public class ShredderDAOImpl implements ShredderDAO {

	private static final Logger logger = LoggerFactory.getLogger(ShredderDAOImpl.class);
	
	private static final String GET_SHREDDER_ID_BY_NAME = "SELECT id FROM Shredder WHERE Username=?";
	

	public static final String SHREDDER_SQL = "sr.Id AS sr_id, sr.Username, " +
			"sr.BirthDate, sr.Email, sr.Password, sr.Description AS sr_description, " +
			"sr.Country, sr.TimeCreated AS sr_timeCreated, sr.ProfileImage, " +
			"sr.ExperiencePoints, sr.ShredderLevel, gs.guitar AS guitarName, " +
			"gs.ImgPath AS guitarImgPath, gs.Digs as guitarDigs, es.equiptment";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addShredder(Shredder shredder) {
		String sql = "INSERT INTO Shredder VALUES (DEFAULT,?,?,?,?,?,?,DEFAULT, ?)";
		jdbcTemplate.update(sql, shredder.getUsername(),
				shredder.getBirthdate(), shredder.getEmail(),
				shredder.getPassword(), shredder.getDescription(),
				shredder.getCountry(), shredder.getProfileImagePath());
		shredder.setId(queryForIdentity(shredder.getUsername()));
		
		insertGuitarsForShredder(shredder.getId(), shredder.getGuitars());
		insertEquiptmentForShredder(shredder.getId(), shredder.getEquiptment());
		
		insertUserRole(shredder.getId());
	}
	
	
	
	private void insertUserRole(int id) {
		jdbcTemplate.update("INSERT INTO UserRole VALUES (DEFAULT, ?, DEFAULT)", id);		
	}


	private void insertGuitarsForShredder(int id, List<GuitarForShredder> list) {
		for ( GuitarForShredder g : list) {
			jdbcTemplate.update("INSERT INTO GuitarForShredder VALUES(?,?)",
					g.getName(), id);
		}
	}

	private void insertEquiptmentForShredder(int id, List <String> equiptment) {
		for ( String g : equiptment) {
			jdbcTemplate.update("INSERT INTO EquiptmentForShredder VALUES(?,?)",
					g, id);
		}
	}

	private int queryForIdentity(String username) {
		return jdbcTemplate.queryForInt(GET_SHREDDER_ID_BY_NAME, username);
	}
	
	public Shredder getShredderByUsernameAndPassword(final String username,
			final String password) {
		String sql = "SELECT "+SHREDDER_SQL+" FROM Shredder sr, GuitarForShredder " +
		"gs, EquiptmentForShredder es WHERE sr.id = gs.ShredderId and sr.id = es.ShredderId AND Username=?";
		
		try {
			List<Shredder> res = jdbcTemplate.query(sql, new Object [] {username}, new ShredderMapper());
			Shredder toRet = res.get(0);
			if ( !password.equals(toRet.getPassword()))
				return null;
			return toRet;
		} catch (DataAccessException e) {
			System.err.println("failed to get user: " + e.getMessage() + " " + e.getLocalizedMessage());
			return null;
		}

	}

	// This one and the one below is totally equal. Could do something with that..
	public List<Shredder> getAllShredders(int page) {
		String sql = "SELECT " + SHREDDER_SQL + " FROM Shredder sr, GuitarForShredder " +
		"gs, EquiptmentForShredder es WHERE sr.id = gs.ShredderId and sr.id = es.ShredderId "
				+ " ORDER BY timeCreated DESC LIMIT 20 OFFSET " + page*20;
		
		try {
			return jdbcTemplate.query(sql, new ShredderMapper());
		} catch (DataAccessException e) {
			System.out.println("getAllShredders: " + e.getMessage());
			return new ArrayList<Shredder>();
		}				
	}

	public List<Shredder> getFansForShredderWithId(int id) {
		try {
			String sql = "SELECT "+SHREDDER_SQL+" FROM Shredder sr, GuitarForShredder " +
		"gs, EquiptmentForShredder es WHERE sr.id = gs.ShredderId and sr.id = es.ShredderId " +
		"AND sr.Id IN (SELECT FaneeId FROM Fan WHERE FanerId=?)";
			return jdbcTemplate.query(sql, new Object [] {id}, new ShredderMapper());
		} catch (DataAccessException e) {
			System.out.println("getFansForShredderWithId " + e.getMessage());
			return new ArrayList<Shredder>();
		}				
	}

	public void createFanRelation(int faner, int fanee) {
		String sql = "INSERT INTO Fan VALUES(?,?,DEFAULT)";
		int res = jdbcTemplate.update(sql, faner, fanee);
	}

	public Shredder getShredderById(int id) {
		System.out.println("get shredder by id: " + id);
		try {
			String sql = "SELECT "+SHREDDER_SQL+" FROM Shredder sr, GuitarForShredder " +
					"gs, EquiptmentForShredder es WHERE sr.id = gs.ShredderId " +
					"and sr.id = es.ShredderId AND sr.Id=?";
			List<Shredder> shredderList = jdbcTemplate.query(sql, new Object [] {id}, new ShredderMapper());
			Shredder sh = shredderList.get(0);
			// Simple solution to get fanees. Could have done it in one sql as well
			sh.setFanees(this.getFansForShredderWithId(id));
			return sh;
		
		} catch (DataAccessException e) {
			System.out.println("getShredderById: " + e.getMessage());
			return null;
		}
	}

	public void persistShredder(Shredder shredder) {
		jdbcTemplate.update("UPDATE Shredder SET Username=?, Birthdate=?,Email=?,Password=?" +
				",Description=?,Country=?,ProfileImage=?,ExperiencePoints=?, ShredderLevel=?" +
				"WHERE Id=?", shredder.getUsername(), shredder.getBirthdate(), shredder.getEmail(),
				shredder.getPassword(), shredder.getDescription(), shredder.getCountry(), shredder.getProfileImagePath(),
				shredder.getLevel().getXp(), shredder.getLevel().getLevel(), shredder.getId());
		
		// TODO: Persist guitar for shredder
	}



	// TODO: Add restriction to check if the result is a fanee
	public List<Shredder> getPotentialFaneesForShredder(Shredder shredder) {
		StringBuilder sql = new StringBuilder("SELECT Distinct ON (sr.id)"+SHREDDER_SQL+
				" FROM Shredder sr, GuitarForShredder " +
				"gs, EquiptmentForShredder es WHERE sr.id = gs.ShredderId and sr.id = es.ShredderId " +
				"and sr.id != ").append(shredder.getId()).append(" and (sr.Country LIKE '%")
				.append(shredder.getCountry()).append("%' ");
		
		// Add guitars
		for ( GuitarForShredder g : shredder.getGuitars() ) {
			sql.append("OR gs.guitar like '").append(g.getName()).append("' ");
		}
		
		// Add equiptment
		for ( String e : shredder.getEquiptment() ) {
			sql.append("OR es.Equiptment like '").append(e).append("' ");
		}
		sql.append(")");
		
		// Add not fanee restriction
		sql.append(" AND sr.id NOT IN ( SELECT faneeId FROM Fan WHERE fanerId = " + shredder.getId() + ") LIMIT 20" );
		
		logger.debug("getPotentialFaneesForShredder() SQL: " + sql.toString());
		try {
			List<Shredder> res = jdbcTemplate.query(sql.toString(), new ShredderMapper());
			return res;
		} catch (DataAccessException e) {
			return new ArrayList<Shredder>();
		}
	}



	public boolean addDiggForGuitar(String id, String guitarName) {
		int shredderId = Integer.parseInt(id);
		String SQL = "UPDATE GuitarForShredder SET Digs = Digs+? WHERE ShredderId=? AND Guitar=?";
		try{
			jdbcTemplate.update(SQL, 1, shredderId, guitarName);
			return true;
		}catch(DataAccessException e) {
			return false;
		}
	}

}
