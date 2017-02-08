package com.lankr.tv_cloud.web.api.webchat;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.po.AwardResult;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.LotteryRecord;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.tmp.ShakeRule.Shaker;
import com.lankr.tv_cloud.tmp.YuanxiaoActive;
import com.lankr.tv_cloud.tmp.YuanxiaoRecord;
import com.lankr.tv_cloud.vo.LotteryRecordVo;
import com.lankr.tv_cloud.vo.datatable.LotteryRecordData;
import com.lankr.tv_cloud.vo.datatable.LotteryRecordDataItem;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.api.webchat.vo.YuanxiaoRecordVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class GamesControl extends BaseWechatController {

	/**
	 * 元宵节首页面页面
	 */
	@RequestAuthority(requiredProject = true, logger = true,wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/yuan/xiao/one/page", method = RequestMethod.GET)
	public ModelAndView yuanxiaoOnePage(HttpServletRequest request) {
		User user = getCurrentUser(request);
		int num = luckyTmpFacade.yuanxiaoTimesInit(user);
		request.setAttribute("num", num);
		bulidRequest("微信元宵节抽奖首页", "tmp_yuanxiao", null,
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/envelopes");

	}

	/**
	 * 元宵节次级页面（暂不使用）
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/yuan/xiao/chou/jiang", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String yuanxiaoChoujiang(HttpServletRequest request) {
		User user = getCurrentUser(request);
		YuanxiaoActive activity = luckyTmpFacade.yuanxiaoPlay(user);
		bulidRequest("微信元宵节抽奖", "tmp_yuanxiao", null, Status.SUCCESS.message(),
				null, "成功", request);
		return gson.toJson(activity);

	}

	/**
	 * 元宵抽奖记录（暂不使用）
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/yuan/xiao/record", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String yuanxiaoRecord(HttpServletRequest request) {
		User user = getCurrentUser(request);
		YuanxiaoRecordVo vo = new YuanxiaoRecordVo();
		List<YuanxiaoRecord> list = luckyTmpFacade.yuanxiaoUserRecord(user);
		if (list != null && list.size() > 0) {
			vo.setStatus(Status.SUCCESS);
			vo.setList(list);
		} else {
			vo.setStatus(Status.FAILURE);
		}
		bulidRequest("微信元宵节查看抽奖记录", "tmp_yuanxiao", null,
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();

	}

	/**
	 * 摇一摇的游戏活动-->old
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true,wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/game/yao/one/page", method = RequestMethod.GET)
	public ModelAndView gameYaoPage(HttpServletRequest request) {
		bulidRequest("微信游戏摇一摇页面", "tmp_yuanxiao", null,
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/disposable/shake");

	}

	/**
	 * 摇一摇抽奖结果 -->old
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/game/yao/action", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String gameYaoAction(HttpServletRequest request) {
		User user = getCurrentUser(request);
		Shaker shaker = luckyTmpFacade.shakePlay(user);
		bulidRequest("微信游戏摇一摇摇奖", "tmp_yuanxiao", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(shaker);

	}

	/**
	 * 摇一摇抽奖记录 -->old
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/game/yao/record", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String gameYaoRecord(HttpServletRequest request) {
		User user = getCurrentUser(request);
		YuanxiaoRecordVo vo = new YuanxiaoRecordVo();
		List<YuanxiaoRecord> list = luckyTmpFacade.gameYaoRecord(user);
		if (list != null && list.size() > 0) {
			vo.setStatus(Status.SUCCESS);
			vo.setList(list);
		} else {
			vo.setStatus(Status.FAILURE);
		}
		bulidRequest("微信游戏摇一摇抽奖记录", "tmp_yuanxiao", null,
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();

	}

	/*
	 * 旧的游戏页面跳转，共用一个公用页面
	 * 
	 * @RequestAuthority(requiredProject = true ,logger=true)
	 * 
	 * @RequestMapping(value = "/game/first/page/{uuid}") public ModelAndView
	 * reportDetail(HttpServletRequest request, @PathVariable String uuid) {
	 * Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid); GamesViewData
	 * viewData = GamesViewData.buildViewData(lottery);
	 * request.setAttribute("gameContext", viewData);
	 * BaseController.bulidRequest("微信游戏首页", "tmp_lottery",
	 * lottery==null?null:lottery.getId(), Status.SUCCESS.message(), null, "成功",
	 * request); return new ModelAndView("/wechat/games/gameCommonView"); }
	 */

	/**
	 * 根据不同模版id进入不同页面
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true,wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/game/first/page/{uuid}")
	public ModelAndView reportDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if (null == lottery || !lottery.isActive()) {
			return redirectErrorPage(request, "敬请期待!");
		}
		if (1 == lottery.getTemplateId()) {
			int tmp = this.gameInit(request, uuid);
			if (Lottery.TIMES_GAME_UNDERLINE == tmp) {
				return redirectErrorPage(request, "游戏未上线，敬请期待!");
			}
			bulidRequest("微信进入摇一摇游戏页面", null, null,
					Status.SUCCESS.message(), null, "成功", request);
			request.setAttribute("lottery", lottery);
			return redirectPageView("/wechat/games/template/1_yaoyiyao");
		} else if (2 == lottery.getTemplateId()) {
			bulidRequest("微信进入转盘游戏页面", null, null,
					Status.SUCCESS.message(), null, "成功", request);
			request.setAttribute("lottery", lottery);
			return redirectPageView("/wechat/games/template/2_zhuanpan");
		} else if (3 == lottery.getTemplateId()) {
			bulidRequest("微信进入刮刮卡游戏页面", null, null,
					Status.SUCCESS.message(), null, "成功", request);
			request.setAttribute("lottery", lottery);
			return redirectPageView("/wechat/games/template/3_guaguaka");
		}
		bulidRequest("进入微信游戏页面错误", null, null,
				Status.FAILURE.message(), null, "失败", request);
		return redirectErrorPage(request, "敬请期待!");
	}

	/**
	 * 根据游戏uuid获取游戏详情
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * @RequestAuthority(requiredProject = true, logger = true)
	 * 
	 * @RequestMapping(value = "/game/detail", produces =
	 * "text/json; charset=utf-8", method = RequestMethod.GET) public
	 * @ResponseBody String getLotteryDetailByUuid(HttpServletRequest request,
	 * @RequestParam String uuid) { Lottery lottery =
	 * gameMgrFacade.queryLotteryByUuid(uuid); if(null != lottery &&
	 * lottery.isActive()){ LotteryReq updateViewData =
	 * LotteryReq.buildUpdateViewData(lottery); return
	 * BaseAPIModel.makeWrappedSuccessDataJson(updateViewData); } return
	 * BaseController.failureModel("服务器繁忙，请稍后重试").toJSON(); }
	 */

	/**
	 * 查看抽奖记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/game/record", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String gameRecord(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if (null == lottery) {
			return failureModel("服务器繁忙,请刷新页面重新尝试").toJSON();
		}
		List<LotteryRecord> list = new ArrayList<LotteryRecord>();
		list = gameMgrFacade.queryRecordByUser(user, lottery);
		LotteryRecordData data = new LotteryRecordData();
		List<LotteryRecordDataItem> items = data.buildForWechat(list);
		LotteryRecordVo vo = new LotteryRecordVo();
		if (null != items && items.size() > 0) {
			vo.setStatus(Status.SUCCESS.message());
			vo.setItems(items);
		} else {
			vo.setStatus(Status.FAILURE.message());
		}
		BaseController.bulidRequest("用户查看微信游戏抽奖记录", "tmp_lottery_record", null,
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();
	}

	public int gameInit(HttpServletRequest request, @RequestParam String uuid) {
		User user = getCurrentUser(request);
		return gameMgrFacade.lotteryInit(uuid, user);
	}

	/**
	 * 开始游戏
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/game/play", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String gamePlay(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		AwardResult result = gameMgrFacade.play(uuid, user);
		BaseController.bulidRequest("微信游戏摇一摇摇奖", null, null,
				Status.SUCCESS.message(), null, "成功,用户	'" + user.getUsername()
						+ "'	参加了摇一摇游戏", request);
		return gson.toJson(result);
	}
}
