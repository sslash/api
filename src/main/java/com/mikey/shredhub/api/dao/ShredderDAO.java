package com.mikey.shredhub.api.dao;

import java.sql.Date;
import java.util.List;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;

public interface ShredderDAO {
	
	public void addShredder(Shredder shredder);

	public Shredder getShredderByUsernameAndPassword(String username,
			String password);

	public List<Shredder> getAllShredders(int page);

	public List<Shredder> getFansForShredderWithId(int id);

	public void createFanRelation(int faner, int fanee);

	public Shredder getShredderById(int id);

	public void persistShredder(Shredder shredder);
	
	public List<Shredder> getPotentialFaneesForShredder(Shredder shredder);

	public boolean addDiggForGuitar(String id, String gIndex);
}
