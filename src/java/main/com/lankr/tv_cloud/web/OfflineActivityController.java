package com.lankr.tv_cloud.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

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
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.ProjectCode;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BroadcastUserSurface;
import com.lankr.tv_cloud.vo.BroadcastUserVo;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;
import com.lankr.tv_cloud.vo.snapshot.OfflineActivityItem;
import com.lankr.tv_cloud.vo.snapshot.ProjectCodeItem;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = "/admin/offline")
public class OfflineActivityController extends AdminWebController {

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/activity/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		return index("/wrapped/offline_activity/list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/list/data",method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String listData(HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<OfflineActivity> pagination = offlineActivityFacade
				.searchPaginationOfflineActivity(q, from, size);
		DataTableModel<?> d = DataTableModel.makeInstance(
				pagination.getResults(), OfflineActivityItem.class);
		d.setiTotalDisplayRecords(pagination.getTotal());
		d.setiTotalRecords(pagination.getPage_rows());
		return d.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/activity/add/page")
	public ModelAndView addPage(HttpServletRequest request) {
		return index("/wrapped/offline_activity/add.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/data/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dataSave(HttpServletRequest request,
			@RequestParam String name, @RequestParam String address,
			@RequestParam int enrollType, @RequestParam String limitNum,
			@RequestParam String price, @RequestParam String integral,
			@RequestParam String cover, @RequestParam String bookStartDate,
			@RequestParam String bookEndDate, @RequestParam String mark,
			@RequestParam String description) {
		/**
		 * 判断时间的合法
		 */
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date bookStartDateD, bookEndDateD;
		bookStartDateD = bookEndDateD = null;
		try {
			bookStartDateD = dateFormat.parse(bookStartDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("报名开始时间不合法").toJSON();
		}
		try {
			bookEndDateD = dateFormat.parse(bookEndDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("报名结束时间不合法").toJSON();
		}

		int limitNumNum = 0;
		try {
			limitNumNum = Integer.parseInt(limitNum);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("报名限制不合法").toJSON();
		}

		int integralNum = 0;
		try {
			integralNum = Integer.parseInt(integral);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("消费积分不合法").toJSON();
		}

		int priceNum = 0;
		try {
			priceNum = Integer.parseInt(price);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("消费积分不合法").toJSON();
		}
		OfflineActivity offlineActivity = new OfflineActivity();
		offlineActivity.setUuid(Tools.getUUID());
		offlineActivity.setStatus(BaseModel.UNAPPROVED);
		offlineActivity.setIsActive(BaseModel.ACTIVE);
		offlineActivity.setMark(mark);
		offlineActivity.setName(name);
		offlineActivity.setPinyin(Tools.getPinYin(name));
		offlineActivity.setDescription(description);
		offlineActivity.setBookStartDate(bookStartDateD);
		offlineActivity.setBookEndDate(bookEndDateD);
		offlineActivity.setAddress(address);
		offlineActivity.setEnrollType(enrollType);
		offlineActivity.setLimitNum(limitNumNum);
		offlineActivity.setCover(cover);
		offlineActivity.setPrice(offlineActivity.getPriceJson(priceNum,
				integralNum));

		ActionMessage<?> actionMessage = offlineActivityFacade
				.addOfflineActivity(offlineActivity);
		if (actionMessage.isSuccess()) {
			// 添加对应的积分商品
			integralFacade.offlineActivityIntegeralConsume(offlineActivity,
					integralNum);
			return successModel().toJSON();
		}
		return failureModel("线下活动保存失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping("/update/page/{uuid}")
	public ModelAndView updatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		// 传回数据
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.isActive()) {
			request.setAttribute("error", "线下活动信息不存在");
			return index("/wrapped/offline_activity/list.jsp");
		}
		OfflineActivityItem vo = new OfflineActivityItem(offlineActivity);
		vo.buildDetail();
		request.setAttribute("vo", vo);
		return index(request, "/wrapped/offline_activity/update.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/data/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dateUpdate(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String name,
			@RequestParam String address, @RequestParam int enrollType,
			@RequestParam String limitNum, @RequestParam String price,
			@RequestParam String integral, @RequestParam String cover,
			@RequestParam String bookStartDate,
			@RequestParam String bookEndDate, @RequestParam String mark,
			@RequestParam String description) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.isActive()) {
			return failureModel("线下活动信息不存在").toJSON();
		}
		/**
		 * 判断时间的合法
		 */
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date bookStartDateD, bookEndDateD;
		bookStartDateD = bookEndDateD = null;
		try {
			bookStartDateD = dateFormat.parse(bookStartDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("报名开始时间不合法").toJSON();
		}
		try {
			bookEndDateD = dateFormat.parse(bookEndDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("报名结束时间不合法").toJSON();
		}

		int limitNumNum = 0;
		try {
			limitNumNum = Integer.parseInt(limitNum);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("报名限制不合法").toJSON();
		}

		int integralNum = 0;
		try {
			integralNum = Integer.parseInt(integral);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("消费积分不合法").toJSON();
		}

		int priceNum = 0;
		try {
			priceNum = Integer.parseInt(price);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return failureModel("消费积分不合法").toJSON();
		}
		offlineActivity.setMark(mark);
		offlineActivity.setName(name);
		offlineActivity.setPinyin(Tools.getPinYin(name));
		offlineActivity.setDescription(description);
		offlineActivity.setBookStartDate(bookStartDateD);
		offlineActivity.setBookEndDate(bookEndDateD);
		offlineActivity.setAddress(address);
		offlineActivity.setEnrollType(enrollType);
		offlineActivity.setLimitNum(limitNumNum);
		offlineActivity.setCover(cover);
		offlineActivity.setPrice(offlineActivity.getPriceJson(priceNum,
				integralNum));

		ActionMessage<?> actionMessage = offlineActivityFacade
				.updateOfflineActivity(offlineActivity);
		if (actionMessage.isSuccess()) {
			// 添加对应的积分商品
			integralFacade.offlineActivityIntegeralConsume(offlineActivity,
					integralNum);
			return successModel().toJSON();
		}
		return failureModel("线下活动修改失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.isActive()) {
			return failureModel("线下活动信息不存在").toJSON();
		}
		if (offlineActivity.getStatus() == BaseModel.UNAPPROVED) {
			offlineActivity.setStatus(BaseModel.APPROVED);
		} else {
			offlineActivity
					.setStatus(offlineActivity.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
							: BaseModel.APPROVED);
		}
		ActionMessage<?> actionMessage = offlineActivityFacade
				.updateOfflineActivityStatus(offlineActivity);
		if (actionMessage.isSuccess()) {
			OfflineActivityItem vo = new OfflineActivityItem(offlineActivity);
			vo.buildList();
			vo.status = "success";
			return gson.toJson(vo);
		}
		return failureModel("线下活动状态修改失败").toJSON();
	}
	
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/bind/initiator", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String bindInitiator(HttpServletRequest request,
			@RequestParam String uuid,@RequestParam String userUuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.isActive()) {
			return failureModel("线下活动信息不存在").toJSON();
		}
		User user=null;
		if(!userUuid.equals("none")){
			user=userFacade.getUserByUuid(userUuid);
		}
		offlineActivity.setInitiatorUser(user);
		ActionMessage<?> actionMessage = offlineActivityFacade.bindInitatorUser(offlineActivity);
		if (actionMessage.isSuccess()) {
			OfflineActivityItem vo = new OfflineActivityItem(offlineActivity);
			vo.buildList();
			vo.status = "success";
			return gson.toJson(vo);
		}
		return failureModel("绑定发起人失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/book/user/list/page/{uuid}")
	public ModelAndView userListPage(HttpServletRequest request,
			@PathVariable String uuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		String name = OptionalUtils.traceValue(offlineActivity, "name");
		request.setAttribute("name", name);
		request.setAttribute("uuid", uuid);
		return index("/wrapped/offline_activity/book_user_list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/book/user/list/data/{uuid}")
	public @ResponseBody String userListData(HttpServletRequest request,
			@PathVariable String uuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		int id = OptionalUtils.traceInt(offlineActivity, "id");
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<SignUpUser> pagination = signUpUserFacade
				.selectSignUpUserForTable(id, SignUpUser.REFER_OFFLINEACTIVITY,
						q, from, size);
		BroadcastUserSurface data = new BroadcastUserSurface();
		data.buildBookUser(pagination.getResults(), webchatFacade,
				certificationFacade);
		data.setiTotalDisplayRecords(pagination.getTotal());
		data.setiTotalRecords(pagination.getPage_rows());
		return data.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/user/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String userUpdateStatus(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int status,
			@RequestParam(required = false) String mark) {
		SignUpUser signUpUser = signUpUserFacade.selectSignUpUserByUuid(uuid);
		if (signUpUser == null) {
			return failureModel("信息不存在").toJSON();
		}
		signUpUser.setStatus(status);
		signUpUser.setMark(mark);
		ActionMessage<?> actionMessage = signUpUserFacade
				.updateSignUpUserStatus(signUpUser);
		if (actionMessage.isSuccess()) {
			BroadcastUserVo vo = new BroadcastUserVo();
			vo.buildBookUser(signUpUser, webchatFacade, certificationFacade);
			vo.setStatus(Status.SUCCESS);
			return vo.toJSON();
		}
		return failureModel("状态修改失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/type/code/list/page/{uuid}")
	public ModelAndView TypeCodeList(HttpServletRequest request,
			@PathVariable String uuid,@RequestParam int type) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		String name = OptionalUtils.traceValue(offlineActivity, "name");
		request.setAttribute("name", name);
		request.setAttribute("uuid", uuid);
		if(type==ProjectCode.CODE_INVITE){
			return index("/wrapped/offline_activity/invite_code_list.jsp");
		}else if(type==ProjectCode.CODE_EXCHANGE){
			return index("/wrapped/offline_activity/exchange_code_list.jsp");
		}
		request.setAttribute("error", "参数不合法");
		return index("/wrapped/offline_activity/list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/type/code/list/data/{uuid}")
	public @ResponseBody String typeCodeData(HttpServletRequest request,
			@PathVariable String uuid,@RequestParam int type) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		int id = OptionalUtils.traceInt(offlineActivity, "id");
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ProjectCode> pagination = projectCodeFacade
				.selectProjectCodeForTable(id,
						ProjectCode.REFER_OFFLINEACTIVITY,
						type, q, from, size);
		DataTableModel<?> d = DataTableModel.makeInstance(
				pagination.getResults(), ProjectCodeItem.class);
		d.setiTotalDisplayRecords(pagination.getTotal());
		d.setiTotalRecords(pagination.getPage_rows());
		return d.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/creat/type/code", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String creatCode(HttpServletRequest request,
			@RequestParam int type, @RequestParam int size,
			@RequestParam String uuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.isActive()) {
			return failureModel("线下活动信息不存在").toJSON();
		}
		projectCodeFacade.addBathCodeByOffline(offlineActivity, type, size);
		return successModel().toJSON();

	}
	
	/**
	 * 获取绑定的user信息
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/bind/user/info", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String bindUserInfo(HttpServletRequest request,
			@RequestParam String uuid) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.isActive()) {
			return failureModel("线下活动信息不存在").toJSON();
		}
		OptionAdditionList optionAdditionList=new OptionAdditionList();
		optionAdditionList.setQ(offlineActivity.getName());
		User user=offlineActivity.getInitiatorUser();
		optionAdditionList.setStatus(Status.SUCCESS);
		if(user==null){
			optionAdditionList.buildUser(null);
		}else{
			optionAdditionList.buildUser(Arrays.asList(user));
		}
		return optionAdditionList.toJSON();

	}
}
