package com.recipepicker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name="images")
@Component
public class ImageModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int imageId;
	
	String name;
	String type;
	byte[] imageBytes;
	
	public ImageModel() {
	}
	
	public ImageModel(String name, String type, byte[] imageBytes) {
		this.name = name;
		this.type = type;
		this.imageBytes = imageBytes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
	
}
