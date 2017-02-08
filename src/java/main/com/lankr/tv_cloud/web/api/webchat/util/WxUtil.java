package com.lankr.tv_cloud.web.api.webchat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;

public class WxUtil {
	protected static Log logger = LogFactory.getLog(WxUtil.class);

	// 一般分享制作的授权链接
	public static String getShareSignature(String url) {
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = (new Random().nextInt(9000000 - 1) + 1000000) + "";
		String jsapi_ticket = WebChatMenu.getJsapi_ticket(3);
		jsapi_ticket = jsapi_ticket == null ? "" : jsapi_ticket;
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr
				+ "&timestamp=" + timestamp + "&url=" + url;
		String signature = DigestUtils.shaHex(str);
		WxSignature wxSignature = new WxSignature();
		wxSignature.setAppId(WebChatMenu.APPID);
		wxSignature.setTimestamp(timestamp);
		wxSignature.setNonceStr(nonceStr);
		wxSignature.setSignature(signature);
		wxSignature.setUrl(WebChatMenu.authCommonUrl(url));
		wxSignature.setStatus(Status.SUCCESS);
		return wxSignature.toJSON();
	}

	/**
	 * 2016-4-29 生成签名的原始链接，和跳转链接不一致的分享签名
	 *
	 * @return
	 */
	public static String getRedireSignature(String url, String redirectUrl) {
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = (new Random().nextInt(9000000 - 1) + 1000000) + "";
		String jsapi_ticket = WebChatMenu.getJsapi_ticket(3);
		jsapi_ticket = jsapi_ticket == null ? "" : jsapi_ticket;
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr
				+ "&timestamp=" + timestamp + "&url=" + url;
		String signature = DigestUtils.shaHex(str);
		WxSignature wxSignature = new WxSignature();
		wxSignature.setAppId(WebChatMenu.APPID);
		wxSignature.setTimestamp(timestamp);
		wxSignature.setNonceStr(nonceStr);
		wxSignature.setSignature(signature);
		wxSignature.setUrl(WebChatMenu.authCommonUrl(redirectUrl));
		wxSignature.setStatus(Status.SUCCESS);
		return wxSignature.toJSON();
	}

	/**
	 * 2016-3-14 直播的专属分享
	 *
	 * @param url
	 * @return
	 */
	public static String getShareCastSignature(String url, Broadcast broadcast) {
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = (new Random().nextInt(9000000 - 1) + 1000000) + "";
		String jsapi_ticket = WebChatMenu.getJsapi_ticket(3);
		jsapi_ticket = jsapi_ticket == null ? "" : jsapi_ticket;
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr
				+ "&timestamp=" + timestamp + "&url=" + url;
		String signature = DigestUtils.shaHex(str);
		WxSignature wxSignature = new WxSignature();
		wxSignature.setAppId(WebChatMenu.APPID);
		wxSignature.setTimestamp(timestamp);
		wxSignature.setNonceStr(nonceStr);
		wxSignature.setSignature(signature);
		if (broadcast != null) {
//			LiveVo vo = new LiveVo();
//			vo.buildWxShare(broadcast);
//			wxSignature.setTitle(vo.getName());
//			wxSignature.setDesc(vo.getMark());
//			wxSignature.setPicUrl(vo.getCover());
		}
		String linkUrl = "/api/webchat/broadcast/first/page/"
				+ broadcast.getUuid();
		wxSignature.setUrl(WebChatMenu.authCommonUrl(linkUrl));
		wxSignature.setStatus(Status.SUCCESS);
		return wxSignature.toJSON();
	}

	// 编辑分享的授权分享
	public static String getShareCommonLink(String url) {
		String redirect_uri = WebChatMenu.LOCAL_URL
				+ "/share/common?redirect_uri=" + url;
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
				+ WebChatMenu.ZHILIAO_STATE + "#wechat_redirect";
		return sao_url;
	}

	/**
	 * 2016-02-24 资源分享的特定链接
	 *
	 * @param url
	 * @param isLogin
	 * @param user
	 * @return
	 */
	public static String getShareSignature(String url, User user, String resUuid) {
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = (new Random().nextInt(9000000 - 1) + 1000000) + "";
		String jsapi_ticket = WebChatMenu.getJsapi_ticket(3);
		jsapi_ticket = jsapi_ticket == null ? "" : jsapi_ticket;
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr
				+ "&timestamp=" + timestamp + "&url=" + url;
		String signature = DigestUtils.shaHex(str);
		WxSignature wxSignature = new WxSignature();
		wxSignature.setAppId(WebChatMenu.APPID);
		wxSignature.setTimestamp(timestamp);
		wxSignature.setNonceStr(nonceStr);
		wxSignature.setSignature(signature);
		wxSignature.setUrl(getShareCommonLink(url, user, resUuid));
		wxSignature.setStatus(Status.SUCCESS);
		return wxSignature.toJSON();
	}

