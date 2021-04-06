package com.recipepicker.entity;

import java.util.Date;

import javax.persistence.Table;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Entity
@Table(name="recipe")
@Component
public class Recipe {

	private String name;
	private String steps;
	private Date uploaded;
	private String url;
	private String image_dir;
	
	@Id
	private int id;
	
	public Recipe(String name, String steps, int id, String url, String image_dir){
		this.name = name;
		this.steps = steps;
		this.uploaded = new Date();
		this.id = id;
		this.url = url;
		this.image_dir = image_dir;
	}
	
	public Recipe() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSteps() {
		return steps;
	}
	public void setSteps(String steps) {
		this.steps = steps;
	}
	public Date getUploaded() {
		return uploaded;
	}
	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
