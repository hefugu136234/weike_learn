package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.web.api.webchat.util.TempleKeyWord;

public interface WxModelMessageFacade {
	/**
	 * 微信模板消息的接口
	 */
	// 直播报名 对应模板-直播报名成功通知
	public Status broadCastBook(Broadcast broadcast, User user);

	// 直播报名的审核 对应模板-审核提醒
	public Status broadCastBookCheck(BroadcastUser broadcastUser, User user);

	// 用户实名审核之后通知 对应模板-审核提醒
	public Status realNameNotice(Certification certification, User user);

	// 用户作品审核通过 对应模板-审核提醒
	public Status oupsCheck(ActivityApplication activityApplication, User user);

	// 注册成功提醒 对应模板-注册提醒
	public Status registerSuccess(User user);

	// 用户实名认证提交 对应模板-服务受理通知
	public Status realNameSubmit(Certification certification, User user);

	// 积分商城兑换商品 对应模板-积分兑换成功通知
	public Status exchageGoods(TempleKeyWord templeKeyWord, User user);

	// 兑换商品发货 对应模板-服务状态提醒
	public Status deliverGoods(String name, User user);

	// 用户作品提交 对应模板-用户投稿通知
	public Status oupsSubmit(ActivityApplication activityApplication,
			User user, String name);

	// 积分日报 对应模板- 积分变动通知
	public Status integralDaily(TempleKeyWord templeKeyWord, User user);

	// 申领vip发货 对应模板-服务状态提醒
	public Status vipDeliverGoods(User user);

	// 活动资源变动模板消息 对应模板-活动变更通知
	public Status onActivityResourceChanged(Activity activity, User user,
			TempleKeyWord templeKeyWord);

	// 直播即将开始消息 对应模板-服务状态提醒
	public Status beforeHourBroadcast(Broadcast cast, User user);
}
