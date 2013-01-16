package com.mikey.shredhub.api.domain.newsitem;

import java.sql.Date;

import com.mikey.shredhub.api.domain.Shred;

public class NewShredFromFanee extends ShredNewsItem {
	
	public NewShredFromFanee(Shred shred, Date timeCreated) {
		super(timeCreated);
		this.shred = shred;
	}

	private Shred shred;

	public Shred getShred() {
		return shred;
	}

	public void setShred(Shred shred) {
		this.shred = shred;
	}
}
