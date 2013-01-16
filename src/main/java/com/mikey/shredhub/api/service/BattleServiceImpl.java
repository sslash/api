package com.mikey.shredhub.api.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.dao.BattleDAO;
import com.mikey.shredhub.api.dao.ShredDAO;
import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.utils.ImageUploadException;
import com.mikey.shredhub.api.utils.LocalImageFileSaver;

@Service
@Transactional (readOnly=true)
public class BattleServiceImpl implements BattleService {

	@Autowired
	private ShredDAO shredDAO;
	
	@Autowired 
	private ServletContext servletContext;	
	
	@Autowired
	private BattleDAO battleDAO;
	
	public Battle getBattleWithId(int battleId) {
		Battle battle =  battleDAO.getBattleWithId(battleId);
		battle.initBattle();
		List <Shred> shredBattles = getShredsForBattle(battle);
		battle.populateBattlesRoundAndPoints(shredBattles);
		return battle;
	}	
	
	private List<Shred> getShredsForBattle(Battle battle) {
		return battleDAO.getShredsForBattleWithId(battle.getId());
	}

	public List<Battle> getOngoingBattlesForShredderWithId(int id) {
		return battleDAO.getOngoingBattlesForShredderWithId(id);
	}

	public List<Battle> getBattleRequestsForShredderWithId(int id) {
		return battleDAO.getBattleRequestsForShredderWithId(id);
	}

	@Transactional(readOnly = false)
	public void acceptBattleWithId(int id) {
		Battle battle = battleDAO.getBattleWithId(id);
		battle.setStatus(Battle.STATUS_ACCEPTED);
		battleDAO.persistBattle(battle);
	}

	public void addBattleShred(int shredderId, int battleId, String videoName, int round,MultipartFile file) throws ImageUploadException {
		Date now = Utilities.getNow();
		Battle battle = battleDAO.getBattleWithId(battleId);
		battle.setRound(round);
		String deployPath = servletContext.getRealPath("/") + "resources/images/";
		new LocalImageFileSaver(deployPath).saveFile(file,videoName);
		battleDAO.persistBattle(battle);
		// Create the first shred
		Shred shred = new Shred();
		shred.setOwner(new Shredder(shredderId));
		shred.setTimeCreated(now);
		shred.setVideoPath(videoName);
		shred.setShredType("battle");
		shredDAO.persistShred(shred);
			
		battleDAO.persistBattleShred(battleId, shred.getId(), round);		
	}
	
	@Transactional(readOnly = false)
	public void declineBattleWithId(int id) {
		Battle battle = battleDAO.getBattleWithId(id);
		battle.setStatus(Battle.STATUS_DECLINED);
		battleDAO.persistBattle(battle);		
	}
	
	@Transactional(readOnly = false)
	public void addShredBattleRequest(int shredderId, int shredeeId,
			String videoName, String battleStyle, MultipartFile file) throws ImageUploadException {
		
		// No need to create an object for this. Just write to db
		String deployPath = servletContext.getRealPath("/") + "resources/images/";
		new LocalImageFileSaver(deployPath).saveFile(file,videoName);
		Date now = Utilities.getNow();
		int battleId = battleDAO.addBattleRequest(shredderId, now, shredeeId, battleStyle);
		this.addBattleShred(shredderId, battleId, videoName, 1, file);		
	}
}
