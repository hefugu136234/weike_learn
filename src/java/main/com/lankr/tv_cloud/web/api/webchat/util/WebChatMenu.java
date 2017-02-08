package com.lankr.tv_cloud.web.api.webchat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.TemplateMessgaeId;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.api.webchat.util.MenuButtons.Button;

public class WebChatMenu {

	public static final String ZHILIAOTOKEN = "zhiliaotoken";

	public static final String APPID = Config.wx_appid;

	public static final String APPSECRET = Config.wx_appsecret;

	public final static String WEBCHAT_URL_PRE = "https://api.weixin.qq.com/cgi-bin";

	public final static String WXY = "wxy";

	public final static String WDSC = "wdsc";

	public final static String GRZX = "grzx";

	public final static String SAO_SAO = "sao_sao";

	public final static String LOCAL_URL = Config.host
			+ BaseWechatController.WX_PRIOR;

	// 微信2次重定向的statekey
	public final static String ZHILIAO_STATE = "zhiliaoState";// 普通信息标志

	// 微信认证头像的标志
	public final static String ZHILIAO_PHOTO_STATE = "zhiliao_photo_state";

	// 全局的access_token，有效7200=2小时，主要用在服务号本身操作，如菜单
	// 也可查询数据api，公众号（暂不使用），无refresh_token，一旦失效，重新请求
	// public static String WHOLE_ACCESS_TOKEN = null;

	// 全局的jsapi_ticket,前台微信签名时，使用
	// public static String WHOLE_JSAPI_TICKET=null;

	private static JsApiTicket jsTicket = null;

	private static WholeAccessToken accessToken = null;

	private static Gson gson = new Gson();

	public static void main(String[] args) {
		WebChatMenu menu = new WebChatMenu();
		// System.out.println(testLoginUrl());
		System.out.println(LOCAL_URL);
	}

	public String creatMenu() {
		String access_token = getAccessToken(3);
		String url = WEBCHAT_URL_PRE + "/menu/create?access_token="
				+ access_token;
		MenuButtons menuButtons = buildMenu();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String json_menu = gson.toJson(menuButtons);
		System.out.println(json_menu);
		String result = HttpUtils.sendPostRequest(url, json_menu, false);
		return result;
	}

	public String delMenu() {
		String access_token = getAccessToken(3);
		String url = WEBCHAT_URL_PRE + "/menu/delete?access_token="
				+ access_token;
		String result = HttpUtils.sendGetRequest(url);
		return result;
	}

	// 全局access_token,必须用户关注，才可拿到头像
	public static String getAccessToken(int requestNum) {
		if (TemplateMessgaeId.message_swich) {
			if (requestNum < 1) {
				return null;
			}
			// System.out.println(accessToken == null ||
			// !accessToken.isValid());
			if (accessToken == null || !accessToken.isValid()) {
				try {
					String url = WEBCHAT_URL_PRE
							+ "/token?grant_type=client_credential&appid="
							+ APPID + "&secret=" + APPSECRET;
					String message = HttpUtils.sendGetRequest(url);
					accessToken = gson
							.fromJson(message, WholeAccessToken.class);
					accessToken.makeDate = System.currentTimeMillis();
				} catch (Exception e) {
					e.printStackTrace();
					// 重连,链接3次
					requestNum--;
					return getAccessToken(requestNum);

				}
			}
			return accessToken.access_token;
		}
		return null;
	}

