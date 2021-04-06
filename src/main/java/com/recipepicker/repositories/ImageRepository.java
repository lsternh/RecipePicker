package com.recipepicker.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.recipepicker.entity.ImageModel;
import com.recipepicker.entity.Recipe;

public interface ImageRepository extends CrudRepository<ImageModel, Integer>{

	Optional<ImageModel> findByName(String name);
	Optional<ImageModel> findById(int id);
}
