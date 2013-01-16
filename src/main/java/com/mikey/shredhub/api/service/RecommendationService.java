package com.mikey.shredhub.api.service;

import java.util.List;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.Tag;

public interface RecommendationService {

	public List <Shred> getRecsBasedOnTags(List<String> tags);
	
	public List <Shred> getRecsBasedOnRatings(int max);
	
	public List <Shred> getRecsBasedOnFansOfShredder(int shredderId);
	
	public List <Shred> getRecsBasedOnShreddersShredderMightKnow(int shredderId);
}
