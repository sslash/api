package com.mikey.shredhub.api.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	
	
	public Map<String, List<ShredNewsItem>> getLatestShredNewsItems(Shredder shredder,
			int resultLimit) {
		this.resultLimit = resultLimit;
		Map <String, List <ShredNewsItem>> shredNews = new HashMap <String, List <ShredNewsItem>>();
		//List <ShredNewsItem> shredNews = new ArrayList<ShredNewsItem>();
		
		this.addNewPotentialFanees(shredder, shredNews);		
		this.addNewestBattles(shredder, shredNews);
		this.addNewBattleShreds(shredder, shredNews);
		this.addNewShredsFromFanees(shredder, shredNews);	
		return shredNews;
	}

	private void addNewPotentialFanees(Shredder shredder,Map<String, List<ShredNewsItem>> shredNews) {
		List <Shredder> shredders = shredderDAO.getPotentialFaneesForShredder(shredder);
		List <ShredNewsItem> resList = new LinkedList<ShredNewsItem>();
		for ( int i = 0; i < shredders.size() && i < resultLimit; i++ ) {
			Shredder sh = shredders.get(i);
			resList.add( new NewPotentialFaneeNewsItem(sh, sh.getTimeCreated()));
		}
		shredNews.put(POT_FANEES, resList);
	}

	private void addNewestBattles(Shredder shredder,Map<String, List<ShredNewsItem>> shredNews) {
		List <Battle> battles = battleDAO.getNewestBattlesFromFanees(shredder.getId()); 
		List <ShredNewsItem> resList = new LinkedList<ShredNewsItem>();
		for ( int i = 0; i < battles.size() && i < resultLimit; i++ ) {
			Battle b = battles.get(i);			
			resList.add(new NewBattleCreatedNewsItem(b));
		}
		shredNews.put(NEWEST_BATTLES, resList);
		
	}

	private void addNewBattleShreds(Shredder shredder,Map<String, List<ShredNewsItem>> shredNews) {
		List<BattleShredNewsItem> battleShreds = battleDAO.getBattleShredsFromFanees(shredder); 
		List <ShredNewsItem> resList = new LinkedList<ShredNewsItem>();
		for ( int i = 0; i < battleShreds.size() && i < resultLimit; i++ ) {
			BattleShredNewsItem b = battleShreds.get(i);
			resList.add(b);
		}
		shredNews.put(BATTLE_SHREDS, resList);
	}

	private void addNewShredsFromFanees(Shredder shredder,Map<String, List<ShredNewsItem>> shredNews) {
		List <Shred> shreds = shredDAO.getShredsFromFaneesForShredderWithId(shredder.getId()); 
		List <ShredNewsItem> resList = new LinkedList<ShredNewsItem>();
		for ( int i = 0; i < shreds.size() && i < resultLimit; i++ ) {
			Shred s = shreds.get(i);
			resList.add(new NewShredFromFanee(s, s.getTimeCreated()));
		}
		shredNews.put(FANEES_NEWS, resList);
	}
}
