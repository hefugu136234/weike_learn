package com.lankr.tv_cloud.web;

import java.lang.reflect.Type;
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

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.ThreeRelation;
import com.lankr.tv_cloud.model.ThreeScreen;
import com.lankr.tv_cloud.support.TencentVodServer;
import com.lankr.tv_cloud.support.VodFile;
import com.lankr.tv_cloud.support.VodInfoData;
import com.lankr.tv_cloud.support.VodPlayUrlsData;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.DynamicSearchVo;
import com.lankr.tv_cloud.vo.OptionAddition;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.vo.ThreeScreenSurface;
import com.lankr.tv_cloud.vo.ThreeScreenVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping("/project/threescreen")
public class ThreescreenController extends AdminWebController {

	private static final String ADD_THREE_KEY = "add_three_key";

	private static final String UPDATE_THREE_KEY = "update_three_key";

	private static final String SANFEN_RELATION = "sanfen_relation";

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		return index(request, "/wrapped/threescreen/threescreen_list.jsp");
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
		Pagination<ThreeScreen> pagination = threeScreenFacade
				.selectThreeScreenList(searchValue, from, pageItemTotal);
		ThreeScreenSurface suface = new ThreeScreenSurface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.bulid(pagination.getResults());
		BaseController.bulidRequest("后台查看三分屏列表", "three_screen", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		ThreeScreen threeScreen = threeScreenFacade
				.selectThreeScreenByUuid(uuid);
		if (threeScreen == null || !threeScreen.isActive()) {
			return BaseController.failureModel("三分屏信息未找到或不可用").toJSON();
		}
		// 首先判断是否是未审核状态
		if (threeScreen.getStatus() == BaseModel.UNAPPROVED) {
			// 更新云服务器视频和pdf
			try {
				VodInfoData vd = TencentVodServer.fetchVideoInfo(threeScreen
						.getFileId());
				if (vd == null || !vd.isOk() || vd.getFile() == null) {
					return BaseController.failureModel("请求视频服务失败!").toJSON();
				} else if (!vd.getFile().isOk()) {
					return BaseController.failureModel(
							vd.getFile().statusMessage()).toJSON();
				}

				// 获取播放地址信息
				VodPlayUrlsData data = TencentVodServer
						.fetchVideoPlayUrls(threeScreen.getFileId());
				if (data == null || !data.isOk()) {
					return BaseController.failureModel("获取播放地址失败.").toJSON();
				}
				ActionMessage message = threeScreenFacade.addPlaysInfo(
						threeScreen, data.getJson_data());
				if (!message.isSuccess()) {
					return BaseController.failureModel("获取播放地址失败.").toJSON();
				}
				VodFile file = vd.getFile();
				threeScreen.setVideoTime(Integer.valueOf(file.getDuration()));
				threeScreen.setVideoCover(file.getImageUrl());
				String url = Config.qn_cdn_host + "/"
						+ threeScreen.getPdfTaskId() + "?odconv/jpg/info";
				String json = HttpUtils.sendGetRequest(url);
				if (json == null || json.isEmpty()) {
					return BaseController.failureModel("请求PDF服务失败!").toJSON();
				}
				JSONObject dataObject = new JSONObject(json);
				int page = dataObject.getInt("page_num");
				threeScreen.setPdfNum(page);
				threeScreen.setStatus(BaseModel.UNDERLINE);
				/**
				 * 2016-2-24,新增积分,视频审核，新增积分
				 */
				integralFacade.actionContributeResource(threeScreen.getResource());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return BaseController.failureModel("文件服务转换失败，请稍后再试!").toJSON();
			}
		} else {
			String division = threeScreen.getDivision();
			if (division == null || division.isEmpty()) {
				return BaseController.failureModel("三分屏对应关系还未编辑，不能上线").toJSON();
			}
			threeScreen
					.setStatus(threeScreen.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
							: BaseModel.APPROVED);
		}
		Status status = threeScreenFacade.updateThreeScreenStatus(threeScreen);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台三分屏的状态转换", "three_screen",
					threeScreen.getId(), Status.SUCCESS.message(), null,
					"成功,状态转变为=" + threeScreen.getStatus(), request);
			ThreeScreenVo vo = new ThreeScreenVo();
			vo.setStatus(status);
			vo.build(threeScreen);
			return vo.toJSON();
		}
		return BaseController.failureModel("状态更新失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/page/add")
	public ModelAndView addPage(HttpServletRequest request) {
		List<Speaker> list = speakerMapper.searchSpeakerList();
		List<OptionAddition> addList = OptionAddition.buildSpaker(list);
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.setAttribute("signature", getQiniuUploaderSignature());
		request.setAttribute("spaker_list", addList);
		request.getSession().setAttribute(ADD_THREE_KEY, token);
		return index(request, "/wrapped/threescreen/threescreen_add.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/save/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String saveData(HttpServletRequest request,
			@RequestParam String token, @RequestParam String name,
			@RequestParam String categoryUuid, @RequestParam String pdfTaskId,
			@RequestParam String fileId, @RequestParam String cover,
			@RequestParam(required = false) String spaker_selector,
			@RequestParam(required = false) String division,
			@RequestParam(required = false) String mark) {
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive()) {
			return BaseController.failureModel("分类不可用").toJSON();
		}
		
		Speaker speaker=null;
		if (spaker_selector != null && !spaker_selector.equals("0")) {
			speaker = speakerMapper.getSpeakerByUuid(spaker_selector);
			if (speaker == null || !speaker.isActive()) {
				return BaseController.failureModel("讲者不可用").toJSON();
			}
		}
		
		String repeat_token = toastRepeatSubmitToken(request, ADD_THREE_KEY);
		if (!token.equals(repeat_token)) {
			return BaseController.failureModel("请勿重复提交数据").toJSON();
		}
		String uuid = Tools.getUUID();
		ThreeScreen threeScreen = new ThreeScreen();
		threeScreen.setUuid(uuid);
		threeScreen.setName(name);
		threeScreen.setPinyin(Tools.getPinYin(name));
		threeScreen.setCategory(category);
		threeScreen.setFileId(fileId);
		threeScreen.setPdfTaskId(pdfTaskId);
		threeScreen.setCoverTaskId(cover);
		threeScreen.setDivision(division);
		threeScreen.setMark(mark);
		threeScreen.setIsActive(BaseModel.ACTIVE);
		threeScreen.setStatus(BaseModel.UNAPPROVED);
		threeScreen.setSpeaker(speaker);
		Status status = threeScreenFacade.addThreeScreen(threeScreen);
		if (status == Status.SUCCESS) {
			// 更新讲者
			resourceFacade.recordResourceSpeaker(threeScreen, speaker);
			BaseController.bulidRequest("后台三分屏新增", "three_screen",
					threeScreen.getId(), Status.SUCCESS.message(), null, "成功,",
					request);
			return BaseController.successModel().toJSON();
		}
		return BaseController.failureModel("数据保存失败").toJSON();

	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping("/update/page/{uuid}")
	public ModelAndView updatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		ThreeScreen threeScreen = threeScreenFacade
				.selectThreeScreenByUuid(uuid);
		if (threeScreen == null || !threeScreen.isActive()) {
			request.setAttribute("threeScreen_error", "三分屏信息未找到或不可用");
			return listPage(request);
		} else {
			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.setAttribute("signature", getQiniuUploaderSignature());
			request.getSession().setAttribute(UPDATE_THREE_KEY, token);
			List<Speaker> list = null;
			Resource resource = threeScreen.getResource();
			if (resource != null) {
				Speaker speaker = resource.getSpeaker();
				if (speaker != null) {
					list = new ArrayList<Speaker>();
					list.add(speaker);
				}
			}
			List<OptionAddition> addList = OptionAddition.buildSpaker(list);
			ThreeScreenVo data = new ThreeScreenVo();
			data.build(threeScreen);
			request.setAttribute("threeScreen_info", data);
			request.setAttribute("spaker_list", addList);
			BaseController.bulidRequest("后台三分屏查看详情", "three_screen",
					threeScreen.getId(), Status.SUCCESS.message(), null, "成功,",
					request);
			return index(request, "/wrapped/threescreen/threescreen_update.jsp");
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateData(HttpServletRequest request,
			@RequestParam String token, @RequestParam String uuid,
			@RequestParam String name, @RequestParam String categoryUuid,
			@RequestParam(required = false) String spaker_selector, @RequestParam String cover,
			@RequestParam(required = false) String division,
			@RequestParam(required = false) String mark) {
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive()) {
			return BaseController.failureModel("分类不可用").toJSON();
		}
		
		String server_token = toastRepeatSubmitToken(request, UPDATE_THREE_KEY);
		if (!token.equals(server_token)) {
			return BaseController.failureModel("请勿重复提交数据").toJSON();
		}
		ThreeScreen threeScreen = threeScreenFacade
				.selectThreeScreenByUuid(uuid);
		if (threeScreen == null || !threeScreen.isActive())
			return BaseController.failureModel("三分屏信息未找到或不可用").toJSON();
		Speaker speaker = null;
		if (spaker_selector != null && !spaker_selector.equals("0")) {
			speaker=speakerMapper.getSpeakerByUuid(spaker_selector);
			if (speaker == null || !speaker.isActive()) {
				return BaseController.failureModel("讲者不可用").toJSON();
			}
		}
		threeScreen.setName(name);
		threeScreen.setPinyin(Tools.getPinYin(name));
		threeScreen.setCategory(category);
		threeScreen.setCoverTaskId(cover);
		threeScreen.setMark(mark);
		Status status = threeScreenFacade.updateThreeScreen(threeScreen);
		if (status == Status.SUCCESS) {
			// 更新讲者
			resourceFacade.recordResourceSpeaker(threeScreen, speaker);
			BaseController.bulidRequest("后台三分屏信息修改", "three_screen",
					threeScreen.getId(), Status.SUCCESS.message(), null, "成功,",
					request);
			return BaseController.successModel().toJSON();
		}
		return BaseController.failureModel("三分屏信息修改失败").toJSON();
	}

	/**
	 * 搜索讲者
	 * 2016-05-16 modified by kalean
	 */
	
	@RequestMapping(value = "/search/speaker", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String searchManufacturer(HttpServletRequest request,
			@RequestParam(required = false) String q) {
		List<Speaker> list = speakerMapper.searchSpeakerListByQ(q);
//		OptionAdditionList model = new OptionAdditionList();
//		model.buildSpeak(list);
		DynamicSearchVo vo = DynamicSearchVo.buildSpeakers(list, q);
		return vo.toJSON();
	}

	// 对应关系页面
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/congruent/page/{uuid}")
	public ModelAndView congruentPage(HttpServletRequest request,
			@PathVariable String uuid) {
		ThreeScreen threeScreen = threeScreenFacade
				.selectThreeScreenByUuid(uuid);
		if (threeScreen == null || !threeScreen.isActive()) {
			request.setAttribute("threeScreen_error", "三分屏信息未找到或不可用");
			return listPage(request);
		} else {
			ThreeScreenVo data = new ThreeScreenVo();
			data.build(threeScreen);
			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.getSession().setAttribute(SANFEN_RELATION, token);
			request.setAttribute("threeScreen_info", data);
			return index(request, "/wrapped/threescreen/threescreen_relate.jsp");
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/relation/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String relationUpdate(HttpServletRequest request,
			@RequestParam String token, @RequestParam String uuid,
			@RequestParam String jsonData) {
		ThreeScreen threeScreen = threeScreenFacade
				.selectThreeScreenByUuid(uuid);
		if (threeScreen == null || !threeScreen.isActive())
			return BaseController.failureModel("三分屏信息未找到或不可用").toJSON();
		List<ThreeRelation> mList = null;
		try {
			Type type = new TypeToken<ArrayList<ThreeRelation>>() {
			}.getType();
			mList = gson.fromJson(jsonData, type);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return BaseController.failureModel("三分屏对应关系格式有错误").toJSON();
		}
		if (mList == null || mList.isEmpty())
			return BaseController.failureModel("请添加三分屏对应关系").toJSON();
		String server_token = toastRepeatSubmitToken(request, SANFEN_RELATION);
		if (!token.equals(server_token)) {
			return BaseController.failureModel("请勿重复提交数据").toJSON();
		}
		threeScreen.setDivision(jsonData);
		Status status = threeScreenFacade
				.updateThreeScreenRelation(threeScreen);
		if (status == Status.SUCCESS) {
			// 更新讲者
			BaseController.bulidRequest("后台三分屏对应关系修改", "three_screen",
					threeScreen.getId(), Status.SUCCESS.message(), null, "成功,",
					request);
			return BaseController.successModel().toJSON();
		}
		return BaseController.failureModel("三分屏对应关系信息修改失败").toJSON();
	}

}
