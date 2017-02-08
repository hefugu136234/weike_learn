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

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.IntegralWeekReport;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceVoteAnswer;
import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ResourceSurface;
import com.lankr.tv_cloud.vo.ResourceSurfaceVo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.JsonConvertor;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;
import com.lankr.tv_cloud.vo.datatable.UserWorksRecordData;
import com.lankr.tv_cloud.vo.snapshot.CollectionUserItem;
import com.lankr.tv_cloud.vo.snapshot.CommentItem;
import com.lankr.tv_cloud.vo.snapshot.NotificationItem;
import com.lankr.tv_cloud.vo.snapshot.PraiseUserItem;
import com.lankr.tv_cloud.vo.snapshot.ResourceVoteAnswerItem;
import com.lankr.tv_cloud.vo.snapshot.UserCollectionItem;
import com.lankr.tv_cloud.vo.snapshot.UserPraiseItem;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontCommentList;

/**
 * @author mayuan
 * @date 2016年7月5日
 */
@Controller
public class ResourceOverViewController extends AdminWebController {

	/**
	 * @Description: 基本信息，投票信息展示
	 *
	 * @author mayuan
	 * @createDate 2016年7月12日
	 * @modifyDate 2016年7月12日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/resourceOverView/{uuid}")
	public ModelAndView showUserOverView(HttpServletRequest request,
			@PathVariable String uuid) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		ResourceSurfaceVo vo = new ResourceSurfaceVo();
		vo.build(resource);
		List<ResourceVoteSubject> voteSubjects = resourceFacade
				.getVotesByResourceId(resource);
		request.setAttribute("resource", vo);
		request.setAttribute("votes", voteSubjects);
		return new ModelAndView("/WEB-INF/wrapped/project_resource_overView");
	}

	/**
	 * @Description: 投票用户列表页面
	 *
	 * @author mayuan
	 * @createDate 2016年7月12日
	 * @modifyDate 2016年7月12日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/voteUser/page/{uuid}")
	public ModelAndView jumpToVoteUserPage(HttpServletRequest request,
			@PathVariable(value = "uuid") String uuid) {
		ResourceVoteOption option = resourceFacade
				.getResourceVoteOptionByUuid(uuid);
		if (null == option || !option.apiUseable()) {
			request.setAttribute("error", "该记录有误，请刷新页面稍后重新尝试");
			return new ModelAndView("redirect:/asset/category/mgr");
		}
		request.setAttribute("option", option);
		return new ModelAndView(
				"/WEB-INF/wrapped/project_resource_overView_userVote");
	}

	/**
	 * @Description: 投票用户列表
	 *
	 * @author mayuan
	 * @createDate 2016年7月12日
	 * @modifyDate 2016年7月12日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/voteUser/datatable/{uuid}")
	public @ResponseBody String getVoteUser(HttpServletRequest request,
			@PathVariable(value = "uuid") String uuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ResourceVoteAnswer> results = resourceFacade
				.getResourceVoteAnswerListByOptionUuid(uuid, q, from, size);
		DataTableModel data = DataTableModel.makeInstance(results.getResults(),
				ResourceVoteAnswerItem.class);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * @Description: 收藏该资源的用户
	 *
	 * @author mayuan
	 * @createDate 2016年7月12日
	 * @modifyDate 2016年7月12日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/collectionUser/datatable/{resourceUuid}")
	public @ResponseBody String getCollectionUserRecord(
			HttpServletRequest request,
			@PathVariable(value = "resourceUuid") String resourceUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<MyCollection> results = resourceFacade
				.getCollectionUserRecord(effectResourceFacade()
						.getResourceByUuid(resourceUuid), q, from, size);

		DataTableModel data = DataTableModel.makeInstance(results.getResults(),
				CollectionUserItem.class);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * @Description: 该资源点赞的用户
	 *
	 * @author mayuan
	 * @createDate 2016年7月12日
	 * @modifyDate 2016年7月12日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/praiseUser/datatable/{resourceUuid}")
	public @ResponseBody String getPraiseUserRecord(HttpServletRequest request,
			@PathVariable(value = "resourceUuid") String resourceUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Praise> results = resourceFacade.getPraiseUserRecord(
				effectResourceFacade().getResourceByUuid(resourceUuid), q,
				from, size);

		DataTableModel data = DataTableModel.makeInstance(results.getResults(),
				PraiseUserItem.class);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * @Description: 获取该资源的评论记录
	 *
	 * @author mayuan
	 * @createDate 2016年7月25日
	 * @modifyDate 2016年7月25日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/commentUser/datatable/{resourceUuid}")
	public @ResponseBody String getCommentUserRecord(
			HttpServletRequest request,
			@PathVariable(value = "resourceUuid") String resourceUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Message> results = resourceFacade.getCommentUserRecord(
				effectResourceFacade().getResourceByUuid(resourceUuid), q,
				from, size);
		DataTableModel data = DataTableModel.makeInstance(results.getResults(),
				CommentItem.class);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * @Description: 管理员删除评论
	 *
	 * @author mayuan
	 * @createDate 2016年7月25日
	 * @modifyDate 2016年7月25日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/commentUser/remove", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String removeResourceComment(
			HttpServletRequest request, @RequestParam String commentUuid) {
		Message message = messageFacade.getByUuid(commentUuid);
		if (null == message)
			return BaseController.failureModel("操作有误，请稍后重试").toJSON();
		message.setIsActive(BaseModel.DISABLE);
		ActionMessage action = messageFacade.removeResourceComment(message);
		if (!action.isSuccess()) {
			return BaseController.failureModel("删除失败，请稍后重试").toJSON();
		}
		return BaseController.successModel("删除成功").toJSON();
		/*
		 * CommentItem result = new CommentItem(message); return
		 * BaseAPIModel.makeWrappedSuccessDataJson(result);
		 */
	}

	/**
	 * @Description: 评论页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/resource/comments/page", method = RequestMethod.GET)
	public ModelAndView resCommentPage(HttpServletRequest request) {
		return index(request, "/wrapped/project_comments.jsp");
	}

	/**
	 * @Description: 加载评论数据
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/resource/comments/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String resCommentData(HttpServletRequest request,
			@RequestParam int pageSize, @RequestParam int currentPage,
			@RequestParam(required=false) String target, @RequestParam(required=false) String content,
			@RequestParam(required=false) String rangeStart, @RequestParam(required=false) String rangeEnd) {
		pageSize = Math.min(pageSize, 20);
		int startPage = Math.max(currentPage - 1, 0) * pageSize;
		
		SubParams params = new SubParams();
		params.setStart(startPage);
		params.setSize(pageSize);
		params.setTarget(target);
		params.setContent(content);
		params.setRangeStart(rangeStart);
		Date date = Tools.stringToDate(rangeEnd);
		if(null != date){
			String nextDayAsString = Tools.getNextDayAsString(date);
			params.setRangeEnd(nextDayAsString);
		}
		
		FrontCommentList commentList = messageFacade.searchAllComments(params);
		return commentList.toJSON(JsonConvertor.JACKSON);
	}
	
	/**
	 * @Description: 后台评论审核
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/resource/comments/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String resCommentUpdate(@RequestParam String commentUuid, HttpServletRequest request) {
		Message message = messageFacade.getByUuid(commentUuid);
		if(null == message)
			return failureModel("操作出错，请稍后重试").toJSON(); 
		message.setStatus(BaseModel.APPROVED);
		ActionMessage<?> action = messageFacade.update(message);
		if(action.isSuccess())
			return successModel("审核成功").toJSON();
		return failureModel("操作出错，请稍后重试").toJSON(); 
	}
	
	

}
