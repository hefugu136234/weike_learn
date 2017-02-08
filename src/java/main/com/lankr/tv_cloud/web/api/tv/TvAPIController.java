package com.lankr.tv_cloud.web.api.tv;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.InvitcodeRecord;
import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrInteractChannel;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.TvAuthentication;
import com.lankr.tv_cloud.model.TvLayout;
import com.lankr.tv_cloud.model.TvQrAuth;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.qr.QrLoginManagement;
import com.lankr.tv_cloud.qr.QrSearchManagement;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.VideoClientData;
import com.lankr.tv_cloud.vo.VideoClientItem;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.CodeConstant;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.vo.api.SimpleDataType;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.api.app.vo.NewsDetailVo;
import com.lankr.tv_cloud.web.api.tv.vo.LayoutItemV2;
import com.lankr.tv_cloud.web.api.tv.vo.LayoutWrappedData;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.BaseFrontController;
import com.lankr.tv_cloud.web.front.vo.FrontBroadcastItem;

/**
 * @author Kalean.Xiang
 *
 */
@Controller
@RequestMapping(value = "/api/tv")
public class TvAPIController extends BaseAPIController {

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm",
			Locale.CHINESE);

	private static final long REFRESH_MIN_INTERVAL = TimeUnit.MINUTES
			.toMillis(15);

	/**
	 * 电视盒子授权方式：
	 * 
	 * @param device
	 *            携带设备的唯一识别码，一般指的是 mac_address
	 * @param token
	 *            原token
	 * */

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/authorize/renew", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String renew_token(
			@RequestParam(required = false) String device,
			HttpServletRequest request) {
		TvAuthentication auth = getHttpRequestWrappedData(TvAuthentication.class);
		if ((System.currentTimeMillis() - auth.getCreateDate().getTime()) > REFRESH_MIN_INTERVAL) {
			auth = apiFacade.createTvAuth(auth.getUser(), device,
					getClientIpAddr(request), request.getParameter("token"));
		}
		TvAuthVo data_wrapped = new TvAuthVo();
		User user = auth.getUser();
		data_wrapped.parse(auth);
		data_wrapped.setStatus(Status.SUCCESS);
		data_wrapped.setCreatedDate(user.getCreateDate());
		data_wrapped.setUsername(user.getUsername());
		data_wrapped.setNickname(user.getNickname());
		data_wrapped.setAvatar(user.getAvatar());
		data_wrapped.setValidDate(user.getUserReference().getValidDate());
		BaseController.bulidRequest("盒子刷新登录", "user", user.getId(),
				Status.SUCCESS.message(), null, "登录成功", request);
		return data_wrapped.toJSON();
	}

	/**
	 * @author Kalean.Xiang
	 * @modifyDate 2016年7月5日 新的接口用qr登录
	 */
	@Deprecated
	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/authorize", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String authorize(@RequestParam String username,
			@RequestParam String password,
			@RequestParam(required = false) String device,
			HttpServletRequest request) {
		try {
			User user = userFacade.login(username, Md5.getMd5String(password));
			TvAuthVo data_wrapped = new TvAuthVo();
			if (user == null || !user.isActive()) {
				data_wrapped.setMessage("用户名或密码错误");
				BaseController.bulidRequest("盒子登录", "user", null,
						Status.FAILURE.message(), null, "用户名或密码错误，登录失败",
						request);
			} else if (user.getUserReference() == null
					|| !user.getUserReference().isActive()) {
				data_wrapped.setMessage("无效用户");
				BaseController.bulidRequest("盒子登录", "user", null,
						Status.FAILURE.message(), null, "无效用户，登录失败", request);
			} else {
				Role user_role = user.getProUserRole();
				// 盒子用户
				if (user_role != null && user_role.isBoxUser()) {
					TvAuthentication auth = apiFacade.createTvAuth(user,
							device, getClientIpAddr(request), null);
					data_wrapped.parse(auth);
					data_wrapped.setStatus(Status.SUCCESS);
					data_wrapped.setCreatedDate(user.getCreateDate());
					data_wrapped.setUsername(user.getUsername());
					data_wrapped.setNickname(user.getNickname());
					data_wrapped.setAvatar(user.getAvatar());
					if (!user.getUserReference().isDateValid()) {
						data_wrapped.setStatus(Status.FAILURE);
						data_wrapped
								.setCode(BaseAPIModel.CodeConstant.TV_RECHARGE);
						data_wrapped.setMessage("用户流量不足，请充值流量卡");
						BaseController.bulidRequest("盒子登录", "user",
								user.getId(), Status.FAILURE.message(), null,
								"用户流量不足，登录失败", request);
					} else {
						data_wrapped.setMessage("登录成功");
						BaseController.bulidRequest("盒子登录", "user",
								user.getId(), Status.SUCCESS.message(), null,
								"登录成功", request);
					}
					// 普通app用户
				} else {
					data_wrapped.setMessage("用户无权登录");
					BaseController.bulidRequest("盒子登录", "user", user.getId(),
							Status.FAILURE.message(), null, "用户无权登录,登录失败",
							request);
				}
			}
			return data_wrapped.toJSON();
		} catch (Exception e) {
			e.printStackTrace();
			return failureModel().toJSON();
		}
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/news/list", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getNewsList(HttpServletRequest request, long updated_at,
			int page_size, String category_uuid) {
		try {
			Category category = assetFacade.getCategoryByUuid(category_uuid);
			if (category == null || !category.isActive()) {
				BaseController.bulidRequest("盒子查看按分类uuid查看新闻列表", "news_info",
						null, Status.FAILURE.message(), null, "分类uuid="
								+ category_uuid + " 分类不可用", request);
				return failureModel("分类不可用").toJSON();
			}
			// 添加访问板块的日志
			BaseController.logForResource(request, category_uuid);
			BaseController.logForMark(request, "category_reference");

			List<NewsInfo> list = newsFacade.selectListByCategoryId(new Date(
					updated_at), page_size, category.getId());
			NewsTvVo vo = new NewsTvVo();
			vo.fommatData(list);
			vo.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("盒子查看按分类uuid查看新闻列表", "news_info", null,
					Status.SUCCESS.message(), null, "分类uuid=" + category_uuid
							+ "成功", request);
			return vo.toJSON();
		} catch (Exception e) {
			BaseController.bulidRequest("盒子查看按分类uuid查看新闻列表", "news_info", null,
					Status.FAILURE.message(), null, "分类uuid=" + category_uuid
							+ " 分类不可用", request);
			return failureModel("获取新闻列表失败").toJSON();
		}
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/news/detail/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getNewsList(HttpServletRequest request,
			@PathVariable String uuid) {
		NewsInfo info = newsFacade.selectInfoByUuidOfApp(uuid);
		if (info == null) {
			BaseController.bulidRequest("盒子查看新闻详情", "news_info", null,
					Status.FAILURE.message(), null, "新闻uuid=" + uuid + " 不存在",
					request);
			return failureModel("此新闻不可用").toJSON();
		}
		NewsDetailVo vo = new NewsDetailVo();
		vo.buildData(info);
		vo.setStatus(Status.SUCCESS);
		BaseController.bulidRequest("盒子查看新闻详情", "news_info", info.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return vo.toJSON();
	}

	@RequestMapping(value = "/news/{uuid}/preview", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public ModelAndView newsPreview(HttpServletRequest request,
			@PathVariable String uuid, Model model) {
		NewsInfo info = newsFacade.selectInfoByUuidOfApp(uuid);
		model.addAttribute("news", info);
		return new ModelAndView("front/news_preview");
	}

	private boolean checkQrFile(HttpServletRequest request, String uuid) {
		String host = Config.host;
		String qr_parent_folder_name = Md5.getMd5String(uuid + host);
		String folder = request.getRealPath("/") + File.separator
				+ qr_storage_relative + File.separator + qr_parent_folder_name;
		String filePath = folder + File.separator + "yy.png";
		File file = new File(filePath);
		if (file.exists() && file.length() != 0)
			return true;
		return false;
	}

	private static String qr_storage_relative = "assets" + File.separator
			+ "qr";

	// 获取视频的播放信息
	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/video/detail", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String videoDetail(@RequestParam String uuid,
			HttpServletRequest request) {
		Video video = assetFacade.getVideoByUUID(uuid);
		if (video == null) {
			BaseController.bulidRequest("盒子播放视频", "asset", null,
					Status.FAILURE.message(), null, "视频uuid=" + uuid + " 不存在",
					request);
			return failureModel("video not found").toJSON();
		}
		if (video.getStatus() == BaseModel.APPROVED) {
			VideoClientItem ci = new VideoClientItem();
			ci.format(video);
			ci.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("盒子播放视频", "asset", video.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return ci.toJSON();
		}
		return failureModel().toJSON();
	}

	/**
	 * @param uuid
	 *            categoryUUid 分类的uuid 获取分类下面的所有视频
	 */
	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/folder/asset/videos", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String categorieVideos(@RequestParam String uuid,
			@RequestParam(required = false) Long updated_at,
			HttpServletRequest request) {
		Category c = assetFacade.getCategoryByUuid(uuid);
		if (c == null) {
			BaseController.bulidRequest("盒子查看视频列表", "asset", null,
					Status.FAILURE.message(), null, "分类uuid=" + uuid + " 不存在",
					request);
			return getStatusJson(Status.NOT_FOUND);
		}
		List<Video> videos = apiFacade.searchAllOnlineVideosByCategoryId(c,
				updated_at);
		VideoClientData vcd = new VideoClientData();
		vcd.setCategoryName(c.getName());
		vcd.setCategoryUuid(c.getUuid());
		vcd.buildData(videos);
		BaseController.bulidRequest("盒子查看视频列表", "asset", null,
				Status.SUCCESS.message(), null, "分类uuid=" + uuid + " 成功",
				request);
		return gson.toJson(vcd);
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/trunk/folder/asset/videos", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String trunkCategoryVideos(@RequestParam String uuid,
			@RequestParam(required = false) Long updated_at,
			HttpServletRequest request) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null) {
			BaseController.bulidRequest("盒子查看子分类视频列表", "asset", null,
					Status.FAILURE.message(), null, "分类uuid=" + uuid + " 不存在",
					request);
			return getStatusJson(Status.NOT_FOUND);
		}
		List<Video> all = new ArrayList<Video>();
		if (category != null) {
			statVideos(category, 3, all, updated_at);
		}
		VideoClientData vcd = new VideoClientData();
		vcd.buildData(all);
		vcd.setCategoryName(category.getName());
		vcd.setCategoryUuid(category.getUuid());
		vcd.setStatus(Status.SUCCESS.message());
		BaseController.bulidRequest("盒子查看子分类视频列表", "asset", null,
				Status.SUCCESS.message(), null, "分类uuid=" + uuid + " 成功",
				request);
		return gson.toJson(vcd);
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/trunk/folder/asset/news", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String trunkCategoryNews(@RequestParam String uuid,
			@RequestParam(required = false) Long updated_at,
			HttpServletRequest request) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null) {
			BaseController.bulidRequest("盒子查看子分类新闻列表", "asset", null,
					Status.FAILURE.message(), null, "分类uuid=" + uuid + " 不存在",
					request);
			return getStatusJson(Status.NOT_FOUND);
		}
		List<Video> all = new ArrayList<Video>();
		if (category != null) {
			statVideos(category, 3, all, updated_at);
		}
		VideoClientData vcd = new VideoClientData();
		vcd.buildData(all);
		vcd.setCategoryName(category.getName());
		vcd.setCategoryUuid(category.getUuid());
		vcd.setStatus(Status.SUCCESS.message());
		BaseController.bulidRequest("盒子查看子分类新闻列表", "asset", null,
				Status.SUCCESS.message(), null, "分类uuid=" + uuid + " 成功",
				request);
		return gson.toJson(vcd);
	}

	private static int max_loop = 3;

	private void statVideos(Category c, int loop, List<Video> total,
			Long updated_at) {
		if (loop > max_loop) {
			return;
		}
		List<Category> children = c.getChildren();
		total.addAll(apiFacade.searchAllOnlineVideosByCategoryId(c, updated_at));
		if (children == null || children.isEmpty()) {
			return;
		}
		for (Category category : children) {
			statVideos(category, loop + 1, total, updated_at);
		}
	}

	/**
	 * spreadable 是否需要所有子分类
	 * */
	@Deprecated
	// 获取分类下的所有资源
	/**
	 * @see categoryDispatcher
	 * */
	// @RequestAuthority(requiredToken = true, logger = true)
	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/resources/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String resources(@RequestParam long updated_at,
			@RequestParam int batch_size, @PathVariable String uuid,
			@RequestParam(required = false) boolean spreadable,
			HttpServletRequest request) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null || !category.isActive()) {
			BaseController.bulidRequest("盒子查看分类", "category", null,
					Status.FAILURE.message(), null, "分类uuid=" + uuid + " 不存在",
					request);
			return BaseController.failureModel("板块不存在或已经被删除").toJSON();
		}
		// 如果是我的收藏
		if ("我的收藏".equals(category.getName())) {
			return myFav(updated_at, batch_size, request);
		}
		ResourcesData data = new ResourcesData();
		data.setStatus(Status.SUCCESS);
		data.buildCategory(category);
		// 判断该分类下面是否绑定布局
		List<TvLayout> layouts = projectFacade.selectTvLayoutsUIData(category);
		if (layouts != null && !layouts.isEmpty()) {
			data.buildLayoutPages(layouts);
			BaseController.bulidRequest("盒子查看分类下模块", "category",
					category.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return data.toJSON();
		}
		if (spreadable) {
			List<Category> all = assetFacade.findAllChildrenCategory(category);
			data.buildCategories(all);
		}

		List<Resource> resources = resourceFacade.searchAPIResources(new Date(
				updated_at), category, batch_size);
		data.buildResource(resources);
		BaseController
				.bulidRequest("盒子根据分类uuid查看资源列表", "category", category.getId(),
						Status.SUCCESS.message(), null, "成功", request);
		return data.toJSON();
	}

	//
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月5日
	 * @modifyDate 2016年5月5日
	 * 
	 */
	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/user/favorites", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String myFav(@RequestParam long updated_at,
			@RequestParam int batch_size, HttpServletRequest request) {
		BaseController.bulidRequest("盒子查看我的收藏列表", "my_collection", null,
				Status.SUCCESS.message(), null, "成功", request);
		return userFavs(request, updated_at, batch_size);
	}

	/**
	 * @param uuid
	 *            resource uuid
	 * */
	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/resource/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String resource(@PathVariable String uuid,
			HttpServletRequest request) {
		// Resource res = resourceFacade.getResourceByUuid(uuid);
		Resource res = getResourceByUuid(uuid);
		System.out.println(res);
		String logMark = boxLogMark(res);
		if (logMark == null) {
			logMark = "盒子查看资源详情";
		} else {
			logMark = "盒子查看" + logMark + "详情";
		}
		// System.out.println(res);
		if (res == null || !res.isValid()) {
			BaseController.bulidRequest(logMark, res, uuid, request);
			return BaseController.failureModel("资源不存在或已被删除").toJSON();
		}
		TvAuthentication auth = getHttpRequestWrappedData(TvAuthentication.class);
		ResourceItemData data = new ResourceItemData().build(res);
		String host = Config.host;
		String content = host + "/api/webchat/res/" + res.getUuid() + "/play";
		String qr_parent_folder_name = Md5.getMd5String(content);
		// 生成qr地址
		String qr = "";
		// if (res.getType() == Type.VIDEO || res.getType() == Type.PDF
		// || res.getType() == Type.THREESCREEN) {
		// File folder = new File(request.getRealPath("/") + File.separator
		// + qr_storage_relative + File.separator
		// + qr_parent_folder_name);
		// folder.mkdirs();
		// File file = new File(folder, "wx.png");
		// qr = host + "/assets/qr/" + qr_parent_folder_name + "/wx.png";
		// if (!file.exists() || file.length() == 0) {
		// Tools.makeQr(file, content);
		// }
		// }
		QrCode qrCode = qrCodeFacade.getQrByResource(res);
		data.setQrURL(OptionalUtils.traceValue(qrCode, "qrurl"));
		// 用户是否收藏了该资源
		data.setFavorited(myCollectionFacade.isFavoritedResourceByUser(res,
				auth.getUser()));
		data.buildRelatedResources(resourceFacade.getResourceRelated(res));
		BaseController.bulidRequest(logMark, res, uuid, request);
		return data.toJSON();
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/resource/{uuid}/count_increase", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String addViewCount(HttpServletRequest request,
			@PathVariable String uuid) {
		Resource res = resourceFacade.getResourceByUuid(uuid);
		String logMark = boxLogMark(res);
		if (logMark == null) {
			logMark = "盒子播放观看资源";
		} else {
			logMark = "盒子播放观看" + logMark;
		}
		if (res == null || !res.isValid()) {
			BaseController.bulidRequest(logMark, res, uuid, request);
			return BaseController.failureModel("#resource not fount").toJSON();
		}
		BaseController.bulidRequest(logMark, res, uuid, request);
		int effect = resourceFacade.countResourceView(res);
		return makeSimpleSuccessInnerDataJson(new SimpleData("count",
				res.getViewCount() + 1, SimpleDataType.Number));
	}

	@RequestMapping(value = "/user/upgrade/invitation", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String userUpgradeByInvitation(
			@RequestParam String token, @RequestParam String code,
			@RequestParam(required = false) String device,
			HttpServletRequest request) {
		TvAuthentication tvAuth = apiFacade.getTvAuthByToken(token);
		if (tvAuth == null || !tvAuth.isActive()) {
			return failureModel("授权认证失败").toJSON();
		}
		User user = tvAuth.getUser();
		if (!user.isActive() || !user.getProUserRole().isCustomer()) {
			return failureModel("用户无法升级").toJSON();
		}
		InvitcodeRecord record = applicableFacade.selectInvitcodeByCode(code);
		if (record == null || !record.isValid()) {
			return failureModel("邀请码不可用").toJSON();
		}
		Status status = userFacade.upgradeUserToBoxUserByInvitation(user,
				record);
		if (status == Status.SUCCESS) {
			request.setAttribute(BaseAPIController.AUTH_KEY, tvAuth);
			return renew_token(device, request);
		}
		return failureModel("升级失败").toJSON();
	}

	// 用户收藏
	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/user/resource/favorite", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String resouceFavorite(@RequestParam String res_uuid,
			HttpServletRequest request) {
		Resource res = resourceFacade.getResourceByUuid(res_uuid);
		if (res == null || !res.isValid()) {
			BaseController.bulidRequest("盒子收藏状态改变", null, null,
					Status.FAILURE.message(), null, "资源uuid=" + res_uuid
							+ " 不可用", request);
			return failureModel("资源不可用或已经被删除").toJSON();
		}
		TvAuthentication auth = BaseController
				.getHttpRequestWrappedData(TvAuthentication.class);
		boolean favorited = myCollectionFacade.favoriteAction(res,
				auth.getUser());
		if (favorited) {
			BaseController.bulidRequest("盒子添加收藏", "resource", res.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		} else {
			BaseController.bulidRequest("盒子取消收藏", "resource", res.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		}
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(new SimpleData(
				"favorited", favorited, SimpleDataType.Boolean));
	}

	private String userFavs(HttpServletRequest request, long updated_at,
			int batch_size) {
		TvAuthentication auth = BaseController
				.getHttpRequestWrappedData(TvAuthentication.class);
		List<Resource> resources = myCollectionFacade.getUserFavoriteResources(
				auth.getUser(), new Date(updated_at), batch_size);
		ResourcesData data = new ResourcesData();
		data.setStatus(Status.SUCCESS);
		data.buildResource(resources);
		return data.toJSON();
	}

	@RequestAuthority(requiredToken = false, logger = false)
	@RequestMapping(value = "/public/html/{name}")
	public void htmlBridge(@PathVariable String name,
			HttpServletResponse response, HttpServletRequest request) {
		try {
			response.sendRedirect("/tv/public/" + name + ".html?ver=3.0");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @since 2.0
	 * */
	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/v2/common/home", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String tvHome(HttpServletResponse response) {
		List<TvLayout> layouts = projectFacade
				.tvHomeLayoutsForAPI(globalDefaultProject());
		LayoutWrappedData data = new LayoutWrappedData();
		data.setStatus(Status.SUCCESS);
		data.setHome(true);
		data.setData(LayoutItemV2.build(layouts, effectResourceFacade()));
		return data.toJSON();
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/activity/data", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityData(HttpServletResponse response,
			@RequestParam String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || !activity.apiUseable()) {
			return failureModel("活动不存在或已下线").toJSON();
		} else {
			BaseController.bulidRequest("盒子进入活动", "activity", activity.getId(),
					Status.SUCCESS.message(), null, "成功", getHandleRequest());
			return activityTVCompletedJson(activity);
		}
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/activity/resources/more", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityResourceMore(
			HttpServletResponse response, @RequestParam String uuid,
			long updated_at, int batch_size) {

		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || !activity.apiUseable()) {
			return failureModel("活动不存在或已下线").toJSON();
		} else {
			batch_size = Math.min(batch_size, 30);
			List<Resource> reses = effectApiFacade().fetchActivityResources(
					activity, new Date(updated_at), batch_size);
			ResourcesData data = new ResourcesData();
			data.buildResource(reses);
			data.setStatus(Status.SUCCESS);
			return data.toJSON();
		}
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/user/logout", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String userLogout() {
		TvAuthentication auth = getHttpRequestWrappedData(TvAuthentication.class);
		logForMark(getHandleRequest(), "用户退出盒子登录");
		BaseAPIModel m = successModel();
		// 删除token
		apiFacade.logoutTvAuth(auth);
		m.setCode(CodeConstant.TV_RELOGIN);
		return m.toJSON();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.web.api.BaseAPIController#getCurrentUser(javax.servlet
	 * .http.HttpServletRequest)
	 */
	public User getCurrentUser(HttpServletRequest request) {
		TvAuthentication auth = getHttpRequestWrappedData(TvAuthentication.class);
		if (auth != null) {
			return auth.getUser();
		}
		return null;
	}

	// @RequestAuthority(requiredToken = false, logger = true)
	/**
	 * @author Kalean.Xiang
	 * @modifyDate 2016年7月6日 将requiredToken 由true 变为 false，未登录的可以直接查看
	 */
	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/v2/category/dispatcher", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String categoryDispatcher(@RequestParam String uuid) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null || !category.apiUseable()) {
			return BaseController.failureModel("板块不存在或已经被删除").toJSON();
		}
		// 获取板块信息
		List<TvLayout> layouts = projectFacade.tvSubLayoutsForAPI(category);
		if (!Tools.isEmpty(layouts)) {
			BaseController.bulidRequest("进入子布局", "category", category.getId(),
					Status.SUCCESS.message(), null, "成功", getHandleRequest());
			LayoutWrappedData data = new LayoutWrappedData();
			data.setStatus(Status.SUCCESS);
			data.setHome(false);
			data.setName(category.getName());
			data.setData(LayoutItemV2.build(layouts, effectResourceFacade()));
			return data.toJSON();
		}
		// 没有子板块，走默认解析器
		List<Resource> resources = effectResourceFacade().searchAPIResources(
				Tools.getCurrentDate(), category, 50);
		ResourcesData data = new ResourcesData();
		data.setStatus(Status.SUCCESS);
		data.buildCategory(category);
		// 构建资源
		data.buildResource(resources);
		// 构建子分类
		List<Category> all = assetFacade.findAllChildrenCategory(category);
		data.buildCategories(all);
		BaseController.bulidRequest("盒子根据分类uuid查看资源列表", "category",
				category.getId(), Status.SUCCESS.message(), null, "成功",
				getHandleRequest());
		return data.toJSON();
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/broadcast/{uuid}/preview", method = RequestMethod.GET)
	public ModelAndView broadcastPrepare(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			request.setAttribute("error", "本直播不存在或已下线");
			return new ModelAndView("tv/broadcast");
		}
		bulidRequest("tv查看直播详情", "broadcast", broadcast.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		User user = getCurrentUser(getHandleRequest());
		FrontBroadcastItem item = new FrontBroadcastItem();
		IntegralConsume consume = integralFacade
				.searchBroadcastIntegralConsume(broadcast);
		int existNUm = broadcastFacade.broadcastBookCount(broadcast);
		item.buildTvData(broadcast, existNUm, consume,user);
		
		// 处理action与参数隔开
		request.setAttribute("vo_data", item);
		return new ModelAndView("tv/broadcast");
	}

	// 点击直播跳转到第三方
	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/broadcast/redirect/thrid/record", method = RequestMethod.POST)
	@ResponseBody
	public String liveViewRecord(HttpServletRequest request, @RequestParam String uuid,@RequestParam String liveUrl) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null) {
			return failureModel().toJSON();
		}
		if(liveUrl.contains(BaseFrontController.PC_PRIOR)){
			bulidRequest("tv查看百度直播", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
		}else{
			bulidRequest("tv点击直播跳转到底三方平台", "broadcast",
					broadcast.getId(), Status.SUCCESS.message(), null, "成功",
					request);
		}
		return Status.SUCCESS.message();
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年6月29日
	 * @modifyDate 2016年6月29日
	 * 
	 */
	@Autowired
	private QrLoginManagement qrLoginManagement;

	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/login/qr/product", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String productQr(HttpServletRequest request,
			@RequestParam String device) {
		String ip = getClientIpAddr(request);
		ActionMessage<TvQrAuth> action = qrLoginManagement.product(ip, device);
		if (action.isSuccess()) {
			TvQrAuth auth = action.getData();
			if (auth != null) {

				return BaseAPIModel.makeSimpleSuccessInnerDataJson(
						"请用微信客户端扫描上面二维码~",
						0,
						new SimpleData("uuid", auth.getUuid()),
						new SimpleData("qrUrl", auth.getQrUrl()),
						new SimpleData("create_time", auth.getCreateDate()
								.getTime(), SimpleDataType.Number),
						new SimpleData("expired_time", qrLoginManagement
								.getExpiredTime(), SimpleDataType.Number));
			} else {
				return failureModel("未能获取到登录二维码").toJSON();
			}
		} else {
			return actionModel(action).toJSON();
		}
	}

	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/login/qr/connect", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String connect(HttpServletRequest request,
			@RequestParam String device, @RequestParam String uuid) {
		String ip = getClientIpAddr(request);
		ActionMessage<TvQrAuth> action = qrLoginManagement.peekTvAuth(ip,
				device, uuid);
		if (action.isSuccess()) {
			TvQrAuth auth = action.getData();
			if (auth != null) {
				return BaseAPIModel.makeSimpleSuccessInnerDataJson(action
						.getMessage(), action.getCode(), new SimpleData(
						"ticket", auth.getKey()), new SimpleData("type",
						"TV_LOGIN"));
			}
		}
		return actionModel(action).toJSON();
	}

	@RequestAuthority(requiredToken = false, logger = true)
	@RequestMapping(value = "/qr/authorized", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String qrLoginByTicket(@RequestParam String ticket,
			@RequestParam String device, HttpServletRequest request) {
		String ip = getClientIpAddr(request);
		ActionMessage<User> action = qrLoginManagement.getTvUserByTicket(
				ticket, device, ip);
		if (action.isSuccess()) {
			User user = action.getData();
			TvAuthVo data_wrapped = new TvAuthVo();
			TvAuthentication auth = apiFacade.createTvAuth(user, device,
					getClientIpAddr(request), null);
			data_wrapped.parse(auth);
			data_wrapped.setStatus(Status.SUCCESS);
			data_wrapped.setCreatedDate(user.getCreateDate());
			data_wrapped.setUsername(user.getUsername());
			data_wrapped.setNickname(user.getNickname());
			data_wrapped.setAvatar(user.getAvatar());
			data_wrapped.setMessage("用户授权成功");
			if (user.vipDateTo() != null) {
				data_wrapped.setValidDateLong(user.vipDateTo().getTime());
			}
			return data_wrapped.toJSON();
		} else {
			return actionModel(action).toJSON();
		}
	}

	@Autowired
	QrSearchManagement qrSearchManagement;

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/search/resource/qr/product", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String productSearchQr(HttpServletRequest request,
			@RequestParam String device) {
		String ip = getClientIpAddr(request);
		ActionMessage<QrInteractChannel> action = qrSearchManagement.product(
				ip, device);
		if (action.isSuccess()) {
			QrInteractChannel auth = action.getData();
			if (auth != null) {
				return BaseAPIModel.makeSimpleSuccessInnerDataJson(
						"请用微信客户端扫描上面二维码~",
						0,
						new SimpleData("uuid", auth.getUuid()),
						new SimpleData("qrUrl", auth.getQrUrl()),
						new SimpleData("create_time", auth.getCreateDate()
								.getTime(), SimpleDataType.Number),
						new SimpleData("expired_time", qrSearchManagement
								.getExpiredTime(), SimpleDataType.Number));
			} else {
				return failureModel("未能获取到登录二维码").toJSON();
			}
		} else {
			return actionModel(action).toJSON();
		}
	}

	@RequestAuthority(requiredToken = true, logger = true)
	@RequestMapping(value = "/search/resource/qr/connect", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String interactConnect(HttpServletRequest request,
			@RequestParam String device, @RequestParam String uuid) {
		String ip = getClientIpAddr(request);
		ActionMessage<QrInteractChannel> action = qrSearchManagement
				.pollingSearchQr(ip, device, uuid);
		if (action.isSuccess()) {
			QrInteractChannel interact = action.getData();
			if (interact != null) {
				if (interact.isResourceTarget()) {
					Resource res = effectResourceFacade().getResourceById(
							interact.getReferId());
					if (res != null && res.apiUseable()) {
						// 超过20s 没有和页面通讯，则判断失去联系
						boolean isTerminal = false;
						try {
							isTerminal = (System.currentTimeMillis() - interact
									.getLastScanDate().getTime()) > TimeUnit.SECONDS
									.toMillis(20);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return BaseAPIModel.makeSimpleSuccessInnerDataJson(
								action.getMessage(), action.getCode(),
								new SimpleData("resUuid", res.getUuid()),
								new SimpleData("type", "TV_SEARCH"),
								new SimpleData("isTerminal", isTerminal,
										SimpleDataType.Boolean));
					}
				}
			}
		}
		return actionModel(action).toJSON();
	}

	@ResponseBody
	@RequestAuthority(requiredToken = false, logger = false)
	@RequestMapping(value = "/app/init/params", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public String appInitParams() {
		String updated_api = "http://api.fir.im/apps/latest/"
				+ Config.box_app_id + "?api_token=" + Config.box_app_api_token;
		String json = BaseAPIModel
				.makeSimpleSuccessInnerDataJson(new SimpleData("updated_api",
						updated_api));
		return encriptedAPIJson(json);
	}

}
