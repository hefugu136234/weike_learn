package com.lankr.tv_cloud.facade.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrMessage;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.model.QrcodeScanRecode;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.api.webchat.util.MessageResponse;
import com.lankr.tv_cloud.web.api.webchat.util.MessageUtil;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.util.WxAccseeTokenByCode;
import com.lankr.tv_cloud.web.api.webchat.util.WxBusinessCommon;
import com.lankr.tv_cloud.web.api.webchat.vo.WxActivityItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxBroadcastItem;

public class WebchatFacadeImp extends FacadeBaseImpl implements WebchatFacade {

	private ExecutorService pool = Executors.newFixedThreadPool(5);

	/**
	 * 2016-06-29 优化
	 */
	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "webChat";
	}

	/**
	 * 处理微信发送过来的消息
	 */
	@Override
	public String handleMessage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		// 默认返回的文本消息内容
		String message = "";
		// xml请求解析
		// 调用消息工具类MessageUtil解析微信发来的xml格式的消息，解析的结果放在HashMap里；
		Map<String, String> requestMap = null;
		try {
			requestMap = MessageUtil.parseXml(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("微信消息解析出错", e);
			return message;
		}
		// 消息类型
		String msgType = requestMap.get("MsgType");
		/**
		 * 此处只用到接收事件接口，有其他接口再添加
		 */
		switch (msgType) {
		// 推送消息
		case MessageUtil.REQ_MESSAGE_TYPE_EVENT:
			message = dealSubscribe(requestMap);
			break;
		// 文本回复消息
		case MessageUtil.REQ_MESSAGE_TYPE_TEXT:
			// message = MessageResponse.dealTextNews(requestMap);
		default:
			break;
		}
		message = Tools.nullValueFilter(message);
		return message;

	}

	/**
	 * 2016-4-20 最新的事件推送
	 */
	public String dealSubscribe(Map<String, String> requestMap) {
		String event = requestMap.get("Event");
		System.out.println("最初event:" + event);
		// 发送方帐号（一个OpenID）
		String fromUserName = requestMap.get("FromUserName");
		String eventKey = requestMap.get("EventKey");
		System.out.println("最初eventKey:" + eventKey);
		String ticket = requestMap.get("Ticket");
		System.out.println("最初ticket:" + ticket);
		/**
		 * 2016-07-12 处理webuser相关信息
		 */
		buildTotalWebUser(fromUserName);

		if (event.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
			if (eventKey != null && ticket != null) {
				// 通过参数的二维码关注
				return scanParamQr(eventKey, requestMap);
			} else {
				// 普通扫描公众号关注
				return MessageResponse.scanCommonQr(requestMap);
			}

		} else if (event.equals(MessageUtil.EVENT_TYPE_SCAN)) {
			/**
			 * 用户已经关注，只是扫描参数二维码
			 */
			return scanParamQr(eventKey, requestMap);
		}
		return null;
	}

	/**
	 * 2016-4-20，此处只是扫描带参数二维码，已订阅用户
	 * 
	 * @he 2016-07-12 扫描带参数二维码
	 * @return
	 */
	public String scanParamQr(String eventKey, Map<String, String> requestMap) {
		/**
		 * 此处的eventKey均带有qrscene_，过滤
		 */
		if (eventKey.contains("qrscene")) {
			// 临时
			eventKey = eventKey.replace("qrscene_", "");
		}
		// 返回图文消息的总入口
		dealSystemQrWx(eventKey, requestMap);
		return MessageResponse.singleMessage(requestMap);

	}

	/**
	 * @he 2016-07-12 当服务器接受推送消息后，先创建于webuser有关信息
	 * @param openId
	 */
	@Override
	public WebchatUser buildTotalWebUser(String openId) {
		WebchatUser webchatUser = searchWebChatUserByOpenid(openId);
		if (webchatUser == null) {
			// 之后优化
			webchatUser = WxBusinessCommon.buildWebchatUser(openId);
			addWebChatUser(webchatUser);
		}
		/**
		 * 2016-05-23 获取用户的关注后的基本信息
		 */
		subscribeUpdateWxInfo(webchatUser);
		return webchatUser;
	}

	/**
	 * 微信处理webchatUser头像及相关信息
	 * 
	 * @param webchatUser
	 * @he 2016-07-12 统一更新微信的基本信息
	 */
	@Override
	public void subscribeUpdateWxInfo(WebchatUser webchatUser) {
		if (webchatUser == null) {
			return;
		}
		if (webchatUser.getId() == 0) {
			return;
		}
		pool.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean flag = WxBusinessCommon.getWxUserInfo(webchatUser);
				if (flag) {
					try {
						updateWebChatUser(webchatUser);
						User user = webchatUser.getUser();
						if (user != null) {
							user.setAvatar(webchatUser.getPhoto());
							userDao.update(user, "user.updateUserAvatar");
							UserExpand expand = user.getUserExpand();
							String sex = OptionalUtils
									.traceValue(expand, "sex");
							if (WxBusinessCommon.judyString(sex)) {
								expand.setSex(webchatUser.getSex());
								userExpandMapper.updateSexExpand(expand);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

	}

	/**
	 * 2016-12-19 获取并保存游客的信息
	 */
	@Override
	public void visitorWxInfo(WxAccseeTokenByCode accseeTokenByCode) {
		// TODO Auto-generated method stub
		pool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				/**
				 * 1.通过access_token 获取用户信息
				 */
				WebchatUser webchatUser = searchWebChatUserByOpenid(accseeTokenByCode
						.getOpenid());
				if(webchatUser==null||webchatUser.getId()==0){
					webchatUser=WxBusinessCommon.buildWebchatUser(accseeTokenByCode.getOpenid());
				}
				WxBusinessCommon.visitorWxInfoBuild(webchatUser,
						accseeTokenByCode);
				if (webchatUser.getId() == 0) {
					// 添加
					addWebChatUser(webchatUser);
				} else {
					//更新
					updateWebChatUser(webchatUser);
				}
			}

		});
	}

	/**
	 * 2016-3-11,eventKey均为数字 通过sceneid 找到 QrScene
	 */
	public void dealSystemQrWx(String eventKey, Map<String, String> requestMap) {
		long sceneid = Long.parseLong(eventKey);
		QrCode qrCode = qrCodeMapper.selectQrCodeByScenId(sceneid);
		// 更新qrCode的扫描次数
		if (qrCode != null) {
			qrCodeMapper.updateQrSaoCount(qrCode);
			QrScene qrScene = qrCodeMapper.selectQrSceneByScenId(sceneid);
			if (qrScene != null) {
				// 插入扫码记录
				String fromUserName = requestMap.get("FromUserName");
				// 记录扫描记录
				senceRecord(qrScene, fromUserName);
				String message = qrScene.getMessage();
				if (message == null || message.isEmpty()) {
					// 4-20 之前生成推送的方式
					buildLatestMessage(requestMap, qrScene);
				} else {
					// 4-20 之后生成推送的新方式
					autoLatestNews(requestMap, qrScene);
				}
			}
		}
	}

	// 记录扫描记录
	public void senceRecord(QrScene qrScene, String openId) {
		WebchatUser webchatUser = searchWebChatUserByOpenid(openId);
		if (webchatUser != null) {
			User user = webchatUser.getUser();
			if (user != null) {
				QrcodeScanRecode qrcodeScanRecode = qrCodeMapper
						.selectRecodeByUser(user.getId(), qrScene.getId());
				if (qrcodeScanRecode == null) {
					qrcodeScanRecode = new QrcodeScanRecode();
					qrcodeScanRecode.setUuid(Tools.getUUID());
					qrcodeScanRecode.setIsActive(BaseModel.ACTIVE);
					qrcodeScanRecode.setStatus(BaseModel.APPROVED);
					qrcodeScanRecode.setQrScene(qrScene);
					qrcodeScanRecode.setUser(user);
					qrcodeScanRecode.setScancount(1);
					qrcodeScanRecode.setViewcount(0);
					qrCodeMapper.addQrcodeScanRecode(qrcodeScanRecode);
				} else {
					qrcodeScanRecode.setScancount(qrcodeScanRecode
							.getScancount() + 1);
					qrCodeMapper.updateQrcodeScanRecode(qrcodeScanRecode);
				}
			}
		}
	}

	/**
	 * 2016-4-20 最新生成的推送消息
	 */
	public void autoLatestNews(Map<String, String> requestMap, QrScene qrScene) {
		QrMessage qrMessage = QrMessage.jsonToMessage(qrScene.getMessage());
		if (qrMessage != null) {
			String authUrl = qrMessage.getRedictUrl();
			int login = QrScene.isLoginByQrsence(qrScene.getType());
			if (qrMessage.getAuth() == QrMessage.NEED_AUTH) {
				authUrl = WxBusinessCommon.qrAuthCommonUrl(
						qrMessage.getRedictUrl(), login, qrScene.getSceneid());
			}
			requestMap.put(MessageResponse.TITLE, qrMessage.getTitle());
			requestMap.put(MessageResponse.DESCRIPTION, qrMessage.getDesc());
			requestMap.put(MessageResponse.PICURL, qrMessage.getCover());
			requestMap.put(MessageResponse.URL, authUrl);
		}

	}

	/**
	 * 
	 * @param requestMap
	 * @param list
	 */
	// public void yaoGame(Map<String, String> requestMap, List<Article> list) {
	// // 初次订阅
	// requestMap.put(TITLE, "摇一摇");
	// requestMap.put(DESCRIPTION, "摇一摇送知了云盒");
	// requestMap.put(PICURL, Config.host
	// + "/assets/img/webchat/game/yao_push.jpg");
	// String url = "/api/webchat/game/yao/one/page";
	// url = WebChatMenu.authCommonUrl(url);
	// requestMap.put(URL, url);
	// }

	/**
	 * 2016-4-20 只是兼容以前的旧二维码 ，以后会过期 判断是哪种二维码
	 * 
	 * @param requestMap
	 * @return
	 */
	public void buildLatestMessage(Map<String, String> requestMap,
			QrScene qrScene) {
		int type = qrScene.getType();
		switch (type) {
		case QrScene.ACTIVTY_QR_TYPE:
			Activity activity = activityMapper.getActivityById(qrScene
					.getReflectId());
			if (activity != null) {
				WxActivityItem item = new WxActivityItem();
				item.buildWonderItem(activity);
				requestMap.put(MessageResponse.TITLE, item.getName());
				requestMap.put(MessageResponse.DESCRIPTION, item.getMark());
				requestMap.put(MessageResponse.PICURL, item.getKv());
				String activityUrl = BaseWechatController.WX_PRIOR
						+ "/activity/total/page/" + activity.getUuid();
				requestMap.put(MessageResponse.URL,
						WebChatMenu.authCommonUrl(activityUrl));
			}
			break;
		case QrScene.ACTIVTY_APPLY_QR_TYPE:
			Activity activityoups = activityMapper.getActivityById(qrScene
					.getReflectId());
			if (activityoups != null) {
				WxActivityItem item = new WxActivityItem();
				item.buildWonderItem(activityoups);
				requestMap.put(MessageResponse.TITLE, item.getName() + "作品申请");
				requestMap.put(MessageResponse.DESCRIPTION, item.getMark());
				requestMap.put(MessageResponse.PICURL, item.getKv());
				String oupsUrl = BaseWechatController.WX_PRIOR
						+ "/activity/opus/page/" + activityoups.getUuid();
				requestMap.put(MessageResponse.URL,
						WebChatMenu.authCommonUrl(oupsUrl));
			}
			break;
		case QrScene.CAST_QR_TYPE:
			// 直播
			Broadcast broadcast = broadMapper.getCastById(qrScene
					.getReflectId());
			if (broadcast != null) {
				WxBroadcastItem liveVo = new WxBroadcastItem();
				liveVo.buildWxSendMessage(broadcast);
				requestMap.put(MessageResponse.TITLE, liveVo.getName());
				requestMap.put(MessageResponse.DESCRIPTION, liveVo.getDesc());
				requestMap.put(MessageResponse.PICURL, liveVo.getBanner());
				String castUrl = BaseWechatController.WX_PRIOR
						+ "/broadcast/first/page/" + liveVo.getUuid();
				requestMap.put(MessageResponse.URL,
						WebChatMenu.authCommonUrl(castUrl));
			}

			break;
		case QrScene.URL_TYPE:

			break;
		case QrScene.ACTIVTY_APPLY_QR_COMMON:
			requestMap.put(MessageResponse.TITLE, "普通作品申请");
			requestMap.put(MessageResponse.DESCRIPTION, "普通作品申请上传通道");
			requestMap.put(MessageResponse.PICURL, Config.host
					+ "/assets/img/app/up_normal.jpg");
			String oupsUrlCommon = BaseWechatController.WX_PRIOR
					+ "/activity/common/opus/page";
			requestMap.put(MessageResponse.URL,
					WebChatMenu.authCommonUrl(oupsUrlCommon));
			break;

		default:
			break;
		}
	}

	@Override
	public WebchatUser searchWebChatUserByOpenid(String openId) {
		// TODO Auto-generated method stub
		return webChatDao.getById(openId,
				getSqlAlias("searchWebChatUserByOpenid"));
	}

	@Override
	public Status addWebChatUser(WebchatUser webchatUser) {
		// TODO Auto-generated method stub
		try {
			webChatDao.add(webchatUser, getSqlAlias("addWebChatUser"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("保存openid失败", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public Status updateWebChatUserStatus(WebchatUser webchatUser) {
		// TODO Auto-generated method stub
		try {
			webChatDao.update(webchatUser,
					getSqlAlias("updateWebChatUserStatus"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("更新openid状态失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public WebchatUser searchWebChatUserByUserId(int userid) {
		// TODO Auto-generated method stub
		return webChatDao.getById(userid,
				getSqlAlias("searchWebChatUserByUserId"));
	}

	@Override
	public Status updateWebChatUser(WebchatUser webchatUser) {
		// TODO Auto-generated method stub
		try {
			webChatDao.update(webchatUser, getSqlAlias("updateWebChatUser"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("updateWebChatUser error", e);
		}
		return Status.FAILURE;
	}

	public static void main(String[] args) {
		String time1 = "2015-12-22 00:00:00";
		String time2 = "2016-01-01 00:00:00";
		// 判断是之前关注，还是最新关注
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = Tools.df1.parse(time1);
			date2 = Tools.df1.parse(time2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date date = new Date();
		System.out.println(date.after(date1) && date.before(date2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.WebchatFacade#userUnbindWechat(com.lankr.tv_cloud
	 * .model.User)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月9日
	 * @modifyDate 2016年5月9日 @ he 2016-06-17 修改数据逻辑
	 *             再失效上一条数据的同时，新增一条数据(保存一条新的webuser，用作下次登录使用)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.WebchatFacade#unbindWechat(java.lang.String)
	 */
	@Override
	public ActionMessage unbindWechat(String openId) {
		String sql = "update webchat_user set modifyDate=NOW(),status=0,isActive=0 where isActive=1 and openId=?";

		int effect = jdbcTemplate.update(sql, new Object[] { openId });
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return codeProvider.code(-2002).getActionMessage();

	}

	@Override
	public WebchatUser searchWebChatUserByUnionid(String unionid) {
		// TODO Auto-generated method stub
		return webChatDao.getById(unionid,
				getSqlAlias("searchWebChatUserByUnionid"));
	}

	@Override
	public int searchCountByOpenid(String openId) {
		// TODO Auto-generated method stub
		return webChatDao.getCountById(openId,
				getSqlAlias("searchWebChatUserCountByOpenid"));
	}

	@Override
	public Pagination<WebchatUser> searchNoRegWechatUsers(String query, int from, int rows,
			int projectId) {
		query = filterSQLSpecialChars(query);
		String sql = "select count(id) from webchat_user where userId is null and openId like '%"
						+ query + "%'";
		Pagination<WebchatUser> pu = initPage(sql, from, rows);
		SubParams param = new SubParams();
		param.setQuery(query);
		List<WebchatUser> wechatUsers = webChatDao.searchAllPagination(
				getSqlAlias("searchNoRegWechatUsers"), param, from, rows);
		pu.setResults(wechatUsers);
		return pu;
	}

	@Override
	public WebchatUser searchWebChatUserByUuid(String uuid) {
		return webChatDao.getById(uuid,
				getSqlAlias("searchWebChatUserByUuid"));
	}
}
