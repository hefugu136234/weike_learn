package com.lankr.tv_cloud.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Shake;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.vo.PdfInfoVo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.datatable.ShakeData;
import com.lankr.tv_cloud.vo.datatable.ShakeDataItem;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
public class GameActivity extends AdminWebController{

	/**
	 * 查看中奖者记录页面跳转
	 * 
	 * @param model
	 * @return	/game_activity/list.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/game/exchange/record/page")
	public ModelAndView renderActivityaddPage(Model model) {
		return index("/wrapped/game_activity/list.jsp");
	}
	
	/**
	 * 后台查询摇一摇兑换列表
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/game/exchange/record/datatable", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String gameExchangeRecord(Model model, HttpServletRequest request,
													@RequestParam(required = false) String isWiner,
													@RequestParam(required = false) String isHandle) {
		String queryKey = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		
		ShakeData data = new ShakeData();
		try {
			Pagination<Shake> results = luckyTmpFacade
					.getExchangeRecord(queryKey, startPage, pageSize, isWiner, isHandle);
			data.build(results.getResults());
			data.setiTotalDisplayRecords(results.getTotal());
			data.setiTotalRecords(results.getPage_rows());
			
			BaseController.bulidRequest("后台查询摇一摇兑换列表", "tmp_yuanxiao", null,
					Status.SUCCESS.message(), null, "成功", request);
			return data.toJSON();
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseController.bulidRequest("后台查询摇一摇兑换列表", "tmp_yuanxiao", null,
				Status.FAILURE.message(), null, "失败", request);
		return data.toJSON();
	}
	
	
	/**
	 * 后台处理中奖者兑奖记录（修改status）
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/game/exchange/record/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8" )
	public @ResponseBody String updateRecordStatus(HttpServletRequest request, @RequestParam String uuid ) {
		Shake shake = luckyTmpFacade.selectShakeByUuid(uuid);
		if (shake == null || !shake.isActive()) {
			BaseController.bulidRequest("后台shake状态转换", "tmp_yuanxiao", null,
					Status.FAILURE.message(), null,
					"uuid=" + uuid + "根据uuid查询记录过程中出错", request);
			return BaseController.failureModel("处理失败，请刷新页面重新尝试").toJSON();
		}
		shake.setStatus(shake.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
					: BaseModel.APPROVED);
		ActionMessage message = luckyTmpFacade.updateShakeStatus(shake);
		if (message == ActionMessage.successStatus()) {
			BaseController.bulidRequest("后台shake状态转换", "tmp_yuanxiao",
					shake.getId(), Status.SUCCESS.message(), null, "成功，转换为="
							+ shake.getStatus(), request);
			ShakeDataItem data = new ShakeDataItem();
			return BaseAPIModel.makeWrappedSuccessDataJson(data.build(shake));
		} else {
			BaseController.bulidRequest("后台shake状态转换", "tmp_yuanxiao",
					shake.getId(), Status.FAILURE.message(), null, "失败，转换为="
							+ shake.getStatus(), request);
			return BaseController.failureModel("处理失败，请刷新页面重新尝试").toJSON();
		}
	}
	
	
}
