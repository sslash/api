package com.mikey.shredhub.api.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.BattleCategori;
import com.mikey.shredhub.api.domain.BattleShred;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.ShredBattler;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.newsitem.BattleShredNewsItem;
import com.mikey.shredhub.api.service.ShredNewsServiceImpl;

@Repository
public class BattleDAOImpl implements BattleDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(BattleDAOImpl.class);

	public int addBattleRequest(final int shredderId,final Date now, final int shredeeId, final String battleStyle) {
		
		
		final String sql = 
				"INSERT INTO Battle VALUES (DEFAULT, ?, ?, ?," +
				" (SELECT Id FROM BattleCategori WHERE CategoriText = ?),DEFAULT)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps =
		                connection.prepareStatement(sql, new String[] {"id"});
		            ps.setInt(1, shredderId);
		            ps.setInt(2, shredeeId);
		            ps.setDate(3, now);
		            ps.setString(4, battleStyle);
		            return ps;
		        }
		    },
		    keyHolder);
		return (Integer) keyHolder.getKey();					
}
	
	public void persistBattleShred(int battleId, int shredId, int round){				
		String battleSql = "INSERT INTO ShredForBattle VALUES (?, ?, ?)";
		jdbcTemplate.update(battleSql, shredId, battleId, round);
	}
	

	public List<Battle> getOngoingBattlesForShredderWithId(int id) {
		String sql = "SELECT * FROM Battle Where (Shredder1 = ? OR Shredder2 = ?) AND Status = 'accepted'";
		try {
			List <Battle> battles =  jdbcTemplate.query(sql, new Object[] { id, id}, 
					new BattleMapper() );
			this.populateBattles(battles);
			return battles;
			
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return new ArrayList<Battle>();
		}	
		
	}

	// Ugly hack to get populate the battle objects
	private void populateBattles(List<Battle> battles) {
		for ( Battle b : battles ) {
			populateBattleWithShreddersAndCategory(b);
		}		
	}
	/**
	 * Fill the battle object with necessary data
	 * @param battle
	 * @return
	 */
	private Battle populateBattleWithShreddersAndCategory(Battle battle) {
		String sql = "SELECT " + ShredderDAOImpl.SHREDDER_SQL + " FROM Shredder sr, GuitarForShredder " +
				"gs, EquiptmentForShredder es WHERE sr.id = ? AND sr.id = gs.ShredderId AND sr.id = es.ShredderId";
		
		Shredder shredder = jdbcTemplate.queryForObject(sql, 
				new Object []{battle.getBattler().getShredderId()}, new ShredderMapper());
		Shredder shreddee = jdbcTemplate.queryForObject(sql, 
				new Object []{battle.getBattlee().getShredderId()}, new ShredderMapper());
		
		battle.getBattler().setShredder(shredder);
		battle.getBattlee().setShredder(shreddee);
		
		BattleCategori categori = jdbcTemplate.queryForObject("SELECT * FROM BattleCategori WHERE Id=?",
				new Object []{battle.getBattleCategori().getId()}, new BattleCategoriMapper());
		battle.setBattleCategori(categori);
		return battle;
	}

	public List<Battle> getBattleRequestsForShredderWithId(int id) {
		String sql = "SELECT * FROM Battle Where Shredder2 = ? AND Status = 'awaiting'";
		try {
			List <Battle> battles = jdbcTemplate.query(sql, new Object [] {id},
					new BattleMapper() );
			this.populateBattles(battles);
			return battles;
			
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return new ArrayList<Battle>();
		}	
	}

	public Battle getBattleWithId(int id) {
		String sql = "SELECT * FROM Battle WHERE Id = ?";
		Battle battle = jdbcTemplate.queryForObject(sql, new Object [] {id}, new BattleMapper() );
		return populateBattleWithShreddersAndCategory(battle);
	}

	public void persistBattle(Battle battle) {		
		String sql = "UPDATE Battle SET Shredder1 = ?, Shredder2 = ?, " +
				"TimeCreated=?,BattleCategori=?,Status=?, Round=? WHERE Id=?";
		jdbcTemplate.update(sql, battle.getBattler().getShredderId(), battle.getBattlee().getShredderId(),
				battle.getTimeCreated(), battle.getBattleCategori().getId(), battle.getStatus(),
				battle.getRound(), battle.getId());
	}

	public List<Shred> getShredsForBattleWithId(int id) {
		String sql =  "SELECT " + ShredDAOImpl.SHRED_SQL + ", sb.round " +
				"FROM Shred s, ShredForBattle sb, Rating r WHERE sb.BattleId=? " +
				"AND s.Id = sb.ShredId AND r.ShredId=s.Id ORDER By sb.round";
		
		try {
			return jdbcTemplate.query(sql, new Object [] {id}, new BattleShredMapper());		
		} catch (DataAccessException e) {
			System.out.println("getShredsForBattleWithId: " + e.getMessage());
			return new ArrayList<Shred>();
		}	
	}
	
	/**
	 * SQL:
	 * SELECT s.Id as s_id, s.Description as s_description, s.Owner as s_owner,	s.TimeCreated as s_timeCreated, VideoPath, ShredType, s.thumbnailpath, sr.Id AS sr_id, sr.Username,	sr.BirthDate, sr.Email, sr.Password, sr.Description AS sr_description,	sr.Country, sr.TimeCreated AS sr_timeCreated, sr.ProfileImage, sr.ExperiencePoints, sr.ShredderLevel, sfb.round, sfb.BattleId, sfb.ShredId FROM ShredForBattle sfb, Shred s, Shredder sr WHERE sfb.ShredId = s.id AND s.owner = sr.id AND s.owner != 4078 AND s.owner IN (SELECT f.faneeid FROM Fan f WHERE f.fanerid = 4078)  ORDER BY s.timecreated DESC;
			
	 */
	// SELECT s.Id as s_id, s.Description as s_description, s.Owner as s_owner, 
	//s.TimeCreated as s_timeCreated, VideoPath, ShredType, s.thumbnailpath, r.currentRating, 
	//r.numberOfRaters, sr.Id AS sr_id, sr.Username, sr.BirthDate, sr.Email, sr.Password, 
	//sr.Description AS sr_description, sr.Country, sr.TimeCreated AS sr_timeCreated, 
	//sr.ProfileImage, sr.ExperiencePoints, sr.ShredderLevel, gs.guitar, es.equiptment,
	//sfb.round, sfb.BattleId FROM ShredForBattle sfb, Shred s, Shredder sr, Rating r, 
	//GuitarForShredder gs, EquiptmentForShredder es WHERE sfb.ShredId = s.id AND
	//r.ShredId=s.Id and s.owner = sr.id AND s.owner != ? AND sr.id = gs.ShredderId
	//AND sr.id = es.ShredderId AND s.owner IN (SELECT f.FaneeId FROM Fan f WHERE f.FanerId = ?)
	//ORDER BY s.timecreated DESC
	public List <BattleShredNewsItem> getBattleShredsFromFanees(Shredder shredder){
		StringBuilder sql = new StringBuilder("SELECT ")
		.append("s.Owner as s_owner, s.TimeCreated as s_timeCreated, ")
		.append("sr.Id AS sr_id, sr.Username as sr_un, sfb.BattleId as b_Id ")
		.append("FROM ShredForBattle sfb, Shred s, Shredder sr ")
		.append("WHERE sr.id IN")
		.append("(SELECT f.FaneeId FROM Fan f WHERE f.FanerId = ?) ")
		.append("AND sfb.ShredId = s.id AND s.owner = sr.id AND sr.id != ? ")
		.append("ORDER BY s.timecreated DESC LIMIT 20");
		return jdbcTemplate.query(sql.toString(), new Object[] {shredder.getId(),shredder.getId()},
				new RowMapper<BattleShredNewsItem>() {
			public BattleShredNewsItem mapRow(ResultSet rs, int rowNum ) throws SQLException {
				
				BattleShred shred = new BattleShred();
				shred.setTimeCreated(rs.getDate("s_timeCreated"));
				Shredder shredder = new Shredder(rs.getInt("s_owner"));
				shredder.setUsername(rs.getString("sr_un"));				
				shred.setOwner(shredder);				
				BattleShredNewsItem newsItem = 
						new BattleShredNewsItem(shred, shred.getTimeCreated(), rs.getInt("b_Id"));
				return newsItem;
			}					
		});
	}
	
	public List<Battle> getNewestBattlesFromFanees(int shredderId) {
		StringBuilder sql = new StringBuilder("SELECT b.id as b_id, s1.username as s1_u, s2.username as s2_u,  s1.id as s1_id, ")
		.append("s2.id as s2_id, s1.ProfileImage as s1_pi, s2.ProfileImage as s2_pi FROM Battle b, Shredder s1, Shredder s2 ")
		.append("WHERE b.status = 'accepted' AND b.shredder1 != ? AND b.shredder2 != ? AND ")
		.append("(shredder1 IN (SELECT f.FaneeId FROM Fan f, Shredder s WHERE f.FanerId = s.id AND f.FanerId = ?) ") 
		.append("OR shredder2 IN (SELECT f.FaneeId FROM Fan f, Shredder s WHERE f.FanerId = s.id AND f.FanerId = ?)) ") 
		.append("AND b.Shredder1 = s1.id AND b.Shredder2 = s2.id ORDER BY b.TimeCreated DESC LIMIT 20");

		try {
			List <Battle> battles = jdbcTemplate.query(
					sql.toString(),
					new Object [] {shredderId, shredderId, shredderId, shredderId},
					new RowMapper() {
						public Battle mapRow(ResultSet rs, int rowNum ) throws SQLException {
							Battle battle = new Battle();
							battle.setId(rs.getInt("b_id"));
							Shredder s1 = new Shredder(rs.getInt("s1_id"));
							s1.setUsername(rs.getString("s1_u"));
							s1.setProfileImagePath(rs.getString("s1_pi"));
							Shredder s2 = new Shredder(rs.getInt("s2_id"));
							s2.setUsername(rs.getString("s2_u"));
							s2.setProfileImagePath(rs.getString("s2_pi"));
							ShredBattler battler = new ShredBattler( s1);
							ShredBattler battlee = new ShredBattler( s2);		
							battle.setBattler(battler);
							battle.setBattlee(battlee);
							return battle;
						}					
					}
				);
			return battles;
			
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return new ArrayList<Battle>();
		}	
	}
}
