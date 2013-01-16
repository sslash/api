package com.mikey.shredhub.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mikey.shredhub.api.dao.ShredDAO;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.Tag;

@Service
@Transactional (readOnly = true)
public class RecommendationServiceImpl implements RecommendationService {
	
	@Autowired
	private ShredDAO shredDAO;

	public List<Shred> getRecsBasedOnTags(List<String> tags) {
		return shredDAO.getShredsWithTagsInTagsList(tags);
	}

	public List<Shred> getRecsBasedOnRatings(int max) {
		return shredDAO.getShredsOrderedByRating(max);
	}

	public List<Shred> getRecsBasedOnFansOfShredder(int shredderId) {
		return shredDAO.getShredsFromFaneesForShredderWithId(shredderId, 0);
	}

	public List<Shred> getRecsBasedOnShreddersShredderMightKnow(
			int shredderId) {
		return shredDAO.getShredsFromShreddersShredderMightKnow(shredderId);
	}

}
