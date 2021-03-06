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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.mikey.shredhub.api.domain.BattleShred;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.ShredComment;
import com.mikey.shredhub.api.domain.ShredRating;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.Tag;
import com.mikey.shredhub.api.domain.newsitem.BattleShredNewsItem;

@Service
public class ShredDAOImpl implements ShredDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String FANEES_SQL = "SELECT faneeid FROM Fan WHERE FanerId=?";

	private static final String GET_ALL_TAGS = "SELECT * FROM Tag";

	private static final String GET_TAG_WITH_LABEL = "SELECT * FROM Tag WHERE Label=?";
	
	private static final int NO_SHREDS_IN_RESULT_SET = 20;
	private static final Logger logger = LoggerFactory.getLogger(ShredDAOImpl.class);

	public static final String SHRED_SQL = "s.Id as s_id, "
			+ "s.Description as s_description, s.Owner as s_owner, "
			+ "s.TimeCreated as s_timeCreated, VideoPath, ShredType, s.thumbnailpath, "
			+ "r.currentRating, r.numberOfRaters";

	public List<Shred> getShredsFromFaneesForShredderWithId(int shredderId, int page) {		
		StringBuilder sql = new StringBuilder("SELECT ")
		.append("sr.username as sr_u, sr.id as sr_id, s.ThumbnailPath as s_tp, ")
		.append("s.id as s_id, s.description as s_desc ")
		.append("FROM Shred s, Shredder sr ")
		.append("WHERE s.Owner IN (" + FANEES_SQL + ") AND s.Owner = sr.id AND ShredType ='normal' ")
		.append("ORDER BY s.TimeCreated DESC LIMIT ")
		.append(NO_SHREDS_IN_RESULT_SET + " OFFSET " + page*NO_SHREDS_IN_RESULT_SET);
		
		
		
		long t1 = System.currentTimeMillis();
		List<Shred> res =  jdbcTemplate.query(sql.toString(), new Object[] { shredderId },
				new RowMapper<Shred>() {
			public Shred mapRow(ResultSet rs, int rowNum ) throws SQLException {
				Shred s = new Shred();
				s.setId(rs.getInt("s_id"));
				s.setThumbnailpath(rs.getString("s_tp"));
				s.setDescription(rs.getString("s_desc"));
				Shredder sh = new Shredder(rs.getInt("sr_id"));
				sh.setUsername(rs.getString("sr_u"));
				s.setOwner(sh);
				return s;				
			}					
		});
				
				
		System.out.println("shreds from fanees: " + (System.currentTimeMillis() - t1));
		logger.info("shreds from fanees: " + (System.currentTimeMillis() - t1));
		
		return res;
		
		
	}

	public List<Tag> getTagsForShredWithId(int id) {
		String sql = "SELECT * FROM Tag WHERE ID in (SELECT TagId FROM "
				+ "TagsForShred WHERE ShredId=?) LIMIT 20";
		try {
			return jdbcTemplate
					.query(sql, new Object[] { id }, new TagMapper());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return new ArrayList<Tag>();
		}
	}

	public List<ShredComment> getShredCommentsForShredWithId(int id) {
		String sql = "SELECT c.id, c.Text, c.TimeCreated, c.Commenter, "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ " FROM CommentForShred c, Shredder sr, GuitarForShredder "
				+ "gs, EquiptmentForShredder es WHERE c.Shred=? AND "
				+ "sr.id = gs.ShredderId and sr.id = es.ShredderId AND sr.Id= " +
				"c.Commenter";
		try {
			return jdbcTemplate.query(sql, new RowMapper<ShredComment>() {

				public ShredComment mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					ShredComment sc = new ShredComment();
					sc.setId(rs.getInt("id"));
					sc.setText(rs.getString("Text"));
					sc.setTimeCreated(rs.getDate("TimeCreated"));
					sc.setCommenter(new ShredderMapper().mapRow(rs, rowNum));
					return sc;
				}
			}, id);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return new ArrayList<ShredComment>();
		}
	}

	public List<Tag> getAllTags() {
		try {
			return (List<Tag>) jdbcTemplate
					.query(GET_ALL_TAGS, new TagMapper());
		} catch (DataAccessException e) {
			System.out.println("error getting tags: " + e.getMessage());
			return new ArrayList<Tag>();
		}
	}

	public Tag getWithLabel(String tagLabel) {
		try {
			return jdbcTemplate.queryForObject(GET_TAG_WITH_LABEL,
					new Object[] { tagLabel }, new TagMapper());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public int persistShred(final Shred shred) {

		// Persist shred
		long t1 = System.currentTimeMillis();		
		
		final String sql = "INSERT INTO Shred VALUES (DEFAULT,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,
						new String[] { "id" });
				ps.setString(1, shred.getDescription());
				ps.setLong(2, shred.getOwner().getId());
				ps.setDate(3, shred.getTimeCreated());
				ps.setString(4, shred.getVideoPath());
				ps.setString(5, shred.getShredType());
				ps.setString(6, shred.getThumbnailpath());
				return ps;
			}
		}, keyHolder);

		shred.setId((Integer) keyHolder.getKey());

		// Persist tags
		String insertTag = "INSERT INTO TagsForShred VALUES (?,?)";
		;
		if (shred.getTags() != null) {
			for (Tag t : shred.getTags()) {
				this.jdbcTemplate.update(insertTag, shred.getId(), t.getId());
			}
		}

		// insert rating for this shred
		jdbcTemplate.update("INSERT INTO Rating VALUES(?, DEFAULT, DEFAULT)",
				shred.getId());

		System.out.println("Persist shred: " + (System.currentTimeMillis() - t1));
		logger.info("Persist shred: " + (System.currentTimeMillis() - t1));
		
		return shred.getId();
	}

	public void addCommentForShred(String text, int shredId, int shredderId,
			Date date) {
		String sql = "INSERT INTO CommentForShred VALUES(DEFAULT, ?,?,?,?)";
		jdbcTemplate.update(sql, text, shredderId, date, shredId);

	}

	public ShredRating getRatingForShredWithId(int shredId) {
		String sql = "SELECT * FROM Rating WHERE ShredId = " + shredId;
		try {
			return jdbcTemplate.queryForObject(sql,
					new RowMapper<ShredRating>() {

						public ShredRating mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							ShredRating rate = new ShredRating();
							rate.setCurrentRating(rs.getInt("currentRating"));
							rate.setNumberOfRaters(rs.getInt("numberOfRaters"));
							return rate;
						}
					});
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void persistRate(int shredId, ShredRating currentRating) {

		long t1 = System.currentTimeMillis();
		
		jdbcTemplate
				.update("UPDATE Rating SET currentRating=?, numberOfRaters=? WHERE ShredId = ?",
						currentRating.getCurrentRating(),
						currentRating.getNumberOfRaters(), shredId);
		System.out.println("Update rating: " + (System.currentTimeMillis() - t1));
		logger.info("Update rating: " + (System.currentTimeMillis() - t1));
	}

	private List<Shred> getShredsFromSQLString(String sql, ShredMapper mapper) {
		return getShredsFromSQLString(sql, new Object[] {}, mapper);
	}

	List<Shred> getShredsFromSQLString(String sql, Object[] params,
			ShredMapper mapper) {
		try {
			List<Shred> shreds = jdbcTemplate.query(sql, params, mapper);

			// Populate
			for (Shred shred : shreds) {
				populateShred(shred);
			}
			return shreds;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return new ArrayList<Shred>();
		}
	}

	private Shred populateShred(Shred shred) {
		// int ownerId = shred.getOwner().getId();
		// shred.setOwner(jdbcTemplate.queryForObject(
		// ShredderDAOImpl.GET_SHREDDER_BY_ID,
		// new Object [] {ownerId}, new ShredderMapper()
		// ));

		// Set rating
		String ratesql = "SELECT * FROM Rating WHERE ShredId=" + shred.getId();
		ShredRating rating = jdbcTemplate.queryForObject(ratesql,
				new BeanPropertyRowMapper<ShredRating>(ShredRating.class));
		shred.setRating(rating);
		shred.setShredComments(this.getShredCommentsForShredWithId(shred
				.getId()));
		// Set tags
		shred.setTags(this.getTagsForShredWithId(shred.getId()));
		return shred;
	}

	public Shred getShredById(int shredId) {
		String sql = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE s.Owner = sr.Id AND s.Id = ? "
				+ "AND r.ShredId = s.id AND gs.ShredderId = sr.id AND es.ShredderId = sr.id";
		return jdbcTemplate.queryForObject(sql, new Object[] { shredId },
				new ShredMapper());
	}

	// Shreds made by shredder with id @id
	public List<Shred> getShredsForShredderWithId(int id) {
		String sql = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE s.Owner = sr.Id AND Owner = ? AND ShredType='normal' AND "
				+ "r.ShredId = s.id AND gs.ShredderId = sr.id AND es.ShredderId = sr.id LIMIT " + NO_SHREDS_IN_RESULT_SET ;
		return getShredsFromSQLString(sql, new Object[] { id },
				new ShredMapper());
	}

	/**
	 * 
	 */
	public List<Shred> getShredsOrderedByRating(int limit, int page) {
		String sql = "SELECT " + ShredderDAOImpl.SHREDDER_SQL + ", "
				+ SHRED_SQL + " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE r.ShredId = s.id AND "
				+ "gs.ShredderId = sr.id AND es.ShredderId = sr.id AND s.owner = sr.id"
				+ " AND s.ShredType = 'normal'"
				+ " ORDER BY CASE WHEN r.currentRating <> 0 AND r.numberOfRaters <> 0"
				+ "THEN r.currentRating/r.numberOfRaters ELSE 0 END desc LIMIT ? OFFSET " + page*limit;

		long t1 = System.currentTimeMillis();
		List<Shred> res =  getShredsFromSQLString(sql, new Object[] { limit },
				new ShredMapper());
		System.out.println("rating: " + (System.currentTimeMillis() - t1));
		logger.info("rating: " + (System.currentTimeMillis() - t1));
		
		return res;
	}

	
	
	/**
	 * Gets shred created by shredder that is fanee of shredders that are fanee
	 * of shredder with id shredderId! Limit result set is 20 shreds.
	 * 
	 * SQL string: 
	 * SELECT sr.Id AS sr_id, sr.Username,sr.BirthDate, sr.Email, sr.Password, sr.Description AS sr_description,sr.Country,
	 *  sr.TimeCreated AS sr_timeCreated, sr.ProfileImage, sr.ExperiencePoints, sr.ShredderLevel, gs.guitar, es.equiptment,
	 *  s.Id as s_id,s.Description as s_description, s.Owner as s_owner, s.TimeCreated as s_timeCreated, VideoPath,
	 *  ShredType, s.thumbnailpath, r.currentRating, r.numberOfRaters  FROM Shred s,Rating r, Shredder sr, 
	 *  GuitarForShredder gs, EquiptmentForShredder es  WHERE r.ShredId = s.id AND gs.ShredderId = sr.id AND
	 *  es.ShredderId = sr.id AND s.Owner IN (SELECT distinct faneeid FROM Fan WHERE FanerId IN 
	 *  (SELECT FaneeId FROM Fan WHERE FanerId = 4078) AND faneeid != 4078 ORDER BY faneeid DESC);
	 */
	public List<Shred> getShredsFromShreddersShredderMightKnow(int shredderId, int page) {
		String shreddersMightKnow = "SELECT distinct faneeid FROM Fan WHERE FanerId IN "
				+ "(SELECT FaneeId FROM Fan WHERE FanerId = ?)  AND faneeid !=  ? ORDER BY faneeid DESC";
		String sql = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es  WHERE r.ShredId = s.id AND "
				+ "gs.ShredderId = sr.id AND es.ShredderId = sr.id AND s.Owner IN ("
				+ shreddersMightKnow + ") LIMIT " + NO_SHREDS_IN_RESULT_SET + " OFFSET " + page*21; // Simply because it fits the view. Should be a parameter though..
		
		long t1 = System.currentTimeMillis();
		List<Shred> res =  getShredsFromSQLString(sql, new Object[] { shredderId,
				shredderId }, new ShredMapper());
		System.out.println("Shreds shredder might know: " + (System.currentTimeMillis() - t1));
		logger.info("Shreds shredder might know: " + (System.currentTimeMillis() - t1));
		return res;
		
	}
	
	
	public List<Shred> getShredsWithTagsInTagsList(List<String> tags) {
		StringBuilder shredId = new StringBuilder(
				"SELECT s.Id FROM Shred s, TagsForShred tfs, Tag t  ");
		shredId.append("WHERE tfs.ShredId = s.id AND tfs.TagId = t.Id AND ");
		shredId.append("(");
		for (int i = 0; i < tags.size() - 1; i++) {
			shredId.append(" t.Label='").append(tags.get(i)).append("' OR ");
		}
		shredId.append(" t.Label='").append(tags.get(tags.size() - 1))
				.append("' )");
		shredId.append(" Group By ( s.id) HAVING (Count(s.id) >= ?)");

		String selectShreds = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE r.ShredId = s.id AND "
				+ "gs.ShredderId = sr.id AND es.ShredderId = sr.id AND s.Id IN ("
				+ shredId.toString() + ") AND s.Owner = sr.id LIMIT " + NO_SHREDS_IN_RESULT_SET;

		
		long t1 = System.currentTimeMillis();
		List<Shred> res =  getShredsFromSQLString(selectShreds,
				new Object[] { tags.size() }, new ShredMapper());
		System.out.println("Shreds by tags: " + (System.currentTimeMillis() - t1));
		logger.info("Shreds by tags: " + (System.currentTimeMillis() - t1));
		return res;
		
	}

	public List<Shred> getAllShreds() {
		String sql = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE r.ShredId = s.id AND "
				+ "gs.ShredderId = sr.id AND es.ShredderId = sr.id AND " +
				"ShredType='normal' AND s.owner = sr.id ORDER BY s.TimeCreated DESC LIMIT " + NO_SHREDS_IN_RESULT_SET;
		return getShredsFromSQLString(sql, new ShredMapper());
	}

	public void deleteCommentForShred(int commentId) {
		
		long t1 = System.currentTimeMillis();
		String SQL = "DELETE FROM CommentForShred WHERE Id = " + commentId;
		jdbcTemplate.execute(SQL);
		System.out.println("Delete comment: " + (System.currentTimeMillis() - t1));
		logger.info("Delete comment: " + (System.currentTimeMillis() - t1));
		
	}	
	
}
