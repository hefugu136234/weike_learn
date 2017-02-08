package com.lankr.tv_cloud.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.NewsData;
import com.lankr.tv_cloud.vo.NewsSuface;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
public class NewsController extends AdminWebController {

	// @Autowired
	// private ProvinceMapper mapper;

	public final static String NESSAVEKEY = "newsSave";

	public final static String NEWSUPDATEKEY = "news_update_key";

	@RequestMapping(value = "/project/news/list/page")
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	public ModelAndView loadNewsList(HttpServletRequest request) {
		return index(request, "/wrapped/news/news_list.jsp");
	}

	@RequestMapping(value = "/project/news/list/data")
	@RequestAuthority(value = Role.PRO_EDITOR)
	public @ResponseBody String loadNewsData(HttpServletRequest request) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<NewsInfo> pagination = newsFacade.selectInfoList(
				searchValue, from, pageItemTotal);
		NewsSuface suface = new NewsSuface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.buildData(pagination.getResults());
		BaseController.bulidRequest("后台查看新闻列表", "news_info", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	@RequestMapping(value = "/project/news/add/page")
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	public ModelAndView loadAddNewsPage(HttpServletRequest request) {
		// if (status != null)
		// request.setAttribute("news_status", status.message());
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(NESSAVEKEY, token);
		return index(request, "/wrapped/news/news_add.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/news/add/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String newsSave(HttpServletRequest request,
			@RequestParam String token, @RequestParam String title,
			@RequestParam String content, @RequestParam String label,
			@RequestParam String summary, @RequestParam String author,
			@RequestParam String qrTaskId, @RequestParam String categoryUuid) {
		Category cate = assetFacade.getCategoryByUuid(categoryUuid);
		if (cate == null || !cate.isActive()) {
			BaseController.bulidRequest("后台新增新闻", "news_info", null,
					Status.FAILURE.message(), null, "种类uuid=" + categoryUuid
							+ " 不可用", request);
			return getStatusJson("分类不可用");
		}
		if (content.isEmpty()) {
			return getStatusJson("新闻内容不能为空");
		}
		String server_token = toastRepeatSubmitToken(request, NESSAVEKEY);
		Status status = Status.SUCCESS;
		if (!token.equals(server_token)) {
			status = Status.SUBMIT_REPEAT;
		} else {
			NewsInfo info = new NewsInfo();
			info.setUuid(Tools.getUUID());
			info.setTitle(title);
			info.setAuthor(author);
			info.setSummary(summary);
			info.setLabel(label);
			info.setContent(content);
			info.setStatus(BaseModel.UNAPPROVED);
			//info.setStatus(BaseModel.UNDERLINE);//20160122 XiaoMa
			info.setIsActive(1);
			info.setQrTaskId(qrTaskId);
			info.setCategory(cate);
			status = newsFacade.addNewsInfo(info);
			if (status == Status.SUCCESS) {
				BaseController.bulidRequest("后台新增新闻", "news_info",
						info.getId(), Status.SUCCESS.message(), null, "成功",
						request);
			} else {
				BaseController.bulidRequest("后台新增新闻", "news_info", null,
						Status.FAILURE.message(), null, "保存失败", request);
			}
		}
		return getStatusJson(status);

	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/news/change/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String changeNewsStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		NewsInfo info = newsFacade.selectInfoByUuid(uuid);
		if (info == null) {
			BaseController.bulidRequest("后台查新闻状态更改", "news_info", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 此新闻不可用",
					request);
			return getStatusJson(Status.NOT_FOUND);
		}
		if(info.getStatus()==BaseModel.UNAPPROVED){
			info.setStatus(BaseModel.APPROVED);
		}
		// 上下线转换
		info.setStatus(info.getStatus() == BaseModel.UNDERLINE ? BaseModel.APPROVED
				: BaseModel.UNDERLINE);
		Status status = newsFacade.updateNewInfoStatus(info);
		if (status == Status.SUCCESS) {
			NewsData data = new NewsData();
			data.buildData(info,true);
			BaseController.bulidRequest("后台新闻状态更改", "news_info", info.getId(),
					Status.SUCCESS.message(), null,
					"成功，状态变更为=" + info.getStatus(), request);
			return gson.toJson(data);
		} else {
			BaseController.bulidRequest("后台新闻状态更改", "news_info", info.getId(),
					Status.FAILURE.message(), null,
					"失败，状态变更为=" + info.getStatus(), request);
		}
		return getStatusJson(Status.FAILURE);
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/news/update/page/{uuid}")
	public ModelAndView loadNewsUpdatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		NewsInfo info = newsFacade.selectInfoByUuid(uuid);
		if (info == null) {
			BaseController.bulidRequest("后台查看新闻详情", "news_info", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 此新闻不存在",
					request);
			request.setAttribute("news_info", "news not found");
			return loadNewsList(request);
		}
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(NEWSUPDATEKEY, token);
		NewsData data = new NewsData();
		data.buildData(info,true);
		request.setAttribute("news_info_update", data);
		BaseController.bulidRequest("后台查看新闻详情", "news_info", info.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return index(request, "/wrapped/news/news_update.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/news/update/data/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getUpdateNewsData(HttpServletRequest request,
			@PathVariable String uuid) {
		NewsInfo info = newsFacade.selectInfoByUuid(uuid);
		if (info == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		NewsData data = new NewsData();
		data.buildData(info,true);
		return gson.toJson(data);
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/news/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateNewsData(HttpServletRequest request,
			@RequestParam String token, @RequestParam String uuid,
			@RequestParam String qrTaskId, @RequestParam String title,
			@RequestParam String label, @RequestParam String summary,
			@RequestParam String content, @RequestParam String author,
			@RequestParam String categoryUuid) {

		Category cate = assetFacade.getCategoryByUuid(categoryUuid);
		if (cate == null || !cate.isActive()) {
			BaseController.bulidRequest("后台新闻修改", "news_info", null,
					Status.FAILURE.message(), null, "种类的uuid=" + categoryUuid
							+ " 不存在", request);
			return getStatusJson("分类不可用");
		}
		NewsInfo info = newsFacade.selectInfoByUuid(uuid);
		if (info == null) {
			BaseController.bulidRequest("后台新闻修改", "news_info", null,
					Status.FAILURE.message(), null, "新闻的uuid=" + uuid
							+ " 此新闻不存在", request);
			return getStatusJson(Status.NOT_FOUND);
		} else {
			String server_token = toastRepeatSubmitToken(request, NEWSUPDATEKEY);
			if (!token.equals(server_token)) {
				return getStatusJson(Status.SUBMIT_REPEAT);
			}
			info.setAuthor(author);
			info.setTitle(title);
			info.setLabel(label);
			info.setContent(content);
			info.setSummary(summary);
			info.setCategory(cate);
			info.setQrTaskId(qrTaskId);
			Status status = newsFacade.updateNewsInfo(info);
			if (status == Status.SUCCESS) {
				BaseController.bulidRequest("后台新闻修改", "news_info",
						info.getId(), Status.SUCCESS.message(), null, "成功",
						request);
			} else {
				BaseController.bulidRequest("后台新闻修改", "news_info",
						info.getId(), Status.FAILURE.message(), null,
						"种类的uuid=" + uuid + " 此新闻不存在", request);
			}
			return getStatusJson(status);
		}
	}

	@RequestMapping(value = "/ckeditor/uploadFile")
	public void uploadFile(@RequestParam("upload") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		// response.setHeader("X-Frame-Options", "SAMEORIGIN");
		String CKEditorFuncNum = request.getParameter("CKEditorFuncNum");
		PrintWriter out;
		String filename = file.getOriginalFilename();
		if (file.getSize() > 0) {
			try {
				String filepath = request.getRealPath("/") + File.separator
						+ "file";
				SaveFileFromInputStream(file.getInputStream(), filepath,
						filename);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		String s = "<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction("
				+ CKEditorFuncNum + ", '" + filename + "');</script>";
		try {
			out = response.getWriter();
			out.print(s);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SaveFileFromInputStream(InputStream stream, String path,
			String filename) throws IOException {
		FileOutputStream fs = new FileOutputStream(path + "/" + filename);
		byte[] buffer = new byte[1024 * 1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}

	/**
	 * 添加省市区
	 */
	// @RequestMapping(value = "/test/add/province")
	// public @ResponseBody String addProvince() {
	// JSONArray array = ParseJson.listgetList();
	// if (array != null) {
	// List<Province> list = new ArrayList<Province>();
	// for (int i = 0; i < array.length(); i++) {
	// Province province = new Province();
	// province.setUuid(Tools.getUUID());
	// province.setIsActive(1);
	// province.setName(array.getString(i).trim());
	// list.add(province);
	// }
	//
	// try {
	// mapper.batchAddProvince(list);
	// return getStatusJson(Status.SUCCESS);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return getStatusJson(Status.FAILURE);
	// }
	// } else {
	// return getStatusJson(Status.FAILURE);
	// }
	// }
	//
	// /**
	// * 添加市
	// */
	// @RequestMapping(value = "/test/add/city")
	// public @ResponseBody String addCity() {
	// JSONObject object = ParseJson.buildMap(2);
	// if (object != null) {
	// List<City> cityList = new ArrayList<City>();
	// for (String key : object.keySet()) {
	// Province province = mapper.selectProByName(key.trim());
	// if (province != null) {
	// JSONArray array = object.getJSONArray(key);
	// if (array != null) {
	// for (int i = 0; i < array.length(); i++) {
	// City city = new City();
	// city.setUuid(Tools.getUUID());
	// city.setIsActive(1);
	// city.setProvince(province);
	// city.setName(array.getString(i).trim());
	// cityList.add(city);
	// }
	// }
	// }
	// }
	// try {
	// mapper.batchAddCity(cityList);
	// return getStatusJson(Status.SUCCESS);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return getStatusJson(Status.FAILURE);
	// } else {
	// return getStatusJson(Status.FAILURE);
	// }
	// }
	//
	// /**
	// * 添加区
	// */
	// @RequestMapping(value = "/test/add/District")
	// public @ResponseBody String addDistrict(){
	// JSONObject object = ParseJson.buildMap(3);
	// if(object!=null){
	// List<District> list=new ArrayList<District>();
	// for (String key:object.keySet()) {
	// String[] val = key.split("-");
	// String per = val[0].trim();
	// String nex = val[1].trim();
	// Province province=mapper.selectProByName(per);
	// if(province!=null){
	// SubParams params=new SubParams();
	// params.id=province.getId();
	// params.query=nex;
	// City city=mapper.selectCtiByNameAndId(params);
	// if(city!=null){
	// JSONArray array = object.getJSONArray(key);
	// for(int i=0;i<array.length();i++){
	// District district=new District();
	// district.setUuid(Tools.getUUID());
	// district.setIsActive(1);
	// district.setProvince(province);
	// district.setCity(city);
	// district.setName(array.getString(i).trim());
	// list.add(district);
	// }
	// }
	// }
	// }
	// try {
	// mapper.batchAddDistrict(list);
	// return getStatusJson(Status.SUCCESS);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return getStatusJson(Status.FAILURE);
	// }
	// else {
	// return getStatusJson(Status.FAILURE);
	// }
	// }
}
