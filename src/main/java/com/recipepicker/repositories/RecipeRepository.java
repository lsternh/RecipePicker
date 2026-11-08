package com.recipepicker.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.recipepicker.entity.RecipeEntity;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Integer>{

	List<RecipeEntity> findByName(String name);
}
