package com.mikey.shredhub.api.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.ShredComment;
import com.mikey.shredhub.api.domain.ShredRating;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.Tag;

@Service
public class ShredDAOImpl implements ShredDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String GET_FANS = "SELECT faneeid FROM Fan WHERE FanerId=?";

	private static final String GET_ALL_TAGS = "SELECT * FROM Tag";

	private static final String GET_TAG_WITH_LABEL = "SELECT * FROM Tag WHERE Label=?";

	public static final String SHRED_SQL = "s.Id as s_id, "
			+ "s.Description as s_description, s.Owner as s_owner, "
			+ "s.TimeCreated as s_timeCreated, VideoPath, ShredType, "
			+ "r.currentRating, r.numberOfRaters";

	public List<Shred> getShredsFromFaneesForShredderWithId(int shredderId) {
		String sql = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s, Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es " + "WHERE s.Owner IN ("
				+ GET_FANS + ") "
				+ "AND s.Owner = sr.id AND sr.id = gs.ShredderId AND "
				+ "sr.id = es.ShredderId AND ShredType='normal' AND "
				+ "r.ShredId = s.id ORDER BY s.TimeCreated DESC";
		return getShredsFromSQLString(sql, new Object[] { shredderId },
				new ShredMapper());
	}

	public List<Tag> getTagsForShredWithId(int id) {
		String sql = "SELECT * FROM Tag WHERE ID in (SELECT TagId FROM "
				+ "TagsForShred WHERE ShredId=?)";
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
		final String sql = "INSERT INTO Shred VALUES (DEFAULT,?,?,?,?,?)";
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
		jdbcTemplate
				.update("UPDATE Rating SET currentRating=?, numberOfRaters=? WHERE ShredId = ?",
						currentRating.getCurrentRating(),
						currentRating.getNumberOfRaters(), shredId);
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

	public List<Shred> getShredsForShredderWithId(int id) {
		String sql = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE s.Owner = sr.Id AND Owner = ? AND ShredType='normal' AND "
				+ "r.ShredId = s.id AND gs.ShredderId = sr.id AND es.ShredderId = sr.id";
		return getShredsFromSQLString(sql, new Object[] { id },
				new ShredMapper());
	}

	/**
	 * Does not care if the shred is a normal or battle shred
	 */
	public List<Shred> getShredsOrderedByRating(int limit) {
		String sql = "SELECT " + ShredderDAOImpl.SHREDDER_SQL + ", "
				+ SHRED_SQL + " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE r.ShredId = s.id AND "
				+ "gs.ShredderId = sr.id AND es.ShredderId = sr.id"
				+ " ORDER BY CASE WHEN r.currentRating <> 0 "
				+ "THEN r.currentRating/r.numberOfRaters ELSE 0 END desc LIMIT ?";

		return getShredsFromSQLString(sql, new Object[] { limit },
				new ShredMapper());
	}

	/**
	 * Gets shred created by shredder that is fanee of shredders that are fanee
	 * of shredder with id shredderId!
	 */
	public List<Shred> getShredsFromShreddersShredderMightKnow(int shredderId) {
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
				+ shreddersMightKnow + ")";
		return getShredsFromSQLString(sql, new Object[] { shredderId,
				shredderId }, new ShredMapper());
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

		System.out.println("sql : " + shredId.toString());

		String selectShreds = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE r.ShredId = s.id AND "
				+ "gs.ShredderId = sr.id AND es.ShredderId = sr.id AND s.Id IN ("
				+ shredId.toString() + ")";

		return getShredsFromSQLString(selectShreds,
				new Object[] { tags.size() }, new ShredMapper());
	}

	public List<Shred> getAllShreds() {
		String sql = "SELECT "
				+ ShredderDAOImpl.SHREDDER_SQL
				+ ", "
				+ SHRED_SQL
				+ " FROM Shred s,"
				+ " Rating r, Shredder sr, GuitarForShredder gs, "
				+ " EquiptmentForShredder es WHERE r.ShredId = s.id AND "
				+ "gs.ShredderId = sr.id AND es.ShredderId = sr.id AND ShredType='normal' ORDER BY s.TimeCreated DESC";
		return getShredsFromSQLString(sql, new ShredMapper());
	}

	public void deleteCommentForShred(int commentId) {
		String SQL = "DELETE FROM CommentForShred WHERE Id = " + commentId;
		jdbcTemplate.execute(SQL);		
	}	
	
}
