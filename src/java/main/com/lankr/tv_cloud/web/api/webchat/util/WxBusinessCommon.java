package com.lankr.tv_cloud.web.api.webchat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;

import com.lankr.tv_cloud.TemplateMessgaeId;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.JSONException;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxBusinessCommon {
	public static final int NEED_LOGIN = 1;
	public static final int NO_LOGIN = 0;

	/**
	 * 2016-4-19 以后所有的推送 分享 登录 授权 获取连接 菜单 处理，通过此处
	 */

	/**
	 * 2016-04-19 二维码授权跳转
	 */
	public static String qrAuthCommonUrl(String redictUrl, int login,
			long sceneid) {
		String redirect_uri = WebChatMenu.LOCAL_URL
				+ "/business/auth/qr/common?sceneid=" + sceneid
				+ "&redirect_uri=" + redictUrl;
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String lastUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ WebChatMenu.APPID
				+ "&"
				+ "redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_base&"
				+ "state="
				+ login
				+ "#wechat_redirect";
		return lastUrl;
	}

	public static void main(String[] args) {
		// String
		// urlString="http://zbox.lankr.cn/api/webchat/business/auth/qr/common?redirect_uri=/api/webchat/activity/total/page/091c6598-ab09-46e6-b583-4ab49d4af001";
		// try {
		// urlString = URLEncoder.encode(urlString, "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(urlString);
		Date date = new Date();
		String dateTime = Tools.formatYMDDate(date);
		dateTime = dateTime + " 00:00:00";
		System.out.println(dateTime);
		try {
			Date zeroDate = Tools.df1.parse(dateTime);
			System.out.println(date.before(zeroDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 2016-07-12 组成webuser的基本信息
	 */
	public static WebchatUser buildWebchatUser(String openId) {
		WebchatUser webchatUser = new WebchatUser();
		webchatUser.setUuid(Tools.getUUID());
		webchatUser.setOpenId(openId);
		webchatUser.setStatus(BaseModel.APPROVED);
		webchatUser.setIsActive(BaseModel.ACTIVE);
		return webchatUser;
	}

	/**
	 * 2016-05-23 如果 wxinfo unionid为空 马上获取信息 如果photo或 通过全局access_token获取用户微信信息
	 * 
	 * @param webchatUser
	 * @return
	 */
	// 获取用户的信息
	public static boolean getWxUserInfo(WebchatUser webchatUser) {
		if (!TemplateMessgaeId.message_swich) {
			return false;
		}
		if (webchatUser == null) {
			return false;
		}
		if (!judyGetData(webchatUser)) {
			return false;
		}
		String message = WebChatMenu.getWxUserInfoByToken(
				webchatUser.getOpenId(), 1);
		// System.out.println("获取数据："+message);
		if (message == null) {
			return false;
		}
		try {
			updateWxInfoModel(webchatUser,message);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void updateWxInfoModel(WebchatUser webchatUser, String message)
			throws Exception {
		JSONObject jsonObject = new JSONObject(message);
		String photo = Tools
				.nullValueFilter(jsonObject.getString("headimgurl"));
		String nickname = jsonObject.getString("nickname");
		if (jsonObject.has("unionid")) {
			String unionid = jsonObject.getString("unionid");
			webchatUser.setUnionid(unionid);
		}

		if (jsonObject.has("sex")) {
			int sex = jsonObject.getInt("sex");
			webchatUser.setSex(Integer.toString(sex));
		}

		// 处理名称中有表情的
		nickname = EmojiFilter.filterEmoji(nickname);
		webchatUser.setNickname(nickname);
		webchatUser.setPhoto(photo);
		message = EmojiFilter.filterEmoji(message);
		webchatUser.setWxinfo(message);
	}
	
	/**
	 * 通过用户主动授权获取 游客信息
	 * @param webchatUser
	 * @param accseeTokenByCode
	 */
	public static void visitorWxInfoBuild(WebchatUser webchatUser,WxAccseeTokenByCode accseeTokenByCode){
		//通过用户主动授权获取 游客信息
		String message=WebChatMenu.getWxUserInfoByToken(accseeTokenByCode.getOpenid(), accseeTokenByCode.getAccess_token());
		if(message==null)
			return ;
		try {
			updateWxInfoModel(webchatUser,message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean judyGetData(WebchatUser webchatUser) {
		String photo = webchatUser.getPhoto();
		String nickname = webchatUser.getNickname();
		String wxinfo = webchatUser.getWxinfo();
		String unionid = webchatUser.getUnionid();
		if (judyString(wxinfo) || judyString(unionid)) {
			return true;
		}
		if (judyString(photo) || judyString(nickname)) {
			// 有一个为空，看更新日期是当天就不在更新，否则更新,一天只更新一次
			Date date = new Date();
			String dateTime = Tools.formatYMDDate(date);
			dateTime = dateTime + " 00:00:00";
			try {
				Date zeroDate = Tools.df1.parse(dateTime);
				if (webchatUser.getModifyDate().before(zeroDate)) {
					return true;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 值为空true 值不空 为false
	 * 
	 * @param string
	 * @return
	 */
	public static boolean judyString(String string) {
		return Tools.isBlank(string);
	}

	// 2016-12-19 判断是否获取游客信息
	public static boolean judyVisitor(WebchatUser webchatUser) {
		String nickname = OptionalUtils.traceValue(webchatUser, "nickname");
		if (Tools.isBlank(nickname)) {
			return true;
		}
		String wxinfo = OptionalUtils.traceValue(webchatUser, "wxinfo");
		if (Tools.isBlank(wxinfo)) {
			return true;
		}
		String unionid = OptionalUtils.traceValue(webchatUser, "unionid");
		if (Tools.isBlank(unionid)) {
			return true;
		}
		return false;
	}

}
