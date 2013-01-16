package com.mikey.shredhub.api.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.dao.BattleDAO;
import com.mikey.shredhub.api.dao.ShredDAO;
import com.mikey.shredhub.api.dao.ShredderDAO;
import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.ShredRating;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.Tag;
import com.mikey.shredhub.api.domain.UpdateShredderLevel;
import com.mikey.shredhub.api.service.exceptions.IllegalShredArgumentException;
import com.mikey.shredhub.api.utils.LocalImageFileSaver;

@Service
@Transactional( readOnly=true )
public class ShredServiceImpl implements ShredService {
	
	@Autowired
	private ShredDAO shredDAO;
	
	@Autowired 
	private ShredderDAO shredderDAO;
	
	@Autowired 
	private ServletContext servletContext;
	
	@Autowired
	private BattleDAO battleDAO;
	
	
	private static final int NO_SHREDS_FOR_TOP_SHREDS = 9;
	
	@Transactional
	public List<Shred> getFanShreds(int shredderId, int page) {
		List <Shred> shreds =  shredDAO.getShredsFromFaneesForShredderWithId(shredderId, page);
		return shreds;
	}

	@Transactional
	public List<Tag> getAllTags() {
		return shredDAO.getAllTags();
	}

	@Transactional( readOnly =false)
	public void addShredForShredderWithId(String text, int id, String[] tagsArr, MultipartFile file) throws Exception {
		Shredder shredder = shredderDAO.getShredderById(id);
		if (shredder==null) {
			throw new Exception("Shredder with id = " + id + " does not exist");
		}
		Date now = Utilities.getNow();
		String fileName = shredder.getId() + file.getOriginalFilename(); 
		String deployPath = servletContext.getRealPath("/") + "resources/images/";
		new LocalImageFileSaver(deployPath).saveFile(file, fileName);
		Shred shred = new Shred();
		shred.setDescription(text);		
		shred.setOwner(shredder);
		shred.setTimeCreated(now);
		shred.setVideoPath(fileName);
		List<Tag> tags = new ArrayList <Tag>();
		for( String tag : tagsArr) {
			Tag t = shredDAO.getWithLabel(tag);
			if ( t == null) {
				throw new Exception("Tag with name " + tag + " does not exist");
			}
			tags.add(t);
		}
		
		shred.setTags(tags);
		shredDAO.persistShred(shred);	
	}
	

	@Transactional (readOnly = false)
	public void addCommentForShred(String text, int shredId, int shredderId) {
		Date date = Utilities.getNow();
		shredDAO.addCommentForShred(text, shredId, shredderId, date);		
	}

	@Transactional ( readOnly = false )
	public void rateShred(int shredId, int newRate) throws IllegalShredArgumentException {
		Shred shred = shredDAO.getShredById(shredId);
		if ( shred == null ) {
			throw new IllegalShredArgumentException("Shred with id: " + shredId + " does not exist");
		}
		ShredRating currentRating = shred.getRating();
		
		// Here I could use the domain model pattern so that the
		// current rating knows how to set its own rating.
		// But I might as well do it here, because it is nice to only
		// operate with pojos
		currentRating.setNumberOfRaters(currentRating.getNumberOfRaters() + 1);
		currentRating.setCurrentRating(currentRating.getCurrentRating()+newRate);
		
		shredDAO.persistRate(shredId, currentRating);
		
		
		// Update xperiencepoints for shredder
		Shredder shredder = shredderDAO.getShredderById(shred.getOwner().getId());
		
		// This could be done in the domain object.
		// The rating rule is super simple, should come up with something killer
		UpdateShredderLevel usl = new UpdateShredderLevel(shredder, newRate);
		boolean res = usl.advanceXp();
		
		shredderDAO.persistShredder( shredder);
	}

	@Transactional
	public List<Battle> getOngoingBattlesForShredderWithId(int id) {
		return battleDAO.getOngoingBattlesForShredderWithId(id);
	}

	@Transactional
	public List<Battle> getBattleRequestsForShredderWithId(int id) {
		return battleDAO.getBattleRequestsForShredderWithId(id);
	}

	@Transactional
	public List<Shred> getShredsForShredderWithId(int id) {
		return shredDAO.getShredsForShredderWithId(id);
	}

	public List<Shred> getAllShreds() {
		return shredDAO.getAllShreds();
	}

	public List<Shred> getTopShredsByRating() {
		return shredDAO.getShredsOrderedByRating(NO_SHREDS_FOR_TOP_SHREDS);
	}

	public Shred getShredById(String id) {
		int intId = Integer.parseInt(id);
		Shred s =  shredDAO.getShredById(intId);
		// add comments
		s.setShredComments(shredDAO.getShredCommentsForShredWithId(intId));
		return s;
	}

	public void deleteCommentForShred(int commentId) {
		shredDAO.deleteCommentForShred(commentId);
	}

}
