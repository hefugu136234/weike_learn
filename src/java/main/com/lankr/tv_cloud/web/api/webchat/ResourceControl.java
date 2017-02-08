package com.lankr.tv_cloud.web.api.webchat;

import java.text.ParseException;
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
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.vo.VotesPost;
import com.lankr.tv_cloud.web.api.webchat.vo.WxCommentList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxMenuItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontCommentList;
import com.lankr.tv_cloud.web.front.vo.FrontResStatus;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class ResourceControl extends BaseWechatController {
	public static final String NONE_USER = "none";

	/**
	 * 所有资源相关的操作 视频 三分频 pdf 新闻
	 */

	/**
	 * 资源的列表页面
	 * 
	 * @param request
	 * @param uuid
	 * @param cateName
	 * @return 2016-04-27 之后有时间做分页处理 2016-06-24 @he 修改
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/home/list/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView homeList(HttpServletRequest request,
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
		bulidRequest("微信根据分类查看资源列表", "category", category.getId(),
				Status.SUCCESS.message(), null, "分类:" + category.getName(),
				request);
		WxMenuItem item = new WxMenuItem();
		item.buildbSimpleData(wxSubject);
		item.buildSimpleResCount(effectResourceFacade(), category.getId());
		request.setAttribute("vo_data", item);
		return redirectPageView("wechat/resource/resource_list");
	}

	/**
	 * @he 2016-06-24 根据分类分页查询资源
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/resource/list/{uuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String resourceList(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam String startTime,
			@RequestParam int size) {
		WxSubject wxSubject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (wxSubject == null || !wxSubject.apiUseable()) {
			return failureModel("此学科分类不存在或下线").toJSON();
		}
		Category category = assetFacade.getCategoryById(wxSubject
				.getReflectId());
		if (category == null) {
			return failureModel("此学科分类不存在或下线").toJSON();
		}
		if (size > 20) {
			size = 20;
		}
		List<Resource> resources = effectResourceFacade().resourceWxPage(
				category.getId(), startTime, size);
		WxResourceList vo = new WxResourceList();
		vo.build(resources);
		return vo.toJSON();
	}

	/**
	 * 2016-06-15 资源页面第一级统一优化 变为统一的入口，受限于登录用户 视频没有详情页 新闻也无详情页
	 * 2016-07-22 资源首页无需登录
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_RESOURCE_SHARE)
	@RequestMapping(value = "/resource/first/view/{uuid}", method = RequestMethod.GET)
	public ModelAndView resourceFirstView(HttpServletRequest request,
			@PathVariable String uuid) {
		return commonResourceFirstView(request, uuid);
	}

	/**
	 * 2016-06-16 资源详情页的统一入口 目前只有有微信详情页的资源才能进入，（pdf，三分屏）
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_RESOURCE_SHARE)
	@RequestMapping(value = "/resource/view/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView resourceViewDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		return commonResourceViewDetail(request, uuid);
	}

	

	/**
	 * 2016-06-15 收藏操作优化，与pc同步
	 * 
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/collection/status", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String collectionStatus(@RequestParam String uuid,
			HttpServletRequest request) {
		Resource res = effectResourceFacade().getResourceByUuid(uuid);
		if (res == null || !res.isValid()) {
			return failureModel("资源不可用或已经下线").toJSON();
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("not login").toJSON();
		}
		FrontResStatus vo = updateCollection(user, res);
		if (vo.getStatus().equals(Status.FAILURE.message())) {
			return vo.toJSON();
		}
		String message = vo.getCollectMessage();
		bulidRequest("微信资源" + message, "resource", res.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();
	}

	/**
	 * 点赞操作要修改
	 * 
	 * @param resUuid
	 * @param request
	 * @return 2016-06-15 点赞操作优化，与pc同步
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/praise/status", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String praiseStatus(@RequestParam String uuid,
			HttpServletRequest request) {
		Resource res = effectResourceFacade().getResourceByUuid(uuid);
		if (res == null || !res.isValid()) {
			return failureModel("资源不可用或已经下线").toJSON();
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("not login").toJSON();
		}
		FrontResStatus vo = updatePraise(user, res);
		if (vo.getStatus().equals(Status.FAILURE.message())) {
			return vo.toJSON();
		}
		String message = vo.getZanMessage();
		bulidRequest("微信资源" + message, "resource", res.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();
	}

	/**
	 * 2016-04-27 之后有时间 收藏和投票优化
	 * 
	 * @param res_uuid
	 * @param request
	 * @return 2016-06-16 投票获取数据重新优化 拉取投票信息
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/resoruce/get/vote/data", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String resourceGetVote(@RequestParam String uuid,
			HttpServletRequest request) {
		// 判断前台是否有投票权限
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.isValid()) {
			return failureModel("资源不存在或已下线").toJSON();
		}
		return buildCommonResVote(request, resource);
	}

	/**
	 * 2016-04-27 之后有时间 收藏和投票优化
	 * 
	 * @param vote_json
	 * @param request
	 * @return 2016-06-16 投票重新优化 post 投票的信息
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/resource/vote/post/data", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String postVotes(@RequestParam String uuid,
			@RequestParam String vote_json, HttpServletRequest request) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.isValid()) {
			return failureModel("资源不可用或已经下线").toJSON();
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return failureModel("not login").toJSON();
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
		@SuppressWarnings("unused")
		int effect = integralFacade.actionUserVoteResource(user, resource);

		bulidRequest("微信对资源投票", "resource", resource.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		// 拉取新数据
		return buildCommonResVote(request, resource);
	}

	/**
	 * 我的收藏
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/my/collection", method = RequestMethod.GET)
	public ModelAndView collection(HttpServletRequest request) {
		User user = getCurrentUser(request);
		WxMenuItem item = new WxMenuItem();
		int resCount=myCollectionFacade.collectResCountByUser(user);
		item.setResCount(resCount);
		request.setAttribute("vo_data", item);
		
		bulidRequest("微信查看-我的收藏-列表", "my_collection", null,
				Status.SUCCESS.message(), null, null, request);
		return redirectPageView("wechat/resource/my_collection");
	}
	
	/**
	 * @he 2016-06-28 
	 * *
	 * 我的收藏列表分页
	 * 注：此处通的为 updateTime的更新时间
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/resource/my/collect/list", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String resourceMyCollect(HttpServletRequest request, @RequestParam String startTime,
			@RequestParam int size) {
		User user = getCurrentUser(request);
		if (size > 20) {
			size = 20;
		}
		Date updateTime=null;
		try {
			updateTime=Tools.df1.parse(startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Resource> resources = myCollectionFacade.getUserFavoriteResources(
				user, updateTime, size);
		WxResourceList vo = new WxResourceList();
		vo.build(resources);
		return vo.toJSON();
	}

	/**
	 * 2016-06-15 之后要废弃 精彩推荐列表 2016-04-27 以后优化分页
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/recommend/list/page", method = RequestMethod.GET)
	public ModelAndView recommendPage(HttpServletRequest request) {
		Category category = assetFacade.getCategoryByName("精彩推荐");
		if (category == null || !category.isActive()) {
			return redirectErrorPage(request, "精彩推荐分类不存在");
		}
		WxMenuItem item = new WxMenuItem();
		item.setUuid(category.getUuid());
		item.buildSimpleResCount(effectResourceFacade(), category.getId());
		request.setAttribute("vo_data", item);
		bulidRequest("微信精彩推荐列表", null, null, Status.SUCCESS.message(), null,
				"成功", request);
		return redirectPageView("wechat/resource/recommend_list");

	}

	/**
	 * 2016-06-15 之后要废弃 分享有礼列表-2016-4-27要做调整2016-04-27 以后优化分页
	 * 
	 * @param request
	 * @return 2016-06-28 修改邀请好友（原来的分享有礼）
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/shareGift/list/page", method = RequestMethod.GET)
	public ModelAndView viewShareGiftList(HttpServletRequest request) {
		Category category = assetFacade.getCategoryByName("分享有礼");
		if (category == null || !category.isActive()) {
			return redirectErrorPage(request, "分享有礼分类不存在");
		}
		WxMenuItem item = new WxMenuItem();
		item.setUuid(category.getUuid());
		item.buildSimpleResCount(effectResourceFacade(), category.getId());
		request.setAttribute("vo_data", item);
		bulidRequest("微信查看分享有礼列表", "resource", null, Status.SUCCESS.message(),
				null, "成功", request);
		return redirectPageView("wechat/resource/share_gift_list");
	}

	/**
	 * @he 2016-06-28 新闻类数据的查询分页
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/resource/news/list/{uuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String resourceNewsList(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam String startTime,
			@RequestParam int size) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null) {
			return failureModel("此学科分类不存在或下线").toJSON();
		}
		if (size > 20) {
			size = 20;
		}
		List<Resource> resources = effectResourceFacade().resourceWxPage(
				category.getId(), startTime, size);
		WxResourceList vo = new WxResourceList();
		vo.buildResNews(resources, shareGiftMpper);
		return vo.toJSON();
	}

	/**
	 * 最新的,分享，资源二维码生成的总入口 oriUserUuid便于分享向后，增加积分使用
	 *
	 * @param request
	 * @return
	 * @he 2016*06-28 最新修改
	 * @he 2016-12-15 统一资源入口（有些重复判断）
	 */
	@RequestAuthority(requiredProject = false, logger = false, wxShareType = WxSignature.WX_RESOURCE_SHARE)
	@RequestMapping(value = "/resource/first/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView resourceFirst(HttpServletRequest request,
			@PathVariable String uuid,
			@RequestParam(required = false) String oriUserUuid) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.isValid()) {
			return redirectErrorPage(request, "资源不存在或已下线");
		}
		User user = getCurrentUser(request);
		//String url = WX_PRIOR + "/resource/first/view/" + uuid;
		if (user != null) {
			String categoryName = OptionalUtils.traceValue(resource,
					"category.name");
			if (resource.getType() == Type.NEWS && categoryName.equals("分享有礼")) {
				// 记录分享有礼的事件
				// 分享有礼
				addViewShare(request, resource, oriUserUuid);
			} else {
				// 一般资源
				/**
				 * 2016-04-39 来自分享的资源点击，增加积分
				 */
				shareResAddIntegral(request, resource, oriUserUuid);
			}
		}

		// 登录前的记忆url
		//setPriorUrl(request, url);
		//return resourceLogingPage(request, resource);
		//return resourceNotLoginPage(request, resource);
		//2016-12-15 修改统一入口（有重复判断）
		return commonResourceFirstView(request,resource);
	}
	
}
	
