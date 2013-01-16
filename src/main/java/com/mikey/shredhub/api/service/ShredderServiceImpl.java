package com.mikey.shredhub.api.service;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.dao.ShredDAO;
import com.mikey.shredhub.api.dao.ShredderDAO;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.utils.ImageUploadException;
import com.mikey.shredhub.api.utils.LocalImageFileSaver;

// TODO: ADd more transactional stuff
@Service
@Transactional( readOnly=true )
public class ShredderServiceImpl implements ShredderService {
	
	@Autowired
	private ShredderDAO shredderDAO;
	
	@Autowired
	private ShredDAO shredDAO;
	
	@Autowired 
	private ServletContext servletContext;	
	

	@Transactional
	public Shredder loginShredder(String username, String password) {
		return shredderDAO.getShredderByUsernameAndPassword(username,password);
	}

	@Transactional
	public List<Shredder> getAllShredders() {
		return shredderDAO.getAllShredders();
	}

	@Transactional
	public List<Shredder> getFansForShredderWithId(int id) {
		return shredderDAO.getFansForShredderWithId(id);
	}

	@Transactional(readOnly = false)
	public void createFanRelation(int faner, int fanee) {
		shredderDAO.createFanRelation(faner, fanee);		
	}

	@Transactional
	public Shredder getShredderWithId(int id) {
		return shredderDAO.getShredderById(id);
	}

	@Transactional
	public boolean getIfShredder1IsFanOfShredder2(int faneer, int fanee) {
		List <Shredder> fans = shredderDAO.getFansForShredderWithId(faneer);
		for ( Shredder s : fans) {
			if (s.getId() == fanee) {
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly=false)
	public void addShredder(Shredder shredder, MultipartFile profileImage) throws ImageUploadException {
		if ( shredder.getBirthdate() == null ){
			shredder.setBirthdate(new java.sql.Date(0));
		}	
		String imgName = shredder.getUsername().split(" ")[0] + ".jpg";
		String deployPath = servletContext.getRealPath("/") + "resources/images/";
		new LocalImageFileSaver(deployPath).saveFile(profileImage, imgName);
		shredder.setProfileImagePath(imgName);
		shredderDAO.addShredder(shredder);
		
	}
}
