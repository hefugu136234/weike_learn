package com.lankr.tv_cloud.web.front;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.web.api.webchat.vo.WxBookUserList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxOfflineActivityItem;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseFrontController.PC_PRIOR)
public class FrontOfflineActivityController extends BaseFrontController {

	// 线下活动详情
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/offline/activity/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView offlineActivityDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.apiUseable()) {
			return redirectErrorPage(request, "此活动不存在或已下线");
		}

		bulidRequest("微信线下活动详情", "offline_activity", offlineActivity.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		int bookNum = signUpUserFacade.bookCountUser(offlineActivity.getId(),
				SignUpUser.REFER_OFFLINEACTIVITY);
		User user = getCurrentUser(request);
		SignUpUser sign = signUpUserFacade
				.selectSignUpUserByUser(offlineActivity.getId(),
						SignUpUser.REFER_OFFLINEACTIVITY, user);
		WxOfflineActivityItem vo = new WxOfflineActivityItem();
		vo.buildDetail(offlineActivity, bookNum, user);
		vo.buildIsBook(sign);
		request.setAttribute("vo_data", vo);
		return redirectPageView("web/offline_activity/detail");
	}

	// 参与报名
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/activity/book", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String offlineActivityBook(HttpServletRequest request,
			@RequestParam String uuid) {
		return addOfflineBook(request, uuid);
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/book/list", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String offlineBookList(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int size,
			@RequestParam int currentPage) {
		int from = Math.max(currentPage - 1, 0);
		if (size > 10) {
			size = 10;
		}
		from = from * size;
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.apiUseable()) {
			return failureModel("此活动不存在或已下线").toJSON();
		}
		Pagination<SignUpUser> pagination = signUpUserFacade.webbookUserList(
				offlineActivity.getId(), SignUpUser.REFER_OFFLINEACTIVITY,
				from, size);
		WxBookUserList wx = new WxBookUserList();
		wx.webBuildList(pagination);
		return wx.toJSON();
	}

}
