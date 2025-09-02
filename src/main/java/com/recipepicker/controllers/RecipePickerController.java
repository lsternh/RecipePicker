package com.recipepicker.controllers;

import java.util.ArrayList;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.recipepicker.entity.Recipe;
import com.recipepicker.repositories.RecipeRepository;

@RestController
public class RecipePickerController {

	@Autowired
	private RecipeRepository recipeRepostiory;
	
	@PostMapping("/add")
	@Operation(summary = "Add recipe")
	public void addRecipe(@RequestBody Recipe recipe) {
		recipeRepostiory.save(recipe);
	}
	
	@GetMapping("/all")
	@Operation(summary = "Get all recipes")
	public ArrayList<Recipe> getRecipe() {
		System.out.println("HERE");
		return (ArrayList<Recipe>) recipeRepostiory.findAll();
	}
	
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete recipe")
	public void deleteRecipe(@PathVariable int id) {
		recipeRepostiory.deleteById(id);
	}
}
