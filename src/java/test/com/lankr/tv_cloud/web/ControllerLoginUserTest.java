package com.lankr.tv_cloud.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
@Ignore("abstract case")
public class ControllerLoginUserTest extends ControllerTest{
	@Autowired 
	protected MockHttpSession session;

	@Before
	public void setUp() throws Exception {
		super.befoerMethod();
		MockHttpServletRequest request=mockMvc.perform(post("/admin/login").param("username", "kalean").param("password", "111"))
		         .andExpect(view().name("common/index"))
				 .andReturn().getRequest();
		session=(MockHttpSession) request.getSession();
		//System.out.println(session);
	}
	
	@Test
	public void test() throws Exception{
	 super.test();
	}


}
