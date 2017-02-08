package com.lankr.tv_cloud.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.InvitcodeRecord;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ApplicableSuface;
import com.lankr.tv_cloud.vo.ApplicableVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@SuppressWarnings("all")
@Controller
@RequestMapping("/project/apply")
public class ApplicableController extends AdminWebController {

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		return index(request, "/wrapped/apply/apply_list.jsp");
	}

	@RequestMapping(value = "/list/data")
	@RequestAuthority(value = Role.PRO_EDITOR)
	public @ResponseBody String listData(HttpServletRequest request) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ApplicableRecords> pagination = applicableFacade
				.selectApplicableList(searchValue, from, pageItemTotal);
		ApplicableSuface suface = new ApplicableSuface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.buildData(pagination.getResults());
		BaseController.bulidRequest("后台申请邀请码列表", "applicable_records", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	// @RequestMapping(value = "/status/update", method = RequestMethod.POST,
	// produces = "text/json; charset=utf-8")
	// @RequestAuthority(value = Role.PRO_EDITOR)
	// public @ResponseBody String updateApply(HttpServletRequest request,
	// @RequestParam String uuid) {
	// ApplicableRecords records = applicableFacade
	// .selectApplicableByUuid(uuid);
	// if (records == null || !records.isActive()) {
	// BaseController.bulidRequest("后台申请邀请码激活", "applicable_records", null,
	// Status.FAILURE.message(), null, "uuid="+uuid+" 此条申请码不存在", request);
	// return getStatusJson(Status.NOT_FOUND);
	// }
	// // 生成一条邀请码记录
	// InvitcodeRecord invitcodeRecord = new InvitcodeRecord();
	// invitcodeRecord.setApplicableRecords(records);
	// invitcodeRecord.setIsActive(1);
	// invitcodeRecord.setSource(0);
	// invitcodeRecord.setStatus(0);
	// invitcodeRecord.setUuid(Tools.getUUID());
	// invitcodeRecord.setMark("");
	// invitcodeRecord.setInvitcode(Tools.generateShortUuid(6));
	// Status status = applicableFacade.addInvitcode(invitcodeRecord);
	// if (status == Status.SUCCESS) {
	// records.setStatus(records.getStatus() == BaseModel.UNAPPROVED ?
	// BaseModel.APPROVED
	// : BaseModel.UNAPPROVED);
	// status = applicableFacade.updateApplicableStatus(records);
	// if (status == Status.SUCCESS) {
	// ApplicableVo data=new ApplicableVo();
	// data.buildData(records);
	// BaseController.bulidRequest("后台申请邀请码生成", "applicable_records",
	// invitcodeRecord.getId(),
	// Status.SUCCESS.message(), null, "成功", request);
	// return gson.toJson(data);
	// }else{
	// BaseController.bulidRequest("后台申请邀请码审核", "applicable_records", null,
	// Status.FAILURE.message(), null, "uuid="+uuid+" 审核失败", request);
	// return getStatusJson("审核失败");
	// }
	// } else {
	// BaseController.bulidRequest("后台邀请码生成", "applicable_records", null,
	// Status.FAILURE.message(), null, "uuid="+uuid+" 审核失败", request);
	// return getStatusJson("生成邀请码失败，请重新生成");
	// }
	//
	// }

	/**
	 * @he 2016-5-12 修改
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value = "/status/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	public @ResponseBody String updateApply(HttpServletRequest request,
			@RequestParam String uuid,@RequestParam int recordStatus) {
		ApplicableRecords records = applicableFacade
				.selectApplicableByUuid(uuid);
		if (records == null || !records.isActive()) {
			return failureModel("vip申请记录不存在").toJSON();
		}
		records.setStatus(recordStatus);
		Status status = applicableFacade.updateApplicableStatus(records);
		if (status == Status.SUCCESS) {
			//发送模板消息
			if(recordStatus==ApplicableRecords.SEND_STATUS){
				User user=records.getUser();
				if(user!=null){
					getModelMessageFacade().vipDeliverGoods(user);
				}
			}
			ApplicableVo data = new ApplicableVo();
			data.setStatus(status);
			data.buildData(records);
			return data.toJSON();
		} 
		return failureModel("状态更改失败").toJSON();
	}

	@RequestMapping(value = "/invite/code/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	public @ResponseBody String getInviteCode(HttpServletRequest request,
			@PathVariable String uuid) {
		ApplicableRecords records = applicableFacade
				.selectApplicableByUuid(uuid);
		if (records == null || !records.isActive()) {
			return getStatusJson(Status.NOT_FOUND);
		}
		InvitcodeRecord invitcodeRecord = applicableFacade
				.selectInvitcodeByRecordId(records.getId());
		if (invitcodeRecord != null) {
			ApplicableVo data = ApplicableVo.buildData(invitcodeRecord);
			return gson.toJson(data);
		} else {
			return getStatusJson("邀请码还未生成");
		}
	}

}
