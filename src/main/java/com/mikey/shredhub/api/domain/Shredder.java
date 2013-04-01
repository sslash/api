package com.mikey.shredhub.api.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Shredder implements Serializable{
	
     private String profileImagePath;
     
     @Size(min=3, max=20, message=
    		    "Username must be between 3 and 20 characters long.")
     @Pattern(regexp="^[a-zA-Z0-9\\s]+$",
     	message="Username must be alphanumeric")
     private String username;
     
     @Pattern(regexp="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}",
    	        message="Invalid email address.")
     private String email;
     
     @Size(min=6, max=20, message="Passwords must be between 6 and 20 characters long.")
     @Pattern(regexp="^[a-zA-Z0-9\\s]+$",
 	        message="Passwords must be alphanumeric")
     private String password;
     
     private Date birthdate;
          
     private String country;
     
     private List <GuitarForShredder> guitars;
     
     private List <String> equiptment; 
          
     private String description;
     
     private Date timeCreated;
     
     private ShredderLevel level;

     private int id;
     
     private List <Shredder> fanees;
     
     
     
	public List<Shredder> getFanees() {
		return fanees;
	}


	public void setFanees(List<Shredder> fanees) {
		this.fanees = fanees;
	}


	public Shredder(int shredderId) {
		this.id = shredderId;
	}
	
	
	public Date getTimeCreated() {
		return timeCreated;
	}


	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}


	public Shredder() {
		equiptment = new ArrayList<String>();
		guitars = new ArrayList<GuitarForShredder>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = Date.valueOf (birthdate);
	}
	
	public void setBirthdate(Date birthdate) {
		
		this.birthdate = birthdate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setId(int id) {
		this.id = id;		
	}
	
	public int getId() {
		return id;
	}

	public List<GuitarForShredder> getGuitars() {
		return guitars;
	}

	public void setGuitars(List<GuitarForShredder> guitars) {
		this.guitars = guitars;
	}

	public List<String> getEquiptment() {
		return equiptment;
	}

	public void setEquiptment(List<String> equiptment) {
		this.equiptment = equiptment;
	}
	
	public void addGuitar(GuitarForShredder guitar) {
		if (guitars == null){
			guitars = new ArrayList<GuitarForShredder>();
		}
		
		guitars.add(guitar);
	}
	
	public void addEquiptment(String equiptment) {
		if (this.equiptment == null){
			this.equiptment= new ArrayList<String>();
		}
		
		this.equiptment.add(equiptment);
	}

	
	public ShredderLevel getLevel() {
		return level;
	}

	public void setLevel(ShredderLevel level) {
		this.level = level;
	}



	@Override
	public String toString() {
		return "Shredder [profileImagePath=" + profileImagePath + ", username="
				+ username + ", email=" + email + ", password=" + password
				+ ", birthdate=" + birthdate + ", country=" + country
				+ ", guitars=" + guitars + ", equiptment=" + equiptment
				+ ", description=" + description + ", timeCreated="
				+ timeCreated + ", level=" + level + ", id=" + id + ", fanees="
				+ fanees + "]";
	}


	@Override
	// Simple id comparison
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shredder other = (Shredder) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
