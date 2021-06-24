package com.recipepicker.entity;

public class Ingredient {
	String ingredient;
	String measurementType;
	String quantity;
	
	public Ingredient() {
	}

	public Ingredient(String ingredient, String measurementType, String quantity) {
		this.ingredient = ingredient;
		this.measurementType = measurementType;
		this.quantity = quantity;
	}
	
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getMeasurementType() {
		return measurementType;
	}
	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	
}
