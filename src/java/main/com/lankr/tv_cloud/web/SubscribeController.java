package com.lankr.tv_cloud.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Subscribe;
import com.lankr.tv_cloud.vo.SubscribeSuface;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping("/project/subscribe")
public class SubscribeController extends AdminWebController{
	
	@RequestAuthority(value = Role.PRO_EDITOR,logger=false)
	@RequestMapping("/list/page")
	public ModelAndView subscribeListPage(HttpServletRequest request) {
		return index(request, "/wrapped/subscribe/subscribe_list.jsp");
	}
	
	@RequestMapping(value = "/list/data")
	@RequestAuthority(value = Role.PRO_EDITOR)
	public @ResponseBody String pdfListData(HttpServletRequest request) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Subscribe> pagination = subscribeFacade.searchPagenation(from, pageItemTotal, searchValue);
		SubscribeSuface suface = new SubscribeSuface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.buildData(pagination.getResults());
		BaseController.bulidRequest("后台查看预约列表", "subscribe", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}
}
