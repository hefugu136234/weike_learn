package com.lankr.tv_cloud.web.front.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;


public class FrontWxOpenUtil {
	/**
	 * 2016-05-23
	 * @he
	 * 前台应用和微信开放平台登录的方法
	 */
	
	/**
	 * 获取开放平台登录的二维码链接
	 */
	public static String getOpenWxQrLink(){
		//登录完后，统一跳转到公共登录之前接口，验证跳转路径
		String sys_redirect_uri=Config.host+"/f/web/auth/logined/before";
		try {
			sys_redirect_uri = URLEncoder.encode(sys_redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String open_login_qr = "https://open.weixin.qq.com/connect/qrconnect?appid="
				+ Config.wx_open_appid + "&" + "redirect_uri=" + sys_redirect_uri
				+ "&response_type=code&scope=snsapi_login&" + "state=" + WebChatMenu.ZHILIAO_STATE
				+ "#wechat_redirect";
		return open_login_qr;
		
	}
	
	public static String getUnionidByCode(String code) {
		String unionid = null;
		try {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
					+ Config.wx_open_appid
					+ "&secret="
					+ Config.wx_open_appsecret
					+ "&code="
					+ code
					+ "&grant_type=authorization_code";
			String message = HttpUtils.sendGetRequest(url);
			System.out.println("message:" + message);
			JSONObject demoJson = new JSONObject(message);
			String access_token = demoJson.getString("access_token");
			System.out.println("access_token:" + access_token);
			int expires_in = demoJson.getInt("expires_in");
			System.out.println("expires_in:" + expires_in);
			String refresh_token = demoJson.getString("refresh_token");
			System.out.println("refresh_token:" + refresh_token);
			String scope = demoJson.getString("scope");
			System.out.println("scope:" + scope);
			String openId = demoJson.getString("openid");
			System.out.println("openid:" + openId);
			unionid = demoJson.getString("unionid");
			System.out.println("unionid:" + unionid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unionid;
	}
	
	/**
	 * 返回视频的时分秒
	 * @return
	 */
	public static String videoTime(int time){
		String val="00:00";
		if(time<60){
			//秒
			val="00:"+repairZero(time);
		}else if(time<3600){
			//分
			int minutes=time/60;
			int seconds=time%60;
			val=repairZero(minutes)+":"+repairZero(seconds);
		}else{
			//小时
			int hour=time/3600;
			int minutes=time%3600/60;
			int seconds=time%3600%60;
			val=hour+":"+repairZero(minutes)+":"+repairZero(seconds);
		}
		return val;
	}
	
	public static String repairZero(int num){
		if(num<10){
			return "0"+num;
		}
		return Integer.toString(num);
	}
   

}
