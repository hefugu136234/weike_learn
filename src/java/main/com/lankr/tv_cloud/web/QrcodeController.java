package com.lankr.tv_cloud.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
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
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrMessage;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.model.QrcodeScanRecode;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.vo.QrSceneSurface;
import com.lankr.tv_cloud.vo.QrSceneVo;
import com.lankr.tv_cloud.vo.QrcodeScanRecodeSurface;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = "/project/qrcode")
public class QrcodeController extends AdminWebController {

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		bulidRequest("后台查看二维码列表", "qrscene", null, Status.SUCCESS.message(),
				null, "成功", request);
		return index("/wrapped/qrcode/qrcode_list.jsp");
	}

	/**
	 * @Description: 列表显示添加类型筛选
	 *
	 * @author mayuan
	 * @createDate 2016年6月12日
	 * @modifyDate 2016年6月12日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/list/data")
	public @ResponseBody String listData(HttpServletRequest request,
					@RequestParam(required = false) String limitType,
					@RequestParam(required = false) String judyType ) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<QrScene> results = qrCodeFacade.searchQrsenceForTable(q, from, size, limitType, judyType);
		QrSceneSurface data = new QrSceneSurface();
		data.buildList(results.getResults(), qrCodeFacade);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/add/page")
	public ModelAndView addPage(HttpServletRequest request) {
		return index(request, "/wrapped/qrcode/qrcode_add.jsp");
	}

	/**
	 * 获取对应资源的数据
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/get/obj/resource", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getObjRes(HttpServletRequest request,
			@RequestParam String q, @RequestParam int type) {
		OptionAdditionList optionAdditionList = new OptionAdditionList();
		if (type == QrScene.ACTIVTY_QR_TYPE) {
			// 活动
			List<Activity> activityList = activityFacade
					.searchActivityByQrSence(q);
			optionAdditionList.buildActivityList(activityList);
		} else if (type == QrScene.CAST_QR_TYPE) {
			// 直播
			List<Broadcast> broadcastList = broadcastFacade
					.searchBroadcastByQrSence(q);
			optionAdditionList.buildBroadCastList(broadcastList);
		} else if (type == QrScene.RESOURCE_TYPE) {
			// 资源
			List<Resource> resourcesList = resourceFacade
					.searchResourceByQrSence(q);
			optionAdditionList.buildCastRecordRes(resourcesList);
		} else if (type == QrScene.GAME_QR) {
			List<Lottery> lotteries = gameMgrFacade.selectLotteryByQrsence(q);
			optionAdditionList.buildGameList(lotteries);
		}
		optionAdditionList.setStatus(Status.SUCCESS);
		return optionAdditionList.toJSON();
	}

	/**
	 * 保存生成二维码
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/add/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dataSave(HttpServletRequest request,
			@RequestParam String name, @RequestParam int type,
			@RequestParam(required = false) String obj,
			@RequestParam String redictUrl, @RequestParam int auth,
			@RequestParam String title, @RequestParam String cover,
			@RequestParam String mark) {
		/**
		 * 首先判断二维码的生成的合法性
		 */
		if (type != QrScene.URL_TYPE) {
			if (obj == null || obj.isEmpty()) {
				return failureModel("请选择对应的二维码载体").toJSON();
			}
		}
		QrScene qrScene = null;
		if (type == QrScene.ACTIVTY_QR_TYPE) {
			// 活动
			Activity activity = activityFacade.getByUuid(obj);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(activity.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.ACTIVTY_QR_TYPE);
			if (qrScene != null) {
				return failureModel("此活动的二维码已存在").toJSON();
			}
			// 生成二维码
			qrScene = new QrScene();
			qrScene.setLimitType(QrScene.QR_FORVER);
			qrScene.setReflectId(activity.getId());
			qrScene.setBusinessId(QrScene.DEFAULT_BUSINESSID);
			qrScene.setType(QrScene.ACTIVTY_QR_TYPE);
		} else if (type == QrScene.CAST_QR_TYPE) {
			// 直播
			Broadcast broadcast = broadcastFacade.getCastByUuid(obj);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(broadcast.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.CAST_QR_TYPE);
			if (qrScene != null) {
				return failureModel("此直播的二维码已存在").toJSON();
			}
			// 生成二维码
			qrScene = new QrScene();
			qrScene.setLimitType(QrScene.QR_TEMP);
			qrScene.setReflectId(broadcast.getId());
			qrScene.setBusinessId(QrScene.DEFAULT_BUSINESSID);
			qrScene.setType(QrScene.CAST_QR_TYPE);
		} else if (type == QrScene.URL_TYPE) {
			// url
			qrScene = new QrScene();
			qrScene.setLimitType(QrScene.QR_TEMP);
			qrScene.setBusinessId(QrScene.DEFAULT_BUSINESSID);
			qrScene.setType(QrScene.URL_TYPE);
		} else if (type == QrScene.RESOURCE_TYPE) {
			// 资源
			Resource resource = resourceFacade.getResourceByUuid(obj);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(resource.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.RESOURCE_TYPE);
			if (qrScene != null) {
				return failureModel("此直播的二维码已存在").toJSON();
			}
			// 生成二维码
			qrScene = new QrScene();
			qrScene.setLimitType(QrScene.QR_FORVER_SEFT);
			qrScene.setReflectId(resource.getId());
			qrScene.setBusinessId(QrScene.DEFAULT_BUSINESSID);
			qrScene.setType(QrScene.RESOURCE_TYPE);
			qrScene.rootPath = request.getServletContext().getRealPath("/");
			qrScene.resourceUuid = resource.getUuid();
		} else if (type == QrScene.GAME_QR) {
			// 游戏
			Lottery lottery = gameMgrFacade.queryLotteryByUuid(obj);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(lottery.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.GAME_QR);
			if (qrScene != null) {
				return failureModel("此直播的二维码已存在").toJSON();
			}
			// 生成二维码
			qrScene = new QrScene();
			qrScene.setLimitType(QrScene.QR_FORVER);
			qrScene.setReflectId(lottery.getId());
			qrScene.setBusinessId(QrScene.DEFAULT_BUSINESSID);
			qrScene.setType(QrScene.GAME_QR);
		} else {
			return failureModel("二维码的类型选择不存在").toJSON();
		}

		qrScene.setUuid(Tools.getUUID());
		qrScene.setName(name);
		qrScene.setPinyin(Tools.getPinYin(name));
		qrScene.setStatus(BaseModel.APPROVED);
		qrScene.setIsActive(BaseModel.ACTIVE);
		QrMessage qrMessage = new QrMessage();
		qrMessage.buildData(redictUrl, auth, title, cover, mark);
		qrScene.setMessage(qrMessage.buildMessageJson());
		ActionMessage actionMessage = qrCodeFacade.addQrsence(qrScene);
		bulidRequest("后台生成二维码", "qrscene", null, Status.SUCCESS.message(),
				null, "成功", request);
		return gson.toJson(actionMessage);

	}

	/**
	 * 通过qrsence的uuid的二维码
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/get/qrcode/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getQrcode(HttpServletRequest request,
			@PathVariable String uuid) {
		QrScene qrScene = qrCodeFacade.selectQrSceneByUuid(uuid);
		if (qrScene == null) {
			return failureModel("二维码信息不存在,请重新生成").toJSON();
		}
		QrCode qrCode = qrCodeFacade.selectQrCodeByScenId(qrScene.getSceneid());
		if (qrCode == null) {
			return failureModel("直播的二维码生成失败").toJSON();
		}
		return successModel(qrCode.getQrurl()).toJSON();
	}
	
	/**
	 * 通过资源和type获取二维码
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/type/res", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String typeRes(HttpServletRequest request,@RequestParam int type,
			@RequestParam String uuid) {
		QrScene qrScene = null;
		if (type == QrScene.ACTIVTY_QR_TYPE) {
			// 活动
			Activity activity = activityFacade.getByUuid(uuid);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(activity.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.ACTIVTY_QR_TYPE);
		} else if (type == QrScene.CAST_QR_TYPE) {
			// 直播
			Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(broadcast.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.CAST_QR_TYPE);
		} else if (type == QrScene.URL_TYPE) {
			
		} else if (type == QrScene.RESOURCE_TYPE) {
			// 资源
			Resource resource = resourceFacade.getResourceByUuid(uuid);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(resource.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.RESOURCE_TYPE);
		} else if (type == QrScene.GAME_QR) {
			// 游戏
			Lottery lottery = gameMgrFacade.queryLotteryByUuid(uuid);
			qrScene = qrCodeFacade.getQrSceneByTypeBuss(lottery.getId(),
					QrScene.DEFAULT_BUSINESSID, QrScene.GAME_QR);
		} 
		if(qrScene==null){
			return failureModel("此二维码不存在").toJSON();
		}
		QrCode qrCode=qrCodeFacade.selectQrCodeByScenId(qrScene.getSceneid());
		if(qrCode==null){
			return failureModel("此二维码不存在").toJSON();
		}
		return successModel(qrCode.getQrurl()).toJSON();
	}

	/**
	 * 二维码的编辑页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/edit/page/{uuid}")
	public ModelAndView editPage(HttpServletRequest request,
			@PathVariable String uuid) {
		// 传回数据
		QrScene qrScene = qrCodeFacade.selectQrSceneByUuid(uuid);
		if (qrScene == null) {
			request.setAttribute("error", "二维码信息不存在");
			return index("/wrapped/qrcode/qrcode_list.jsp");
		}
		int type = qrScene.getType();
		QrSceneVo vo = new QrSceneVo();
		vo.buildUpdatePage(qrScene);
		if (type == QrScene.ACTIVTY_QR_TYPE) {
			// 活动
			Activity activity = activityFacade.getActivityById(qrScene
					.getReflectId());
			String qrType = vo.getQrType();
			qrType = qrType + "——" + OptionalUtils.traceValue(activity, "name");
			vo.setQrType(qrType);
		} else if (type == QrScene.CAST_QR_TYPE) {
			// 直播
			Broadcast broadcast = broadcastFacade.getCastById(qrScene
					.getReflectId());
			String qrType = vo.getQrType();
			qrType = qrType + "——"
					+ OptionalUtils.traceValue(broadcast, "name");
			vo.setQrType(qrType);
		} else if (type == QrScene.URL_TYPE) {
			// url
		} else if (type == QrScene.RESOURCE_TYPE) {
			// 资源
			Resource resource = resourceFacade.getResourceById(qrScene
					.getReflectId());
			String qrType = vo.getQrType();
			qrType = qrType + "——" + OptionalUtils.traceValue(resource, "name");
			vo.setQrType(qrType);
		} else if (type == QrScene.GAME_QR) {
			// 游戏
			Lottery lottery = gameMgrFacade.getLotteryById(qrScene
					.getReflectId());
			String qrType = vo.getQrType();
			qrType = qrType + "——" + OptionalUtils.traceValue(lottery, "name");
			vo.setQrType(qrType);
		}
		request.setAttribute("data_vo", vo);
		return index(request, "/wrapped/qrcode/qrcode_update.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dataUpdate(HttpServletRequest request,
			@RequestParam String name, @RequestParam String uuid,
			@RequestParam String redictUrl, @RequestParam int auth,
			@RequestParam String title, @RequestParam String cover,
			@RequestParam String mark) {
		QrScene qrScene = qrCodeFacade.selectQrSceneByUuid(uuid);
		if (qrScene == null) {
			return failureModel("二维码信息不存在,请重新生成").toJSON();
		}
		qrScene.setName(name);
		qrScene.setPinyin(Tools.getPinYin(name));
		QrMessage qrMessage = new QrMessage();
		qrMessage.buildData(redictUrl, auth, title, cover, mark);
		qrScene.setMessage(qrMessage.buildMessageJson());
		Status status = qrCodeFacade.updateQrScene(qrScene);
		if (status == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("更新失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/user/list/page/{uuid}")
	public ModelAndView userListPage(HttpServletRequest request,
			@PathVariable String uuid) {
		QrScene qrScene = qrCodeFacade.selectQrSceneByUuid(uuid);
		String name = OptionalUtils.traceValue(qrScene, "name");
		request.setAttribute("name", name);
		request.setAttribute("uuid", uuid);
		return index(request, "/wrapped/qrcode/qrcode_user_list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/user/list/data/{uuid}")
	public @ResponseBody String userListData(HttpServletRequest request,
			@PathVariable String uuid) {
		QrScene qrScene = qrCodeFacade.selectQrSceneByUuid(uuid);
		int qrsceneId = OptionalUtils.traceInt(qrScene, "id");
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<QrcodeScanRecode> results = qrCodeFacade
				.searchQrcodeScanRecodeForTable(qrsceneId, q, from, size);
		QrcodeScanRecodeSurface data = new QrcodeScanRecodeSurface();
		data.buildList(results.getResults(), webchatFacade, certificationFacade);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}
	
}
