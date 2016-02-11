package com.wheretoeat;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WhereToEatApiApplication.class)
@WebAppConfiguration
public class WhereToEatApiApplicationTests {
	
	@Rule
	public RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(restDocumentation).snippets().withDefaults(
						HttpDocumentation.httpResponse()))
				.build();
	}
	
	@Test
	public void testLocationsDoc() throws Exception {
		mockMvc.perform(get("/locations").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(4)))			
			.andDo(document("getAllLocations", responseFields(
					fieldWithPath("[]").description("The locations list."),
					fieldWithPath("[].name").description("The name of the location."))));
	}
	
	@Test
	public void testGetLocation() throws Exception {
		mockMvc.perform(get("/locations/3").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("locationId", is(3)))
			.andExpect(jsonPath("name", is("Plaza - Pizza Hut")))
			.andExpect(jsonPath("minutesOnRoad", is(25)))
			
			.andDo(document("getOneLocation", responseFields(
					attributes(key("title").value("The fields of the retrieved location object.")),
					fieldWithPath("locationId").description("The location's integer id."),
					fieldWithPath("name").description("The name of the location."),
					fieldWithPath("minutesOnRoad").description("The time that we will spend travelling to the location."))));
	}
}
