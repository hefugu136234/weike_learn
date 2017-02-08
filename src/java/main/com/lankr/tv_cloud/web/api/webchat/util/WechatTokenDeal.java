package com.lankr.tv_cloud.web.api.webchat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.web.api.webchat.util.MenuButtons.Button;


public class WechatTokenDeal {

	public static HashMap<String, Object> cacheMap=new HashMap<String, Object>();

	//局部临时access_token,作用获取用户的信息，有效7200=2小时
	public final static String INTERIM_ACCESS_TOKEN="interim_access_token";
	/**
	 * 局部刷新refresh_token，刷新access_token，有效30天，一旦刷新access_token，
	 * refresh_token将失效，重新获取access_token，refresh_token
	 */
	public final static String INTERIM_REFRESH_TOKEN="interim_refresh_token";

	//全局的access_token，有效7200=2小时，主要用在服务号本身操作，如菜单
	//也可查询数据api，公众号（暂不使用），无refresh_token，一旦失效，重新请求
	public final static String WHOLE_ACCESS_TOKEN="whole_access_token";

	public static void main(String[] args) {
		WechatTokenDeal deal=new WechatTokenDeal();
//		String nickname=deal.getUserInfo();
//		String nickname="{🎀asda案说法三十&*（（*……%&*&*个HJHKB内科——+++}";
//		nickname=EmojiFilter.filterEmoji(nickname);
//		System.out.println(nickname);
//		deal.delMenu();
//		deal.creatMenu();


	}



	/**
	 * 11-24 生成带参数的二维码
	 * 用户扫描带场景值二维码时，可能推送以下两种事件：

如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
如果用户已经关注公众号，在用户扫描后会自动进入会话，微信也会将带场景值扫描事件推送给开发者。
	 */
	public final static String SCENE_URL="https://api.weixin.qq.com/cgi-bin/qrcode/create";

	//11-28正式上线的场景id
	public final static int SCENE_ID=20151128;
	//2016-3-23 摇一摇
	public final static int YAO_SCENE_ID=99999999;

	/**
	 *知了测试
	 * @return
	 */
//	public final static String appid="wx276a6518ef3995a3";
//	public final static String appsecret="0762905a3b431911df541f3dab30e002";
//	public final static String LOCAL_URL ="http://zbox.lankr.cn/api/webchat";

	public final static String appid="wx6f33bbfa98752634";
	public final static String appsecret="30f76c4cd1d9202071960a7f6f34bbd3";
	public final static String LOCAL_URL ="http://weike.z-box.com.cn/api/webchat";

	public static String getAccessToken() {
		String url = WebChatMenu.WEBCHAT_URL_PRE
				+ "/token?grant_type=client_credential&appid=" + appid
				+ "&secret=" + appsecret;
		String access_object = HttpUtils.sendGetRequest(url);
		System.out.println("getAccessTokenResult="+access_object);
		JSONObject demoJson = new JSONObject(access_object);
		String access_token = demoJson.getString("access_token");
		return access_token;
	}

	//获取用户基本信息的测试
	public String getUserInfo(){
		//o9UKvw5Z9chFspnDSF-o4rn54ZSg (xing) o9UKvw8f0tM0R47ajWSnL8yo_ZZM
		String access_token=getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token
				+ "&openid=o9UKvw8f0tM0R47ajWSnL8yo_ZZM"
				+ "&lang=zh_CN";
		String message=HttpUtils.sendGetRequest(url);
		System.out.println(message);
		JSONObject demoJson = new JSONObject(message);
		String nickname=demoJson.getString("nickname");
		System.out.println(nickname);
		System.out.println(nickname.length());
		for(int i = 0 ; i < nickname.length() ; i++){
			System.out.println(nickname.charAt(i));
		}


		return nickname;
	}



	public void createQr(){
		ActionInfo info=new ActionInfo();
		Scene sce=new Scene();
		sce.setScene_id(YAO_SCENE_ID);
		info.setScene(sce);
		WxTempQr wxTempQr=new WxTempQr();
		wxTempQr.setExpire_seconds(TimeUnit.DAYS.toSeconds(30));
		//wxTempQr.setExpire_seconds(TimeUnit.D.toSeconds(300));
		wxTempQr.setAction_name(WxTempQr.QR_SCENE);//临时
		wxTempQr.setAction_info(info);

		Gson gson=new Gson();
		String json=gson.toJson(wxTempQr);

		String acess_token=getAccessToken();

		String s_url=SCENE_URL+"?access_token="+acess_token;

		String message=HttpUtils.sendPostRequest(s_url, json,true);
		System.out.println("message="+message);
		JSONObject demoJson = new JSONObject(message);
		String ticket = demoJson.getString("ticket");
		System.out.println("ticket="+ticket);
		long expire_seconds = demoJson.getLong("expire_seconds");
		System.out.println(expire_seconds);
		String url = demoJson.getString("url");
		System.out.println("url="+url);

	}

	public String creatMenu() {
		String access_token = getAccessToken();
		String url = WebChatMenu.WEBCHAT_URL_PRE + "/menu/create?access_token="
				+ access_token;
		MenuButtons menuButtons = buildMenu();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String json_menu = gson.toJson(menuButtons);
		//System.out.println(json_menu);
		String result = HttpUtils.sendPostRequest(url, json_menu, false);
		System.out.println(result);
		return result;
	}

	public String delMenu() {
		String access_token = getAccessToken();
		String url = WebChatMenu.WEBCHAT_URL_PRE + "/menu/delete?access_token="
				+ access_token;
		String result = HttpUtils.sendGetRequest(url);
		System.out.println(result);
		return result;
	}


	public MenuButtons buildMenu() {
		MenuButtons menuButtons = new MenuButtons();
		 MenuButtons.Button button1 = new MenuButtons.Button();
		 button1.setType("view");
		 button1.setName("知了微课");
		 button1.setKey("zlxy");
		 button1.setUrl(getWebAutoUrl("/api/webchat/index"));
		 MenuButtons.Button button2 = new MenuButtons.Button();
		 button2.setType("view");
		 button2.setName("精彩直播");
		 button2.setKey("ZBHD");
		 button2.setUrl(getWebAutoUrl("/api/webchat/broadcast/wonder/page"));
		 MenuButtons.Button button3 = new MenuButtons.Button();
		 button3.setType("view");
		 button3.setName("个人中心");
		 button3.setKey("GRZX");
		 button3.setUrl(getWebAutoUrl("/api/webchat/my/center"));
		Button[] button = {button1 ,button2,button3};
		menuButtons.setButton(button);
		return menuButtons;
	}

	// 获取微信的认证链接
	public static String getWebAutoUrl(String url) {
		String redirect_uri = LOCAL_URL + "/auth/common/skip?redirect_uri="+url;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
			System.out.println(redirect_uri);
			System.out.println(URLDecoder.decode(redirect_uri, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String entrance_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ appid
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ WebChatMenu.ZHILIAO_STATE + "#wechat_redirect";
		System.out.println(entrance_url);
		return entrance_url;
	}


}
