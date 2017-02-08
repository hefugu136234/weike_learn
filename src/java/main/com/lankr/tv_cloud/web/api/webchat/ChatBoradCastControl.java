package com.lankr.tv_cloud.web.api.webchat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.vo.BroadcastBookVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxBroadcastItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxBroadcastList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.api.webchat.vo.WxWonderCastVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class ChatBoradCastControl extends BaseWechatController {
	
//	/**
//	 * @he 2016-06-28 以后 优化
//	 * 直播页面第一级页面
//	 * 这个接口以后数据要改版是再更改 （2016-07-13）
//	 */
//	@RequestAuthority(requiredProject = false, logger = true,wxShareType = WxSignature.WX_LIVE_SHARE)
//	@RequestMapping(value = "/broadcast/first/page/{uuid}", method = RequestMethod.GET)
//	public ModelAndView castFirstPage(HttpServletRequest request,
//			@PathVariable String uuid,
//			@RequestParam(required = false) String originWxUrl) {
//		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
//		if (broadcast == null || !broadcast.apiUseable()) {
//			return redirectErrorPage(request, "本直播不存在或已下线");
//		}
//		LiveVo wx = new LiveVo();
//		IntegralConsume consume=integralFacade.searchBroadcastIntegralConsume(broadcast);
//		int existNUm = broadcastFacade.broadcastBookCount(broadcast);
//		wx.buildFirstDetail(broadcast,existNUm,consume);
//		request.setAttribute("broadcast_data", wx);
//		bulidRequest("微信进入直播一级页面", "broadcast",
//				broadcast.getId(), Status.SUCCESS.message(), null, "成功",
//				request);
//		// 记录微信来源
//		bulidRequest(originWxUrl, request);
//		return redirectPageView("wechat/live/live_first");
//	}
	
	/**
	 * 2016-12-07 已优化
	 */
	@RequestAuthority(requiredProject = false, logger = true,wxShareType = WxSignature.WX_LIVE_SHARE)
	@RequestMapping(value = "/broadcast/first/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView castFirstPage(HttpServletRequest request,
			@PathVariable String uuid,
			@RequestParam(required = false) String originWxUrl) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			return redirectErrorPage(request, "本直播不存在或已下线");
		}
		WxBroadcastItem wx = new WxBroadcastItem();
		IntegralConsume consume=integralFacade.searchBroadcastIntegralConsume(broadcast);
		int existNUm = broadcastFacade.broadcastBookCount(broadcast);
		wx.buildFrist(broadcast, existNUm, consume);
		request.setAttribute("vo_data", wx);
		bulidRequest("微信进入直播一级页面", "broadcast",
				broadcast.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		// 记录微信来源
		bulidRequest(originWxUrl, request);
		return redirectPageView("wechat/live/live_first");
	}

	/**
	 * @he 2016-06-28 以后 优化
	 * 直播输入pincode，对比，是否可以进入直播
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/broadcast/check/pincode", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String checkPincode(HttpServletRequest request,
			@RequestParam String pincode, @RequestParam String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			request.setAttribute("error", "本直播不存在或已下线");
			BaseController.bulidRequest("微信直播验证pincode", "broadcast", null,
					Status.SUCCESS.message(), null, "直播uuid=" + uuid
							+ " 不存在或已下线", request);
			return failureModel("本直播不存在或已下线").toJSON();
		}
		if (pincode == null || pincode.isEmpty()) {
			BaseController.bulidRequest("微信直播验证pincode", "broadcast",
					broadcast.getId(), Status.FAILURE.message(), null,
					"失败，未输入pincode", request);
			return failureModel("请输入pincode").toJSON();
		}
		String pinCode = Tools.nullValueFilter(broadcast.getPincode());
		if (!pinCode.equals(pincode)) {
			BaseController.bulidRequest("微信直播验证pincode", "broadcast",
					broadcast.getId(), Status.FAILURE.message(), null,
					"失败，输入pincode=" + pincode + " 不正确", request);
			return failureModel("pincode不正确").toJSON();
		}
		BaseController.bulidRequest("微信直播验证pincode", "broadcast",
				broadcast.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		return successModel().toJSON();
	}

//	/**
//	 * @he 2016-06-28 以后 优化
//	 * 直播详情第二级页面
//	 * 由于有分享的存在，要改的话直播首页和详情页都得改
//	 */
//	@RequestAuthority(requiredProject = true, logger = true,wxShareType = WxSignature.WX_LIVE_SHARE)
//	@RequestMapping(value = "/broadcast/detail/page/{uuid}", method = RequestMethod.GET)
//	public ModelAndView detailPage(HttpServletRequest request,
//			@PathVariable String uuid) {
//		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
//		if (broadcast == null || !broadcast.apiUseable()) {
//			return redirectErrorPage(request, "本直播不存在或已下线");
//		}
//		LiveVo wx = new LiveVo();
//		wx.buildDetail(broadcast);
//		User user = getCurrentUser(request);
//		BroadcastPaltUrl.platCastEntrance(user, broadcast, wx);
//		// 处理action与参数隔开
//		wx.buildRequestLisveParam(wx);
//		request.setAttribute("broadcast_data", wx);
//		bulidRequest("微信查看直播详情", "broadcast", broadcast.getId(),
//				Status.SUCCESS.message(), null, "成功", request);
//		return redirectPageView("wechat/live");
//	}
//	
	/**
	 * 2016-12-07 已优化
	 */
	@RequestAuthority(requiredProject = true, logger = true,wxShareType = WxSignature.WX_LIVE_SHARE)
	@RequestMapping(value = "/broadcast/detail/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView detailPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			return redirectErrorPage(request, "本直播不存在或已下线");
		}
		User user = getCurrentUser(request);
		WxBroadcastItem vo = new WxBroadcastItem();
		vo.buildDetail(broadcast,user);
		request.setAttribute("vo_data", vo);
		bulidRequest("微信查看直播详情", "broadcast", broadcast.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/live/live_detail");
	}
	
	
	@RequestAuthority(requiredProject = true, logger = true,wxShareType = WxSignature.WX_LIVE_SHARE)
	@RequestMapping(value = "/broadcast/baidu/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView baiduPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			return redirectErrorPage(request, "本直播不存在或已下线");
		}
		WxBroadcastItem vo = new WxBroadcastItem();
		vo.buildBaiduLive(broadcast);
		request.setAttribute("vo_data", vo);
		bulidRequest("微信观看百度直播直播", "broadcast", broadcast.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/live/live_baidu");
	}




	// 点击直播跳转到第三方 @he 2016-06-28 以后 优化
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/broadcast/redirect/thrid/record", method = RequestMethod.POST)
	@ResponseBody
	public String shareRes(HttpServletRequest request, @RequestParam String uuid,@RequestParam String url) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null) {
			return failureModel().toJSON();
		}
		if(!url.contains(BaseWechatController.WX_PRIOR)){
			bulidRequest("微信点击直播跳转到底三方平台", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
		}
		return Status.SUCCESS.message();
	}


	/**
	 * @he 2016-06-28 以后 优化
	 * 直播之前判断是否可以跳转2级页面 1.直播的时间，报名的时间 2.判断直播状态是否开放 3，直播人员报名
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/cast/book/user", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String castBookUser(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("not login").toJSON();
		}
		BroadcastBookVo vo = new BroadcastBookVo();
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast==null||!broadcast.apiUseable()) {
			vo.buildError("直播信息不存在或已下线");
			return vo.toJSON();
		}
		Date date = new Date();
		// 判断是否超出直播的时间
		if (date.after(broadcast.getEndDate())) {
			// 当前时间超出直播时间
			vo.buildRecord(broadcast);
			if(vo.getHasRes().equals("hasRes")){
				bulidRequest("微信直播跳转到录播", "resource",
						null, Status.SUCCESS.message(),
						broadcast.getName(), "成功", request);
			}
			return vo.toJSON();
		}
		// 是否报名
		BroadcastUser broadcastUser = broadcastFacade
				.searchBroadcastUserByUserId(user, broadcast);
		if (broadcastUser == null) {
			// 未报名，需要报名
			/**
			 * 报名的限制条件 1.是否在报名时间内 2.是否在报名人数限定内 满足这2个条件才能报名
			 */
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date bookStartDate = broadcast.getBookStartDate();
			Date bookEndDate = broadcast.getEndDate();
			if (bookStartDate != null && date.before(bookStartDate)) {
				vo.buildError("直播报名还未开始，开始时间："
						+ dateFormat.format(bookStartDate));
				return vo.toJSON();
			}

			if (bookEndDate != null && date.after(bookEndDate)) {
				vo.buildError("直播报名已经结束，结束时间：" + dateFormat.format(bookEndDate));
				return vo.toJSON();
			}

			// 报名人数上限
			int numLimit = broadcast.getLimitNum();
			if (numLimit > 0) {
				// 数据库已报名的人数，包括审核和未审核的
				int existNUm = broadcastFacade.broadcastBookCount(broadcast);
				if (existNUm >= numLimit) {
					vo.buildError("直播报名人数已达上限");
					return vo.toJSON();
				}
			}

			// 开始报名
			broadcastUser = new BroadcastUser();
			broadcastUser.setUuid(Tools.getUUID());
			broadcastUser.setUser(user);
			broadcastUser.setBroadcast(broadcast);
			broadcastUser.setIsActive(BaseModel.ACTIVE);
			broadcastUser.setStatus(BaseModel.UNAPPROVED);
			Status status = broadcastFacade.addBroadcastUser(broadcastUser);
			if (status == Status.FAILURE) {
				return failureModel("直播报名失败").toJSON();
			}
			// 此处接直播报名的模板消息
			getModelMessageFacade().broadCastBook(broadcast, user);
			bulidRequest("微信直播报名", "broadcast_user",
					broadcastUser.getId(), Status.SUCCESS.message(),
					broadcast.getName(), "直播uuid=" + uuid, request);
		}
		/**
		 * 判断直播类型，除了审核类型，其他均为开放
		 */
		int castType = broadcast.getCastType();
		if (Broadcast.CHECK_TYPE == castType) {
			int numLimit = broadcast.getLimitNum();
			int existNUm = broadcastFacade.broadcastBookCount(broadcast);
			// 需要审核
			if (broadcastUser.getStatus() == BaseModel.UNAPPROVED) {
				vo.buildFaile(numLimit, existNUm, "您的直播报名还在审核中，请耐心等候");
				return vo.toJSON();
			}
			if (broadcastUser.getStatus() == BaseModel.UNDERLINE) {
				vo.buildFaile(numLimit, existNUm, "您的直播报名申请审核不通过 ,请留意微信消息信息");
				return vo.toJSON();
			}
		}
		vo.buildSuccess();
		return vo.toJSON();
	}
	
	/**
	 * 精彩直播
	 * @he 2016-06-28 修改
	 */
	@RequestAuthority(requiredProject = false, logger = true,wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/broadcast/wonder/page", method = RequestMethod.GET)
	public ModelAndView broadcastWonder(HttpServletRequest request) {
		bulidRequest("微信精彩直播列表", null, null,
				Status.SUCCESS.message(), null, "成功", request);
		WxWonderCastVo vo = new WxWonderCastVo();
		List<Banner> banners = bannerFacade.getWxBanner(Banner.TYPE_WECHAT,Banner.POSITION_BORADCAST);
		vo.buildBanner(banners);
		
		//获取即将开始直播（报名开始和报名结束之间）
		List<Broadcast> liveComing=broadcastFacade.wxLiveComing();
		vo.buildLivComing(liveComing, broadcastFacade);
		//获取正在直播（开始直播和结束直播之间）
		List<Broadcast> living=broadcastFacade.wxLiving();
		vo.buildLiving(living, broadcastFacade);
		//获取已结束直播（数量）
		int livedCount=broadcastFacade.WxLivedCount();
		vo.setLivedCount(livedCount);
		request.setAttribute("vo_data", vo);
		return redirectPageView("wechat/live/broadcast_wonder");
	}
	
	/**
	 * 最新直播列表-直播已结束且有录播的视频
	 * 2016-04-22
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/broadcast/has/record", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String castHasRecord(HttpServletRequest request,
			@RequestParam String startTime, @RequestParam int size) {
		if (size > 20) {
			size = 20;
		}
		List<Broadcast> list=broadcastFacade.searchCastRecordPage(startTime, size);
		WxBroadcastList vo = new WxBroadcastList();
		vo.buildData(list,broadcastFacade);
		return vo.toJSON();
	}
	
	/**
	 * 获取直播页的轮播图
	 * 2016-04-22
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/broadcast/carousel/baner", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String carouselBanner(HttpServletRequest request, @RequestParam int size) {
		if (size > 30) {
			size = 30;
		}
		List<Broadcast> list=broadcastFacade.searchCastCarousel(size);
		WxBroadcastList vo = new WxBroadcastList();
		vo.buildCarousel(list, broadcastFacade);
		return vo.toJSON();
	}

	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
//		ChatBoradCastControl castControl = new ChatBoradCastControl();
//		String ssString = "http://live.vhdong.com/Home/LoginAPI?cid=19&url=ych&username=18601692529&mobile=18601692529&regist=true";
//		LiveVo wx = new LiveVo();
//		wx.setRequestUrl(ssString);
//		castControl.buildRequestLisveParam(wx);
		long time = TimeUnit.DAYS.toMillis(29);
		time += System.currentTimeMillis();
		Date date = new Date();
		date.setTime(time);
		System.out.println(Tools.formatYMDHMSDate(date));
	}
}
