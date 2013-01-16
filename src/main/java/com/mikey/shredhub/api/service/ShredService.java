package com.mikey.shredhub.api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Tag;
import com.mikey.shredhub.api.service.exceptions.IllegalShredArgumentException;

public interface ShredService {

	public List<Shred> getFanShreds(int shredderId, int i);

	public List<Tag> getAllTags();

	public void addShredForShredderWithId(String imgPath, int id, String[] tagsArr, MultipartFile file) throws Exception;

	public void addCommentForShred(String text, int shredId, int shredderId);

	public void rateShred(int shredId, int rating) throws IllegalShredArgumentException;

	public List<Battle> getOngoingBattlesForShredderWithId(int id);

	public List<Battle> getBattleRequestsForShredderWithId(int id);

	public List<Shred> getShredsForShredderWithId(int id);

	public List <Shred> getAllShreds();
	
	public List <Shred> getTopShredsByRating();

	public Shred getShredById(String id);

	public void deleteCommentForShred(int commentId);

}
