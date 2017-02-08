package com.lankr.tv_cloud.web.api.webchat.util;

import javax.servlet.http.HttpServletRequest;

import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.api.webchat.vo.WxActivityItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxBroadcastItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxExpertVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;

public class WxShare {

	/**
	 * 2016-06-20 微信签名的链接跳转，title
	 */
	public static void buildWxSignatureDetail(HttpServletRequest request,
			String viewName, int shareType, String localHref, User user,
			WxSignature wxSignature) {
		if(viewName.equals("wechat/error/error")){
			//返回错误页面
			buildCommonShare(localHref, wxSignature);
			return ;
		}
		wxSignature.setShareType(shareType);
		switch (shareType) {
		case WxSignature.WX_COMMON_SHARE:
			buildCommonShare(localHref, wxSignature);
			break;
		case WxSignature.WX_RESOURCE_SHARE:
			buildResourceShare(request, user, viewName, wxSignature);
			break;
		case WxSignature.WX_ACTIVITY_SHARE:
			buildActivityShare(request,wxSignature);
			break;
		case WxSignature.WX_LIVE_SHARE:
			buildLiveShare(request,wxSignature);
			break;
		case WxSignature.WX_ACTIVITY_EXPERT_SHARE:
			buildActivityExpertShare(request,wxSignature);
			break;

		default:
			break;
		}

	}

	public static void buildCommonShare(String localHref,
			WxSignature wxSignature) {
		String title = "知了云盒";
		String picUrl = WxUtil.getCommonShareImg();
		String url = WebChatMenu.authCommonUrl(localHref);
		String desc = "“知了云盒”内容丰富，涵盖麻醉、内科、外科等各大领域。";
		wxSignature.buildDetail(title, picUrl, desc, url);
	}

	public static void buildResourceShare(HttpServletRequest request,
			User user, String viewName, WxSignature wxSignature) {
		WxResourceItem item = (WxResourceItem) request.getAttribute("res_vo");
		String url = BaseWechatController.WX_PRIOR + "/resource/first/page/"
				+ item.getUuid();
		if (viewName.equals("wechat/resource/share_gift_detail")) {
			// 分享有礼
			// 来自谁的分享
			String oriUserUuid = request.getParameter("oriUserUuid");
			if (oriUserUuid != null && !oriUserUuid.isEmpty()) {
				wxSignature.setOriUserUuid(oriUserUuid);
			}
			String title = OptionalUtils.traceValue(item, "news.title");
			String picUrl = WxUtil.getCommonShareImg();
			if (user != null) {
				url += "?oriUserUuid=" + user.getUuid();
			}
			url = WebChatMenu.authCommonUrl(url);
			String desc = OptionalUtils.traceValue(item, "news.summary");
			wxSignature.buildDetail(title, picUrl, desc, url);
		} else if (viewName.equals("wechat/resource/news_detail")) {
			// 新闻
			String title = OptionalUtils.traceValue(item, "news.title");
			String picUrl = WxUtil.getCommonShareImg();
			url = WebChatMenu.authCommonUrl(url);
			String desc = OptionalUtils.traceValue(item, "news.summary");
			wxSignature.buildDetail(title, picUrl, desc, url);
		} else {
			// 其他资源
			String title = OptionalUtils.traceValue(item, "name");
			String picUrl = OptionalUtils.traceValue(item, "cover");
			String desc = OptionalUtils.traceValue(item, "desc");
			String speakerName = OptionalUtils.traceValue(item, "speakerName");
			desc = speakerName + " 简介:" + desc;
			url = WebChatMenu.authCommonUrl(url);
			wxSignature.buildDetail(title, picUrl, desc, url);
		}
	}
	//活动分享
	public static void buildActivityShare(HttpServletRequest request, WxSignature wxSignature){
		WxActivityItem item=(WxActivityItem) request.getAttribute("vo_data");
		String title = OptionalUtils.traceValue(item, "name");
		String picUrl = OptionalUtils.traceValue(item, "kv");
		String desc = OptionalUtils.traceValue(item, "description");
		String url = BaseWechatController.WX_PRIOR + "/activity/total/page/"
				+ item.getUuid();
		url = WebChatMenu.authCommonUrl(url);
		wxSignature.buildDetail(title, picUrl, desc, url);
	}
	//活动专家
	public static void buildActivityExpertShare(HttpServletRequest request, WxSignature wxSignature){
		WxExpertVo item=(WxExpertVo) request.getAttribute("vo_data");
		String title = OptionalUtils.traceValue(item, "name");
		String picUrl = OptionalUtils.traceValue(item, "cover");
		String desc = OptionalUtils.traceValue(item, "joinProfessor");
		String url = BaseWechatController.WX_PRIOR + "/activity/expert/detail/"
				+ item.getUuid();
		url = WebChatMenu.authCommonUrl(url);
		wxSignature.buildDetail(title, picUrl, desc, url);
	}
	//直播
	public static void buildLiveShare(HttpServletRequest request, WxSignature wxSignature){
		WxBroadcastItem item=(WxBroadcastItem) request.getAttribute("vo_data");
		String title = OptionalUtils.traceValue(item, "name");
		String picUrl = OptionalUtils.traceValue(item, "cover");
		String desc = OptionalUtils.traceValue(item, "desc");
		String url = BaseWechatController.WX_PRIOR + "/broadcast/first/page/"
				+ item.getUuid();
		url = WebChatMenu.authCommonUrl(url);
		wxSignature.buildDetail(title, picUrl, desc, url);
	}

}
