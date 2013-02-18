package com.mikey.shredhub.api.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mikey.shredhub.api.dao.BattleDAO;
import com.mikey.shredhub.api.dao.ShredDAO;
import com.mikey.shredhub.api.dao.ShredderDAO;
import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.newsitem.BattleShredNewsItem;
import com.mikey.shredhub.api.domain.newsitem.NewBattleCreatedNewsItem;
import com.mikey.shredhub.api.domain.newsitem.NewPotentialFaneeNewsItem;
import com.mikey.shredhub.api.domain.newsitem.NewShredFromFanee;
import com.mikey.shredhub.api.domain.newsitem.ShredNewsItem;


@Service
public class ShredNewsServiceImpl implements ShredNewsService {
	
	private static final Logger logger = LoggerFactory.getLogger(ShredNewsServiceImpl.class);

	@Autowired
	private ShredderDAO shredderDAO;
	
	@Autowired
	private ShredDAO shredDAO;
	
	@Autowired
	private BattleDAO battleDAO;
	
	/* Max limit per newsitem type */
	private  int resultLimit;
	
	public static final String FANEES_NEWS = "FANEES";
	
	public static final String NEWEST_BATTLES = "BATTLES";
	
	public static final String BATTLE_SHREDS = "BATTLE_SHREDS";
	
	public static final String POT_FANEES = "POTENTIAL_FANEES";
	
	public Map<String, List/*<ShredNewsItem>*/> getLatestShredNewsItems(Shredder shredder,
			int resultLimit) {
		this.resultLimit = resultLimit;
		Map <String, List /*<ShredNewsItem>*/> shredNews = new HashMap <String, List /*<ShredNewsItem>*/>();	
		long t1 = System.currentTimeMillis();
		
		this.addNewPotentialFanees(shredder, shredNews);
		logger.info("add pot fanees: Time: " + (System.currentTimeMillis() - t1) + (System.currentTimeMillis() - t1)/1000);
		t1 = System.currentTimeMillis();
		this.addNewestBattles(shredder, shredNews);
		logger.info("newest battles: Time: " + (System.currentTimeMillis() - t1) + (System.currentTimeMillis() - t1)/1000);
		t1 = System.currentTimeMillis();
		this.addNewBattleShreds(shredder, shredNews);
		logger.info("battle shreds: Time: " + (System.currentTimeMillis() - t1) + (System.currentTimeMillis() - t1)/1000);
		t1 = System.currentTimeMillis();
		this.addNewShredsFromFanees(shredder, shredNews);	
		logger.info("shreds from fanees: Time: " + (System.currentTimeMillis() - t1) + (System.currentTimeMillis() - t1)/1000);
		return shredNews;
	}

	private void addNewPotentialFanees(Shredder shredder,Map<String, List/*<ShredNewsItem>*/> shredNews) {
		List <Shredder> shredders = shredderDAO.getPotentialFaneesForShredder(shredder);
		List <ShredNewsItem> resList = new LinkedList<ShredNewsItem>();
		for ( int i = 0; i < shredders.size() && i < resultLimit; i++ ) {
			Shredder sh = shredders.get(i);
			resList.add( new NewPotentialFaneeNewsItem(sh, sh.getTimeCreated()));
		}
		shredNews.put(POT_FANEES, resList);
	}

	private void addNewestBattles(Shredder shredder,Map<String, List/*<ShredNewsItem>*/> shredNews) {
		shredNews.put(NEWEST_BATTLES, battleDAO.getNewestBattlesFromFanees(shredder.getId()));		
	}

	private void addNewBattleShreds(Shredder shredder,Map<String, List/*<ShredNewsItem>*/> shredNews) {
		shredNews.put(BATTLE_SHREDS, battleDAO.getBattleShredsFromFanees(shredder));
	}

	private void addNewShredsFromFanees(Shredder shredder,Map<String, List/*<ShredNewsItem>*/> shredNews) {
		List <Shred> shreds = shredDAO.getShredsFromFaneesForShredderWithId(shredder.getId(), 0); 
		List <ShredNewsItem> resList = new LinkedList<ShredNewsItem>();
		for ( int i = 0; i < shreds.size() && i < resultLimit; i++ ) {
			Shred s = shreds.get(i);
			resList.add(new NewShredFromFanee(s, s.getTimeCreated()));
		}
		shredNews.put(FANEES_NEWS, resList);
	}
}
