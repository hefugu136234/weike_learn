package com.lankr.tv_cloud.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class APIControllerTest extends ControllerTest{
	
	@Before
	public void befoerMethod() {
		super.befoerMethod();
	}
	
	@Test
	public void test() throws Exception{
		super.test();
	}

	@Test
	public void testProjectStructure() throws Exception {
		mockMvc.perform(
				get("/api/2b90f333-d784-49ea-a3b4-c393fd5724c4/home/structure"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json; charset=utf-8"))
				.andExpect(jsonPath("$.status", is("success")))
				.andExpect(jsonPath("$.widgets",hasSize(6)));
	}

	@Test
	public void testCategorieVideos() throws Exception {
		mockMvc.perform(
				get("/api/ee676f87-a50a-41f2-b2e8-e1fb68692a3f/asset/videos"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json; charset=utf-8"))
				.andExpect(jsonPath("$.aaData[0].uuid", is("9b9e21a7-1180-480f-baac-2d8664d8c8e5")))
				.andExpect(jsonPath("$.aaData",hasSize(1)));
	}

	@Test
	public void testTrunkCategoryVideos() throws Exception {
		mockMvc.perform(
				get("/api/trunk/ee676f87-a50a-41f2-b2e8-e1fb68692a3f/asset/videos"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json; charset=utf-8"))
				.andExpect(jsonPath("$.aaData",hasSize(3)));
	}

	@Test
	public void testVideoDetail() throws Exception {
		mockMvc.perform(
				get("/api/video/d73c9fbe-45dd-481e-9f73-fedfcf3f393f/detail"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json; charset=utf-8"))
				.andExpect(jsonPath("$.uuid",is("d73c9fbe-45dd-481e-9f73-fedfcf3f393f")));
	}

	@Test
	public void testGetVoide() throws Exception {
		mockMvc.perform(
				get("/api/2b90f333-d784-49ea-a3b4-c393fd5724c4/videos/ee676f87-a50a-41f2-b2e8-e1fb68692a3f"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json; charset=utf-8"))
				.andExpect(jsonPath("$.status",is("success")))
				.andExpect(jsonPath("$.videoList", hasSize(3)));
	}

}
