package com.lankr.tv_cloud.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.model.Widget;
import com.lankr.tv_cloud.vo.VideoClientData;
import com.lankr.tv_cloud.vo.VideoClientItem;
import com.lankr.tv_cloud.vo.api.ClientWidgetData;

@Controller
@RequestMapping("/api")
public class APIController extends AdminWebController {

	private static int max_loop = 3;

	@RequestMapping(value = "/{uuid}/home/structure", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String projectStructure(@PathVariable String uuid) {
		Project project = projectFacade.getProjectByUuid(uuid);
		if (project == null || !project.isActive()) {
			return getStatusJson("project is invalidate");
		}
		List<Widget> widgets = projectFacade.selectProjectWidgets(project);
		ClientWidgetData data = new ClientWidgetData();
		try {
			data.format(widgets);
			data.setStatus(Status.SUCCESS.message());
		} catch (Exception e) {
			data.setStatus(Status.FAILURE.message());
			e.printStackTrace();
		}
		return gson.toJson(data);
	}

	/**
	 * @param uuid
	 *            分类的uuid 获取分类下面的所有视频
	 */
	@RequestMapping(value = "/{uuid}/asset/videos", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String categorieVideos(@PathVariable String uuid,
			@RequestParam(required = false) Long updated_at) {
		Category c = assetFacade.getCategoryByUuid(uuid);
		if (c == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		List<Video> videos = apiFacade.searchAllOnlineVideosByCategoryId(c,
				updated_at);
		VideoClientData vcd = new VideoClientData();
		vcd.setCategoryName(c.getName());
		vcd.setCategoryUuid(c.getUuid());
		vcd.buildData(videos);
		return gson.toJson(vcd);
	}

	/**
	 * @param 以uuid为主干
	 *            ，所有子分类的视频
	 * 
	 */
	@RequestMapping(value = "/trunk/{uuid}/asset/videos", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String trunkCategoryVideos(@PathVariable String uuid,
			@RequestParam(required = false) Long updated_at) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		List<Video> all = new ArrayList<Video>();
		if (category != null) {
			statVideos(category, 1, all, updated_at);
		}
		VideoClientData vcd = new VideoClientData();
		vcd.buildData(all);
		vcd.setCategoryName(category.getName());
		vcd.setCategoryUuid(category.getUuid());
		vcd.setStatus(Status.SUCCESS.message());
		return gson.toJson(vcd);
	}

	@RequestMapping(value = "/video/{uuid}/detail", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String videoDetail(@PathVariable String uuid) {
		Video video = assetFacade.getVideoByUUID(uuid);
		if (video == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		if (video.getStatus() == BaseModel.APPROVED) {
			VideoClientItem ci = new VideoClientItem();
			ci.format(video);
			return gson.toJson(ci);
		}
		return getStatusJson(Status.NOT_FOUND);
	}

	private void statVideos(Category c, int loop, List<Video> total,
			Long updated_at) {
		if (loop > max_loop) {
			return;
		}
		List<Category> children = c.getChildren();
		total.addAll(apiFacade.searchAllOnlineVideosByCategoryId(c, updated_at));
		if (children == null || children.isEmpty()) {
			return;
		}
		for (Category category : children) {
			statVideos(category, loop + 1, total, updated_at);
		}
	}

	@RequestMapping("/doc")
	public ModelAndView apiPage() {
		return new ModelAndView("api");
	}

//	@RequestMapping(value = "/resource/{uuid}/info", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
//	public @ResponseBody String assetDetail(@PathVariable String uuid,
//			HttpServletRequest request) {
//		Resource res = assetFacade.getResourceByUuid(uuid);
//		if (res == null || !res.isValid()) {
//			return BaseController.failureModel("资源不存在或已被删除").toJSON();
//		}
//		ResourceItemData data = new ResourceItemData();
//		data.build(res);
//		BaseController.bulidRequest("微信扫描获取资源信息", "resource", res.getId(),
//				request);
//		return data.toJSON();
//	}

}
