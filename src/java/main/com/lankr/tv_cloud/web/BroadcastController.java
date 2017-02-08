package com.lankr.tv_cloud.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lankr.tv_cloud.broadcast.CurrentLivePlatfromData;
import com.lankr.tv_cloud.broadcast.LiveActionShowJs;
import com.lankr.tv_cloud.broadcast.ZhiLiaoLivePlatfrom;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.CastBanner;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BroadCastBandVo;
import com.lankr.tv_cloud.vo.BroadcastSurface;
import com.lankr.tv_cloud.vo.BroadcastUserSurface;
import com.lankr.tv_cloud.vo.BroadcastUserVo;
import com.lankr.tv_cloud.vo.BroadcastWebVo;
import com.lankr.tv_cloud.vo.ChosenItem;
import com.lankr.tv_cloud.vo.DynamicSearchVo;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = "/project/broadcast")
public class BroadcastController extends AdminWebController {

	private static final String ADD_BROADCAST_KEY = "add_broadcast_key";

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		return index("/wrapped/broadcast/broadcast_list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/list/data")
	public @ResponseBody String listData(HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Broadcast> results = broadcastFacade
				.searchBroadcastForTable(q, from, size);
		BroadcastSurface data = new BroadcastSurface();
		data.buildList(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		BaseController.bulidRequest("后台查看直播列表", "broadcast", null,
				Status.SUCCESS.message(), null, "成功", request);
		return data.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/add/page")
	public ModelAndView addPage(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(ADD_BROADCAST_KEY, token);
		BroadcastWebVo vo = new BroadcastWebVo();
		vo.buildPlatFromList();
		request.setAttribute("data_vo", vo);
		return index(request, "/wrapped/broadcast/broadcast_add.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/data/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dataSave(HttpServletRequest request,
			@RequestParam String token, @RequestParam String name,
			@RequestParam int castType, @RequestParam String limitNum,
			@RequestParam String integral,
			@RequestParam(required = false) String bookStartDate,
			@RequestParam(required = false) String bookEndDate,
			@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam int platFormType,
			@RequestParam(required = false) String castAction,
			@RequestParam(required = false) String mark,
			@RequestParam(required = false) String pincode,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String tvDescription,
			@RequestParam(required = false) String speakerUuid) {
		/**
		 * 判断时间的合法
		 */
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date bookStartDateD, bookEndDateD, startDateD, endDateD;
		bookStartDateD = bookEndDateD = startDateD = bookStartDateD = endDateD = null;
		try {
			startDateD = dateFormat.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播开始时间不合法").toJSON();
		}
		try {
			endDateD = dateFormat.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播结束时间不合法").toJSON();
		}
		try {
			bookStartDateD = dateFormat.parse(bookStartDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播报名开始时间不合法").toJSON();
		}
		try {
			bookEndDateD = dateFormat.parse(bookEndDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播报名结束时间不合法").toJSON();
		}
		Speaker speaker = null;
		if (!Tools.isBlank(speakerUuid)) {
			speaker = assetFacade.getSpeakerByUuid(speakerUuid);
		}

		int limitNumNum = 0;
		try {
			limitNumNum = Integer.parseInt(limitNum);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		int integralNum = 0;
		try {
			integralNum = Integer.parseInt(integral);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		User user = getCurrentUser(request);
		Broadcast broadcast = new Broadcast();
		broadcast.setUuid(Tools.getUUID());
		broadcast.setName(Tools.nullValueFilter(name));
		broadcast.setPinyin(Tools.getPinYin(name));
		broadcast.setCreatUser(user);
		if(speaker!=null){
			broadcast.setSpeaker(speaker);
		}
		broadcast.setCastType(castType);
		broadcast.setLimitNum(limitNumNum);
		broadcast.setPlatFormType(platFormType);
		broadcast.setBookStartDate(bookStartDateD);
		broadcast.setBookEndDate(bookEndDateD);
		broadcast.setStartDate(startDateD);
		broadcast.setEndDate(endDateD);
		broadcast.setPincode(pincode);
		broadcast.setMark(mark);
		broadcast.setDescription(description);
		broadcast.setTvDescription(tvDescription);
		broadcast.setIsActive(BaseModel.APPROVED);
		broadcast.setStatus(BaseModel.UNAPPROVED);
		if (broadcast.getPlatFormType() == CurrentLivePlatfromData.YIDUO_PLAT) {
			if(Tools.isBlank(castAction)){
				return failureModel("直播的接口地址不能为空").toJSON();
			}
			broadcast.setCastAction(castAction);
		}else if(broadcast.getPlatFormType() == CurrentLivePlatfromData.ZHILIAO_PLAT){
			//知了平台
			String castActionJs=ZhiLiaoLivePlatfrom.creatLive(broadcast);
			LiveActionShowJs liveActionShowJs=LiveActionShowJs.parseMessage(castActionJs);
			if(liveActionShowJs==null||!liveActionShowJs.creatSuccess()){
				logger.info(castActionJs);
				return failureModel("直播创建失败").toJSON();
			}
			broadcast.setCastShowJs(castActionJs);
		}else if(broadcast.getPlatFormType() == CurrentLivePlatfromData.BAIDU_PLAT){
			broadcast.setCastAction(castAction);
		}
//		String repeat_token = toastRepeatSubmitToken(request, ADD_BROADCAST_KEY);
//		if (!token.equals(repeat_token)) {
//			return failureModel("不能重复提交数据，请重新刷新").toJSON();
//		}

		Status status = broadcastFacade.addBroadcast(broadcast);
		if (status == Status.SUCCESS) {
			// 创建直播对应的积分信息
			integralFacade
					.saveBroadcastIntegeralConsume(broadcast, integralNum);
			BaseController.bulidRequest("后台新增直播", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return successModel().toJSON();
		} else {
			BaseController.bulidRequest("后台新增直播", "broadcast", null,
					Status.FAILURE.message(), null, "失败", request);
			return failureModel("保存失败").toJSON();
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping("/update/page/{uuid}")
	public ModelAndView updatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		// 传回数据
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.isActive()) {
			request.setAttribute("error", "直播信息不存在");
			return index("/wrapped/broadcast/broadcast_list.jsp");
		}
		BroadcastWebVo vo = new BroadcastWebVo();
		vo.buildPlatFromList();
		vo.buildUpdateData(broadcast);
		IntegralConsume consume = integralFacade
				.searchBroadcastIntegralConsume(broadcast);
		if (consume != null) {
			vo.setIntegral(consume.getIntegral());
		} else {
			vo.setIntegral(0);
		}
		bulidRequest("后台查看直播信息页面", "broadcast",
				broadcast.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		request.setAttribute("data_vo", vo);
		Speaker speaker = broadcast.getSpeaker();
		if (null != speaker) {
			ChosenItem item = DynamicSearchVo.speakerItem(speaker);
			request.setAttribute("chosenItem", item);
		}
		return index(request, "/wrapped/broadcast/broadcast_update.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/data/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dataUpdate(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String name,
			@RequestParam int castType, @RequestParam String limitNum,
			@RequestParam String integral,
			@RequestParam(required = false) String bookStartDate,
			@RequestParam(required = false) String bookEndDate,
			@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam(required=false) String castAction,
			@RequestParam(required = false) String mark,
			@RequestParam(required = false) String pincode,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String tvDescription,
			@RequestParam(required = false) String speakerUuid) {
		/**
		 * 判断时间的合法
		 */
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date bookStartDateD, bookEndDateD, startDateD, endDateD;
		bookStartDateD = bookEndDateD = startDateD = bookStartDateD = endDateD = null;
		try {
			startDateD = dateFormat.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播开始时间不合法").toJSON();
		}
		try {
			endDateD = dateFormat.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播结束时间不合法").toJSON();
		}
		try {
			if (!bookStartDate.isEmpty()) {
				bookStartDateD = dateFormat.parse(bookStartDate);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播报名开始时间不合法").toJSON();
		}
		try {
			if (!bookEndDate.isEmpty()) {
				bookEndDateD = dateFormat.parse(bookEndDate);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("直播报名结束时间不合法").toJSON();
		}

		int limitNumNum = 0;
		try {
			limitNumNum = Integer.parseInt(limitNum);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		int integralNum = 0;
		try {
			integralNum = Integer.parseInt(integral);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		broadcast.setName(Tools.nullValueFilter(name));
		broadcast.setPinyin(Tools.getPinYin(name));
		broadcast.setCastType(castType);
		broadcast.setLimitNum(limitNumNum);
		if (broadcast.getPlatFormType() == CurrentLivePlatfromData.YIDUO_PLAT) {
			if(Tools.isBlank(castAction)){
				return failureModel("直播的接口地址不能为空").toJSON();
			}
			broadcast.setCastAction(castAction);
		}
		broadcast.setBookStartDate(bookStartDateD);
		broadcast.setBookEndDate(bookEndDateD);
		broadcast.setStartDate(startDateD);
		broadcast.setEndDate(endDateD);
		if (!Tools.isBlank(speakerUuid)) {
			Speaker speaker = assetFacade.getSpeakerByUuid(speakerUuid);
			if(speaker!=null){
				broadcast.setSpeaker(speaker);
			}
		}
		broadcast.setCastAction(castAction);
		broadcast.setPincode(pincode);
		broadcast.setMark(mark);
		broadcast.setDescription(description);
		broadcast.setTvDescription(tvDescription);
		Status status = broadcastFacade.updateBroadcast(broadcast);
		if (status == Status.SUCCESS) {
			// 创建直播对应的积分信息
			integralFacade
					.saveBroadcastIntegeralConsume(broadcast, integralNum);
			BaseController.bulidRequest("后台更新直播", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return successModel().toJSON();
		} else {
			BaseController.bulidRequest("后台更新直播", "broadcast", null,
					Status.FAILURE.message(), null, "失败", request);
			return failureModel("保存失败").toJSON();
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping("/config/page/{uuid}")
	public ModelAndView configPage(HttpServletRequest request,
			@PathVariable String uuid) {
		// 传回数据
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.isActive()) {
			request.setAttribute("error", "直播信息不存在");
			return index("/wrapped/broadcast/broadcast_list.jsp");
		}
		BroadcastWebVo vo = new BroadcastWebVo();
		vo.buildConfig(broadcast);
		BaseController.bulidRequest("后台查看直播配置页面", "broadcast",
				broadcast.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		request.setAttribute("data_vo", vo);
		return index(request, "/wrapped/broadcast/broadcast_config.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/update/config", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateConfig(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String banner) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.isActive()) {
			return failureModel("直播信息不存在").toJSON();
		}
		try {
			List<CastBanner> castBannerList = gson.fromJson(banner,
					new TypeToken<List<CastBanner>>() {
					}.getType());
			broadcast.setBanner(gson.toJson(castBannerList));
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return failureModel("上传的图片参数不合法").toJSON();
		}
		Status status = broadcastFacade.updateBroadcastCover(broadcast);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台更新直播配置", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return successModel().toJSON();
		} else {
			BaseController.bulidRequest("后台更新直播配置", "broadcast", null,
					Status.FAILURE.message(), null, "失败", request);
			return failureModel("保存失败").toJSON();
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.isActive()) {
			return failureModel("直播信息不存在").toJSON();
		}
		if (broadcast.getBanner() == null || broadcast.getBanner().isEmpty()) {
			return failureModel("请先完成直播的配置信息").toJSON();
		}
		if (broadcast.getStatus() == BaseModel.UNAPPROVED) {
			broadcast.setStatus(BaseModel.APPROVED);
		} else {
			broadcast
					.setStatus(broadcast.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
							: BaseModel.APPROVED);
		}
		Status status = broadcastFacade.updateBroadcastStatus(broadcast);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台更新直播状态", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			BroadcastWebVo vo = new BroadcastWebVo();
			vo.buildTableData(broadcast);
			vo.setStatus(Status.SUCCESS);
			return vo.toJSON();
		} else {
			BaseController.bulidRequest("后台更新直播状态", "broadcast", null,
					Status.FAILURE.message(), null, "失败", request);
			return BaseController.failureModel("状态修改失败").toJSON();
		}
	}

	/**
	 * 获取直播的二维码
	 */
	// @RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	// @RequestMapping(value = "/get/qrcode/{uuid}", method = RequestMethod.GET,
	// produces = "text/json; charset=utf-8")
	// public @ResponseBody String getQrcode(HttpServletRequest request,
	// @PathVariable String uuid) {
	// Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
	// if (broadcast == null || !broadcast.isActive()) {
	// return failureModel("直播信息不存在").toJSON();
	// }
	// if(broadcast.getStatus()==BaseModel.UNAPPROVED){
	// return failureModel("直播请先审核").toJSON();
	// }
	// QrCode qrCode = qrCodeFacade.getBroadcastQrTemp(broadcast.getId());
	// if (qrCode == null) {
	// return failureModel("直播的二维码生成失败，请重新生成").toJSON();
	// }
	// return successModel(qrCode.getQrurl()).toJSON();
	// }

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/user/list/page/{uuid}")
	public ModelAndView userListPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		String broadName = OptionalUtils.traceValue(broadcast, "name");
		request.setAttribute("broadName", broadName);
		request.setAttribute("uuid", uuid);
		return index("/wrapped/broadcast/broadcast_user_list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/user/list/data/{uuid}")
	public @ResponseBody String userListData(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		int broadcastId = OptionalUtils.traceInt(broadcast, "id");
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<BroadcastUser> results = broadcastFacade
				.searchBroadcastUserForTable(q, from, size, broadcastId);
		BroadcastUserSurface data = new BroadcastUserSurface();
		data.buildList(results.getResults(), webchatFacade, certificationFacade);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		BaseController.bulidRequest("后台查看直播的报名人员列表", "broadcast_user", null,
				Status.SUCCESS.message(), null, "成功", request);
		return data.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/user/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String userUpdateStatus(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int status,
			@RequestParam(required = false) String mark) {
		BroadcastUser broadcastUser = broadcastFacade
				.searchBroadcastUserByUuid(uuid);
		if (broadcastUser == null) {
			return failureModel("信息不存在").toJSON();
		}
		broadcastUser.setStatus(status);
		broadcastUser.setMark(mark);
		Status statusMark = broadcastFacade
				.updateBroadcastUserStatus(broadcastUser);
		if (statusMark == Status.SUCCESS) {
			BaseController.bulidRequest("后台更新直播报名人员状态", "broadcast_user",
					broadcastUser.getId(), Status.SUCCESS.message(), null,
					"成功", request);
			BroadcastUserVo vo = new BroadcastUserVo();
			vo.buildTableData(broadcastUser, webchatFacade, certificationFacade);
			vo.setStatus(Status.SUCCESS);
			return vo.toJSON();
		} else {
			BaseController.bulidRequest("后台更新直播报名人员状态", "broadcast", null,
					Status.FAILURE.message(), null, "失败", request);
			return BaseController.failureModel("状态修改失败").toJSON();
		}
	}

	/**
	 * 直播绑定录播，获取直播信息
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/get/band/info", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String userUpdateStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.isActive()) {
			return failureModel("直播信息不存在，或已下线").toJSON();
		}
		// 获取10条最新的视频信息资源
		List<Resource> list = resourceFacade.getWebCastInitList();

		BroadCastBandVo vo = new BroadCastBandVo();
		vo.buildData(broadcast, list);
		return vo.toJSON();
	}

	/**
	 * 直播绑定录播，通过
	 * 
	 * @param request
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/search/record", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String searchRecord(HttpServletRequest request,
			@RequestParam String q) {
		// 根据search最多搜索15条
		List<Resource> list = resourceFacade.getWebCastSearchList(q);
		DynamicSearchVo search = DynamicSearchVo.buildResources(list, q);
		return search.toJSON();
	}

	/**
	 * 直播绑定录播
	 * 
	 * @param args
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/bind/record", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String bindRecord(HttpServletRequest request,
			@RequestParam String uuid,
			@RequestParam(required = false) String resUuid,
			@RequestParam(required = false) String resourceUrl) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.isActive()) {
			return failureModel("直播信息不存在，或已下线").toJSON();
		}
		Resource resource = null;
		if (resUuid != null && !resUuid.isEmpty() && !resUuid.equals("0")) {
			resource = resourceFacade.getResourceByUuid(resUuid);
			if (resource == null || !resource.apiUseable()) {
				return failureModel("资源信息不存在，或已下线").toJSON();
			}
		}

		if (resource == null && (resourceUrl == null || resourceUrl.isEmpty())) {
			return failureModel("请配置直播的录播资源信息").toJSON();
		}
		broadcast.setResource(resource);
		broadcast.setResourceUrl(resourceUrl);
		Status status = broadcastFacade.updateBroadcastRes(broadcast);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台更新直播的录播", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			BroadcastWebVo vo = new BroadcastWebVo();
			vo.buildTableData(broadcast);
			vo.setStatus(Status.SUCCESS);
			return vo.toJSON();
		}
		return failureModel("直播的录播信息更新失败").toJSON();
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

}
