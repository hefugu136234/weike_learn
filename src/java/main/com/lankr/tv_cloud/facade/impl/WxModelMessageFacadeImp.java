package com.lankr.tv_cloud.facade.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.lankr.tv_cloud.TemplateMessgaeId;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WxModelMessageFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.TempleMessageRecord;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.api.webchat.util.ModelKeyNote;
import com.lankr.tv_cloud.web.api.webchat.util.ModelMessage;
import com.lankr.tv_cloud.web.api.webchat.util.TempleKeyWord;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;

public class WxModelMessageFacadeImp extends FacadeBaseImpl implements
		WxModelMessageFacade {

	protected static Gson gson = new Gson();

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status broadCastBook(Broadcast broadcast, User user) {
		// TODO Auto-generated method stub
		if (broadcast == null)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String broadTime = dateFormat.format(broadcast.getStartDate());
		broadTime = broadTime + "~" + dateFormat.format(broadcast.getEndDate());
		String castName = broadcast.getName();

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.broadCastBook_id);

		// 直播页面
		String castUrl = BaseWechatController.WX_PRIOR
				+ "/broadcast/first/page/" + broadcast.getUuid();
		// 记录消息来源
		castUrl = castUrl + "?originWxUrl=" + BaseController.WX_TEMPLE;
		castUrl = WebChatMenu.authCommonUrl(castUrl);
		mesageMessage.setUrl(castUrl);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"您好，您已成功报名一个直播");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				castName);
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				broadTime);
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处查看直播详情");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.broadCastBook);
	}

	@Override
	public Status broadCastBookCheck(BroadcastUser broadcastUser, User user) {
		// TODO Auto-generated method stub
		if (broadcastUser == null)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		// 审核状态不发送
		if (broadcastUser.getStatus() == BaseModel.UNAPPROVED) {
			return Status.FAILURE;
		}

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.broadCastBookCheck_id);

		// 直播
		String center = BaseWechatController.WX_PRIOR
				+ "/broadcast/first/page/"
				+ OptionalUtils.traceValue(broadcastUser, "broadcast.uuid");
		// 记录消息来源
		center = center + "?originWxUrl=" + BaseController.WX_TEMPLE;
		center = WebChatMenu.authCommonUrl(center);
		mesageMessage.setUrl(center);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();
		String firstValue = "", keyword1Value = "";
		if (broadcastUser.getStatus() == BaseModel.APPROVED) {
			firstValue = "您的直播《"
					+ OptionalUtils.traceValue(broadcastUser, "broadcast.name")
					+ "》报名审核通过";
			keyword1Value = "通过";
		} else {
			firstValue = "您的直播《"
					+ OptionalUtils.traceValue(broadcastUser, "broadcast.name")
					+ "》报名审核未通过,原因：" + broadcastUser.getMark();
			keyword1Value = "未通过";
		}

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				firstValue);
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				keyword1Value);
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.df1.format(new Date()));
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"点击此处，查看直播");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.broadCastBookCheck);
	}

	@Override
	public Status realNameNotice(Certification certification, User user) {
		// TODO Auto-generated method stub
		if (certification == null)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		// 只有审核通过与未通过，发送，其他状态不发送
		if (certification.getStatus() != Certification.HAS_VERIFY
				&& certification.getStatus() != Certification.NO_PASS_VERIFY) {
			return Status.FAILURE;
		}

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.realNameNotice_id);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first, keyword1, remark;
		first = keyword1 = remark = null;
		if (certification.getStatus() == Certification.HAS_VERIFY) {
			first = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "您的实名认证审核已通过");
			keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "通过");
			remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "点击此处继续访问知了");

			String center = BaseWechatController.WX_PRIOR + "/my/center";
			// 记录消息来源
			center = center + "?originWxUrl=" + BaseController.WX_TEMPLE;
			center = WebChatMenu.authCommonUrl(center);
			mesageMessage.setUrl(center);
		} else {
			first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
					Tools.nullValueFilter(certification.getMark()));
			keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "未通过");
			remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
					"请注意查看实名要求，点击此处再次进行认证");

			String realname = BaseWechatController.WX_PRIOR
					+ "/activity/real/name/page";
			// 记录消息来源
			realname = realname + "?originWxUrl=" + BaseController.WX_TEMPLE;
			realname = WebChatMenu.authCommonUrl(realname);
			mesageMessage.setUrl(realname);
		}

		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.df1.format(new Date()));

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.realNameNotice);
	}

	@Override
	public Status oupsCheck(ActivityApplication activityApplication, User user) {
		// TODO Auto-generated method stub
		if (activityApplication == null)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		if (activityApplication.getStatus() != BaseModel.APPROVED) {
			return Status.FAILURE;
		}

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.oupsCheck_id);

		// 我的作品列表
		String center = BaseWechatController.WX_PRIOR
				+ "/activity/my/oups/page";
		// 记录消息来源
		center = center + "?originWxUrl=" + BaseController.WX_TEMPLE;
		center = WebChatMenu.authCommonUrl(center);
		mesageMessage.setUrl(center);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"恭喜，作品《" + activityApplication.getName() + "》已审核通过");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "通过");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.df1.format(new Date()));
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"点击此处，查看您的作品列表");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.oupsCheck);
	}

	@Override
	public Status registerSuccess(User user) {
		// TODO Auto-generated method stub
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;
		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.registerSuccess_id);

		// 首页
		String center = BaseWechatController.WX_PRIOR + "/index";
		// 记录消息来源
		center = center + "?originWxUrl=" + BaseController.WX_TEMPLE;
		center = WebChatMenu.authCommonUrl(center);
		mesageMessage.setUrl(center);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"欢迎，您已注册成功账号！");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.nullValueFilter(user.getNickname()));
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				user.getUsername());
		ModelKeyNote keyword3 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.df1.format(new Date()));
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处开始访问知了");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("keyword3", keyword3);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.registerSuccess);
	}

	@Override
	public Status realNameSubmit(Certification certification, User user) {
		// TODO Auto-generated method stub
		if (certification == null)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		// 只有未审核
		if (certification.getStatus() != Certification.NO_VERIFY) {
			return Status.FAILURE;
		}

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.realNameSubmit_id);

		// 实名认证页面
		String realname = BaseWechatController.WX_PRIOR
				+ "/activity/real/name/page";
		// 记录消息来源
		realname = realname + "?originWxUrl=" + BaseController.WX_TEMPLE;
		realname = WebChatMenu.authCommonUrl(realname);
		mesageMessage.setUrl(realname);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"您好，已收到您提交的实名认证申请，我们会尽快对资料进行审核，请留意微信消息通知。");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"实名认证");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.df1.format(new Date()));
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"点击此处可随时查看实名认证的进展");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.realNameSubmit);
	}

	@Override
	public Status exchageGoods(TempleKeyWord templeKeyWord, User user) {
		// TODO Auto-generated method stub
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.exchageGoods_id);

		// 跳转成已兑换列表
		String realname = BaseWechatController.WX_PRIOR
				+ "/wx/exchange/jifen/page";
		// 记录消息来源
		realname = realname + "?originWxUrl=" + BaseController.WX_TEMPLE;
		realname = WebChatMenu.authCommonUrl(realname);
		mesageMessage.setUrl(realname);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"您的积分商品兑换成功，我们会尽快安排发货。");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getKeyword1());
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getKeyword2());
		ModelKeyNote keyword3 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getKeyword3());
		ModelKeyNote keyword4 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"知了云盒在线商店");
		ModelKeyNote keyword5 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getKeyword5());
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处查看您已兑换的商品列表。");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("keyword3", keyword3);
		data.put("keyword4", keyword4);
		data.put("keyword5", keyword5);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.exchageGoods);
	}

	@Override
	public Status oupsSubmit(ActivityApplication activityApplication,
			User user, String name) {
		// TODO Auto-generated method stub
		if (activityApplication == null)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		if (activityApplication.getStatus() != BaseModel.UNAPPROVED) {
			return Status.FAILURE;
		}

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.oupsSubmit_id);

		// 我的作品列表
		String center = BaseWechatController.WX_PRIOR
				+ "/activity/my/oups/page";
		// 记录消息来源
		center = center + "?originWxUrl=" + BaseController.WX_TEMPLE;
		center = WebChatMenu.authCommonUrl(center);
		mesageMessage.setUrl(center);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"您好，已收到您的作品提交申请，请尽快上传或寄出作品。");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				activityApplication.getName());
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR, name);
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"点击此处查看您已提交的作品");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.oupsSubmit);
	}

	@Override
	public Status deliverGoods(String name, User user) {
		// TODO Auto-generated method stub
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.deliverGoods_id);

		// 积分已兑换列表
		String center = BaseWechatController.WX_PRIOR
				+ "/wx/exchange/jifen/page";
		// 记录消息来源
		center = center + "?originWxUrl=" + BaseController.WX_TEMPLE;
		center = WebChatMenu.authCommonUrl(center);
		mesageMessage.setUrl(center);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"积分兑换的商品已发货。");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"积分兑换" + name);
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"已发货");
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"点击次数查看已兑换的商品列表");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.deliverGoods);
	}

	// 申领的vip卡发货
	@Override
	public Status vipDeliverGoods(User user) {
		// TODO Auto-generated method stub
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.vipDeliverGoods_id);

		// 积分已兑换列表
		String vip = BaseWechatController.WX_PRIOR + "/active/page/code";
		// 记录消息来源
		vip = WebChatMenu.authCommonUrl(vip);
		mesageMessage.setUrl(vip);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"您申领的vip卡已发货。");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"申领vip卡");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"已发货");
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"点击查看我的vip详情");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.vipDeliverGoods);
	}

	@Override
	public Status integralDaily(TempleKeyWord templeKeyWord, User user) {
		// TODO Auto-generated method stub
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.integralDaily_id);

		// 跳转到我的积分
		String realname = BaseWechatController.WX_PRIOR + "/wx/my/jifen";
		// 记录消息来源
		realname = realname + "?originWxUrl=" + BaseController.WX_TEMPLE;
		realname = WebChatMenu.authCommonUrl(realname);
		mesageMessage.setUrl(realname);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getFirst());
		ModelKeyNote FieldName = buildModelKeyNote(null, "时间");
		ModelKeyNote Account = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.df1.format(new Date()));
		ModelKeyNote change = buildModelKeyNote(null, "历史");
		ModelKeyNote CreditChange = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getKeyword2());
		ModelKeyNote CreditTotal = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getKeyword1());
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处，查看积分详情。");

		data.put("FieldName", FieldName);
		data.put("Account", Account);
		data.put("first", first);
		data.put("change", change);
		data.put("CreditChange", CreditChange);
		data.put("CreditTotal", CreditTotal);
		data.put("Remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.integralDaily);
	}

	public String getOpenIdByUserId(User user) {
		if (user == null)
			return null;
		WebchatUser webchatUser = webChatDao.getById(user.getId(),
				"webChat.searchWebChatUserByUserId");
		if (webchatUser == null)
			return null;
		String opneid = webchatUser.getOpenId();
		if (opneid == null || opneid.isEmpty())
			return null;
		return opneid;

	}

	public ModelKeyNote buildModelKeyNote(String color, String value) {
		ModelKeyNote note = new ModelKeyNote(color, value);
		return note;
	}

	public Status sendTempleMessage(ModelMessage mesageMessage, User user,
			int interfaceType) {
		if (TemplateMessgaeId.message_swich) {
			try {
				String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
						+ WebChatMenu.getAccessToken(3);
				String json = gson.toJson(mesageMessage);
				String message = HttpUtils.sendPostRequest(url, json, true);
				System.out.println("发送模板消息：" + message);
				JSONObject demoJson = new JSONObject(message);
				int errcode = demoJson.getInt("errcode");
				TempleMessageRecord templeMessageRecord = new TempleMessageRecord();
				templeMessageRecord.setInterfaceType(interfaceType);
				templeMessageRecord.setIsActive(1);
				templeMessageRecord.setUserId(user.getId());
				templeMessageRecord.setMessage(message);
				templeMessageRecord.setUuid(Tools.getUUID());
				templeMessageRecord.setOpenId(mesageMessage.getTouser());
				if (errcode == 0) {
					// 发送成功
					templeMessageRecord.setStatus(1);
				} else {
					templeMessageRecord.setStatus(0);
				}
				templeMessageMapper.addTempleMessageRecord(templeMessageRecord);
				return Status.SUCCESS;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Status.FAILURE;
	}

	@Override
	public Status onActivityResourceChanged(Activity activity, User user,
			TempleKeyWord templeKeyWord) {
		if (null == activity || null == user || null == templeKeyWord)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.onActivityResourceChanged_id);

		// 活动页面
		String castUrl = BaseWechatController.WX_PRIOR
				+ "/activity/total/page/" + activity.getUuid();
		// 记录消息来源
		castUrl = castUrl + "?originWxUrl=" + BaseController.WX_TEMPLE;
		castUrl = WebChatMenu.authCommonUrl(castUrl);
		mesageMessage.setUrl(castUrl);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getFirst());
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				activity.getName());
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				templeKeyWord.getKeyword2());
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处查看活动详情");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.onActivityResourceChanged);
	}

	@Override
	public Status beforeHourBroadcast(Broadcast cast, User user) {
		if (null == cast || null == user)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage.setTemplate_id(TemplateMessgaeId.beforeHourBroadcast_id);

		// 直播页面
		String castUrl = BaseWechatController.WX_PRIOR+"/broadcast/first/page/" + cast.getUuid();
		// 记录消息来源
		castUrl = castUrl + "?originWxUrl=" + BaseController.WX_TEMPLE;
		castUrl = WebChatMenu.authCommonUrl(castUrl);
		mesageMessage.setUrl(castUrl);

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();
		
		String catsName=OptionalUtils.traceValue(cast, "name");

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"您报名的直播《"+catsName+"》，还有一个小时就要开始啦！");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				catsName);
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"一个小时之后开始");
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处查看直播详情");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.beforeHourBroadcast);
	}

}
