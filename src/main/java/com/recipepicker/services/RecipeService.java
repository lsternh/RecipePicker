package com.recipepicker.services;

import com.recipepicker.dto.RecipeDto;
import com.recipepicker.entity.RecipeEntity;
import com.recipepicker.mappers.RecipeMapper;
import com.recipepicker.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private static final Duration RECIPE_CACHE_TTL = Duration.ofMinutes(30);

    private final RecipeRepository recipeRepository;
    private final RedisTemplate<String, RecipeEntity> redisTemplate;
    private final RecipeMapper mapper;

    public RecipeDto getRecipeById(int recipeId) {
        String cacheKey = recipeCacheKey(recipeId);

        // 1. Shared Redis cache lookup
        RecipeEntity cachedRecipe = redisTemplate.opsForValue().get(cacheKey);
        if (cachedRecipe != null) {
            log.info("Using recipe from cache");
            return mapper.recipeEntityToRecipeDto(cachedRecipe);
        }

        // 2. Database fallback
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
        log.info("Using recipe from database");

        // 3. Populate cache for all application servers
        redisTemplate.opsForValue().set(cacheKey, recipe, RECIPE_CACHE_TTL);

        return mapper.recipeEntityToRecipeDto(recipe);
    }

    private String recipeCacheKey(int recipeId) {
        // Braces keep related recipe keys in the same Redis Cluster slot.
        return "recipe:v1:{" + recipeId + "}";
    }
}
