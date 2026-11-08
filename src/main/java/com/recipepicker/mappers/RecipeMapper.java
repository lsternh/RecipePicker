package com.recipepicker.mappers;

import com.recipepicker.dto.RecipeDto;
import com.recipepicker.entity.RecipeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    RecipeDto recipeEntityToRecipeDto(RecipeEntity recipe);

    RecipeEntity recipeDtoToRecipeEntity(RecipeDto recipe);

}
