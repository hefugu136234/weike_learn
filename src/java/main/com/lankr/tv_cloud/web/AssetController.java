package com.lankr.tv_cloud.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.lankr.orm.mybatis.mapper.CategoryExpandMapper;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.AssetPrice;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.CategoryExpand;
import com.lankr.tv_cloud.model.CategoryExpandStatus;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.support.CCSparkServerTools;
import com.lankr.tv_cloud.support.LetvCloudV1;
import com.lankr.tv_cloud.support.Signature;
import com.lankr.tv_cloud.support.TencentVodServer;
import com.lankr.tv_cloud.support.VodFile;
import com.lankr.tv_cloud.support.VodInfoData;
import com.lankr.tv_cloud.support.VodPlayUrlsData;
import com.lankr.tv_cloud.support.qiniu.QiniuUtils;
import com.lankr.tv_cloud.support.qiniu.QiniuVideoAvinfo;
import com.lankr.tv_cloud.utils.QQSecret;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BooleanResultVo;
import com.lankr.tv_cloud.vo.CCVideo;
import com.lankr.tv_cloud.vo.CCVideoData;
import com.lankr.tv_cloud.vo.CategoryExpandVo;
import com.lankr.tv_cloud.vo.CategorySnapshot;
import com.lankr.tv_cloud.vo.MediaCentralVo;
import com.lankr.tv_cloud.vo.OptionAddition;
import com.lankr.tv_cloud.vo.SpeakerDataTable;
import com.lankr.tv_cloud.vo.SpeakerSufaceVo;
import com.lankr.tv_cloud.vo.SpeakerVo;
import com.lankr.tv_cloud.vo.VideoClientData;
import com.lankr.tv_cloud.vo.VideoClientItem;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.api.app.vo.CommonAppItemVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@SuppressWarnings("all")
@Controller
public class AssetController extends AdminWebController {

	@Autowired
	private LetvCloudV1 letvSupport;
	@Autowired
	private CategoryExpandMapper mapper;

	private static String video_save_key = "video_save_key";

	@RequestAuthority(value = Role.PRO_USER_LEVEL, logger = false)
	@RequestMapping(value = "/asset/category/mgr", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) {
		return index(request, "/wrapped/category_mgr.jsp");
	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL, logger = false)
	@RequestMapping(value = "/asset/category/node", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String categoryNode(
			@RequestParam(required = false) String uuid,
			@RequestParam(required = false) String begin,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return BaseController.getEmptyJson();
		List<CategorySnapshot> css = new ArrayList<CategorySnapshot>();
		Project root = user.getStubProject();
		Category begin_category = null;
		if (!Tools.isBlank(begin)) {
			begin_category = assetFacade.getCategoryByUuid(begin);
		}
		List<Category> children = null;
		if ("#".equals(uuid)) {
			CategorySnapshot cs = new CategorySnapshot();
			if (begin_category != null) {
				cs.setId(begin_category.getUuid());
				cs.setText(begin_category.getName());
				cs.setChildren(begin_category.getChildren() != null
						&& !begin_category.getChildren().isEmpty());
			} else {
				cs.setId(root.getUuid());
				cs.setText(HtmlUtils.htmlEscape(root.getProjectName()));
				children = assetFacade.fetchProjectRootCategory(root);
				cs.setChildren(children != null && !children.isEmpty());
				cs.setDeletable(false);
				cs.setEditable(false);
				cs.setAddable(true);
			}
			css.add(cs);
		} else if (root.getUuid().equals(uuid)) {
			if (begin_category != null) {
				children = begin_category.getChildren();
			} else {
				children = assetFacade.fetchProjectRootCategory(root);
			}
			if (children != null && !children.isEmpty()) {
				for (int i = 0; i < children.size(); i++) {
					Category c = children.get(i);
					CategorySnapshot cs = new CategorySnapshot();
					cs.setId(c.getUuid());
					cs.setText(HtmlUtils.htmlEscape(c.getName()));
					cs.setChildren(c.getChildren() != null
							&& !c.getChildren().isEmpty());
					cs.setAddable(true);
					cs.setDeletable(!cs.isChildren());
					cs.setEditable(true);
					css.add(cs);
				}
			}
		} else {
			Category c = assetFacade.getCategoryByUuid(uuid);
			if (c != null) {
				return gson.toJson(c.makeChildrenClientData());
			}
		}
		return gson.toJson(css);
	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL, logger = true)
	@RequestMapping(value = "/asset/category/node/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String addCategory(@RequestParam String parent_uuid,
			@RequestParam String categoryName, HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Project pro = user.getStubProject();
		Status status = Status.FAILURE;
		String uuid = Tools.getUUID();
		Category category = new Category();
		category.setName(categoryName);
		category.setPinyin(Tools.getPinYin(categoryName));
		category.setUuid(uuid);
		category.setProject(pro);
		category.setUser(user);
		if (pro.getUuid().equals(parent_uuid)) {
			status = assetFacade.addCategory(category);
		} else {
			Category parent = assetFacade.getCategoryByUuid(parent_uuid);
			if (parent == null) {
				status = Status.NOT_FOUND;
			} else {
				if (pro.isSame(parent.getProject())) {
					category.setParent(parent);
					status = assetFacade.addCategory(category);
				} else {
					status = Status.PARAM_ERROR;
				}
			}
		}
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台新增分类", "category", category.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		} else {
			BaseController.bulidRequest("后台新增分类", "category", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return getStatusJson(status);
	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL)
	@RequestMapping(value = "/asset/category/node/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateCategory(@RequestParam String uuid,
			@RequestParam String categoryName, HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Project pro = user.getStubProject();
		Status status = Status.FAILURE;
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null) {
			status = Status.NOT_FOUND;
		} else {
			if (pro.isSame(category.getProject())) {
				category.setName(categoryName);
				category.setPinyin(Tools.getPinYin(categoryName));
				status = assetFacade.updateCategory(category);
			} else {
				status = Status.PARAM_ERROR;
			}
		}
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台分类修改", "category", category.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		} else {
			BaseController.bulidRequest("后台新增修改", "category", null,
					Status.FAILURE.message(), null, "失败", request);
		}

		return getStatusJson(status);
	}

