package com.recipepicker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipepicker.RecipePickerMainApplication;
import com.recipepicker.entity.RecipeEntity;

@ActiveProfiles(value="test")
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = RecipePickerMainApplication.class)
public class ControllerTest {

	@Autowired
	private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonCreator;

	private final String UID = "dev";
	private final String PWD = "devpw";
	
	@WithMockUser("dev")
	@Test
	public void test_add() throws JsonProcessingException, Exception {
		RecipeEntity recipeEntity = RecipeEntity.builder()
                .name("Pizza")
                .description("pizza pizza")
                .build();
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.post("https://localhost:8101/add")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCreator.writeValueAsString(recipeEntity))
				.with(SecurityMockMvcRequestPostProcessors.httpBasic(UID, PWD)))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		//assertEqauls(response.);
	}
}
