package com.lankr.tv_cloud.web;

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

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.OptionAddition;
import com.lankr.tv_cloud.vo.PdfInfoVo;
import com.lankr.tv_cloud.vo.PdfSuface;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping("/project/pdf")
public class PdfController extends AdminWebController {
	private static final String ADD_PDF_KEY = "add_pdf_key";

	private static final String UPDATE_PDF_KEY = "update_pdf_key";

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/list/page")
	public ModelAndView pdfListPage(HttpServletRequest request) {
		return index(request, "/wrapped/pdf/pdf_list.jsp");
	}

	@RequestMapping(value = "/list/data")
	@RequestAuthority(value = Role.PRO_EDITOR)
	public @ResponseBody String pdfListData(HttpServletRequest request) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<PdfInfo> pagination = pdfFacade.selectPdfInfoList(
				searchValue, from, pageItemTotal);
		PdfSuface suface = new PdfSuface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.buildData(pagination.getResults());
		BaseController.bulidRequest("后台查看PDF列表", "pdf_info", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/add/page")
	public ModelAndView addPdfPage(HttpServletRequest request) {
		// modified by mayuan start -->查询最新的10个讲者
		List<Speaker> list = speakerMapper.searchSpeakerList();
		List<OptionAddition> addList = OptionAddition.buildSpaker(list);
		request.setAttribute("spaker_list", addList);
		// modified by mayuan end

		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.setAttribute("signature", getQiniuUploaderSignature());
		request.getSession().setAttribute(ADD_PDF_KEY, token);
		return index(request, "/wrapped/pdf/pdf_add.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String addPdfData(HttpServletRequest request,
			@RequestParam String token, @RequestParam String name,
			@RequestParam String taskId,
			@RequestParam String categoryUuid,
			@RequestParam(required = false) String qrTaskId,
			@RequestParam String coverTaskId,
			@RequestParam int show_type,
			// modified by mayuan -->新增pdf添加讲者选项
			@RequestParam(required = false) String spaker_selector,
			@RequestParam(required = false) String pdfsize,
			@RequestParam(required = false) String pdfnum,
			@RequestParam(required = false) String mark) {
		// modified by mayuan start

		// modified by mayuan end
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive()) {
			BaseController.bulidRequest("后台新增PDF", "pdf_info", null,
					Status.FAILURE.message(), null, "种类uuid=" + categoryUuid
							+ " 不可用", request);
			return BaseController.failureModel("分类不可用").toJSON();
		}

		Speaker speaker = null;
		if (spaker_selector != null && !spaker_selector.equals("0")) {
			speaker = speakerMapper.getSpeakerByUuid(spaker_selector);
			if (speaker == null || !speaker.isActive()) {
				return BaseController.failureModel("讲者不可用").toJSON();
			}
		}
		String repeat_token = toastRepeatSubmitToken(request, ADD_PDF_KEY);
		if (!token.equals(repeat_token)) {
			return BaseController.failureModel("不能重复提交数据，请重新刷新")
					.toJSON();
		}
		PdfInfo pdfInfo = new PdfInfo();
		pdfInfo.setUuid(Tools.getUUID());
		pdfInfo.setName(Tools.nullValueFilter(name));
		pdfInfo.setNamePinyin(Tools.getPinYin(name));
		pdfInfo.setCategory(category);
		pdfInfo.setIsActive(BaseModel.APPROVED);
		pdfInfo.setStatus(BaseModel.UNAPPROVED);
		pdfInfo.setQrTaskId(Tools.nullValueFilter(qrTaskId));
		pdfInfo.setCoverTaskId(Tools.nullValueFilter(coverTaskId));
		pdfInfo.setMark(Tools.nullValueFilter(mark));
		pdfInfo.setTaskId(Tools.nullValueFilter(taskId));
		pdfInfo.setPdfnum(Tools.nullValueFilter(pdfnum));
		pdfInfo.setPdfsize(Tools.nullValueFilter(pdfsize));
		pdfInfo.setShowType(show_type);
		pdfInfo.setSpeaker(speaker); // 添加讲者
		Status status = pdfFacade.addPdfInfo(pdfInfo);
		// 成功后调用拆分pdf接口
		if (status == Status.SUCCESS) {
			// 更新讲者
			resourceFacade.recordResourceSpeaker(pdfInfo, speaker);
			BaseController.bulidRequest("后台新增PDF", "pdf_info", pdfInfo.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel().toJSON();
		} else {
			BaseController.bulidRequest("后台新增PDF", "pdf_info", null,
					Status.FAILURE.message(), null, "失败", request);
			return BaseController.failureModel("保存失败").toJSON();
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping("/update/page/{uuid}")
	public ModelAndView updatePdfPage(HttpServletRequest request,
			@PathVariable String uuid) {
		PdfInfo pdfInfo = pdfFacade.selectPdfInfoByUuid(uuid);
		if (pdfInfo == null || !pdfInfo.isActive()) {
			request.setAttribute("pdf_error", "pdf信息未找到");
			BaseController.bulidRequest("后台查看PDF详情", "pdf_info", null,
					Status.SUCCESS.message(), null,
					"uuid=" + uuid + " 此pdf不存在", request);
			return pdfListPage(request);
		} else {
			// modified by mayuan start -->更新页面回显讲者信息
			List<Speaker> list = null;
			Resource resource = pdfInfo.getResource();
			if (resource != null) {
				Speaker speaker = resource.getSpeaker();
				if (speaker != null) {
					list = new ArrayList<Speaker>();
					list.add(speaker);
				}
			}
			List<OptionAddition> addList = OptionAddition.buildSpaker(list);
			request.setAttribute("spaker_list", addList);
			// modified by mayuan end

			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.getSession().setAttribute(UPDATE_PDF_KEY, token);
			PdfInfoVo data = new PdfInfoVo();
			data.buildData(pdfInfo);
			request.setAttribute("pdf_info", data);
			BaseController.bulidRequest("后台查看PDF详情", "pdf_info",
					pdfInfo.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return index(request, "/wrapped/pdf/pdf_update.jsp");
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updatePdfStatus(HttpServletRequest request,
			String uuid) {
		PdfInfo pdfInfo = pdfFacade.selectPdfInfoByUuid(uuid);
		if (pdfInfo == null || !pdfInfo.isActive()) {
			BaseController.bulidRequest("后台PDF状态转换", "pdf_info", null,
					Status.FAILURE.message(), null,
					"uuid=" + uuid + " 此PDF不存在", request);
			return getStatusJson(Status.NOT_FOUND);
			// 上下线转换
		}
		if (pdfInfo.getStatus() == BaseModel.UNAPPROVED) {
			// 从云端验证pdf状态
			try {
				String url = Config.qn_cdn_host + "/" + pdfInfo.getTaskId()
						+ "?odconv/jpg/info";
				String json = HttpUtils.sendGetRequest(url);
				if (json != null && !json.isEmpty()) {
					JSONObject dataObject = new JSONObject(json);
					if (dataObject != null) {
						int page = dataObject.getInt("page_num");
						pdfInfo.setStatus(BaseModel.APPROVED);
						pdfInfo.setPdfnum(String.valueOf(page));
						pdfInfo.setCoverTaskId(pdfInfo.getPdfDefCover());
						/**
						 * 2016-2-24,新增积分,视频审核，新增积分
						 */
						integralFacade.actionContributeResource(pdfInfo.getResource());
					}
				}
				// if(pdfInfo.getStatus()!=BaseModel.APPROVED){
				// return getStatusJson("pdf正在转换中,请稍后再审核！");
				// }
			} catch (Exception e) {
				e.printStackTrace();
				return BaseController.failureModel("PDF无法正常使用，请检查原始文件是否已经损坏.")
						.toJSON();
			}

		} else {
			pdfInfo.setStatus(pdfInfo.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
					: BaseModel.APPROVED);
		}
		Status status = pdfFacade.updatePdfInfoStatus(pdfInfo);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台PDF状态转换", "pdf_info",
					pdfInfo.getId(), Status.SUCCESS.message(), null, "成功，转换为="
							+ pdfInfo.getStatus(), request);
			PdfInfoVo data = new PdfInfoVo();
			data.buildData(pdfInfo);
			return gson.toJson(data);
		} else {
			BaseController.bulidRequest("后台PDF状态转换", "pdf_info",
					pdfInfo.getId(), Status.FAILURE.message(), null, "失败，转换为="
							+ pdfInfo.getStatus(), request);
		}
		return getStatusJson(Status.FAILURE);
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateShop(HttpServletRequest request,
			@RequestParam String token, @RequestParam String uuid,
			@RequestParam(required = false) String qrTaskId, @RequestParam String coverTaskId,
			@RequestParam String name, @RequestParam String categoryUuid,
			@RequestParam int show_type,
			// modified by mayuan -->添加讲者
			@RequestParam String spaker_selector,
			@RequestParam(required = false) String mark) {
		// modified by yuan start

		// modified by mayuan end

		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive()) {
			BaseController.bulidRequest("后台PDF信息修改", "pdf_info", null,
					Status.FAILURE.message(), null, "种类uuid=" + categoryUuid
							+ " 不可用", request);
			return BaseController.failureModel("分类不可用").toJSON();
		}
		String server_token = toastRepeatSubmitToken(request, UPDATE_PDF_KEY);
		Status status = Status.SUCCESS;
		if (!token.equals(server_token)) {
			return BaseController.failureModel("不能重复提交数据，请重新刷新").toJSON();
		} else {
			PdfInfo pdfInfo = pdfFacade.selectPdfInfoByUuid(uuid);
			if (pdfInfo == null || !pdfInfo.isActive())
				return BaseController.failureModel("此pdf不可用").toJSON();
			Speaker speaker = null;
			if (spaker_selector != null && !spaker_selector.equals("0")) {
				speaker=speakerMapper.getSpeakerByUuid(spaker_selector);
				if (speaker == null || !speaker.isActive()) {
					return BaseController.failureModel("讲者不可用").toJSON();
				}
			}
			pdfInfo.setName(Tools.nullValueFilter(name));
			pdfInfo.setNamePinyin(Tools.getPinYin(name));
			pdfInfo.setQrTaskId(Tools.nullValueFilter(qrTaskId));
			pdfInfo.setCategory(category);
			pdfInfo.setCoverTaskId(Tools.nullValueFilter(coverTaskId));
			pdfInfo.setMark(Tools.nullValueFilter(mark));
			pdfInfo.setShowType(show_type);
			status = pdfFacade.updatePdfInfo(pdfInfo);
			if (status == Status.SUCCESS) {
				// 更新讲者
				resourceFacade.recordResourceSpeaker(pdfInfo, speaker);
				BaseController.bulidRequest("后台PDF信息修改", "pdf_info",
						pdfInfo.getId(), Status.SUCCESS.message(), null, "成功",
						request);
				return BaseController.successModel().toJSON();
			} else {
				BaseController.bulidRequest("后台PDF信息修改", "pdf_info",
						pdfInfo.getId(), Status.FAILURE.message(), null, "失败",
						request);
				return BaseController.failureModel("修改失败").toJSON();
			}
		}
	}

	// @RequestAuthority(value = Role.PRO_EDITOR)
	// @RequestMapping(value = "/qn/signature", produces =
	// "text/json; charset=utf-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String getSignature() {
	// String token = QiniuUtils.getSimpleUploadPolicy(null, 1,
	// TimeUnit.SECONDS);
	// return token;
	// }

}
