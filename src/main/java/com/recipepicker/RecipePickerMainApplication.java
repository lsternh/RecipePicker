package com.recipepicker;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.recipepicker.config.BasicAuthConfig;
import com.recipepicker.controllers.RecipePickerController;
import com.recipepicker.repositories.RecipeRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class RecipePickerMainApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(RecipePickerMainApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("It's Alive!");

		};
	}

}

