package com.lankr.tv_cloud.web.api.webchat;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.vo.WxBookUserList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxOfflineActivityItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxOfflineActivityList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class WxOfflineActivityController extends BaseWechatController {
	/**
	 * 2016-08-29 开发的线下活动
	 */

	// 线下活动的列表
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/activity/list", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String offlineActivityList(HttpServletRequest request,
			@RequestParam String startTime, @RequestParam int size) {
		if (size > 20) {
			size = 20;
		}
		List<OfflineActivity> list = offlineActivityFacade
				.wxOfflineActivityList(startTime, size);
		WxOfflineActivityList wx = new WxOfflineActivityList();
		wx.wxBuildList(list);
		return wx.toJSON();
	}

	// 线下活动详情
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
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
		return redirectPageView("wechat/offline_activity/offline_activity_detail");
	}

	// 参与报名
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/activity/book", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String offlineActivityBook(HttpServletRequest request,
			@RequestParam String uuid) {
		return addOfflineBook(request, uuid);
	}

	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/offline/book/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView offlineBookDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.apiUseable()) {
			return redirectErrorPage(request, "此活动不存在或已下线");
		}

		bulidRequest("微信线下活动的报名详情", "offline_activity",
				offlineActivity.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		int bookNum = signUpUserFacade.bookCountUser(offlineActivity.getId(),
				SignUpUser.REFER_OFFLINEACTIVITY);
		User user = getCurrentUser(request);
		WxOfflineActivityItem vo = new WxOfflineActivityItem();
		vo.buildDetail(offlineActivity, bookNum, user);

		request.setAttribute("vo_data", vo);

		return redirectPageView("wechat/offline_activity/offline_activity_apply");
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/book/list", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String offlineBookList(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String startTime,
			@RequestParam int size) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.apiUseable()) {
			return failureModel("此活动不存在或已下线").toJSON();
		}
		if (size > 20) {
			size = 20;
		}
		List<SignUpUser> list = signUpUserFacade.wxbookUserList(
				offlineActivity.getId(), SignUpUser.REFER_OFFLINEACTIVITY,
				startTime, size);
		WxBookUserList wx = new WxBookUserList();
		wx.buildList(list);
		return wx.toJSON();
	}
	
	//我报名的活动
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/offline/activity/off/my", method = RequestMethod.GET)
	public ModelAndView offlineBOffMy(HttpServletRequest request) {
		bulidRequest("微信报名的线下活动", "offline_activity",
				null, Status.SUCCESS.message(), null, "成功",
				request);
		User user=getCurrentUser(request);
		//待审核
		int waitCheck=signUpUserFacade.bookLineActivityOfUser(0, user);
		request.setAttribute("waitCheck", waitCheck);
		//已审核
		int checked=signUpUserFacade.bookLineActivityOfUser(1, user);
		request.setAttribute("checked", checked);
		return redirectPageView("wechat/offline_activity/my_offline_activity");
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/activity/my/book/list", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String myBookActivity(HttpServletRequest request,
			@RequestParam int status, @RequestParam String startTime,
			@RequestParam int size) {
		User user=getCurrentUser(request);
		if (size > 20) {
			size = 20;
		}
		List<OfflineActivity> list = offlineActivityFacade.bookLineActivityOfUserList(status, user, startTime, size);
		WxOfflineActivityList wx = new WxOfflineActivityList();
		wx.wxBuildListOfUser(list, signUpUserFacade);
		return wx.toJSON();
	}

}
