package com.mikey.shredhub.api.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Shred implements Serializable {
	private int id;
	
	private String description;
	
	private Shredder owner;
	
	private Date timeCreated;
	
	private String videoPath;
	
	private String thumbnailpath;
	
	private List<Tag> tags;
	
	private List <ShredComment> shredComments;
	
	private ShredRating rating;
	
	private String shredType = "normal";
	
	public String getShredType() {
		return shredType;
	}
	
	public void setShredType(String shredType) {
		this.shredType = shredType;
	}

	public int getId() {
		return id;
	}
	

	public String getThumbnailpath() {
		return thumbnailpath;
	}

	public void setThumbnailpath(String thumbnailpath) {
		this.thumbnailpath = thumbnailpath;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Shredder getOwner() {
		return owner;
	}

	public void setOwner(Shredder owner) {
		this.owner = owner;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<ShredComment> getShredComments() {
		return shredComments;
	}

	public void setShredComments(List<ShredComment> shredComments) {
		this.shredComments = shredComments;
	}

	public ShredRating getRating() {
		return rating;
	}
	
	public void setRating(ShredRating rating) {
		this.rating = rating;		
	}

	@Override
	public String toString() {
		return "Shred [id=" + id + ", description=" + description + ", owner="
				+ owner + ", timeCreated=" + timeCreated + ", videoPath="
				+ videoPath + ", thumbnailpath=" + thumbnailpath + ", tags="
				+ tags + ", shredComments=" + shredComments + ", rating="
				+ rating + ", shredType=" + shredType + "]";
	}
}
