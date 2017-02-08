package com.lankr.tv_cloud.web.front;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonSyntaxException;
import com.lankr.orm.mybatis.mapper.ResourceMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.vo.VotesPost;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontCommentList;
import com.lankr.tv_cloud.web.front.vo.FrontNewsVo;
import com.lankr.tv_cloud.web.front.vo.FrontPdfVo;
import com.lankr.tv_cloud.web.front.vo.FrontResStatus;
import com.lankr.tv_cloud.web.front.vo.FrontResVote;
import com.lankr.tv_cloud.web.front.vo.FrontResourceItem;
import com.lankr.tv_cloud.web.front.vo.FrontResourceList;
import com.lankr.tv_cloud.web.front.vo.FrontThreeScreenVo;
import com.lankr.tv_cloud.web.front.vo.FrontVideoVo;

@Controller
@RequestMapping(value = BaseFrontController.PC_PRIOR)
public class WebResourceController extends BaseFrontController {

	/**
	 * 需要知道 1.点击的大分类 2，点击的小分类
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/category/list/{uuid}")
	public ModelAndView categoryListPage(HttpServletRequest request,
			@PathVariable String uuid) {
		WxSubject wxSubject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (wxSubject == null || !wxSubject.apiUseable()) {
			return redirectErrorPage(request, "此学科分类不存在或下线");
		}
		Category category = assetFacade.getCategoryById(wxSubject
				.getReflectId());
		if (category == null) {
			return redirectErrorPage(request, "此学科分类不存在或下线");
		}
		int root = wxSubject.getIsRoot();
		if (root == WxSubject.NOT_TOOT) {
			WxSubject parent = wxSubject.getParent();
			if (parent == null || !parent.apiUseable()) {
				return redirectErrorPage(request, "此学科分类不存在或下线");
			}
		}
		// 一级学科分类
		FrontResourceList frontResourceList = new FrontResourceList(true);
		frontResourceList.buildCatePageData(wxSubject, wxSubjectFacade,
				effectResourceFacade(), assetFacade, activityFacade);
		request.setAttribute("data_total", frontResourceList);

		bulidRequest("web查看分类的资源列表", "category", category.getId(),
				Status.SUCCESS.message(), null, "成功", request);

		return redirectPageView("web/category");

	}

	/**
	 * 学科的子学科分类中的分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/sub/subject/page", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String subSubjectPage(HttpServletRequest request,
			@RequestParam int size, @RequestParam int currentPage,
			@RequestParam String uuid) {
		WxSubject wxSubject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		int searchId = 0;
		if (wxSubject != null && wxSubject.apiUseable()) {
			Category category = assetFacade.getCategoryById(wxSubject
					.getReflectId());
			searchId = OptionalUtils.traceInt(category, "id");
		}
		int from = Math.max(currentPage - 1, 0);
		if (size > 10) {
			size = 10;
		}
		from = from * size;
		Pagination<Resource> pageData = effectResourceFacade()
				.resourceFrontPage(searchId, from, size);
		FrontResourceList frontResourceList = new FrontResourceList();
		frontResourceList.buildPageItem(pageData);
		return frontResourceList.toJSON();
	}

	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/resource/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView resorceDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		return resorceDetailCommon(request, uuid);
	}

	/**
	 * 资源收藏
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/res/collection/status", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String collectionStatus(@RequestParam String uuid,
			HttpServletRequest request) {
		Resource res = effectResourceFacade().getResourceByUuid(uuid);
		if (res == null || !res.isValid()) {
			return failureModel("资源不可用或已经下线").toJSON();
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("请先登录，再收藏资源").toJSON();
		}
		FrontResStatus vo = updateCollection(user, res);
		if (vo.getStatus().equals(Status.FAILURE.message())) {
			return vo.toJSON();
		}
		String message = vo.getCollectMessage();
		bulidRequest("web资源" + message, "resource", res.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();
	}

	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/res/praise/status", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String praiseStatus(@RequestParam String uuid,
			HttpServletRequest request) {
		Resource res = effectResourceFacade().getResourceByUuid(uuid);
		if (res == null || !res.isValid()) {
			return failureModel("资源不可用或已经下线").toJSON();
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("请先登录，再点赞资源").toJSON();
		}

		FrontResStatus vo = updatePraise(user, res);
		if (vo.getStatus().equals(Status.FAILURE.message())) {
			return vo.toJSON();
		}
		String message = vo.getZanMessage();
		bulidRequest("web资源" + message, "resource", res.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();

	}

	/**
	 * 第一次点击视频和三分屏时，增加积分
	 * 
	 * @param request
	 * @param resUuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/res/first/view/add/jifen", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String firstViewAddJifen(HttpServletRequest request,
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

		String typeAction = "web播放";
		if (resource.getType() == Type.VIDEO) {
			typeAction = typeAction + "视频资源";
		} else if (resource.getType() == Type.THREESCREEN) {
			typeAction = typeAction + "三分屏资源";
		}

		bulidRequest(typeAction, "resource", resource.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return successModel().toJSON();
	}

	/**
	 * 获取资源的投票内容
	 * 
	 * @param uuid
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/res/get/vote/data", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String resGetVote(@RequestParam String uuid,
			HttpServletRequest request) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.isValid()) {
			return failureModel("资源不存在或已下线").toJSON();
		}
		return buildCommonResVote(request, resource);
	}

	/**
	 * 给资源进行投票
	 * 
	 * @param votes_data
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/res/vote/post/data", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String resVotePost(@RequestParam String uuid,
			@RequestParam String vote_json, HttpServletRequest request) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.isValid()) {
			return failureModel("资源不可用或已经下线").toJSON();
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("请先登录，再投票").toJSON();
		}
		VotesPost vp = null;
		try {
			vp = gson.fromJson(vote_json, VotesPost.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (vp == null) {
			return failureModel("提交的投票数据有误").toJSON();
		}
		List<VotesPost.VotePostItem> posts = vp.posts;
		if (posts == null || posts.isEmpty()) {
			return failureModel("请选中选项，再提交").toJSON();
		}
		// 提交投票
		for (VotesPost.VotePostItem votePostItem : posts) {
			effectResourceFacade().votedByUser(user, votePostItem.s_uuid,
					votePostItem.selected_options);
		}
		// 投票成功增加积分
		int effect = integralFacade.actionUserVoteResource(user, resource);

		bulidRequest("web对资源投票", "resource", resource.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		// 拉取新数据
		return buildCommonResVote(request, resource);
	}

}
