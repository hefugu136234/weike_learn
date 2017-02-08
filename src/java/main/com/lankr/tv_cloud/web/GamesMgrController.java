package com.lankr.tv_cloud.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.GameMgrFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Award;
import com.lankr.tv_cloud.model.AwardWebData;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.LotteryRecord;
import com.lankr.tv_cloud.model.LotteryReq;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.GamesViewData;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.datatable.GamesData;
import com.lankr.tv_cloud.vo.datatable.GamesDataItem;
import com.lankr.tv_cloud.vo.datatable.LotteryRecordData;
import com.lankr.tv_cloud.vo.datatable.LotteryRecordDataItem;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

/**
 * 游戏管理
 * @author mayuan
 */

@Controller
@RequestMapping(value = "/project/games")
public class GamesMgrController extends AdminWebController {

	private static final String GAMES_SUBMIT_KEY = "games_submit_key";
	
	@Autowired
	private GameMgrFacade gameMgrFacade;
	
	/**
	 * 游戏列表页面跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		return index("/wrapped/gamesMgr/gamesMgr_list.jsp");
	}

	/**
	 * 游戏新增页面跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/add/page")
	public ModelAndView renderActivityaddPage(Model model) {
		List<AwardWebData> result = new ArrayList<AwardWebData>();
		model.addAttribute("token",makeRepeatSubmitToken(GAMES_SUBMIT_KEY));
		model.addAttribute("awards", gson.toJson(result));
		return index("/wrapped/gamesMgr/gamesMgr_addOrUpdate.jsp");
	}
	
	/**
	 * 新增,更新 游戏数据保存
	 * 
	 * @param gameData
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/data/saveOrUpdate", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String gameSaveOrUpdate(HttpServletRequest request, 
												@RequestParam String gameData,@RequestParam String token) {
		/*if (Tools.isBlank(token) || !token.equals(toastRepeatSubmitToken(request, GAMES_SUBMIT_KEY))) {
			return failureModel("系统繁忙，请刷新页面重新尝试").toJSON();
		}*/
		LotteryReq lottery = LotteryReq.getJsonData(gameData);
		if (null == lottery) 
			return BaseAPIController.failureModel("奖品参数不正确，请重新填写").toJSON();
		
		//非空判断，格式化日期
		Lottery lotteryAfter = lottery.tidyData(lottery);
		
		if(gameMgrFacade.queryLotteryByUuid(lotteryAfter.getUuid()) != null){
			lotteryAfter.setPage(gameMgrFacade.queryLotteryByUuid(lotteryAfter.getUuid()).getPage());
		}
		lotteryAfter.setPinyin(Tools.getPinYin(lotteryAfter.getName()));
		
