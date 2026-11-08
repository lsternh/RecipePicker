package com.recipepicker.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="recipe")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeEntity {

	private String name;
    @Column(length = 2000)
    private String description;
    @ElementCollection
    @CollectionTable(name = "recipe_step", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "step")
    @OrderColumn(name = "step_order")
    private List<String> steps;
	private LocalDateTime uploaded;
	private String url;
    @ElementCollection
    @CollectionTable(name = "recipe_ingredient", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
	private List<String> ingredients;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

}
