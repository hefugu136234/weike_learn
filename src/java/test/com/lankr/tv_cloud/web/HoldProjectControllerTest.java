package com.lankr.tv_cloud.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

@Ignore("abstract case")
public class HoldProjectControllerTest extends ControllerLoginUserTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		session = (MockHttpSession) mockMvc
				.perform(
						post("/user/hold/project").param("uuid",
								"2b90f333-d784-49ea-a3b4-c393fd5724c4")
								.session(session)).andReturn().getRequest()
				.getSession();
	}

	@Test
	public void test() throws Exception {
		super.test();
	}

}
