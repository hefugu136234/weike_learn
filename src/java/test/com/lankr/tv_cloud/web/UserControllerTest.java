package com.lankr.tv_cloud.web;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
public class UserControllerTest extends HoldProjectControllerTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	@Ignore
	public void testNewUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveGlobleUser() throws Exception {
		MockHttpServletRequest request=(MockHttpServletRequest) mockMvc.perform(
				get("/admin/user/new").session(session))
				.andExpect(status().isOk())
				//.andDo(print());
				.andExpect(forwardedUrl("/common/index.jsp"))
				.andReturn().getRequest();
		
		String token=(String) request.getAttribute("token");
		
		request=mockMvc.perform(
				post("/admin/user/save")
				.param("token", token)
				.param("username", "testperson11")
				.param("password", "111")
				.session(session))
				.andExpect(status().isOk())
				//.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	           .andReturn().getRequest();
		assertEquals("success", request.getAttribute("status"));
	}

	@Test
	@Ignore
	public void testGloabUserListPage() {
		 
	}

	@Test
	public void testAdminUserDatatable() throws Exception {
		 mockMvc.perform(
					get("/admin/user/datatable").session(session))
					.andExpect(status().isOk())
					//.andDo(print())
					//.andExpect(forwardedUrl("/common/index.jsp"))
					//.andReturn().getRequest();
		 .andExpect(jsonPath("$.iTotalRecords", is(10)));
	}

	@Test
	@Ignore
	public void testLoadUserReferenceListPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testProUserReferenceDatatable() throws Exception {
		 mockMvc.perform(
					get("/project/userReference/user/datatable").session(session))
					.andExpect(status().isOk())
					//.andDo(print())
					//.andExpect(forwardedUrl("/common/index.jsp"))
					//.andReturn().getRequest();
		 .andExpect(jsonPath("$.iTotalRecords", is(10)));
	}

	@Test
	@Ignore
	public void testProjectNewUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveProjectUser() throws Exception {
		MockHttpServletRequest request=(MockHttpServletRequest) mockMvc.perform(
				get("/project/user/new").session(session))
				.andExpect(status().isOk())
				//.andDo(print());
				.andExpect(forwardedUrl("/common/index.jsp"))
				.andReturn().getRequest();
		
		String token=(String) request.getAttribute("token");
		
		request=mockMvc.perform(
				post("/project/new/user/save")
				.param("token", token)
				.param("username", "testjunit11")
				.param("password", "111")
				.param("roleName", "pro_editor")
				.session(session))
				.andExpect(status().isOk())
				//.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	           .andReturn().getRequest();
		assertEquals("success", request.getAttribute("status"));
	}

	@Test
	public void testChangeUserStatus() throws Exception {
		mockMvc.perform(
				post("/project/manage/isAction/user")
				.param("uuid", "2ded57a1-1ee4-44bd-b638-f4c57dcad49a")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"));
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            //.andExpect(jsonPath("$.status", content().e));
	}

	@Test
	@Ignore
	public void testChangeUserStatusHttpServletRequest() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLoadtPasswordPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUserPassWord() throws Exception {
		mockMvc.perform(
				post("/user/update/password")
				.param("oldPassword", "111")
				.param("password", "111")
				.session(session))
				.andExpect(status().isOk())
				//.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
		.andExpect(jsonPath("$.status", is("success")));
	}

	@Test
	@Ignore
	public void testComparePassword() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testChangeAdverStatus() {
		fail("Not yet implemented");
	}

}
