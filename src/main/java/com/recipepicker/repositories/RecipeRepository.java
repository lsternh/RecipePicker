package com.recipepicker.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.recipepicker.entity.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Integer>{

	List<Recipe> findByName(String name);
}
