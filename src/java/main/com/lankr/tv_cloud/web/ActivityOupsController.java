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
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.MessageVO;
import com.lankr.tv_cloud.vo.OptionAddition;
import com.lankr.tv_cloud.vo.OupsCodeSuface;
import com.lankr.tv_cloud.vo.OupsCodeVo;
import com.lankr.tv_cloud.vo.PdfSuface;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.vo.api.SimpleDataType;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@SuppressWarnings("all")
@Controller
@RequestMapping(value = "/admin/oups")
public class ActivityOupsController extends AdminWebController {

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/list/page")
	public ModelAndView oupsListPage(HttpServletRequest request) {
		return index(request, "/wrapped/activity/oups_list.jsp");
	}

	@RequestMapping(value = "/list/data")
	@RequestAuthority(value = Role.PRO_EDITOR)
	public @ResponseBody String oupsListData(HttpServletRequest request) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ActivityApplication> pagination = activityOpusFacade
				.selectOupsList(searchValue, from, pageItemTotal);
		OupsCodeSuface suface = new OupsCodeSuface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.buildData(pagination.getResults());
		BaseController.bulidRequest("后台查看作品编号列表",
				"activity_resource_application", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/view/relate/page/{uuid}")
	public ModelAndView viewRelatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (application == null || !application.isActive()) {
			request.setAttribute("error", "作品不存在");
			return new ModelAndView("redirect:/admin/oups/list/page");
		}
		OupsCodeVo vo = new OupsCodeVo();
		vo.build(application);
		request.setAttribute("oupsCodeVo", vo);
		return index(request, "/wrapped/activity/oups_relate.jsp");
	}

	/**
	 * 解除作品的绑定
	 * 
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/remove/binding", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String removeBinding(HttpServletRequest request,
			@RequestParam String uuid) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (application == null || !application.isActive()) {
			BaseController.bulidRequest("后台查看作品解除绑定",
					"activity_resource_application", null,
					Status.FAILURE.message(), null, "作品uuid=" + uuid + "不存在",
					request);
			return failureModel("作品不存在").toJSON();
		}
		Status status = activityOpusFacade.removeBinding(application);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台查看作品解除绑定",
					"activity_resource_application", application.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		} else {
			BaseController.bulidRequest("后台查看作品解除绑定",
					"activity_resource_application", application.getId(),
					Status.FAILURE.message(), null, "失败", request);
			return failureModel("解除绑定失败").toJSON();
		}

	}

	/**
	 * 2016-4-1 查看作品编号的信息
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/view/detail/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String viewDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (application == null || !application.isActive()) {
			return failureModel("作品不存在").toJSON();
		}
		OupsCodeVo vo = new OupsCodeVo();
		vo.setStatus(Status.SUCCESS);
		vo.build(application);
		return vo.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/{uuid}/fixed/prepare", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody String preChangeStatus(@PathVariable String uuid,
			HttpServletRequest request) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (application == null || !application.isActive()) {
			return failureModel("作品不存在").toJSON();
		}
		OupsCodeVo vo = new OupsCodeVo();
		vo.setStatus(Status.SUCCESS);
		vo.build(application);
		return vo.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/{uuid}/fixed/process", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String processChangeStatus(@PathVariable String uuid,
			@RequestParam int target_status, HttpServletRequest request) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);

		if (application == null || !application.isActive()) {
			return failureModel("作品不存在").toJSON();
		}

		SimpleData bundled = new SimpleData("bundled", application.isBundled(),
				SimpleDataType.Boolean);

		if (!ActivityApplication.validStatus(target_status)) {
			return BaseAPIModel.makeSimpleFailureInnerDataJson(new SimpleData(
					"ret_status", application.getStatus(),
					SimpleDataType.Number),
					new SimpleData("message", "错误的状态码"), bundled);
		}
		String message = "";
		Status s = Status.FAILURE;
		if (!application.isBundled()) {
			if (target_status == ActivityApplication.STATUS_SUCCESS) {
				message = "切换此状态需要先关联资源";
			} else {
				activityOpusFacade.changeActivityApplicationStatus(application,
						target_status);
				s = Status.SUCCESS;
			}
		} else {
			if (target_status <= ActivityApplication.STATUS_RECEIVED) {
				message = "已经关联的资源，无法切换到此状态";
			} else {
				activityOpusFacade.changeActivityApplicationStatus(application,
						target_status);
				s = Status.SUCCESS;
			}
		}
		if (s == Status.SUCCESS) {
			return successModel().toJSON();
		} else {
			return BaseAPIModel.makeSimpleFailureInnerDataJson(new SimpleData(
					"ret_status", application.getStatus(),
					SimpleDataType.Number), new SimpleData("message", message),
					bundled);
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/{uuid}/interact/msgs", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody String interactData(@PathVariable String uuid) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);

		if (application == null || !application.isActive()) {
			return failureModel("作品不存在").toJSON();
		}
		List<Message> msgs = messageFacade.searchOpusMessages(application);
		List<MessageVO> vomsgs = MessageVO.build(msgs);
		return BaseAPIModel.makeWrappedSuccessDataJson(vomsgs);
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/{uuid}/interact/msg/create", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String interactMsgCreate(@PathVariable String uuid,
			@RequestParam String message, HttpServletRequest request) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (application == null || !application.isActive()) {
			return failureModel("作品不存在").toJSON();
		}
		ActionMessage am = messageFacade.addOpusMessage(message, application,
				getCurrentUser(request));
		if (am.isSuccess()) {
			return successModel().toJSON();
		} else {
			return failureModel(am.getMessage()).toJSON();
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/interact/msg/{uuid}/del", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String interactMsgdel(@PathVariable String uuid,
			HttpServletRequest request) {
		Message msg = messageFacade.getByUuid(uuid);
		if (msg == null) {
			return failureModel("消息不存在或已经被删除").toJSON();
		} else {
			messageFacade.del(msg);
			return successModel().toJSON();
		}
	}

}
