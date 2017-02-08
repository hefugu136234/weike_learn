package com.lankr.tv_cloud.web;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class ProjectControllerTest extends ControllerLoginUserTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	@Ignore("content testDo_save_project")
	public void testPage_create_project() {
	}

	@Test
	public void testPage_list_project() throws Exception {
		 mockMvc.perform(
				get("/admin/project/list").session(session))
				.andExpect(status().isOk())
				//.andDo(print())
				.andExpect(forwardedUrl("/common/index.jsp"));
	}

	@Test
	public void testDo_save_project() throws Exception {
		MockHttpServletRequest request=(MockHttpServletRequest) mockMvc.perform(
				get("/admin/project/new").session(session))
				.andExpect(status().isOk())
				//.andDo(print());
				.andExpect(forwardedUrl("/common/index.jsp"))
				.andReturn().getRequest();
		
		String token=(String) request.getAttribute("token");
		
		 request=(MockHttpServletRequest)mockMvc.perform(
				post("/admin/project/save").param("project_name", "工程juint测试")
				.param("apply", "test")
				.param("username", "kalean")
				.param("token", token)
				.session(session))
				.andExpect(status().isOk())
						//.andDo(print())
						//.andExpect(forwardedUrl("/common/index.jsp"));
				.andReturn().getRequest();
		 String message=(String) request.getAttribute("message");
		 assertEquals("success", message);
		 
	}

	@Test
	public void testProjectList() throws Exception {
		mockMvc.perform(
				get("/admin/project/datatable").session(session))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json;charset=utf-8"))
						//.andDo(print());
				.andExpect(jsonPath("$.iTotalRecords", is(10)));
	}

	@Test
	public void testFetchUserInfo() throws Exception {
		mockMvc.perform(
				get("/admin/user/fetch").param("query", "")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json;charset=utf-8"))
						//.andDo(print());
				.andExpect(jsonPath("$.[0].uuid", is("dee47264-5cb0-45d2-a8da-1a9531b07f2d")));
	}

}
