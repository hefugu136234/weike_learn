package com.lankr.tv_cloud.web.api.webchat;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.util.TempleKeyWord;
import com.lankr.tv_cloud.web.api.webchat.vo.CommonItemVo;
import com.lankr.tv_cloud.web.api.webchat.vo.ExchangeComfirm;
import com.lankr.tv_cloud.web.api.webchat.vo.IntegralRecordWXVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxReceiptAddressListVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxReceiptAddressVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class WeChatJifenShop extends BaseWechatController {

	/**
	 * @he 2016-06-28 以后 优化 积分商城
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/wx/jifen/shop", method = RequestMethod.GET)
	public ModelAndView wxJifenShop(HttpServletRequest request) {
		// 头部公共信息
		wxUserShowBaseInfo(request);

		// 商品列表
		List<IntegralConsume> list = integralFacade.getLatestGoodsForWx();
		request.setAttribute("goods_list", list);
		bulidRequest("微信查看积分兑换商品列表", "integral_record", null,
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/score_shop");
	}

	/**
	 * @he 2016-06-28 以后 优化 商品详情
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/wx/jifen/shop/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView wxJifenShopDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		IntegralConsume integralConsume = integralFacade
				.getIntegralConsumeByUuid(uuid);
		if(integralConsume==null||!integralConsume.apiUseable()){
			return redirectErrorPage(request, "积分商品信息不存在或已下线");
		}
		String name, description;
		name = integralConsume.getName();
		description = integralConsume.getDescription();
		int integral = integralConsume.getIntegral();
		request.setAttribute("cover", integralConsume.getCover());
		request.setAttribute("name", name);
		request.setAttribute("description", description);
		request.setAttribute("integral", integral);
		bulidRequest("微信查看积分兑换商品详情", "integral_consume",
				integralConsume.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		return redirectPageView("wechat/score_show");
	}

	/**
	 * @he 2016-06-28 以后 优化 查看商品兑换需不需要地址
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/wx/jifen/exchange/need/address", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String exchangeNeedAddress(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("not login").toJSON();
		}
		IntegralConsume integralConsume = integralFacade
				.getIntegralConsumeByUuid(uuid);
		if (integralConsume == null || !integralConsume.apiUseable()) {
			return failureModel("兑换的商品不存在或已下线").toJSON();
		}
		if (integralConsume.getType() == IntegralConsume.TYPE_ENTITY_GOODS) {
			ReceiptAddress receiptAddress = receiptAddressFacade
					.getReceiptAddressDefault(user.getId());
			if (receiptAddress == null) {
				CommonItemVo itemVo = new CommonItemVo();
				List<Province> list = EphemeralData.getIntansce(provinceMapper)
						.getProvinces();
				if (list != null && !list.isEmpty()) {
					itemVo.buildProvince(list);
				}
				itemVo.setStatus(Status.ERROR);
				itemVo.setMessage("此商品为实物，请先个人中心，收货地址管理中确认默认收货地址");
				return itemVo.toJSON();
			}
		}
		return successModel().toJSON();
	}

	/**
	 * @he 2016-06-28 以后 优化 点击积分兑换的确认页面
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_NO_SHARE)
	@RequestMapping(value = "/wx/jifen/shop/exchange/comfirm/{uuid}", method = RequestMethod.GET)
	public ModelAndView comfirmExchange(HttpServletRequest request,
			@PathVariable String uuid) {
		User user = getCurrentUser(request);
		IntegralConsume integralConsume = integralFacade
				.getIntegralConsumeByUuid(uuid);
		ExchangeComfirm comfirm = new ExchangeComfirm();
		if (integralConsume.getType() == IntegralConsume.TYPE_ENTITY_GOODS) {
			ReceiptAddress receiptAddress = receiptAddressFacade
					.getReceiptAddressDefault(user.getId());
			comfirm.buildData(integralConsume, receiptAddress);
		} else {
			comfirm.buildData(integralConsume);
		}
		bulidRequest("微信积分兑换商品确认", "integral_consume", integralConsume.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		request.setAttribute("comfirm_data", comfirm);
		return redirectPageView("wechat/score_shop_submit");
	}

	/**
	 * @he 2016-06-28 以后 优化 积分兑换的商品
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/jifen/exchange/goods", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String jifenExchange(HttpServletRequest request,
			@RequestParam String uuid,
			@RequestParam(required = false) String addressUuid) {
		User user = getCurrentUser(request);
		IntegralConsume integralConsume = integralFacade
				.getIntegralConsumeByUuid(uuid);
		if (integralConsume == null
				|| integralConsume.getStatus() != BaseModel.ACTIVE) {
			bulidRequest("微信活积分兑换商品", "integral_consume", null,
					Status.FAILURE.message(), null, "兑换商品的uuid=" + uuid
							+ " 不存在或已下线", request);
			return failureModel("兑换商品不存在，或已下线").toJSON();
		}

		/**
		 * 2016-4-1 添加物流地址
		 */
		ActionMessage actionMessage = null;
		if (integralConsume.getType() == IntegralConsume.TYPE_ENTITY_GOODS
				&& addressUuid != null && !addressUuid.isEmpty()) {
			ReceiptAddress receiptAddress = receiptAddressFacade
					.getReceiptAddressByUuid(addressUuid);
			actionMessage = integralFacade.userConsumeIntgral(user,
					integralConsume, receiptAddress);
		} else {
			actionMessage = integralFacade.userConsumeIntgral(user,
					integralConsume, null);
		}

		if (actionMessage != null) {
			if (actionMessage.getStatus() == Status.SUCCESS) {
				/**
				 * 2016-3-17 兑换商品，发送模板消息
				 */
				TempleKeyWord keyword = buildGoodsTemple(integralConsume, user);
				getModelMessageFacade().exchageGoods(keyword, user);

				BaseController.bulidRequest("微信活积分兑换商品", "integral_consume",
						integralConsume.getId(), Status.SUCCESS.message(),
						null, "成功", request);
				return successModel().toJSON();
			}
			return failureModel(actionMessage.getMessage()).toJSON();
		}
		return failureModel("兑换商品失败").toJSON();
	}

	public TempleKeyWord buildGoodsTemple(IntegralConsume integralConsume,
			User user) {
		TempleKeyWord keyword = new TempleKeyWord();
		keyword.setKeyword1(Tools.nullValueFilter(integralConsume.getName()));
		keyword.setKeyword2(String.valueOf(integralConsume.getIntegral()));
		int score = integralFacade.fetchUserIntegralTotal(user);
		keyword.setKeyword3(String.valueOf(score));
		keyword.setKeyword5(Tools.df1.format(new Date()));
		return keyword;
	}

	/**
	 * @he 2016-06-28 以后 优化 我的积分
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/wx/my/jifen", method = RequestMethod.GET)
	public ModelAndView wxMyJifen(HttpServletRequest request,
			@RequestParam(required = false) String originWxUrl) {
		// 头部公共信息
		wxUserShowBaseInfo(request);

		bulidRequest("微信查看我的积分", "integral_record", null,
				Status.SUCCESS.message(), null, "成功", request);
		// 记录微信来源
		bulidRequest(originWxUrl, request);
		return redirectPageView("wechat/my_score");
	}

	/**@he 2016-06-28 以后 优化
	 * 积分的历史纪录
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/wx/view/jifen/record", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String viewJIfenRecord(HttpServletRequest request,
			@RequestParam String startTime, @RequestParam int size) {
		User user = getCurrentUser(request);
		if (size > 20) {
			size = 20;
		}
		
		List<IntegralRecord> list = integralFacade.userIntegralDetailWx(user,
				startTime, size);
		IntegralRecordWXVo vo = new IntegralRecordWXVo();
		vo.buildData(list);
		return vo.toJSON();
	}

	/**@he 2016-06-28 以后 优化
	 * 查看资源（video,threescreen,pdf）,增加积分 点击播放增长积分 2016-06-16 修改优化
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/view/res/add/jifen", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String viewResAddJifen(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel().toJSON();
		}
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.apiUseable()) {
			return failureModel("资源不存在或已下线").toJSON();
		}
		// 增加积分，观看次数
		buildResCommonFirstView(request, resource);

		String typeAction = "微信播放";
		if (resource.getType() == Type.VIDEO) {
			typeAction = typeAction + "视频资源,增加积分";
		} else if (resource.getType() == Type.THREESCREEN) {
			typeAction = typeAction + "三分屏资源,增加积分";
		}
		bulidRequest(typeAction, "resource", resource.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return successModel().toJSON();
	}

	/**@he 2016-06-28 以后 优化
	 * 2016-3-17 积分的兑换记录页面
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/wx/exchange/jifen/page", method = RequestMethod.GET)
	public ModelAndView exchangeJIfenPage(HttpServletRequest request,
			@RequestParam(required = false) String originWxUrl) {
		bulidRequest("微信查看兑换记录", "integral_record", null,
				Status.SUCCESS.message(), null, "成功", request);
		// 记录微信来源
		bulidRequest(originWxUrl, request);
		return redirectPageView("wechat/exchange_his");
	}

	/**
	 * @he 2016-06-28 以后 优化
	 * 2016-3-17 积分的兑换数据
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/wx/exchange/list/show", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String exchangeListShow(HttpServletRequest request,
			@RequestParam String startTime, @RequestParam int size) {
		User user = getCurrentUser(request);
		if (size > 20) {
			size = 20;
		}
		List<IntegralRecord> list = integralFacade.userExchangeIntegralWx(user,
				startTime, size);
		IntegralRecordWXVo vo = new IntegralRecordWXVo();
		vo.buildData(list);
		return vo.toJSON();
	}

	/**@he 2016-06-28 以后 优化
	 * 收货地址列表页面
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/shop/address/list/page", method = RequestMethod.GET)
	public ModelAndView addressShopListPage(HttpServletRequest request) {
		bulidRequest("微信查看我的收货地址页面", "receipt_address", null,
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/address/address_list");
	}

	/**
	 * @he 2016-06-28 以后 优化
	 * 收货地址列表数据
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/shop/address/list/data", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String addressListData(HttpServletRequest request,
			@RequestParam String startTime, @RequestParam int size,
			@RequestParam(required = false) String action,
			@RequestParam(required = false) boolean isFirst) {
		User user = getCurrentUser(request);
		if (size > 20) {
			size = 20;
		}
		WxReceiptAddressListVo vo = new WxReceiptAddressListVo();
		if (action.equals("refresh")) {
			// 刷新，找出默认地址
			ReceiptAddress receiptAddress = receiptAddressFacade
					.getReceiptAddressDefault(user.getId());
			List<ReceiptAddress> list = receiptAddressFacade
					.wxReceiptAddressPageLimit(user.getId(), startTime, size);
			vo.buildDefault(receiptAddress, list);
		} else if (action.equals("downMore") && isFirst) {
			ReceiptAddress receiptAddress = receiptAddressFacade
					.getReceiptAddressDefault(user.getId());
			List<ReceiptAddress> list = receiptAddressFacade
					.wxReceiptAddressPageLimit(user.getId(), startTime, size);
			vo.buildDefault(receiptAddress, list);
		} else {
			// 加载更多数据
			List<ReceiptAddress> list = receiptAddressFacade
					.wxReceiptAddressPageLimit(user.getId(), startTime, size);
			vo.buildNoDefault(list);
		}
		return vo.toJSON();
	}

	/**@he 2016-06-28 以后 优化
	 * 新增收货地址
	 */
	@RequestAuthority(requiredProject = true, logger = false, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/shop/address/add/page", method = RequestMethod.GET)
	public ModelAndView addressAddPage(HttpServletRequest request) {
		return redirectPageView("wechat/address/address_add");
	}

	/**@he 2016-06-28 以后 优化
	 * 收货地址保存
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/shop/address/add/data", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addressAddData(HttpServletRequest request,
			@RequestParam String name, @RequestParam String phone,
			@RequestParam String cityUuid,
			@RequestParam(required = false) String districtUuid,
			@RequestParam String address,
			@RequestParam(required = false) String postCode,
			@RequestParam(required = false) String latitudeVal,
			@RequestParam(required = false) String longitudeVal) {
		User user = getCurrentUser(request);
		City city = provinceMapper.selectCtiByUUid(cityUuid);
		District district = provinceMapper.selectDisByUUid(districtUuid);
		ReceiptAddress receiptAddress = new ReceiptAddress();
		receiptAddress.setUuid(Tools.getUUID());
		receiptAddress.setName(name);
		receiptAddress.setPhone(phone);
		receiptAddress.setAddress(address);
		receiptAddress.setPostCode(postCode);
		receiptAddress.setUser(user);
		receiptAddress.setStatus(BaseModel.APPROVED);
		receiptAddress.setIsActive(BaseModel.ACTIVE);
		receiptAddress.setCity(city);
		receiptAddress.setDistrict(district);
		if (latitudeVal != null && !latitudeVal.isEmpty()) {
			receiptAddress.setLatitude(Float.parseFloat(latitudeVal));
		}
		if (longitudeVal != null && !longitudeVal.isEmpty()) {
			receiptAddress.setLongitude(Float.parseFloat(longitudeVal));
		}
		/**
		 * 判断是否有默认地址
		 */
		ReceiptAddress defaultAddress = receiptAddressFacade
				.getReceiptAddressDefault(user.getId());
		if (defaultAddress == null) {
			receiptAddress.setDefaultAddress(ReceiptAddress.DEFAULT_ADDRESS);
		} else {
			receiptAddress
					.setDefaultAddress(ReceiptAddress.NOt_DEFAULT_ADDRESS);
		}
		Status status = receiptAddressFacade.addReceiptAddress(receiptAddress);
		if (status == Status.SUCCESS) {
			bulidRequest("微信新增收货地址", "receipt_address", receiptAddress.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		} 
//		else {
//			bulidRequest("微信新增收货地址", "receipt_address", null,
//					Status.FAILURE.message(), null, "失败", request);
//		}
		return failureModel("新增地址保存失败").toJSON();
	}

	/**@he 2016-06-28 以后 优化
	 * 修改收货地址
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/shop/address/update/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView addressUpdatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		ReceiptAddress receiptAddress = receiptAddressFacade
				.getReceiptAddressByUuid(uuid);
		if (receiptAddress == null || !receiptAddress.apiUseable()) {
			return redirectErrorPage(request, "收货地址不存在，或下线");
		}
		WxReceiptAddressVo vo = new WxReceiptAddressVo();
		vo.updateData(receiptAddress);
		request.setAttribute("address_data", vo);
		bulidRequest("微信查看收货地址详情", "receipt_address", receiptAddress.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/address/address_update");
	}

	/**@he 2016-06-28 以后 优化
	 * 更新收货地址
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/shop/address/update/data", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addressUpdateData(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String name,
			@RequestParam String phone, @RequestParam String cityUuid,
			@RequestParam(required = false) String districtUuid,
			@RequestParam String address,
			@RequestParam(required = false) String postCode,
			@RequestParam(required = false) String latitudeVal,
			@RequestParam(required = false) String longitudeVal) {
		ReceiptAddress receiptAddress = receiptAddressFacade
				.getReceiptAddressByUuid(uuid);
		if (receiptAddress == null || !receiptAddress.apiUseable()) {
			return failureModel("收货地址不存在，或下线").toJSON();
		}
		City city = provinceMapper.selectCtiByUUid(cityUuid);
		District district = provinceMapper.selectDisByUUid(districtUuid);
		receiptAddress.setName(name);
		receiptAddress.setPhone(phone);
		receiptAddress.setAddress(address);
		receiptAddress.setPostCode(postCode);
		receiptAddress.setCity(city);
		receiptAddress.setDistrict(district);
		if (latitudeVal != null && !latitudeVal.isEmpty()) {
			receiptAddress.setLatitude(Float.parseFloat(latitudeVal));
		}
		if (longitudeVal != null && !longitudeVal.isEmpty()) {
			receiptAddress.setLongitude(Float.parseFloat(longitudeVal));
		}
		Status status = receiptAddressFacade
				.updateReceiptAddress(receiptAddress);
		if (status == Status.SUCCESS) {
			bulidRequest("微信修改收货地址", "receipt_address", receiptAddress.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		} else {
			bulidRequest("微信修改收货地址", "receipt_address", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return failureModel("修改地址失败").toJSON();
	}

	/**@he 2016-06-28 以后 优化
	 * 设置默认地址
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/shop/address/set/default", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addressSetDefault(HttpServletRequest request,
			@RequestParam String uuid) {
		ReceiptAddress receiptAddress = receiptAddressFacade
				.getReceiptAddressByUuid(uuid);
		if (receiptAddress == null || !receiptAddress.apiUseable()) {
			return failureModel("收货地址不存在，或下线").toJSON();
		}
		Status status = receiptAddressFacade
				.updateReceiptAddressDefault(receiptAddress);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("微信设置默认收货地址", "receipt_address",
					receiptAddress.getId(), Status.SUCCESS.message(), null,
					"成功", request);
			return successModel().toJSON();
		} else {
			BaseController.bulidRequest("微信设置默认收货地址", "receipt_address", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return failureModel("设置默认收货地址失败").toJSON();
	}

}
