package com.lankr.tv_cloud.web.api.webchat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonSyntaxException;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSearchResourceList;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.sun.net.httpserver.Authenticator.Success;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class SearchControl extends BaseWechatController {

	/**
	 * 搜索的结果页
	 *
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/search/page", method = RequestMethod.GET)
	public ModelAndView searchResult(HttpServletRequest request) {
		return redirectPageView("wechat/search/search");
	}

	/**
	 * 搜索动作
	 *
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/search/action/result", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String searchAction(HttpServletRequest request,
			@RequestParam String search) {
		bulidRequestFiledC(search, request);
		bulidRequest("微信搜索动作", "resource", null, Status.SUCCESS.message(),
				null, "成功", request);
		List<Resource> resources = resourceFacade.searchResourceListByQ(search);
		WxResourceList vo = new WxResourceList();
		vo.setQuery(search);
		vo.build(resources);
		if (vo.getHits() > 0) {
			request.setAttribute(LOG_OPERATE, Status.SUCCESS.message());
		} else {
			request.setAttribute(LOG_OPERATE, Status.FAILURE.message());
		}
		return vo.toJSON();

	}

	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/search/thrid/action/result", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String searchThridAction(HttpServletRequest request,
			@RequestParam String search) {
		try {
			search = URLEncoder.encode(search, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url=Config.search_host+"/api/search/resource?keyword="+search;
		String result=HttpUtils.sendGetRequest(url);
		try {
			WxSearchResourceList list=gson.fromJson(result, WxSearchResourceList.class);
			if(list==null||!list.getStatus().equals(Status.SUCCESS.message())){
				return failureModel("搜索失败").toJSON();
			}
			WxResourceList vo = new WxResourceList();
			vo.buildSearch(list.getResourceVoItems());
			if (vo.getHits() > 0) {
				request.setAttribute(LOG_OPERATE, Status.SUCCESS.message());
			} else {
				request.setAttribute(LOG_OPERATE, Status.FAILURE.message());
			}
			bulidRequestFiledC(search, request);
			bulidRequest("微信搜索动作", "resource", null, Status.SUCCESS.message(),
					null, "成功", request);
			return vo.toJSON();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return failureModel("搜索失败").toJSON();

	}


}
