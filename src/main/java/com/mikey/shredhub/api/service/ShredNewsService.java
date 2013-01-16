package com.mikey.shredhub.api.service;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.newsitem.ShredNewsItem;

public interface ShredNewsService {

	Map<String, List<ShredNewsItem>> getLatestShredNewsItems(Shredder shredder, int resultLimit);

	
}
