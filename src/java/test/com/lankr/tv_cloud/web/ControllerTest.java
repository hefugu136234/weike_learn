package com.lankr.tv_cloud.web;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationMVC.xml",
		"classpath:spring/applicationContext.xml" })
@Ignore("abstract case")
public abstract class ControllerTest {
	@Autowired
	protected WebApplicationContext ctx;
	
	protected MockMvc mockMvc;
	
	@Before
	public void befoerMethod() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	public void test() throws Exception{
		ComboPooledDataSource bean=(ComboPooledDataSource) ctx.getBean("mysqlDataSource");
		assertEquals("root", bean.getUser());
		//assertEquals("kalean", bean.getPassword());
		assertEquals("111111", bean.getPassword());
	}


}
