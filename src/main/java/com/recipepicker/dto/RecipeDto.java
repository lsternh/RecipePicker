package com.recipepicker.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {
    private String name;
    private String description;
    private List<String> steps;
    private LocalDateTime uploaded;
    private String url;
    private List<String> ingredients;
    private int id;
}
