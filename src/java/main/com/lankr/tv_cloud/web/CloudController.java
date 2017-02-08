package com.lankr.tv_cloud.web;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.IntegralWeekReport;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.Widget;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BannerWidget;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@SuppressWarnings("all")
@Controller
public class CloudController extends AdminWebController {

	private static Gson gson = new Gson();

	@RequestMapping(value = "/wechat", method = RequestMethod.GET)
	public ModelAndView entrance(HttpServletRequest request) {
		return new ModelAndView("wechat/subscribe");
	}

	@RequestMapping(value = "/api/failure", produces = "text/json; charset=utf-8")
	public @ResponseBody String _50x(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String path = null;
			Platform type = requestKind();
			if (type == Platform.WECHAT) {
				path = "/error/500_wx.html";
			} else if (type == Platform.WEBAPP) {
				path = "/error/500.html";
			}
			if (path != null) {
				request.getRequestDispatcher(path).forward(request, response);
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BaseController.failureModel("处理发生异常").toJSON();
	}

	@RequestMapping(value = "/api/bad_request", produces = "text/json; charset=utf-8")
	public @ResponseBody String _40x(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String path = null;
			if (isRequestFromWechat()) {
				path = "/error/404_wx.html";
			} else {
				path = "/error/404.html";
			}
			request.getRequestDispatcher(path).forward(request, response);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BaseController.failureModel("请求错误").toJSON();
	}

	// @RequestMapping("/manual.html")
	// public ModelAndView manual() {
	// return new ModelAndView("manual");
	// }

	@RequestAuthority(logger = false)
	@RequestMapping(value = "/tv/home/settings", method = RequestMethod.GET)
	public ModelAndView tvBannerSetting(HttpServletRequest request) {
		return index(request, "/wrapped/tv_async.jsp");
	}

	@RequestAuthority(logger = false)
	@RequestMapping(value = "/tv/home/save", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String savePosition(@RequestParam String data_json,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null)
			return getStatusJson(Status.NO_PERMISSION);
		Project project = user.getStubProject();
		if (project == null)
			return getStatusJson(Status.NO_PERMISSION);
		try {
			Type type = new com.google.gson.reflect.TypeToken<List<BannerWidget>>() {
			}.getType();
			List<BannerWidget> widgets = gson.fromJson(data_json, type);
			List<Widget> ws = new ArrayList<Widget>();
			for (int i = 0; i < widgets.size(); i++) {
				BannerWidget widget = widgets.get(i);
				// 获取category
				Category category = assetFacade.getCategoryByUuid(widget
						.getCategoryId());
				if (category == null) {
					return getStatusJson("some widget(s) has no category?");
				}
				if (!category.getProject().isSame(category.getProject())) {
					// throw new Exception("not the same project alias");
					return getStatusJson("project error");
				}
				Widget w = new Widget(widget.getX(), widget.getY(),
						widget.getOffset_x(), widget.getOffset_y(),
						widget.getImageUrl());
				w.setCategory(category);
				w.setProject(project);
				w.setUser(user);
				w.setUuid(Tools.getUUID());
				w.setMark(widget.getMark());
				ws.add(w);
			}
			return getStatusJson(projectFacade.addWidgets(project, ws));
		} catch (Exception e) {
			logger.error("banner json data to objects with an error ", e);
			return getStatusJson(Status.PARAM_ERROR);
		}
	}

	@RequestAuthority(logger = false)
	@RequestMapping(value = "/tv/home/structure", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String getStructure(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null)
			return getStatusJson(Status.NO_PERMISSION);
		Project project = user.getStubProject();
		if (project == null)
			return getStatusJson(Status.NO_PERMISSION);
		List<Widget> widgets = projectFacade.selectProjectWidgets(project);

		List<BannerWidget> bws = new ArrayList<BannerWidget>();
		if (widgets != null && widgets.size() > 0) {
			for (int i = 0; i < widgets.size(); i++) {
				Widget widget = widgets.get(i);
				bws.add(new BannerWidget("widget-" + (i + 1), widget
						.getTv_cover(), widget.getCategory().getUuid(), widget
						.getCategory().getName(), widget.getX(), widget.getY(),
						widget.getOffset_x(), widget.getOffset_y(), widget
								.getMark()));
			}
		}
		return gson.toJson(bws);
	}

}
