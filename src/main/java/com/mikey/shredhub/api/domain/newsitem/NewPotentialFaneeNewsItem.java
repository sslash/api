package com.mikey.shredhub.api.domain.newsitem;

import java.sql.Date;

import com.mikey.shredhub.api.domain.Shredder;

/**
 * Can possibly merge this with shredderlevelnewsitem
 * 
 * @author michaekg
 *
 */
public class NewPotentialFaneeNewsItem extends ShredNewsItem {
	
	public NewPotentialFaneeNewsItem(Shredder sh, Date timeCreated) {
		super(timeCreated);
		this.shredder= sh;		
	}

	private Shredder shredder;

	public Shredder getShredder() {
		return shredder;
	}

	public void setShredder(Shredder shredder) {
		this.shredder = shredder;
	}
	
	

}
