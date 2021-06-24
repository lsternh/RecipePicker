package com.recipepicker.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springfox.documentation.spring.web.json.Json;

@Entity
@Table(name="recipe")
@Component
public class Recipe {

	private String name;
	private String steps;
	private Date uploaded;
	private String url;
	private String image_dir;
	@Transient
	private List<Ingredient> ingredientsList;
	private String ingredients;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	public Recipe(String name, String steps, String url, String image_dir, List<Ingredient> ingredients){
		this.name = name;
		this.steps = steps;
		this.uploaded = new Date();
		this.url = url;
		this.image_dir = image_dir;
		setIngredientsList(ingredientsList);
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage_dir() {
		return image_dir;
	}

	public void setImage_dir(String image_dir) {
		this.image_dir = image_dir;
	}

	public List<Ingredient> getIngredientsList() {
		return ingredientsList;
	}

	public String getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(List<Ingredient> ingredientsList) {
		ObjectMapper mapper = new ObjectMapper();
	    String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(ingredientsList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ingredients = jsonString;
	}
	
	public void setIngredientsList(List<Ingredient> ingredientsList) {
		this.ingredientsList = ingredientsList;
	}
	
	
}
