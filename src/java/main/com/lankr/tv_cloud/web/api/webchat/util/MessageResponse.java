package com.lankr.tv_cloud.web.api.webchat.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;

/**
 *
 *
 * @author jiangyin
 */
public class MessageResponse {

	public final static String QRSCENE = "QRSCENE";

	public final static String TITLE = "TITLE";

	public final static String DESCRIPTION = "DESCRIPTION";

	public final static String PICURL = "PICURL";

	public final static String URL = "URL";

	/**
	 * 回复文本消息
	 *
	 * @param fromUserName
	 * @param toUserName
	 * @param respContent
	 * @return
	 */
	public static String getTextMessage(String fromUserName, String toUserName,
			String respContent) {

		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setFuncFlag(0);

		textMessage.setContent(respContent);
		return MessageUtil.textMessageToXml(textMessage);
	}

	/**
	 * 创建图文消息
	 *
	 * @param fromUserName
	 * @param toUserName
	 * @param articleList
	 * @return
	 */
	public static String getNewsMessage(String fromUserName, String toUserName,
			List<Article> articleList) {

		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setFuncFlag(0);

		// 设置图文消息个数
		newsMessage.setArticleCount(articleList.size());
		// 设置图文消息包含的图文集合
		newsMessage.setArticles(articleList);
		// 将图文消息对象转换成xml字符串
		return MessageUtil.newsMessageToXml(newsMessage);
	}

	/**
	 * @he 2016-07-12 返回文字的文本消息 2016-2-25 微信公众号，接受到文本消息，返回推送
	 */
	public static String dealTextNews(Map<String, String> requestMap) {
		// 发送方帐号（一个OpenID）
		String content = requestMap.get("Content");
		if (content.equals("vr")) {
			String title = "vr视频";
			String description = "欢迎观看vr视频";
			String picurl = Config.host + "/assets/img/app/vr/vr.jpg";
			String url = Config.host + "/api/webchat/vr/video/test";
			requestMap.put(TITLE, title);
			requestMap.put(DESCRIPTION, description);
			requestMap.put(PICURL, picurl);
			requestMap.put(URL, url);
			return singleMessage(requestMap);
		}
		return null;
	}

	/**
	 * @he 2016-07-12 单独返回一条图文消息
	 * @return
	 * 无title 返回空数据
	 */
	public static String singleMessage(Map<String, String> requestMap) {
		// 开发者- 公众帐号
		String toUserName = requestMap.get("ToUserName");
		// 发送方帐号（一个OpenID）
		String fromUserName = requestMap.get("FromUserName");
		String title=requestMap.get(TITLE);
		String description=requestMap.get(DESCRIPTION);
		String picurl=requestMap.get(PICURL);
		String url=requestMap.get(URL);
		if(title==null){
			return null;
		}
		List<Article> list = new ArrayList<Article>();
		list.add(new Article(title, description, picurl, url));
		return getNewsMessage(fromUserName, toUserName,
				list);
	}

	// 向微信返回的图文消息
	/**
	 * 以后返回多条
	 *
	 * @param requestMap
	 * @param list
	 */
	public void imageMessage(Map<String, String> requestMap, List<Article> list) {
		// 开发者- 公众帐号
		// String toUserName = requestMap.get("ToUserName");
		// // 发送方帐号（一个OpenID）
		// String fromUserName = requestMap.get("FromUserName");
		Article artice = new Article();
		artice.setTitle(requestMap.get(TITLE));
		artice.setDescription(requestMap.get(DESCRIPTION));
		artice.setPicUrl(requestMap.get(PICURL));
		artice.setUrl(requestMap.get(URL));
		list.add(artice);
	}

	/**
	 * 2016-07-12
	 * @he 扫描普通二维码
	 * @param requestMap
	 * @return
	 */
	public static String scanCommonQr(Map<String, String> requestMap) {
		requestMap.put(TITLE, "欢迎访问知了微课");
		requestMap.put(DESCRIPTION, "更多手术展演，指南解读，作品比赛尽在知了微课。还有更多积分好礼等您兑换！");
		requestMap.put(PICURL, Config.host + "/assets/img/webchat/index.jpg");
		requestMap.put(URL, WebChatMenu.authCommonUrl(BaseWechatController.WX_INDEX));
		return singleMessage(requestMap);
	}

}
