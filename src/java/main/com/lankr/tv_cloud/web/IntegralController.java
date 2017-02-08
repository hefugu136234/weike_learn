package com.lankr.tv_cloud.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.LogisticsAddress;
import com.lankr.tv_cloud.model.LogisticsInfo;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.IntegralConsumeItemvo;
import com.lankr.tv_cloud.vo.IntegralConsumeSuface;
import com.lankr.tv_cloud.vo.LogisticsInfoVo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.datatable.IntegralConsumeDataItem;
import com.lankr.tv_cloud.vo.datatable.IntegralExchangeData;
import com.lankr.tv_cloud.vo.datatable.IntegralExchangeDataItem;
import com.lankr.tv_cloud.web.api.webchat.util.TempleKeyWord;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
public class IntegralController extends AdminWebController {

	/**
	 * 兑换记录页面跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/admin/exchange/list")
	public ModelAndView integralExchangeList(HttpServletRequest request,
			Model model) {
		BaseController.bulidRequest("后台查看兑换记录列表", "integral_record", null,
				Status.SUCCESS.message(), null, "成功", request);
		return index("/wrapped/integral/exchange_list.jsp");
	}

	/**
	 * 兑换列表数据查询
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/integral/exchange/datatable")
	public @ResponseBody String activityDataJson(Model model,
			HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<IntegralRecord> results = integralFacade
				.allIntegralConsumeRecords(q, from, size);
		IntegralExchangeData data = new IntegralExchangeData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 积分商品页面跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/integral/goods/list/page")
	public ModelAndView integralGoodsListPage(HttpServletRequest request) {
		BaseController.bulidRequest("后台积分商品列表", "integral_consume", null,
				Status.SUCCESS.message(), null, "成功", request);
		return index("/wrapped/integral/integral.jsp");
	}

	/**
	 * 积分商品列表数据查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/integral/goods/list/datatable")
	public @ResponseBody String goodsListData(HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<IntegralConsume> results = integralFacade
				.searchIntegralConsumeList(q, from, size);
		IntegralConsumeSuface suface = new IntegralConsumeSuface();
		suface.build(results.getResults());
		suface.setiTotalDisplayRecords(results.getTotal());
		suface.setiTotalRecords(results.getPage_rows());
		return suface.toJSON();
	}

	/**
	 * 积分商品状态更改
	 * 
	 * @param uuid
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/project/integral/goods/status/change", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String goodsUpdateStatus(@RequestParam String uuid,
			HttpServletRequest request) {
		IntegralConsume consume = integralFacade.getIntegralConsumeByUuid(uuid);
		if (consume.getStatus() == BaseModel.UNAPPROVED) {
			consume.setStatus(BaseModel.APPROVED);
		} else {
			consume.setStatus(consume.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
					: BaseModel.APPROVED);
		}
		Status status = integralFacade.updateIntegralConsumeStatus(consume);
		BaseController.bulidRequest("后台积分商品状态修改", "integral_consume",
				consume.getId(), status.message(), null, null, request);
		IntegralConsumeItemvo vo = new IntegralConsumeItemvo();
		vo.setStatus(status);
		vo.setIntegralConsume(consume);
		return vo.toJSON();
	}

	/**
	 * 积分商品添加页面跳转
	 * 
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/consume/new")
	public ModelAndView consumeNewPage(HttpServletRequest request) {
		return index("/wrapped/integral/add.jsp");
	}

	/**
	 * 积分商品保存操作
	 * 
	 * @param name
	 * @param price
	 * @param integral
	 * @param description
	 * @param cover
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/admin/consume/save", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String saveConsume(HttpServletRequest request,
			@RequestParam String name, @RequestParam float price,
			@RequestParam int integral, @RequestParam int number,
			@RequestParam(required = false) String description,
			@RequestParam String cover, @RequestParam String goodType,
			@RequestParam int userLimited, @RequestParam int sign) {
		if (goodType == null || goodType.isEmpty()) {
			return failureModel("请选择商品类型").toJSON();
		}
		userLimited = Math.max(Activity.INFINITE, userLimited);
		sign = (sign == BaseModel.TRUE ? BaseModel.TRUE : BaseModel.FALSE);
		IntegralConsume consume = new IntegralConsume();
		consume.setName(name);
		consume.setCover(cover);
		consume.setDescription(description);
		consume.setIntegral(integral);
		consume.setPrice(price);
		consume.setType(Integer.parseInt(goodType));
		consume.setNumber(number);
		consume.setUserLimited(userLimited);
		consume.setSign(sign);
		ActionMessage message = integralFacade.addIntegralConsume(consume);
		if (message.getStatus() == Status.SUCCESS) {
			BaseController.bulidRequest("后台新增积分商品", "integral_consume",
					consume.getId(), Status.SUCCESS.message(), null, null,
					request);
		}
		return actionModel(message).toJSON();
	}

	/**
	 * 积分商品页面修改
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/consume/update/page/{uuid}")
	public ModelAndView consumeUpdatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		IntegralConsume consume = integralFacade.getIntegralConsumeByUuid(uuid);
		IntegralConsumeDataItem item = IntegralConsumeDataItem
				.updateData(consume);
		request.setAttribute("data_update", item);
		return index("/wrapped/integral/update.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/admin/consume/update", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String updateConsume(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String name,
			@RequestParam float price, @RequestParam int integral,
			@RequestParam(required = false) String description,
			@RequestParam String cover, @RequestParam String goodType,
			@RequestParam int number, @RequestParam int sign, @RequestParam int userLimited) {
		if (goodType == null || goodType.isEmpty()) {
			return failureModel("请选择商品类型").toJSON();
		}
		IntegralConsume consume = integralFacade.getIntegralConsumeByUuid(uuid);
		if (consume == null) {
			return failureModel("商品信息不存在").toJSON();
		}
		consume.setName(name);
		consume.setPinyin(Tools.getPinYin(name));
		consume.setCover(cover);
		consume.setDescription(description);
		consume.setIntegral(integral);
		consume.setPrice(price);
		consume.setType(Integer.parseInt(goodType));
		consume.setNumber(number);
		consume.setUserLimited(userLimited);
		consume.setSign(sign);
		Status status = integralFacade.updateIntegralConsume(consume);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台修改积分商品", "integral_consume",
					consume.getId(), Status.SUCCESS.message(), null, null,
					request);
			return successModel().toJSON();
		}
		return failureModel("修改积分商品失败").toJSON();
	}

	/**
	 * 积分兑换申请审核
	 * 
	 * @param uuid
	 *            申请记录唯一标识
	 * @param status
	 *            目标状态
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/integral/exchange/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityStatus(@RequestParam String uuid,
			@RequestParam int status) {
		IntegralRecord recordByUuid = integralFacade
				.getIntegralRecordByUuid(uuid);
		if (null == recordByUuid)
			return failureModel("该申请记录有误").toJSON();
		recordByUuid.setStatus(status);
		IntegralRecord integralRecord_updateResult = integralFacade
				.updateIntegralRecordStatus(recordByUuid);
		if (null != integralRecord_updateResult) {
			String goodsName = "";
			if (integralRecord_updateResult.getConsume() != null) {
				/**
				 * 2016-3-17 积分兑换发货，发送模板消息
				 */
				goodsName = Tools.nullValueFilter(integralRecord_updateResult
						.getConsume().getName());
				getModelMessageFacade().deliverGoods(goodsName,
						integralRecord_updateResult.getUser());
			}
			return BaseAPIModel
					.makeWrappedSuccessDataJson(IntegralExchangeDataItem
							.build(integralRecord_updateResult));
		}
		return failureModel("审核出错").toJSON();
	}

	/**
	 * 用户积分明细
	 * 
	 * @param model
	 * @param request
	 * @param userUuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/admin/integralRecord/datatable/{userUuid}")
	public @ResponseBody String integralRecord(Model model,
			HttpServletRequest request,
			@PathVariable(value = "userUuid") String userUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<IntegralRecord> results = integralFacade
				.userIntegralRecords(userFacade.getUserByUuid(userUuid), q, from,
						size);
		IntegralExchangeData data = new IntegralExchangeData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 系统管理员手动修改积分
	 * 
	 * @param request
	 * @param uuid
	 * @param integral
	 * @return
	 */
	/*
	 * @RequestAuthority(value = Role.SUPER_ADMIN_LEVEL)
	 * 
	 * @RequestMapping(value = "/admin/integral/superAdminChangeIntegral",
	 * method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	 * public @ResponseBody String superAdminChangeIntegral(HttpServletRequest
	 * request, @RequestParam String uuid,
	 * 
	 * @RequestParam int integral){ if(StringUtils.isEmpty(uuid) || 0 ==
	 * integral){ return failureModel("出错啦，请稍后重试").toJSON(); } User user =
	 * userFacade.getUserByUuid(uuid); if(null == user){ return
	 * failureModel("出错啦，请稍后重试").toJSON(); } ActionMessage action =
	 * integralFacade.superAdminChangeIntegral(user, integral); if
	 * (action.isSuccess()) { return successModel().toJSON(); } return
	 * actionModel(action).toJSON(); }
	 */

	/**
	 * 用户积分明细页面跳转
	 * 
	 * @param request
	 * @param userUuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping("/admin/user/integralDetail/{uuid}")
	public ModelAndView newIntegralDetailPage(HttpServletRequest request,
			@PathVariable(value = "uuid") String userUuid) {
		if (StringUtils.isEmpty(userUuid)) {
			return new ModelAndView("/common/index");
		}
		// 查询用户基本信息
		User user = userFacade.getUserByUuid(userUuid);

		if (null == user) {
			return new ModelAndView("/common/index");
		}
		request.setAttribute("user", user);
		// 查询用户当前总积分
		int integralTotal = integralFacade.fetchUserIntegralTotal(user);
		request.setAttribute("totalIntegral", integralTotal);

		return index(request, "/wrapped/user_integral_detail.jsp");
	}

	@RequestAuthority(value = Role.PRO_ADMIN, logger = true)
	@RequestMapping(value = "/admin/user/integral/grant", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String grantUserIntegral(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int type,
			@RequestParam int value, @RequestParam String mark,
			@RequestParam String code) {
		// 查询用户基本信息
		User user = userFacade.getUserByUuid(uuid);
		if (user == null || !user.isActive()) {
			return failureModel("用户无效").toJSON();
		}
		if (!validCode(code, ValidationCodeMaker.USER_GRANT_INTEGRAL)) {
			return failureModel("验证码错误").toJSON();
		}
		try {
			value = Math.abs(value);
			if (type == -1) {
				value = -1 * value;
			}
			if (Tools.isBlank(mark)) {
				mark = "管理员变更";
			}
			integralFacade.actionSystemGrantUserIntegral(user, value, mark);
			bulidRequest("管理员修改用户积分 value:" + value, "user", user.getId(),
					"success", "修改积分", null, request);
			return successModel().toJSON();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return failureModel("修改积分失败").toJSON();
	}

	/**
	 * 2016-4-1 查看积分兑换的收货地址
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/integral/exchange/get/address", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String exchangeGetAddress(HttpServletRequest request,
			@RequestParam String uuid) {
		IntegralRecord record = integralFacade.getIntegralRecordByUuid(uuid);
		if (record == null) {
			return failureModel("此兑换记录不存在").toJSON();
		}
		LogisticsInfo logisticsInfo = receiptAddressFacade
				.getLogisticsInfoByRecordId(record);
		if (logisticsInfo == null) {
			return failureModel("兑换记录收货的地址信息不存在").toJSON();
		}
		LogisticsInfoVo vo = new LogisticsInfoVo();
		vo.buildData(logisticsInfo);
		return vo.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/integral/exchange/update/address", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String exchangeUpdateAddress(
			HttpServletRequest request, @RequestParam String uuid,
			@RequestParam String name, @RequestParam String phone,
			@RequestParam String address) {
		LogisticsInfo logisticsInfo =receiptAddressFacade.getLogisticsInfoByUuid(uuid);
		if (logisticsInfo == null) {
			return failureModel("此收货地址不存在").toJSON();
		}
		LogisticsAddress logisticsAddress=new LogisticsAddress();
		logisticsAddress.setName(name);
		logisticsAddress.setPhone(phone);
		logisticsAddress.setAddress(address);
		String json=gson.toJson(logisticsAddress);
		logisticsInfo.setLogistics(json);
		Status status = receiptAddressFacade.updateLogisticsInfo(logisticsInfo);
		if (status == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("更新收货地址失败").toJSON();
	}
}