		//lotteryAfer 为前台传输的数据封装的对象
		ActionMessage action = gameMgrFacade.addOrUpdateLottery(lotteryAfter);
		if (!action.isSuccess()) {
			if(StringUtils.isEmpty(lotteryAfter.getUuid())){
				BaseController.bulidRequest("新建游戏", "tmp_lottery",
						null, Status.FAILURE.message(), null, "失败", request);
			}else{
				BaseController.bulidRequest("更新游戏", "tmp_lottery",
						lotteryAfter.getId(), Status.FAILURE.message(), null, "失败", request);
			}
			return BaseAPIController.failureModel(action.getMessage()).toJSON();
		} else {
			if(StringUtils.isEmpty(lotteryAfter.getUuid())){
				BaseController.bulidRequest("新建游戏", "tmp_lottery",
						null, Status.SUCCESS.message(), null, "成功", request);
			}else{
				BaseController.bulidRequest("更新游戏", "tmp_lottery",
						lotteryAfter.getId(), Status.SUCCESS.message(), null, "成功", request);
			}
			return BaseController.successModel().toJSON();
		}
	}
	
	/**
	 * 游戏列表数据加载
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR )
	@RequestMapping(value = "/list/data")
	public @ResponseBody String listData(HttpServletRequest request) {
		String query = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		
		Pagination<Lottery> results = gameMgrFacade.searchGameListForTable(query, startPage, pageSize);
		GamesData data = new GamesData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 游戏上下线操作
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if (null == lottery) {
			return failureModel("该游戏记录有误，请刷新页面重新尝试").toJSON();
		}
		if (BaseModel.UNDERLINE == lottery.getStatus() && ( null == gameMgrFacade.queryAwardListByLotteryId(lottery.getId()))) {
			return failureModel("请完善游戏配置信息").toJSON();
		}
		if (lottery.getStatus() == BaseModel.UNAPPROVED) {
			lottery.setStatus(BaseModel.APPROVED);
		} else {
			lottery.setStatus(lottery.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE : BaseModel.APPROVED);
		}
		//ActionMessage action = gameMgrFacade.updateLotteryStatus(lottery);
		lottery.setAwards(gameMgrFacade.queryAwardListByLotteryId(lottery.getId()));
		ActionMessage action = gameMgrFacade.addOrUpdateLottery(lottery);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台更新游戏状态", "tmp_lottery",
					lottery.getId(), Status.SUCCESS.message(), null, "成功", request);
			GamesDataItem item = GamesDataItem.build(lottery);
			return BaseAPIModel.makeWrappedSuccessDataJson(item);
		} else {
			BaseController.bulidRequest("后台更新游戏状态", "tmp_lottery", 
					lottery.getId(), Status.FAILURE.message(), null, "失败", request);
			return BaseController.failureModel(action.getMessage()).toJSON();
		}
	}

	/**
	 * 删除游戏
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/remove", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String removeLottery(HttpServletRequest request,
			@RequestParam String uuid) {
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if (null == lottery) {
			return failureModel("该游戏记录有误，请刷新页面重新尝试").toJSON();
		}
		if(lottery.getStatus() == BaseModel.APPROVED){
			return failureModel("请先下线该游戏").toJSON();
		}
		lottery.setIsActive(BaseModel.DISABLE);
		lottery.setAwards(gameMgrFacade.queryAwardListByLotteryId(lottery.getId()));
		ActionMessage action = gameMgrFacade.addOrUpdateLottery(lottery);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台删除游戏", "tmp_lottery",
					lottery.getId(), Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel().toJSON();
		} else {
			BaseController.bulidRequest("后台删除游戏", "tmp_lottery", 
					lottery.getId(), Status.FAILURE.message(), null, "失败", request);
			return BaseController.failureModel(action.getMessage()).toJSON();
		}
	}
	
	/**
	 * 展示页面设置跳转
	 * 
	 * @param model
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/pageConfig/{uuid}")
	public ModelAndView pageConfig(Model model, @PathVariable String uuid) {
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if(null != lottery){
			model.addAttribute("page", lottery.getPage());
		}
		model.addAttribute("uuid",uuid);
		return index("/wrapped/gamesMgr/gamesMgr_viewSet.jsp");
	}
	
	/**
	 * 保存展示页面
	 * 
	 * @param uuid
	 * @param page
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/pageSet", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String pageSet(HttpServletRequest request, @RequestParam String uuid,
															@RequestParam String page) {
		if(StringUtils.isEmpty(uuid) || StringUtils.isEmpty(page)){
			return BaseAPIController.failureModel("参数错误").toJSON();
		}
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if (null == lottery) {
			return failureModel("该游戏记录有误，请刷新页面重新尝试").toJSON();
		}
		lottery.setPage(page);
		lottery.setAwards(gameMgrFacade.queryAwardListByLotteryId(lottery.getId()));
		ActionMessage action = gameMgrFacade.addOrUpdateLottery(lottery);
		if (!action.isSuccess()) {
			BaseController.bulidRequest("保存游戏展示页面", "tmp_lottery",
					lottery.getId(), Status.FAILURE.message(), null, "失败", request);
			return BaseAPIController.failureModel(action.getMessage()).toJSON();
		} else {
			BaseController.bulidRequest("保存游戏展示页面", "tmp_lottery",
					lottery.getId(), Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel().toJSON();
		}
	}
	
	/**
	 * 游戏更新页面跳转
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR )
	@RequestMapping(value = "/update/page/{uuid}" )
	public ModelAndView updatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if (null == lottery) {
			request.setAttribute("error", "该记录有误，请刷新页面重新尝试");
			return index("/wrapped/gamesMgr/gamesMgr_list.jsp");
		}
		LotteryReq updateViewData = LotteryReq.buildUpdateViewData(lottery);
		List<Award> awards = gameMgrFacade.queryAwardListByLotteryId(lottery.getId());
		List<AwardWebData> result = new ArrayList<AwardWebData>();
		if(null != awards && awards.size() != 0){
			for (Award award : awards) {
				result.add(new AwardWebData().format(award));
			}
		}
		request.setAttribute("token",makeRepeatSubmitToken(GAMES_SUBMIT_KEY));
		request.setAttribute("lottery", updateViewData);
		request.setAttribute("awards", gson.toJson(result));
		return index(request, "/wrapped/gamesMgr/gamesMgr_addOrUpdate.jsp");
	}
	
	/**
	 * 删除奖品
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/removeAward", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String removeAward(HttpServletRequest request,
			@RequestParam String uuid) {
		Award award = gameMgrFacade.queryAwardByUuid(uuid);
		if (null == award) {
			return failureModel("删除失败，请刷新页面重新尝试").toJSON();
		}
		ActionMessage action = gameMgrFacade.removeAward(award);
		if (action.isSuccess()) {
			BaseController.bulidRequest("删除奖品", "tmp_award",
					award.getId(), Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel().toJSON();
		} else {
			BaseController.bulidRequest("删除奖品", "tmp_award", 
					award.getId(), Status.FAILURE.message(), null, "失败", request);
			return BaseController.failureModel(action.getMessage()).toJSON();
		}
	}
	
	/**
	 * 生成游戏入口链接
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR )
	@RequestMapping(value = "/viewUrl", method = RequestMethod.POST, produces = "text/json; charset=utf-8" )
	public @ResponseBody String getViewUrl(HttpServletRequest request, @RequestParam String uuid) {
		Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
		if (null == lottery) {
			return failureModel("该记录有误，请刷新页面重新尝试").toJSON();
		}
		/*if(StringUtils.isEmpty(lottery.getPage())){
			return failureModel("请配置游戏页面").toJSON();
		}*/
		GamesViewData viewData = GamesViewData.buildViewData(lottery);
		String gameUrlAddress = "/api/webchat/game/first/page/" + viewData.getUuid();
		viewData.setUrl(gameUrlAddress);
		return BaseAPIModel.makeWrappedSuccessDataJson(viewData);
	}
	
	/**
	 * 抽奖记录页面跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/record/page")
	public ModelAndView renderGamesRecordPage(Model model) {
		List<Lottery> lotterys = gameMgrFacade.searchGameList();
		if(null == lotterys){
			lotterys = new ArrayList<Lottery>();
		}
		model.addAttribute("lotterys", lotterys);
		return index("/wrapped/gamesMgr/gamesMgr_recordList.jsp");
	}
	
	/**
	 * 抽奖记录列表
	 * 
	 * @param model
	 * @param request
	 * @param gameId
	 * @param isWiner
	 * @param isHandle
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/record/data", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String gamesRecordData(Model model, HttpServletRequest request,
													@RequestParam(required = false) String gameId,
													@RequestParam(required = false) String isWiner,
													@RequestParam(required = false) String isHandle,
													@RequestParam(required = false) String exchangeCode) {
		String queryKey = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		
		LotteryRecordData data = new LotteryRecordData();
		try {
			Pagination<LotteryRecord> results = gameMgrFacade
					.getLotteryRecord(queryKey, startPage, pageSize, gameId, isWiner, isHandle, exchangeCode);
			data.build(results.getResults());
			data.setiTotalDisplayRecords(results.getTotal());
			data.setiTotalRecords(results.getPage_rows());
			BaseController.bulidRequest("后台查询游戏参与列表", "tmp_lottery_record", null,
					Status.SUCCESS.message(), null, "成功", request);
			return data.toJSON();
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseController.bulidRequest("后台查询游戏参与列表", "tmp_lottery_record", null,
				Status.FAILURE.message(), null, "失败", request);
		return data.toJSON();
	}
	
	/**
	 * 处理抽奖结果
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/record/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8" )
	public @ResponseBody String updateRecordStatus(HttpServletRequest request, @RequestParam String uuid ) {
		LotteryRecord record = gameMgrFacade.queryLotteryRecordByUuid(uuid);
		if (record == null || !record.isActive()) {
			BaseController.bulidRequest("后台处理抽奖结果", "tmp_lottery_record", null,
					Status.FAILURE.message(), null,
					"uuid=" + uuid + "根据uuid查询记录过程中出错", request);
			return BaseController.failureModel("处理失败，请刷新页面重新尝试").toJSON();
		}
		record.setStatus(record.getStatus() == BaseModel.TRUE ? BaseModel.FALSE
					: BaseModel.TRUE);
		ActionMessage message = gameMgrFacade.updateLotteryRecordStatus(record);
		if (message == ActionMessage.successStatus()) {
			BaseController.bulidRequest("后台处理抽奖结果", "tmp_lottery_record",
					record.getId(), Status.SUCCESS.message(), null, "成功，状态转换为="
							+ record.getStatus(), request);
			return BaseAPIModel.makeWrappedSuccessDataJson(LotteryRecordDataItem.build(record));
		} else {
			BaseController.bulidRequest("后台处理抽奖结果", "tmp_lottery_record",
					record.getId(), Status.FAILURE.message(), null, "失败，状态转换为="
							+ record.getStatus(), request);
			return BaseController.failureModel("处理失败，请刷新页面重新尝试").toJSON();
		}
	}

}
