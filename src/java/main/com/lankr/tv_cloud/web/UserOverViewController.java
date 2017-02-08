package com.lankr.tv_cloud.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.IntegralWeekReport;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;
import com.lankr.tv_cloud.vo.datatable.UserWorksRecordData;
import com.lankr.tv_cloud.vo.snapshot.NotificationItem;
import com.lankr.tv_cloud.vo.snapshot.UserCollectionItem;
import com.lankr.tv_cloud.vo.snapshot.UserPraiseItem;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

/**
 * @author mayuan
 * @date 2016年7月5日
 */
@Controller
public class UserOverViewController extends AdminWebController{

	/**
	 * @Description: 用户信息总览
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value="/project/userOverView/{uuid}" )
	public ModelAndView showUserOverView(HttpServletRequest request, @PathVariable String uuid){
		User user = userFacade.getUserByUuid(uuid);
		if(null != user){
			request.setAttribute("user_info", user);
			UserExpand userExpand = userFacade.getUserExpendByUserId(user.getId());
			if(null != userExpand){
				request.setAttribute("userExpand_info", userExpand);
			}
			Certification certification = certificationFacade.getCertifiActiveByUserId(user);
			if(null != certification){
				request.setAttribute("certification_info", certification);
			}
			IntegralWeekReport userMaxAndHisRecord = integralFacade.getUserMaxAndHisRecordByUserId(user);
			if(null != userMaxAndHisRecord){
				request.setAttribute("userIntegral_info", userMaxAndHisRecord);
			}
			ReceiptAddress receiptAddress = receiptAddressFacade.getReceiptAddressDefault(user.getId());
			if(null != receiptAddress){
				request.setAttribute("address_info", receiptAddress);
			}
			WebchatUser webChatUser = webchatFacade.searchWebChatUserByUserId(user.getId());
			if(null != webChatUser){
				request.setAttribute("webChatUser_info", webChatUser);
			}
		}
		return new ModelAndView("/WEB-INF/wrapped/project_user_overView");
	}
	
	/**
	 * @Description: 用户作品列表
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/userWorkers/datatable/{userUuid}")
	public @ResponseBody String getUserWorksRecord(HttpServletRequest request,
												@PathVariable(value = "userUuid") String userUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Resource> results = userFacade
							.getUserWorksRecord(userFacade.getUserByUuid(userUuid),q , from, size);
		
		UserWorksRecordData data = new UserWorksRecordData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}
	
	/**
	 * @Description: 用户收藏列表
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/userCollection/datatable/{userUuid}")
	public @ResponseBody String getUserCollectionRecord(HttpServletRequest request,
												@PathVariable(value = "userUuid") String userUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<MyCollection> results = userFacade
							.getUserCollectionRecord(userFacade.getUserByUuid(userUuid),q , from, size);
		
		DataTableModel data = DataTableModel.makeInstance(
				results.getResults(), UserCollectionItem.class);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}
	
	/**
	 * @Description: 用户点赞列表
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/userPraise/datatable/{userUuid}")
	public @ResponseBody String getUserPraiseRecord(HttpServletRequest request,
												@PathVariable(value = "userUuid") String userUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Praise> results = userFacade
							.getUserPraiseRecord(userFacade.getUserByUuid(userUuid),q , from, size);
		
		DataTableModel data = DataTableModel.makeInstance(
				results.getResults(), UserPraiseItem.class);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}
	
}
