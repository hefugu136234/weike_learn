package com.lankr.tv_cloud.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class AdvertisementControllerTest extends HoldProjectControllerTest {
	
	@Before
	public void befoerMethod() {
		super.befoerMethod();
	}

	@Test
	public void testLoadadverListPage() throws Exception {
		mockMvc.perform(
				get("/project/adver/list/page").session(session))
				.andExpect(status().isOk())
				//.andDo(print())
				.andExpect(forwardedUrl("/common/index.jsp"));
	}

	@Test
	public void testGetAdverListData() throws Exception {
		mockMvc.perform(
				get("/project/adver/list").session(session))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/plain; charset=ISO-8859-1"))
						//.andDo(print())
				//.andExpect(jsonPath("$.aaData", hasSize(2)));
				.andExpect(jsonPath("$.iTotalRecords",is(10)));
	}

	@Test
	public void testChangeAdverStatus() throws Exception {
		mockMvc.perform(
				post("/project/change/adver/stats").param("uuid", "c94979b2-c364-49d4-b6b8-db03a07d9185")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json; charset=utf-8"))
						//.andDo(print());
				.andExpect(jsonPath("$.uuid", is("c94979b2-c364-49d4-b6b8-db03a07d9185")));
	}

	@Test
	public void testAdverAddPage() throws Exception {
		mockMvc.perform(
				get("/project/adver/add/page").session(session))
				.andExpect(status().isOk())
				//.andDo(print());
				.andExpect(forwardedUrl("/common/index.jsp"));
	}

	@Test
	public void testLoadAdverPostion() throws Exception {
		mockMvc.perform(
				get("/project/adver/postion/data").session(session))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json;charset=utf-8"))
						//.andDo(print());
				.andExpect(jsonPath("$.*", hasSize(3)));
	}

	@Test
	public void testAdverSave() throws Exception {
		MockHttpServletRequest request=(MockHttpServletRequest) mockMvc.perform(
				get("/project/adver/add/page").session(session))
				.andExpect(status().isOk())
				//.andDo(print());
				.andExpect(forwardedUrl("/common/index.jsp"))
				.andReturn().getRequest();
		
		String token=(String) request.getAttribute("token");
		//session=(MockHttpSession) request.getSession();
		
		System.out.println("token---"+token);
		
		mockMvc.perform(
				post("/project/adver/add/save").param("name", "广告测试juint")
				.param("positon", "1")
				.param("adtime", "15")
				.param("token", token)
				.session(session))
				.andExpect(status().isOk())
						//.andDo(print())
						.andExpect(forwardedUrl("/common/index.jsp"));
				//.andExpect(jsonPath("$.*", hasSize(3)));
	}

	@Test
	public void testAdverDetail() throws Exception {
		mockMvc.perform(
				get("/project/adver/c94979b2-c364-49d4-b6b8-db03a07d9185/detail").session(session))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("text/json;charset=utf-8"))
						//.andDo(print());
				.andExpect(jsonPath("$.uuid", is("c94979b2-c364-49d4-b6b8-db03a07d9185")));
	}

	@Test
	public void testAdverUpdate() throws Exception {
		mockMvc.perform(
				post("/porject/adver/update").param("uuid", "91d7e056-9cc4-4fed-b1b1-86402adeedb8")
				.param("adname", "广告测试juint1")
				.param("adpostion", "1")
				.param("adtime", "15")
				.session(session))
				.andExpect(status().isOk())
						//.andDo(print());
		        .andExpect(jsonPath("$.uuid", is("91d7e056-9cc4-4fed-b1b1-86402adeedb8")));
	}

}
