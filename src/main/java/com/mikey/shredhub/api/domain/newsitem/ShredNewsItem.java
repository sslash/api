package com.mikey.shredhub.api.domain.newsitem;

import java.sql.Date;

/**
 * A newsitem might be:
 * 
 * - New shred from a fanee
 * - New Battle shred from one of your fanees
 * - New Battle started
 * - A fanee reached a new level
 * - Someone you might know started shredding 
 *
 * @author michaekg
 *
 */
public abstract class ShredNewsItem implements Comparable <ShredNewsItem>{
	
	
	protected Date timeCreated;
	
	public ShredNewsItem(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	public int compareTo(ShredNewsItem other) {
		return this.timeCreated.compareTo(other.getTimeCreated());
	}
	
	
}
