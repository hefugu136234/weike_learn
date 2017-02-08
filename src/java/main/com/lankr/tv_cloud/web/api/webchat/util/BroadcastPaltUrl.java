package com.lankr.tv_cloud.web.api.webchat.util;

import com.lankr.tv_cloud.broadcast.CurrentLivePlatfromData;
import com.lankr.tv_cloud.broadcast.LiveActionShowJs;
import com.lankr.tv_cloud.broadcast.LivePlatform;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.front.BaseFrontController;

public class BroadcastPaltUrl {
	
	public final static int WX_PLAT=1;
	
	public final static int WEB_PLAT=2;
	
	public final static int TV_PLAT=3;

	/**
	 * 医多平台暂时使用的测试参数配置
	 * 
	 * @param url
	 * @param user
	 * @return
	 */
	public static String getYiDuoUrl(String url, User user) {
		// TODO Auto-generated method stub
		// 测试的先返回
		String username = OptionalUtils.traceValue(user, "username");
		if (!url.contains("?")) {
			url = url + "username=" + username + "&mobile=" + username
					+ "&regist=true";
			return url;
		}
		url = url + "&username=" + username + "&mobile=" + username
				+ "&regist=true";
		return url;
	}

	public static String getZhiLiao(String message, User user) {
		String url = null;
		String username = OptionalUtils.traceValue(user, "username");
		LiveActionShowJs liveActionShowJs = LiveActionShowJs
				.parseMessage(message);
		if (liveActionShowJs == null) {
			return url;
		}
		url = liveActionShowJs.getAttendeeJoinUrl();
		url += "&nickName=" + username;
		return url;

	}
	
	public static String getBaidu(String uuid,int plat) {
		String url = "/broadcast/baidu/page/"+uuid;
		if(plat==WX_PLAT){
			url=BaseWechatController.WX_PRIOR+url;
		}else if(plat==WEB_PLAT){
			url=BaseFrontController.PC_PRIOR+url;
		}else if(plat==TV_PLAT){
			url=BaseFrontController.PC_PRIOR+"/tv"+url;
		}
		return url;

	}

	/**
	 * 获取直播链接
	 */
	public static String platCastEntrance(User user, Broadcast broadcast,int plat) {
		String url = null;
		if (user == null || broadcast == null) {
			return url;
		}
		int livePlatId = broadcast.getPlatFormType();
		LivePlatform livePlat = CurrentLivePlatfromData
				.getCurrentLiveFromData().get(livePlatId);
		if (livePlat == null) {
			return url;
		}
		switch (livePlatId) {
		case CurrentLivePlatfromData.YIDUO_PLAT:
			url = getYiDuoUrl(broadcast.getCastAction(), user);
			break;
		case CurrentLivePlatfromData.ZHILIAO_PLAT:
			url = getZhiLiao(broadcast.getCastShowJs(), user);
		case CurrentLivePlatfromData.BAIDU_PLAT:
			url=getBaidu(broadcast.getUuid(),plat);
		default:
			break;
		}
		return url;
	}

}
