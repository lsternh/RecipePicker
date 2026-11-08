package com.recipepicker.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.recipepicker.dto.RecipeDto;
import com.recipepicker.mappers.RecipeMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.recipepicker.entity.RecipeEntity;
import com.recipepicker.repositories.RecipeRepository;

@RestController
public class RecipePickerController {

    @Autowired
    RecipeMapper mapper;

    @Autowired
	private RecipeRepository recipeRepostiory;

	@PostMapping("/add")
	@Operation(summary = "Add recipe")
	public void addRecipe(@RequestBody RecipeDto recipeDto) {
        RecipeEntity recipeEntity = mapper.recipeDtoToRecipeEntity(recipeDto);
		recipeRepostiory.save(recipeEntity);
	}
	
	@GetMapping("/all")
	@Operation(summary = "Get all recipes")
	public List<RecipeDto> getRecipe() {
        return StreamSupport.stream(recipeRepostiory.findAll().spliterator(), false)
                .map(mapper::recipeEntityToRecipeDto)
                .collect(Collectors.toList());
    }
	
	@GetMapping("/get/{id}")
	@Operation(summary = "Get a recipe")
	public Optional<RecipeEntity> getRecipe(@PathVariable int id) {
		return recipeRepostiory.findById(id);
	}
	
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete recipe")
	public void deleteRecipe(@PathVariable int id) {
		recipeRepostiory.deleteById(id);
	}
}
