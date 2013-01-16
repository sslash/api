package com.mikey.shredhub.api.domain;

import java.io.Serializable;
import java.sql.Date;

public class ShredComment implements Serializable {

	private int id;
	
	private String text;
	
	private Shredder commenter;
	
	private Date timeCreated;
	
	private Shred shred; // Don't think I need this..

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Shredder getCommenter() {
		return commenter;
	}

	public void setCommenter(Shredder commenter) {
		this.commenter = commenter;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Shred getShred() {
		return shred;
	}

	public void setShred(Shred shred) {
		this.shred = shred;
	}
	
	
}
