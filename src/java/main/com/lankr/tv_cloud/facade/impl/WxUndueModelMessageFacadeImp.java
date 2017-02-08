package com.lankr.tv_cloud.facade.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lankr.tv_cloud.TemplateMessgaeId;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.TempleMessageRecord;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.api.webchat.util.ModelKeyNote;
import com.lankr.tv_cloud.web.api.webchat.util.ModelMessage;
import com.lankr.tv_cloud.web.api.webchat.util.TempleKeyWord;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;

public class WxUndueModelMessageFacadeImp extends WxModelMessageFacadeImp {
	/**
	 * @ he 2016-06-22 正式服务器 过度时使用一个月的模板消息发送的数据
	 */
	// 对应模板 报名状态通知
	// 直播报名
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
				"您好，您报名的直播《" + castName + "》已通过");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"报名成功");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"直播时间 " + broadTime);
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

	// 对应模板 审核提醒
	// 直播报名的审核
	@Override
	public Status broadCastBookCheck(BroadcastUser broadcastUser, User user) {
		// TODO Auto-generated method stub
		return super.broadCastBookCheck(broadcastUser, user);
	}

	// 对应模板-审核提醒
	// 用户实名审核之后通知
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

		String name = OptionalUtils.traceValue(user, "nickname");

		Map<String, ModelKeyNote> data = new HashMap<String, ModelKeyNote>();

		ModelKeyNote first, keyword3, remark;
		first = keyword3 = remark = null;
		if (certification.getStatus() == Certification.HAS_VERIFY) {
			first = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "您的实名认证审核已通过");
			keyword3 = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "通过");
			remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "点击此处继续访问知了");

			String center = BaseWechatController.WX_PRIOR + "/my/center";
			// 记录消息来源
			center = center + "?originWxUrl=" + BaseController.WX_TEMPLE;
			center = WebChatMenu.authCommonUrl(center);
			mesageMessage.setUrl(center);
		} else {
			first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
					Tools.nullValueFilter(certification.getMark()));
			keyword3 = buildModelKeyNote(ModelKeyNote.BULE_COLOR, "未通过");
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
		data.put("keyword1", buildModelKeyNote(ModelKeyNote.BULE_COLOR, name));
		data.put("keyword2", keyword2);
		data.put("keyword3", keyword3);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.realNameNotice);
	}

	// 对应模板-审核提醒
	// 用户作品审核通过
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

		String name = OptionalUtils.traceValue(user, "nickname");

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
		ModelKeyNote keyword3 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"审核通过");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				Tools.df1.format(new Date()));
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"点击此处，查看您的作品列表");

		data.put("first", first);
		data.put("keyword1", buildModelKeyNote(ModelKeyNote.BULE_COLOR, name));
		data.put("keyword2", keyword2);
		data.put("keyword3", keyword3);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.oupsCheck);
	}

	// 对应模板 新用户注册提醒
	// 注册成功提醒
	// @Override
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
				OptionalUtils.traceValue(user, "username"));
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				OptionalUtils.traceValue(user, "nickname"));
		ModelKeyNote keyword3 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"微信注册");
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

	// 对应模板 服务状态提醒
	// 用户实名认证提交
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
				"未审核");
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

	// // 对应模板 积分兑换成功通知
	// 积分商城兑换商品
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
				templeKeyWord.getKeyword2());
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"获得“" + templeKeyWord.getKeyword1() + "”");
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处查看您已兑换的商品列表。");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.exchageGoods);
	}

	// 对应模板 服务状态提醒
	// 兑换商品发货
	@Override
	public Status deliverGoods(String name, User user) {
		// TODO Auto-generated method stub
		return super.deliverGoods(name, user);
	}

	// 对应模板 服务状态提醒
	// 用户作品提交
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

		ModelKeyNote first = buildModelKeyNote(
				ModelKeyNote.BULE_COLOR,
				"您好，已收到您的作品《"
						+ OptionalUtils.traceValue(activityApplication, "name")
						+ "》提交申请，请尽快上传或寄出作品文件。");
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"作品申请");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"未审核");
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

	// 对应模板 服务状态提醒
	// 积分日报
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

		String firstValue = templeKeyWord.getFirst() + "历史积分："
				+ templeKeyWord.getKeyword2() + "，当前积分："
				+ templeKeyWord.getKeyword1();

		ModelKeyNote first = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				firstValue);
		ModelKeyNote keyword1 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"积分周报");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"已通知");
		ModelKeyNote remark = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"请点击此处，查看积分详情。");

		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("remark", remark);
		mesageMessage.setData(data);
		return sendTempleMessage(mesageMessage, user,
				TempleMessageRecord.integralDaily);
	}

	// 对应模板 服务状态提醒
	// 申领vip发货
	@Override
	public Status vipDeliverGoods(User user) {
		// TODO Auto-generated method stub
		return super.vipDeliverGoods(user);
	}

	// 对应模板 服务状态提醒
	// 活动资源变动模板消息
	@Override
	public Status onActivityResourceChanged(Activity activity, User user,
			TempleKeyWord templeKeyWord) {
		// TODO Auto-generated method stub
		if (null == activity || null == user || null == templeKeyWord)
			return Status.FAILURE;
		String opneid = getOpenIdByUserId(user);
		if (opneid == null)
			return Status.FAILURE;

		ModelMessage mesageMessage = new ModelMessage();
		mesageMessage.setTouser(opneid);
		mesageMessage
				.setTemplate_id(TemplateMessgaeId.onActivityResourceChanged_id);

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
				"活动《" + OptionalUtils.traceValue(activity, "name") + "》变更通知");
		ModelKeyNote keyword2 = buildModelKeyNote(ModelKeyNote.BULE_COLOR,
				"已通知");
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

	// 对应模板 服务状态提醒
	// 直播即将开始消息
	@Override
	public Status beforeHourBroadcast(Broadcast cast, User user) {
		// TODO Auto-generated method stub
		return super.beforeHourBroadcast(cast, user);
	}

}
