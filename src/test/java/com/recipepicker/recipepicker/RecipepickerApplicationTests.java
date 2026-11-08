package com.recipepicker.recipepicker;

import com.recipepicker.RecipePickerMainApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RecipePickerMainApplication.class)
@ActiveProfiles("test")
class RecipepickerApplicationTests {

	@Test
	void contextLoads() {
	}

}
