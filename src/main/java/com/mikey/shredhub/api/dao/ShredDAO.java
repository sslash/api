package com.mikey.shredhub.api.dao;

import java.sql.Date;
import java.util.List;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.ShredComment;
import com.mikey.shredhub.api.domain.ShredRating;
import com.mikey.shredhub.api.domain.Tag;

public interface ShredDAO {

	public List<Shred> getShredsFromFaneesForShredderWithId(int shredderId);

	public List<Tag> getTagsForShredWithId(int id);

	public List<ShredComment> getShredCommentsForShredWithId(int id);

	public List<Tag> getAllTags();

	public Tag getWithLabel(String tagLabel);

	public int persistShred(Shred shred);

	public void addCommentForShred(String text, int shredId, int shredderId,
			Date date);

	public ShredRating getRatingForShredWithId(int shredId);

	public void persistRate(int shredId, ShredRating currentRating);

	public Shred getShredById(int shredId);

	public List<Shred> getShredsForShredderWithId(int id);

	public List<Shred> getShredsOrderedByRating(int max);
	
	public List <Shred> getShredsFromShreddersShredderMightKnow(int shredderId);
	
	public List<Shred> getShredsWithTagsInTagsList(List <String> tags);

	public List<Shred> getAllShreds();

	public void deleteCommentForShred(int index);

}
