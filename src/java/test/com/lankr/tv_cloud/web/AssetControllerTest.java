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
public class AssetControllerTest extends HoldProjectControllerTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	@Ignore
	public void testCategoryNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddCategory() throws Exception {
		mockMvc.perform(
					post("/asset/category/node/save").param("parent_uuid", "ee676f87-a50a-41f2-b2e8-e1fb68692a3f")
					.param("categoryName", "测试节点名称")
					.session(session))
					.andExpect(status().isOk())
					.andExpect(content().contentType("text/json; charset=utf-8"))
							//.andExpect(forwardedUrl("/common/index.jsp"));
					//.andDo(print());
		            .andExpect(jsonPath("$.status", is("success")));
	}

	@Test
	public void testUpdateCategory() throws Exception {
		mockMvc.perform(
				post("/asset/category/node/update").
				 param("uuid", "cfa60780-3509-46a8-a08d-1ddc49971743")
				.param("categoryName", "测试节点名称修改")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.status", is("success")));
	}

	@Test
	@Ignore("稍后测试")
	public void testDeleteCategory() throws Exception {
		mockMvc.perform(
				post("/delete/category/node").
				 param("uuid", "e482be7e-3690-4fd5-afb7-e05cada3028c")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.status", is("success")));
	}

	@Test
	@Ignore
	public void testUploadPrepare() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testVideoInfo() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testVideoUpload() {
		fail("Not yet implemented");
	}

	@Test
	public void testVideoSave() throws Exception {
		MockHttpServletRequest request=(MockHttpServletRequest) mockMvc.perform(
				get("/asset/video/upload").session(session))
				.andExpect(status().isOk())
				//.andDo(print());
				.andExpect(forwardedUrl("/common/index.jsp"))
				.andReturn().getRequest();
		
		String token=(String) request.getAttribute("token");
		//session=(MockHttpSession) request.getSession();
		
		System.out.println("token---"+token);
		
		request=mockMvc.perform(
				post("/asset/video/save").param("ccVideoId", "111")
				.param("title", "视频junit测试")
				.param("categoryUuid", "cfa60780-3509-46a8-a08d-1ddc49971743")
				.param("token", token)
				.session(session))
				.andExpect(status().isOk())
						//.andDo(print())
						//.andExpect(forwardedUrl("/common/index.jsp"));
				.andReturn().getRequest();
		assertEquals("success", request.getAttribute("status"));
	}

	@Test
	public void testFetchThqs() throws Exception {
		mockMvc.perform(
				get("/asset/cc/thqs")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"));
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            //.andExpect(jsonPath("$.message", is("userid\u003d87ACA21FD20C9C52\u0026time\u003d1429605150\u0026hash\u003dddf456287672fd6c98126f8f7926c83d")));
	}

	@Test
	public void testFetchCcVideoData() throws Exception {
		mockMvc.perform(
				get("/asset/cc/video")
				.param("uuid", "d73c9fbe-45dd-481e-9f73-fedfcf3f393f")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.video.id", is("8796B5C1B3465F639C33DC5901307461")));
	}

	@Test
	@Ignore
	public void testVideoMgr() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchVideos() throws Exception {
		mockMvc.perform(
				get("/asset/videos/datatable")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.iTotalRecords", is(10)));
	}

	@Test
	public void testChangeVideoStatus() throws Exception {
		mockMvc.perform(
				post("/asset/video/status")
				.param("uuid", "64bcbf00-d685-47a0-bde8-d77e2bf5ad25")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.status", is("视频正在处理中,请稍后再试...")));
	}

	@Test
	public void testVideoDetail() throws Exception {
		mockMvc.perform(
				get("/asset/video/64bcbf00-d685-47a0-bde8-d77e2bf5ad25/detail")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.uuid", is("64bcbf00-d685-47a0-bde8-d77e2bf5ad25")));
	}

	@Test
	public void testVideoupdate() throws Exception {
		mockMvc.perform(
				post("/asset/video/64bcbf00-d685-47a0-bde8-d77e2bf5ad25/update")
				.param("cover", "333")
				.param("title", "视频junit测试333")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.uuid", is("64bcbf00-d685-47a0-bde8-d77e2bf5ad25")));
	}

	@Test
	public void testVideoCategoryUpdate() throws Exception {
		mockMvc.perform(
				post("/asset/video/cb1cddaa-9302-405f-8617-93049e02d02d/category/update")
				.param("categoryUuid", "9091efb2-6ba8-426e-bef7-0d2fe2ae8120")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/json; charset=utf-8"))
						//.andExpect(forwardedUrl("/common/index.jsp"));
				//.andDo(print());
	            .andExpect(jsonPath("$.uuid", is("cb1cddaa-9302-405f-8617-93049e02d02d")));
	}

	@Test
	@Ignore
	public void testSearchRootUuid() {
		fail("Not yet implemented");
	}

}