	public static String getShareCommonLink(String url, User user,
			String resUuid) {
		String redirect_uri = WebChatMenu.LOCAL_URL
				+ "/share/res/common?resUuid=" + resUuid + "&redirect_uri="
				+ url;
		if (user != null) {
			String userUuid = user.getUuid();// 分享着的uuid
			redirect_uri += "&userUuid=" + userUuid;
		}
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
				+ WebChatMenu.ZHILIAO_STATE + "#wechat_redirect";
		return sao_url;
	}

	/**
	 * 拿到openid 之后，判断user状态，来判断返回的页面，公用方法
	 *
	 * @param webchatUser
	 * @return
	 */
	public static String isLogin(WebchatUser webchatUser) {
		String url = null;
		if (webchatUser == null) {
			// 跳转登陆，无openid，下次订阅登录关联
			url = "/api/webchat/page/login";
		} else {
			User user = webchatUser.getUser();
			if (user == null) {
				url = "/api/webchat/page/login";
			} else {
				// 有user 可登陆
				if (!BaseWechatController.loginIsAbel(user)) {
					url = "/api/webchat/isable/page/login";
				}
			}
		}
		// 返回null，表示可登陆，否则，跳转该页面
		return url;
	}

//	// 通过授权的code，获取openid，返回null，表示不是来自微信requestNum,请求次数
//	public static String getOpenIdByCode(String code, int requestNum) {
//		String openId = null;
//		try {
//			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
//					+ WebChatMenu.APPID
//					+ "&secret="
//					+ WebChatMenu.APPSECRET
//					+ "&code=" + code + "&grant_type=authorization_code";
//			System.out.println("--url--"+url);
//			String message = HttpUtils.sendGetRequest(url);
//			System.out.println("message:" + message);
//			logger.info("message:" + message);
//			JSONObject demoJson = new JSONObject(message);
//			if(!demoJson.has("openid")){
//				return openId;
//			}
//
//			String access_token = demoJson.getString("access_token");
//			System.out.println("access_token:" + access_token);
//			int expires_in = demoJson.getInt("expires_in");
//			System.out.println("expires_in:" + expires_in);
//			String refresh_token = demoJson.getString("refresh_token");
//			System.out.println("refresh_token:" + refresh_token);
//			String scope = demoJson.getString("scope");
//			System.out.println("scope:" + scope);
//			openId = demoJson.getString("openid");
//			System.out.println("openid:" + openId);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return openId;
//		}
//		return openId;
//	}
	
	//2016-12-19 修改
	// 通过授权的code，获取openid，返回null，表示不是来自微信requestNum,请求次数
	public static String getOpenIdByCode(String code, int requestNum) {
		WxAccseeTokenByCode accseeTokenByCode=getOpenIdByCode(code);
		String opneid=OptionalUtils.traceValue(accseeTokenByCode,"openid");
		return opneid;
	}
	
	//通过授权（不论静默还是用户
	public static WxAccseeTokenByCode getOpenIdByCode(String code){
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ WebChatMenu.APPID
				+ "&secret="
				+ WebChatMenu.APPSECRET
				+ "&code=" + code + "&grant_type=authorization_code";
		System.out.println("--url--"+url);
		String message = HttpUtils.sendGetRequest(url);
		System.out.println("--message--"+message);
		WxAccseeTokenByCode accseeTokenByCode=WxAccseeTokenByCode.instanceToken(message);
		return accseeTokenByCode;
	}
	
	public static void main(String[] args) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ "wx276a6518ef3995a3"
				+ "&secret=0762905a3b431911df541f3dab30e002"
				+ "&code=0312HP5z0vodgi13op7z0cTH5z02HP54&grant_type=authorization_code";
		System.out.println("--url--"+url);
		String message = HttpUtils.sendGetRequest(url);
		System.out.println(message);
	}

	public static String getDefaultAvatar(String avatar) {
		if (avatar == null || avatar.isEmpty()) {
			return Config.host + "/assets/img/app/default_img.jpg";
		}
		return avatar;
	}

	public static String getResourceCover(String cover) {
		if (cover == null) {
			return "";
		}
		if (!cover.isEmpty() && !cover.startsWith("http")) {
			cover = "http://cloud.lankr.cn/api/image/" + cover
					+ "?m/2/h/500/f/jpg";
		}
		return cover;
	}

    public static String getCommonShareImg(){
    	return Config.host+"/assets/img/app/logo_share.png";
    }
	// 获取jssdk的签名
	public static String getShareSignature(long timestamp, String nonceStr,
			String url) {
		String jsapi_ticket = WebChatMenu.getJsapi_ticket(1);
		jsapi_ticket = jsapi_ticket == null ? "" : jsapi_ticket;
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr
				+ "&timestamp=" + timestamp + "&url=" + url;
		String signature = DigestUtils.shaHex(str);
		return signature;
	}

	/**
	 * 2016-06-20 微信签名的公共方法
	 */
	public static WxSignature getWxSignature(String localHref) {
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = (new Random().nextInt(9000000 - 1) + 1000000) + "";
		String appid = Config.wx_appid;
		String signature = getShareSignature(timestamp, nonceStr, localHref);
		WxSignature wxSignature = new WxSignature();
		wxSignature.buildBase(timestamp, nonceStr, appid, signature);
		return wxSignature;
	}

}
