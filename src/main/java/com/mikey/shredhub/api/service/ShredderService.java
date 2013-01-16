package com.mikey.shredhub.api.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.utils.ImageUploadException;

public interface ShredderService {
	
	public void addShredder(Shredder shredder, MultipartFile profileImage) throws ImageUploadException;

	public Shredder loginShredder(String username, String password);

	public List<Shredder> getAllShredders();

	public List<Shredder> getFansForShredderWithId(int id);

	public void createFanRelation(int id, int id2);

	public Shredder getShredderWithId(int id);

	public boolean getIfShredder1IsFanOfShredder2(int id, int id2);
}