	/**
	 * 2015-9-25修改
	 * 
	 * @param uuid
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/delete/category/node", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String deleteCategory(@RequestParam String uuid,
			HttpServletRequest request) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null) {
			return BaseController.failureModel("分类不存在或不可用").toJSON();
		} else {
			List<Category> list = category.getChildren();
			if (list != null && !list.isEmpty()) {
				return BaseController.failureModel("该分类存在子节点，不能删除").toJSON();
			}
			List<Resource> all = resourceFacade.getResourcesByCateId(category
					.getId());
			if (all != null && !all.isEmpty()) {
				return BaseController.failureModel("该分类存在相关资源信息，不可删除").toJSON();
			}
			category.setIsActive(0);
			Status status = assetFacade.deleteCategory(category);
			if (status == Status.SUCCESS) {
				BaseController.bulidRequest("后台分类删除", "category",
						category.getId(), Status.SUCCESS.message(), null, "成功",
						request);
				return getStatusJson(status);
			} else {
				return BaseController.failureModel("删除分类失败").toJSON();
			}
		}
	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL)
	@RequestMapping(value = "/asset/upload/prepare", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String uploadPrepare(HttpServletRequest request) {

		try {
			System.out.println(letvSupport.videoUploadInit("test"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getStatusJson(Status.SUCCESS);
	}

	@RequestAuthority
	@RequestMapping(value = "/asset/video/info", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String videoInfo(HttpServletRequest request) {
		try {
			return letvSupport.videoGet(9078367);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getStatusJson(Status.FAILURE);
	}

	@RequestMapping(value = "/video/qiniu/uploader/signature", produces = "text/json; charset=utf-8")
	public @ResponseBody String videoQiniu() {
		String token = getQiniuUploaderSignature();
		System.out.println("token:" + token);
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(new SimpleData(
				"uptoken", token));
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/asset/video/upload/{type}", method = RequestMethod.GET)
	public ModelAndView videoUpload(HttpServletRequest request,
			@PathVariable int type) {
		request.setAttribute("video_type", type);
		return index(request, "/wrapped/asset_upload.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/asset/video/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String videoSave(HttpServletRequest request,
			@RequestParam(required = false) String fileId,
			@RequestParam String title,
			@RequestParam(required = false) String qiuniu_key,
			@RequestParam String categoryUuid, @RequestParam int videotype,
			@RequestParam(required = false) boolean need_price,
			@RequestParam(required = false) Float price,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String speaker_uuid) {
		User user = getCurrentUser(request);
		// String valid_token = toastRepeatSubmitToken(request, video_save_key);
		// if (!token.equals(valid_token)) {
		// return BaseAPIController.failureModel("表单重复提交").toJSON();
		// }
		Project project = user.getStubProject();
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !project.isSame(category.getProject())) {
			return BaseAPIController.failureModel("提交参数错误，保存失败").toJSON();
		}
		if (price == null)
			price = .0f;
		if (!need_price) {
			price = -0.001f;
		}
		Status status = Status.SUCCESS;
		Video video = new Video();
		video.setProject(project);
		video.setCategory(category);
		video.setUser(user);
		String uuid = Tools.getUUID();
		video.setUuid(uuid);
		video.setTitle(title);
		video.setStatus(BaseModel.UNAPPROVED);
		video.setPinyin(Tools.getPinYin(title));
		video.setMark(description);
		video.setNeed_price(need_price);
		video.setPrice(price);
		video.setStatus(BaseModel.UNAPPROVED);

		if (videotype == Video.QQ_COMMON) {
			video.setFileId(fileId);
		} else if (videotype == Video.QIUNIU_CLOUD) {
			video.setFileId(qiuniu_key);
			video.setAssetUrl(Config.qn_cdn_host + "/" + qiuniu_key);
		}
		video.setVideoType(videotype);

		status = assetFacade.addVideo(video);
		if (status == Status.SUCCESS && !Tools.isBlank(speaker_uuid)) {
			resourceFacade.recordResourceSpeaker(video,
					assetFacade.getSpeakerByUuid(speaker_uuid));
		}
		if (need_price && status == Status.SUCCESS) {
			// 记录历史价格
			AssetPrice ap = new AssetPrice();
			ap.setVideo(video);
			ap.setPrice(price);
			assetFacade.addAssetPrice(ap);
			BaseController.bulidRequest("后台新建视频信息", "asset", video.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		}
		return BaseAPIController.getStatusJson(status);
	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL, logger = false)
	@RequestMapping(value = "/asset/cc/thqs", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String fetchThqs(HttpServletRequest request) {
		// get query params
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Map<String, String[]> map = request.getParameterMap();
		if (map != null) {
			try {
				Map<String, String> params = new HashMap<String, String>();
				Iterator<String> keys = map.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					String values[] = map.get(key);
					String value = (values == null || values.length < 1 ? ""
							: values[0]);
					params.put(key, value);
				}
				return gson
						.toJson(new Message(CCSparkServerTools.thqs(params)));
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return getStatusJson(Status.FAILURE);
	}

	@RequestAuthority(value = Role.PRO_ADMIN, logger = false)
	@RequestMapping(value = "/asset/cc/video", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String fetchCcVideoData(@RequestParam String uuid,
			HttpServletRequest request) {

		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Video video = assetFacade.getVideoByUUID(uuid);
		if (video == null) {
			return getStatusJson("video not found");
		}
		if (!user.getStubProject().isSame(video.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		CCVideoData data = CCSparkServerTools.requestCCVideoData(video
				.getCcVideoId());
		return gson.toJson(data);

	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL, logger = false)
	@RequestMapping(value = "/asset/video/mgr", method = RequestMethod.GET)
	public ModelAndView videoMgr(HttpServletRequest request) {
		return index(request, "/wrapped/video_mgr.jsp");
	}

	// 查询video列表 modified by mayaun --> 添加categoryUuid字段
	@RequestAuthority(value = Role.PRO_USER_LEVEL)
	@RequestMapping(value = "/asset/videos/datatable", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String fetchVideos(HttpServletRequest request,
			String categoryUuid) {
		User user = getCurrentUser(request);
		VideoClientData cvd = new VideoClientData();
		if (user == null || user.getStubProject() == null) {
			cvd.setStatus(Status.NO_PERMISSION.message());
		} else {
			String q = nullValueFilter(request.getParameter("sSearch"));
			String searchsortIdString = request.getParameter("iSortCol_0");
			String sortValString = request.getParameter("sSortDir_0");// ["desc"]iSortCol_0":["2"]
			String sortvalueString = request.getParameter("mDataProp_"
					+ searchsortIdString);
			Object sizeObj = request.getParameter("iDisplayLength");
			int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
			Object fromObj = request.getParameter("iDisplayStart");
			int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);

			Pagination<Video> pagination = assetFacade.searchVideos(user
					.getStubProject().getId(), q, from, size, sortValString,
					sortvalueString, categoryUuid);

			cvd.setiTotalDisplayRecords(pagination.getTotal());
			cvd.setiTotalRecords(pagination.getPage_rows());
			cvd.buildData(pagination.getResults());
			cvd.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("后台新建视频信息", "asset", null,
					Status.SUCCESS.message(), null, "成功", request);
		}
		return gson.toJson(cvd);
	}

	// 更改视频状态 cc视频接口
	@RequestAuthority(value = Role.PRO_ADMIN, logger = false)
	@RequestMapping(value = "/asset/video/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String changeCCVideoStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Video video = assetFacade.getVideoByUUID(uuid);

		if (video == null) {
			return getStatusJson("video not found");
		}
		if (!user.getStubProject().isSame(video.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		// 如果视频没有经过审核，需要从cc服务器更新数据
		if (video.getStatus() == BaseModel.UNAPPROVED) {
			CCVideoData data = CCSparkServerTools.requestCCVideoData(video
					.getCcVideoId());
			if (!data.videoAvailable()) {
				return getStatusJson("请求视频服务失败...");
			}
			if (data.getVideo() == null) {
				return getStatusJson("视频正在处理中,请稍后再试...");
			}
			CCVideo cc = data.getVideo();
			video.setDuration(cc.getDuration());
			video.setThumbnailSmall(cc.getImage());
			video.setStatus(BaseModel.APPROVED);
		} else {
			// 上线和下线的转换
			video.setStatus(video.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
					: BaseModel.APPROVED);
		}
		Status status = assetFacade.updateVideo(video);
		if (status == Status.SUCCESS) {
			VideoClientItem item = new VideoClientItem();
			item.format(video);
			item.setStatus(Status.SUCCESS);
			return gson.toJson(item);
		}
		return getStatusJson(Status.FAILURE);
	}

	// 腾讯视频接口状态改变
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/asset/t/video/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String changeTencentVideoStatus(
			HttpServletRequest request, @RequestParam String uuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Video video = assetFacade.getVideoByUUID(uuid);
		if (video == null) {
			return getStatusJson("video not found");
		}
		if (!user.getStubProject().isSame(video.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		// 保留原服务的可用性
		if (!StringUtils.isBlank(video.getCcVideoId())) {
			return changeCCVideoStatus(request, uuid);
		}
		// 如果视频没有经过审核，需要从腾讯云服务器更新数据
		if(video.isQiuniu()){
			//七牛
			if (video.getStatus() == BaseModel.UNAPPROVED) {
				//此处去七牛获取视频的信息
				QiniuVideoAvinfo qiniuVideoAvinfo=QiniuUtils.videoAvinfo(video.getAssetUrl());
				if(qiniuVideoAvinfo==null){
					return getStatusJson("请求七牛视频数据失败!");
				}
				float duration_f=Float.valueOf(qiniuVideoAvinfo.getFormat().getDuration());
				int duration=(int)duration_f;
				long size=Long.valueOf(qiniuVideoAvinfo.getFormat().getSize());
				String imageUrl=QiniuUtils.QiniuVideoCover(video.getAssetUrl(), duration);
				if(Tools.isBlank(imageUrl)){
					return getStatusJson("请求七牛视频帧数缩略图失败!");
				}
				video.setDuration(duration);
				video.setSize(size);
				video.setThumbnailSmall(imageUrl);
				video.setStatus(BaseModel.APPROVED);
			}else{
				// 上线和下线的转换
				video.setStatus(video.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
						: BaseModel.APPROVED);
			}
		}else{
			//腾讯云
			if (video.getStatus() == BaseModel.UNAPPROVED) {
				VodInfoData vd = TencentVodServer.fetchVideoInfo(video.getFileId());
				if (vd == null || !vd.isOk() || vd.getFile() == null) {
					return getStatusJson("请求视频服务失败!");
				} else if (!vd.getFile().isOk()) {
					return getStatusJson(vd.getFile().statusMessage());
				}
				// 获取播放地址信息
				VodPlayUrlsData data = TencentVodServer.fetchVideoPlayUrls(video
						.getFileId());
				if (data == null || !data.isOk()) {
					return getStatusJson("获取播放地址失败.");
				}
				VodFile file = vd.getFile();
				video.setDuration(Integer.valueOf(file.getDuration()));
				video.setSize(Long.valueOf(file.getSize()));
				video.setPlays_metainfo(data.getJson_data());
				video.setThumbnailSmall(file.getImageUrl());
				video.setStatus(BaseModel.APPROVED);

				/**
				 * 2016-2-24,新增积分,视频审核，新增积分
				 */
				integralFacade.actionContributeResource(video.getResource());
			} else {
				// 上线和下线的转换
				video.setStatus(video.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
						: BaseModel.APPROVED);
			}
		}
		
		Status status = assetFacade.updateVideo(video);
		if (status == Status.SUCCESS) {
			VideoClientItem item = new VideoClientItem();
			item.format(video);
			item.setStatus(Status.SUCCESS);
			if (status == Status.SUCCESS) {
				BaseController.bulidRequest("后台视频状态转变", "asset", null,
						Status.SUCCESS.message(), null,
						"成功，状态转变为=" + video.getStatus(), request);
			}
			return gson.toJson(item);
		}
		return getStatusJson(Status.FAILURE);
	}

	@RequestAuthority(value = Role.PRO_EDITOR, redirect = false)
	@RequestMapping(value = "/asset/video/{uuid}/detail", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String videoDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Video video = assetFacade.getVideoByUUID(uuid);
		if (video == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		if (!user.getStubProject().isSame(video.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		VideoClientItem ci = new VideoClientItem();
		ci.format(video);
		ci.setStatus(Status.SUCCESS);
		BaseController.bulidRequest("后台查看视频详情", "asset", video.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(ci);
	}

	// @RequestAuthority(value = Role.PRO_EDITOR )
	@RequestAuthority(value = Role.PRO_EDITOR, redirect = false)
	@RequestMapping(value = "/asset/video/{uuid}/update", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String videoupdate(HttpServletRequest request,
			@PathVariable(value = "uuid") String uuid,
			@RequestParam String cover, @RequestParam String title,
			@RequestParam(required = false) String categoryUuid,
			@RequestParam(required = false) String mark,
			@RequestParam(required = true) boolean need_price,
			@RequestParam(required = false) Float price,
			@RequestParam(required = false) String speaker_uuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return BaseController.failureModel(Status.NO_PERMISSION.message())
					.toJSON();
		Video video = assetFacade.getVideoByUUID(uuid);
		if (video == null) {
			return BaseController.failureModel(Status.NOT_FOUND.message())
					.toJSON();
		}
		if (!user.getStubProject().isSame(video.getProject())) {
			return BaseController.failureModel(Status.NO_PERMISSION.message())
					.toJSON();
		}
		if (StringUtils.isNoneEmpty(categoryUuid)) {
			video.setCategory(assetFacade.getCategoryByUuid(categoryUuid));
		}
		video.setThumbnailSmall(cover);
		video.setTitle(title);
		video.setPinyin(Tools.getPinYin(title));
		video.setMark(mark);
		video.setNeed_price(need_price);
		if (price == null)
			price = .0f;
		if (need_price) {
			if (price < 0) {
				return BaseController.failureModel("价格不能小于0").toJSON();
			}
			video.setPrice(price);
		} else {
			price = -0.001f;
		}
		if (!Tools.isBlank(speaker_uuid)) {
			resourceFacade.recordResourceSpeaker(video,
					assetFacade.getSpeakerByUuid(speaker_uuid));
		}
		Status status = assetFacade.updateVideo(video);
		if (status == Status.SUCCESS) {
			VideoClientItem ci = new VideoClientItem();
			Video client_video = assetFacade.getVideoByUUID(uuid);
			ci.format(client_video);
			BaseController.bulidRequest("后台视频信息更新", "asset", video.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return ci.toJSON();
		}
		return BaseController.failureModel("修改失败").toJSON();
	}

	/**
	 * 更改视频分类
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, redirect = false, logger = false)
	@RequestMapping(value = "/asset/video/{uuid}/category/update", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String videoCategoryUpdate(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam String categoryUuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Video video = assetFacade.getVideoByUUID(uuid);
		if (video == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		if (!user.getStubProject().isSame(video.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null)
			return getStatusJson(Status.NOT_FOUND);
		video.setCategory(category);
		Status status = assetFacade.updateVideo(video);
		if (status == Status.SUCCESS) {
			VideoClientItem ci = new VideoClientItem();
			ci.format(assetFacade.getVideoByUUID(uuid));
			return gson.toJson(ci);
		}
		return getStatusJson(status);
	}

	/**
	 * 通过uuid找到根节点的uuid
	 * 
	 * @author Administrator
	 *
	 */
	@RequestAuthority(value = Role.PRO_USER_LEVEL, logger = false)
	@RequestMapping(value = "/search/parents/{uuid}/root", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String searchRootUuid(HttpServletRequest request,
			@PathVariable String uuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null)
			return getStatusJson(Status.NOT_FOUND);
		String list = "";
		Category parent = category.getParent();
		while (parent != null) {
			if (list.isEmpty()) {
				list = parent.getUuid();
			} else {
				list = parent.getUuid() + ":" + list;
			}
			parent = parent.getParent();
		}
		list = "{\"list\":\"" + list + "\"}";
		return list;
	}

	private class Message {
		String message;

		public Message(String message) {
			this.message = message;
		}
	}

	private List<Video> addVoidList(List<Video> list, Category category,
			int loop) {
		if (loop > max_loop)
			return list;
		List<Video> currentList = apiFacade.getVideoByCategoryId(category
				.getProject().getId(), category.getId());
		if (currentList != null && currentList.size() > 0) {
			list.addAll(currentList);
		}
		List<Category> children = category.getChildren();
		if (children == null || children.size() == 0) {
			return list;
		} else {
			for (Category model : children) {
				list = addVoidList(list, model, loop + 1);
			}
			return list;
		}
	}

	private static int max_loop = 3;

	@RequestMapping(value = "/project/qq/secret")
	public @ResponseBody String getQQSecret(HttpServletRequest request) {
		QQSecret qqSecret = new QQSecret(Config.qq_secretId, "");
		return gson.toJson(qqSecret);
	}

	@RequestMapping(value = "/project/qq/signature")
	public @ResponseBody String getSignature(HttpServletRequest request,
			@RequestParam String args) {
		String signature = Signature.getSignatureVodOfJs(Config.qq_secretKey,
				args);
		return signature == null ? "" : signature;
	}

	/**
	 * 获取种类的分类信息
	 */
	@Deprecated
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/category/relate/cover/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String relateCover(HttpServletRequest request,
			@PathVariable String uuid) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null || !category.isActive()) {
			return BaseController.failureModel("分类不存在或不可用").toJSON();
		}
		CategoryExpand categoryExpand = mapper.selectExpandByCateId(category
				.getId());
		CategoryExpandVo vo = new CategoryExpandVo();
		vo.build(categoryExpand);
		vo.setStatus(Status.SUCCESS);
		return vo.toJSON();
	}

	/**
	 * @author mayuan
	 * @createDate 2016年5月25日
	 * @modifyDate 2016年5月25日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/category/relate/cover_new/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String relateCoverNew(HttpServletRequest request,
			@PathVariable String uuid) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null || !category.isActive()) {
			return BaseController.failureModel("分类不存在或不可用").toJSON();
		}
		List<MediaCentral> mediaCentrals = mediaCentralFacade
				.getCategoryMedias(category);
		MediaCentralVo vo = new MediaCentralVo();
		vo.build(mediaCentrals);
		vo.setStatus(Status.SUCCESS);
		return vo.toJSON();
	}

	/**
	 * 保存分类信息
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@Deprecated
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/category/expand/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String cateSaveCover(HttpServletRequest request,
			@RequestParam Integer typeId, @RequestParam String categoryUuid,
			@RequestParam String taskId) {
		CategoryExpandVo vo = new CategoryExpandVo();
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive()) {
			return BaseController.failureModel("分类不存在或不可用").toJSON();
		}
		CategoryExpand categoryExpand = mapper.selectExpandByCateId(category
				.getId());
		try {
			if (categoryExpand != null) {
				// 更新
				if (typeId == CategoryExpandStatus.WX_PLATFORM) {
					categoryExpand.setWxTaskId(taskId);
				} else if (typeId == CategoryExpandStatus.APP_PLATFORM) {
					categoryExpand.setAppTaskId(taskId);
				} else if (typeId == CategoryExpandStatus.TV_PLATFORM) {
					categoryExpand.setTvTaskId(taskId);
				} else if (typeId == CategoryExpandStatus.WEB_PLATFORM) {
					categoryExpand.setWebTaskId(taskId);
				}
				mapper.updateExpand(categoryExpand);
			} else {
				// 新增
				String m_uuid = Tools.getUUID();
				categoryExpand = new CategoryExpand();
				categoryExpand.setUuid(m_uuid);
				categoryExpand.setAffect(0);
				categoryExpand.setByname("");
				categoryExpand.setCategory(category);
				categoryExpand.setIsActive(1);
				categoryExpand.setStatus(1);
				categoryExpand.setMark("");
				if (typeId == CategoryExpandStatus.WX_PLATFORM) {
					categoryExpand.setWxTaskId(taskId);
				} else if (typeId == CategoryExpandStatus.APP_PLATFORM) {
					categoryExpand.setAppTaskId(taskId);
				} else if (typeId == CategoryExpandStatus.TV_PLATFORM) {
					categoryExpand.setTvTaskId(taskId);
				} else if (typeId == CategoryExpandStatus.WEB_PLATFORM) {
					categoryExpand.setWebTaskId(taskId);
				}
				mapper.addExpand(categoryExpand);
			}
			// 返回数据
			vo.setStatus(Status.SUCCESS);
			CategoryExpand m_catCategoryExpand = mapper
					.selectExpandByCateId(category.getId());
			vo.build(m_catCategoryExpand);
			return vo.toJSON();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return BaseController.failureModel("数据保存失败").toJSON();
		}
	}

	/**
	 * @author mayuan
	 * @createDate 2016年5月25日
	 * @modifyDate 2016年5月25日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/category/cover/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String cateSaveCoverNew(HttpServletRequest request,
			@RequestParam Integer typeId, @RequestParam String categoryUuid,
			@RequestParam(required = false) String taskId,
			@RequestParam(required = false) String cssValue) {
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive()) {
			return BaseController.failureModel("分类不存在或不可用").toJSON();
		}
		if (null != typeId && StringUtils.isNotEmpty(categoryUuid)) {
			try {
				mediaCentralFacade.saveCategoryMedia(category, typeId, taskId,
						cssValue);
			} catch (Exception e) {
				e.printStackTrace();
				return BaseController.failureModel("数据保存失败").toJSON();
			}
		} else {
			return BaseController.failureModel("缺少必要参数").toJSON();
		}
		List<MediaCentral> mediaCentrals = mediaCentralFacade
				.getCategoryMedias(category);
		MediaCentralVo vo = new MediaCentralVo();
		vo.build(mediaCentrals);
		vo.setStatus(Status.SUCCESS);
		return vo.toJSON();
	}

	// speaker page
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/project/speaker/mgr")
	public ModelAndView speakerListPage(HttpServletRequest request) {
		return index(request, "/wrapped/speaker/list.jsp");
	}

	private static final String SPEAKER_SUBMIT_KEY = "speaker_submit_key";

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/project/speaker/new")
	public ModelAndView speakerNew(HttpServletRequest request,
			@RequestParam(required = false) String speaker_uuid) {
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		CommonAppItemVo app = new CommonAppItemVo();
		app.setStatus(Status.SUCCESS);
		app.buildProvince(list);
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.setAttribute("province_list", app.toJSON());
		List<Departments> departments = hospitalMapper.selectDePatList();
		CommonAppItemVo app_de = new CommonAppItemVo();
		app_de.setStatus(Status.SUCCESS);
		app_de.buildDepartments(departments);
		request.getSession().setAttribute(SPEAKER_SUBMIT_KEY, token);
		request.setAttribute("department_list", app_de.toJSON());
		if (!Tools.isBlank(speaker_uuid)) {
			Speaker speaker = assetFacade.getSpeakerByUuid(speaker_uuid);
			SpeakerSufaceVo speakerVo = new SpeakerSufaceVo();
			speakerVo.buildData(speaker);
			// speakerVo.buildProvince(list);
			// speakerVo.buildDepartments(departments);
			if (speaker != null) {
				Hospital hospital = speaker.getHospital();
				if (hospital != null) {
					Province province = hospital.getProvince();
					if (province != null) {
						List<City> cityList = EphemeralData.getIntansce(
								provinceMapper).getCitys(province);
						speakerVo.buildCity(cityList);
					}
					City city = hospital.getCity();
					if (city != null) {
						List<Hospital> hosList = hospitalMapper
								.selectHosListByCityId(city.getId());
						speakerVo.buildHospital(hosList);
					}
				}
			}
			request.setAttribute("speaker", speakerVo);
		}
		return index(request, "/wrapped/speaker/new_or_update.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/project/speaker/{uuid}/edit")
	public ModelAndView speakerEdit(HttpServletRequest request,
			@PathVariable String uuid) {
		return speakerNew(request, uuid);
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/speaker/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveSpeaker(@RequestParam String token,
			@RequestParam String name, @RequestParam int sex,
			@RequestParam(required = false) String mobile,
			@RequestParam String hospital_uuid,
			@RequestParam String department_uuid,
			@RequestParam(required = false) String position,
			@RequestParam(required = false) String resume,
			@RequestParam(required = false) String speaker_uuid,
			@RequestParam(required = false) String avatar,
			HttpServletRequest request) {
		String safe = toastRepeatSubmitToken(request, SPEAKER_SUBMIT_KEY);
		if (!token.equals(safe)) {
			return BaseAPIController.failureModel("提交失败，页面过期或者重复提交").toJSON();
		}
		Speaker speaker;
		if (!Tools.isBlank(speaker_uuid)) {
			speaker = assetFacade.getSpeakerByUuid(speaker_uuid);
			if (speaker == null) {
				return BaseAPIController.failureModel("更新的讲者不存在").toJSON();
			}
			speaker.setName(name);
			speaker.setPinyin(Tools.getPinYin(name));
			speaker.setMobile(mobile);
			speaker.setSex(sex);
			speaker.setResume(resume);
			speaker.setPosition(position);
			speaker.setHospital(hospitalMapper
					.selectHospitalByUUid(hospital_uuid));
			speaker.setDepartment(hospitalMapper
					.selectDePatByUuid(department_uuid));
			speaker.setAvatar(avatar);
			return BaseAPIController.getStatusJson(assetFacade
					.updateSpeaker(speaker));
		}
		speaker = new Speaker();
		speaker.setUuid(Tools.getUUID());
		speaker.setName(name);
		speaker.setPinyin(Tools.getPinYin(name));
		speaker.setMobile(mobile);
		speaker.setSex(sex);
		speaker.setResume(resume);
		speaker.setPosition(position);
		speaker.setHospital(hospitalMapper.selectHospitalByUUid(hospital_uuid));
		speaker.setDepartment(hospitalMapper.selectDePatByUuid(department_uuid));
		speaker.setStatus(BaseModel.UNAPPROVED);
		speaker.setIsActive(1);
		speaker.setAvatar(avatar);
		return BaseAPIController.getStatusJson(assetFacade.addSpeaker(speaker));
	}

	// 查询讲者列表
	@RequestAuthority(value = Role.PRO_USER_LEVEL)
	@RequestMapping(value = "/project/speakers/datatable", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String fetchSpeakers(HttpServletRequest request) {
		User user = getCurrentUser(request);
		SpeakerDataTable cvd = new SpeakerDataTable();
		if (user == null || user.getStubProject() == null) {
			cvd.setStatus(Status.NO_PERMISSION.message());
		} else {
			String q = nullValueFilter(request.getParameter("sSearch"));
			Object sizeObj = request.getParameter("iDisplayLength");
			int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
			Object fromObj = request.getParameter("iDisplayStart");
			int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
			Pagination<Speaker> pagination = assetFacade
					.searchAllSpeakersPaginatio(from, size, q);
			List<Speaker> speakers = pagination.getResults();
			if (speakers != null) {
				for (int i = 0; i < speakers.size(); i++) {
					SpeakerVo vo = new SpeakerVo();
					vo.build(speakers.get(i));
					cvd.addItem(vo);
				}
			}
			cvd.setiTotalDisplayRecords(pagination.getTotal());
			cvd.setiTotalRecords(pagination.getPage_rows());
			cvd.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("后台查看讲者列表", "speaker", null,
					Status.SUCCESS.message(), null, "成功", request);
		}
		return cvd.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, redirect = false, logger = false)
	@RequestMapping(value = "/user/speakers/json", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String fetchVideoSpeaker() {
		List<SpeakerVo> speakers = assetFacade.fetchAllSimpleSpeakers();
		return BaseAPIModel.makeWrappedSuccessDataJson(speakers);
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/speaker/status/change", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateSpeakerStatus(HttpServletRequest request,
			String uuid) {
		Speaker speaker = assetFacade.getSpeakerByUuid(uuid);
		if (speaker == null || !speaker.isActive()) {
			BaseController.bulidRequest("后台讲者状态更改", "speaker", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 此讲者不存在",
					request);
			return failureModel("此讲者信息已不存在").toJSON();
		}
		// 上下线转换
		speaker.setStatus(speaker.getStatus() == BaseModel.UNAPPROVED ? BaseModel.APPROVED
				: BaseModel.UNAPPROVED);
		Status status = assetFacade.updateSpeakerStatus(speaker);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台讲者状态更改", "speaker", speaker.getId(),
					Status.SUCCESS.message(), null,
					"成功，转换为=" + speaker.getStatus(), request);
			SpeakerVo vo = new SpeakerVo();
			vo.setStatus(status);
			vo.build(speaker);
			return vo.toJSON();
		} else {
			BaseController.bulidRequest("后台讲者状态更改", "speaker", speaker.getId(),
					Status.FAILURE.message(), null,
					"失败，转换为=" + speaker.getStatus(), request);
		}
		return failureModel("更新失败").toJSON();
	}

	// 根据视频分类查询视频列表
	@RequestAuthority(value = Role.PRO_USER_LEVEL, logger = false)
	@RequestMapping(value = "/asset/videos/datatable/{categoryUuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String clickButtonSearch(HttpServletRequest request,
			@PathVariable("categoryUuid") String categoryUuid) {
		// System.out.println(categoryUuid);
		return this.fetchVideos(request, categoryUuid);
	}

	// 跳转到视频更新页面
	@RequestAuthority(value = Role.PRO_USER_LEVEL)
	@RequestMapping(value = "/asset/videos/jumpUpdataPage/{vodeoUuid}")
	public ModelAndView updateVideoPage(HttpServletRequest request,
			@PathVariable String vodeoUuid) {
		Video video = assetFacade.getVideoByUUID(vodeoUuid);
		if (video == null || !video.isActive()) {
			request.setAttribute("video_error", "video信息未找到");
			return this.videoMgr(request);
		} else {
			// 更新页面回显讲者信息
			List<Speaker> list = null;
			Resource resource = video.getResource();
			if (resource != null) {
				Speaker speaker = resource.getSpeaker();
				if (speaker != null) {
					list = new ArrayList<Speaker>();
					list.add(speaker);
				}
			}
			List<OptionAddition> speakerList = OptionAddition.buildSpaker(list);
			request.setAttribute("spaker_list", speakerList);

			/*
			 * String token = Tools.getUUID(); request.setAttribute("token",
			 * token); request.getSession().setAttribute(video_save_key, token);
			 */

			VideoClientItem videoVo = new VideoClientItem();
			videoVo.format(video);
			request.setAttribute("video_info", videoVo);
			request.setAttribute("speaker", videoVo.getSpeaker());
			BaseController.bulidRequest("后台查看视频详情", "asset", video.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return index(request, "/wrapped/asset_update.jsp");
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/speaker/userClean", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateSpeaker(HttpServletRequest request,
			@RequestParam String speakerUuid) {
		Speaker speaker = assetFacade.getSpeakerByUuid(speakerUuid);
		if (speaker == null || !speaker.isActive()) {
			return failureModel("此讲者信息已不存在").toJSON();
		}
		speaker.setUser(null);
		Status status = assetFacade.updateSpeakerCleanUser(speaker);
		if (status == Status.SUCCESS) {
			SpeakerVo vo = new SpeakerVo();
			vo.setStatus(status);
			vo.build(speaker);
			return vo.toJSON();
		}
		return failureModel("解除关联失败，请重试").toJSON();
	}

	// 加载省市下拉列表
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/getProvinceList", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getProvinceList(HttpServletRequest request) {
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		if (null != list) {
			CommonAppItemVo app = new CommonAppItemVo();
			app.buildProvince(list);
			app.setStatus(Status.SUCCESS);
			return app.toJSON();
		}
		return failureModel("出错啦，请稍后重试").toJSON();
	}

	// 加载科室下拉列表
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/getDepartList", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getDepartList(HttpServletRequest request) {
		List<Departments> departments = hospitalMapper.selectDePatList();
		if (null != departments) {
			CommonAppItemVo app_de = new CommonAppItemVo();
			app_de.setStatus(Status.SUCCESS);
			app_de.buildDepartments(departments);
			return app_de.toJSON();
		}
		return failureModel("出错啦，请稍后重试").toJSON();
	}

	// 加载厂商下拉列表
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/getManufacturerList", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getManufacturerList(HttpServletRequest request) {
		List<Manufacturer> manufacturers = groupFacade.selectManufacturer();
		if (null != manufacturers) {
			CommonAppItemVo data = new CommonAppItemVo();
			data.setStatus(Status.SUCCESS);
			data.buildManufacturers(manufacturers);
			return data.toJSON();
		}
		return failureModel("出错啦，请稍后重试").toJSON();
	}

}