	/**
	 * 取得头像用户的2015-11-25 新
	 *
	 * @return 使用全局token requestNum 重新链接的次数
	 */
	public static String getUserInfoByToken(String openid, int requestNum) {
		String photo = null;
		String access_token = getAccessToken(3);
		if (requestNum < 1) {
			return photo;
		}
		if (access_token == null) {
			return photo;
		}
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
				+ access_token + "&openid=" + openid + "&lang=zh_CN";
		try {
			String message = HttpUtils.sendGetRequest(url);
			JSONObject jsonObject = new JSONObject(message);
			// 1订阅 0取消订阅
			int subscribe = jsonObject.getInt("subscribe");
			if (subscribe == 1) {
				photo = jsonObject.getString("headimgurl");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			requestNum--;
			return getUserInfoByToken(openid, requestNum);
		}
		return photo;
	}

	public MenuButtons buildMenu() {
		MenuButtons menuButtons = new MenuButtons();
		// MenuButtons.Button button1 = new MenuButtons.Button();
		// button1.setType("click");
		// button1.setName("微学院");
		// button1.setKey(WXY);
		// MenuButtons.Button button2 = new MenuButtons.Button();
		// button2.setType("click");
		// button2.setName("我的收藏");
		// button2.setKey(WDSC);
		// MenuButtons.Button button3 = new MenuButtons.Button();
		// button3.setType("click");
		// button3.setName("个人中心");
		// button3.setKey(GRZX);
		MenuButtons.Button button1 = new MenuButtons.Button();
		button1.setType("view");
		button1.setName("知了微课");
		button1.setKey("zlxy");
		button1.setUrl(getWebAutoUrl(WXY));
		MenuButtons.Button button2 = new MenuButtons.Button();
		button2.setType("view");
		button2.setName("个人中心");
		button2.setKey("grzx");
		button2.setUrl(getWebAutoUrl(GRZX));
		Button[] button = { button1, button2 };
		menuButtons.setButton(button);
		return menuButtons;
	}

	// 获取授权认证的token
	// public static String oauthAccessToken(String code) {
	// String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
	// + APPID + "&secret=" + APPSECRET + "&code=" + code
	// + "&grant_type=authorization_code";
	// String message = HttpUtils.sendGetRequest(url);
	// return message;
	// }

	public static String getJsapi_ticket(int requestNum) {
		if (requestNum < 1) {
			return null;
		}
		String access_token = getAccessToken(3);
		if (access_token == null) {
			return null;
		}
		System.out.println(jsTicket == null || !jsTicket.isValid());
		if (jsTicket == null || !jsTicket.isValid()) {
			try {
				String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
						+ access_token + "&type=jsapi";
				jsTicket = gson.fromJson(HttpUtils.sendGetRequest(url),
						JsApiTicket.class);
				jsTicket.makeDate = System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
				requestNum--;
				return getJsapi_ticket(requestNum);
			}
		}
		return jsTicket.ticket;

	}

	private class JsApiTicket {

		String ticket;
		// s
		private long expires_in;

		private long makeDate;

		// 临近1分钟的时候过期
		boolean isValid() {
			return expires_in
					- TimeUnit.MILLISECONDS.toSeconds(System
							.currentTimeMillis() - makeDate) > 60;
		}
	}

	private class WholeAccessToken {

		private String access_token;

		private long expires_in;

		private long makeDate;

		// 临近1分钟的时候过期

		boolean isValid() {
			return expires_in
					- TimeUnit.MILLISECONDS.toSeconds(System
							.currentTimeMillis() - makeDate) > 60;
		}
	}

	// 获取微信的认证链接
	public static String getWebAutoUrl(String key) {
		// index collect tv_user personal_cente
		// http://lankrdemo20150901.lankr.cn/api/webchat/verification?redirect_uri=index
		System.out.println(LOCAL_URL);
		String redirect_uri = LOCAL_URL + "/verification?redirect_uri=";
		switch (key) {
		case WXY:
			redirect_uri = redirect_uri + "index";
			break;
		case WDSC:
			redirect_uri = redirect_uri + "collect";
			break;
		case GRZX:
			redirect_uri = redirect_uri + "center";
			break;

		default:
			break;
		}

		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
			System.out.println(redirect_uri);
			System.out.println(URLDecoder.decode(redirect_uri, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String entrance_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		System.out.println(entrance_url);
		return entrance_url;
	}

	public static String getPhotoUrl(String redirect) {
		String redirect_uri = LOCAL_URL + "/verification?redirect_uri="
				+ redirect;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
			System.out.println(redirect_uri);
			System.out.println(URLDecoder.decode(redirect_uri, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String entrance_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_userinfo&"
				+ "state="
				+ ZHILIAO_PHOTO_STATE + "#wechat_redirect";
		return entrance_url;
	}

	// 微信扫一扫看视频，认证链接
	public static String lookVideo(String uuid) {
		// http://lankrdemo20150901.lankr.cn/api/webchat/video_look?uuid=index
		String redirect_uri = LOCAL_URL + "/video_look?uuid=" + uuid;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sao_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		return sao_url;

	}

	/**
	 * 11-20 通过unionid 关联用户，在webuser再加unionid字段， 订阅 和 授权都要修改，unionid更新存储
	 *
	 * @return
	 */

	// 微信测试入口页面
	public static String enterUrl() {
		String url = "http://www.z-box.com.cn/api/webchat/verification?redirect_uri=index";
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sao_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ url
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		return sao_url;
	}


	// web扫描登录页面
	public static String testLoginUrl() {
		// 开放平台的id
		String appid = "wx8a99fe828626f7af";
		String state = "登录测试";
		String url = "http://lankrdemo20150901.lankr.cn/api/webchat/t_login";
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String loginUrl = "https://open.weixin.qq.com/connect/qrconnect?appid="
				+ appid + "&" + "redirect_uri=" + url
				+ "&response_type=code&scope=snsapi_login&" + "state=" + state
				+ "#wechat_redirect";
		return loginUrl;
	}

	public static String weboauthAccessToken(String code) {
		String appid = "wx8a99fe828626f7af";
		String appsecret = "d4624c36b6795d1d99dcf0547af5443d";
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appid + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";
		String message = HttpUtils.sendGetRequest(url);
		return message;
	}

	public static String webgetUser(String access_token, String openid) {
		String appid = "wx8a99fe828626f7af";
		String appsecret = "d4624c36b6795d1d99dcf0547af5443d";
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ access_token + "&openid=" + openid;
		String message = HttpUtils.sendGetRequest(url);
		return message;
	}

	// 11-24 扫二维码 企业用户进入注册
	public static String getCompanyRegUrl() {
		String redirect_uri = LOCAL_URL + "/company/page/reg";
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
			System.out.println(redirect_uri);
			System.out.println(URLDecoder.decode(redirect_uri, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String entrance_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		System.out.println(entrance_url);
		return entrance_url;
	}

	// 微信分享有礼的授权链接
	public static String enterUrl(String url) {
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sao_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ url
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		return sao_url;
	}

	// 微信扫一扫看PDF，认证链接
	public static String lookpdf(String uuid) {
		// http://lankrdemo20150901.lankr.cn/api/webchat/video_look?uuid=index
		String redirect_uri = LOCAL_URL + "/res/pdf/" + uuid;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sao_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		return sao_url;

	}

	/**
	 * 通过服务器的全局access_token
	 * 16-01-04,获取用户信息
	 */
	public static String getWxUserInfoByToken(String openid, int requestNum) {
		String message = null;
		String access_token = getAccessToken(1);
		if (access_token == null) {
			return message;
		}
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
				+ access_token + "&openid=" + openid + "&lang=zh_CN";
		try {
			message = HttpUtils.sendGetRequest(url);
			System.out.println("获取信息-message:" + message);
			JSONObject jsonObject = new JSONObject(message);
			// 1订阅 0取消订阅
			if (jsonObject.has("subscribe")) {
				int subscribe = jsonObject.getInt("subscribe");
				if (subscribe != 1) {
					message = null;
				}
			} else {
				message = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	/**
	 * 通过用户主动授权获取，用户的信息
	 * @param openid
	 * @param access_token
	 * @return
	 */
	public static String getWxUserInfoByToken(String openid, String access_token) {
		String message = null;
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ access_token + "&openid=" + openid + "&lang=zh_CN";
		try {
			message = HttpUtils.sendGetRequest(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	

	/**
	 * 微信工厂生成临时二维码, 最多30天，
	 */
	public static String createTempQr(long sceneid, int days, int requestNum) {
		String ticket = null;
		String access_token = getAccessToken(1);
		if (requestNum < 1) {
			return ticket;
		}
		if (access_token == null) {
			return ticket;
		}
		ActionInfo info = new ActionInfo();
		Scene sce = new Scene();
		sce.setScene_id(sceneid);
		info.setScene(sce);
		WxTempQr wxTempQr = new WxTempQr();
		wxTempQr.setExpire_seconds(TimeUnit.DAYS.toSeconds(days));
		wxTempQr.setAction_name(WxTempQr.QR_SCENE);// 临时
		wxTempQr.setAction_info(info);

		String json = gson.toJson(wxTempQr);
		try {
			String s_url = WechatTokenDeal.SCENE_URL + "?access_token="
					+ access_token;
			String message = HttpUtils.sendPostRequest(s_url, json, true);
			System.out.println(message);
			JSONObject demoJson = new JSONObject(message);
			ticket = demoJson.getString("ticket");
			System.out.println(ticket);
			// long expire_seconds = demoJson.getLong("expire_seconds");
			// System.out.println(expire_seconds);
			// String url = demoJson.getString("url");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			requestNum--;
			return createTempQr(sceneid, days, requestNum);
		}
		if (ticket != null && !ticket.isEmpty()) {
			ticket = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
					+ ticket;
			// try {
			// ticket = URLEncoder.encode(ticket, "UTF-8");
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		return ticket;
	}

	/**
	 * 生成永久微信二维码
	 *
	 * @param sceneid
	 * @param days
	 * @param requestNum
	 * @return
	 */
	public static String createLimitsceneQr(long sceneid, int requestNum) {
		String ticket = null;
		String access_token = getAccessToken(3);
		if (requestNum < 1) {
			return ticket;
		}
		if (access_token == null) {
			return ticket;
		}
		ActionInfo info = new ActionInfo();
		Scene sce = new Scene();
		sce.setScene_id(sceneid);
		info.setScene(sce);
		WxTempQr wxTempQr = new WxTempQr();
		wxTempQr.setAction_name(WxTempQr.QR_LIMIT_SCENE);// 永久
		wxTempQr.setAction_info(info);

		String json = gson.toJson(wxTempQr);
		try {
			String s_url = WechatTokenDeal.SCENE_URL + "?access_token="
					+ access_token;
			String message = HttpUtils.sendPostRequest(s_url, json, true);
			System.out.println(message);
			JSONObject demoJson = new JSONObject(message);
			ticket = demoJson.getString("ticket");
			System.out.println(ticket);
			// long expire_seconds = demoJson.getLong("expire_seconds");
			// System.out.println(expire_seconds);
			// String url = demoJson.getString("url");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			requestNum--;
			return createLimitsceneQr(sceneid, requestNum);
		}
		if (ticket != null && !ticket.isEmpty()) {
			ticket = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
					+ ticket;
			// try {
			// ticket = URLEncoder.encode(ticket, "UTF-8");
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		return ticket;
	}

	// 2016-2-3 扫二维码 进入活动详情界面
	public static String getActivityDetail(String activityUuid) {
		String redirect_uri = LOCAL_URL + "/sao/qr/activity/" + activityUuid;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
			// System.out.println(redirect_uri);
			// System.out.println(URLDecoder.decode(redirect_uri, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String entrance_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		System.out.println(entrance_url);
		return entrance_url;
	}

	/**
	 * 通过微信的media_id，拿到上传的图片
	 *
	 * @param openid
	 * @param requestNum
	 * @return
	 */
	public static String getWxUserUploadFile(String media_id) {
		String access_token = getAccessToken(3);
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
				+ access_token + "&media_id=" + media_id;
		return url;
	}

	/**
	 * 2016-3-1,走一个公用的授权链接进入页面，能取到user跳转跳转链接，否则跳转登录页面 rediturl跳转链接
	 * 2016-07-11 生成微信的默认授权链接，指定了默认方法
	 */
	public static String authCommonUrl(String rediturl) {
		String redirect_uri = LOCAL_URL + "/auth/common/skip?redirect_uri="
				+ rediturl;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		return url;
	}

	// 生成认证授权信息
	/**
	 * 生成一个微信的授权链接，不指定默认入口
	 * @param redirect_uri
	 * @return
	 * 2016-07-11 @he
	 */
	public static String wxAuthLinkNoDefault(String redirect_uri) {
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sao_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		return sao_url;

	}
	
	/**
	 * 2016-12-19 获取游客信息的授权页面
	 * @param rediturl
	 * @return
	 */
	public static String authCommonVisitor(String rediturl) {
		String redirect_uri = LOCAL_URL + "/auth/common/visitor?redirect_uri="
				+ rediturl;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_userinfo&"
				+ "state="
				+ ZHILIAO_STATE + "#wechat_redirect";
		return url;
	}

}
