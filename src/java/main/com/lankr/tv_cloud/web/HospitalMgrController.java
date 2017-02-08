package com.lankr.tv_cloud.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.Tool;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;



//import com.alibaba.rocketmq.common.message.Message;
import com.lankr.orm.mybatis.mapper.BannerMapper;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.HospitalMgrFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.support.rocketMQ.Producer;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BannerSurface;
import com.lankr.tv_cloud.vo.BannerVo;
import com.lankr.tv_cloud.vo.PdfInfoVo;
import com.lankr.tv_cloud.vo.PdfSuface;
import com.lankr.tv_cloud.vo.SpeakerSufaceVo;
import com.lankr.tv_cloud.vo.datatable.ActivityData;
import com.lankr.tv_cloud.vo.datatable.HospitalMgrData;
import com.lankr.tv_cloud.vo.datatable.HospitalMgrDataItem;
import com.lankr.tv_cloud.web.api.app.vo.CommonAppItemVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

import net.minidev.json.JSONArray;

@SuppressWarnings("all")
@Controller
public class HospitalMgrController extends AdminWebController {

	private static final String HOSPITAL_SAVE_KEY = "hospital_save_key";
	private static final String HOSPITAL_UPDATE_KEY = "hospital_updae_key";
	private Producer producer = Producer.instance();

	@Autowired
	private HospitalMgrFacade hospitalMgrFacade;

	/**
	 * 医院列表页面跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/hospital/mgr", method = RequestMethod.GET)
	public ModelAndView bannerMgrIndex(HttpServletRequest request) {
		this.buildProvinceList(request);
		return index(request, "/wrapped/hospital_mgr/hospital_list.jsp");
	}

	/**
	 * 列表展示医院数据
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/hospital/mgr/datatable")
	public @ResponseBody String hospitalList(Model model,
			HttpServletRequest request,
			@RequestParam(required = false) String provinceUuid,
			@RequestParam(required = false) String cityUuid) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		String searchsortIdString = request.getParameter("iSortCol_0");
		String sortValString = request.getParameter("sSortDir_0");
		String sortvalueString = request.getParameter("mDataProp_"
				+ searchsortIdString);
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);

		Pagination<Hospital> results = hospitalMgrFacade
				.searchActivitiesForDatatable(q, from, size, provinceUuid,
						cityUuid);
		HospitalMgrData data = new HospitalMgrData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 新增医院页面跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/hospital/mgr/add/page")
	public ModelAndView hospitalAddPage(HttpServletRequest request) {
		this.buildProvinceList(request);

		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(HOSPITAL_SAVE_KEY, token);
		return index(request, "/wrapped/hospital_mgr/hospital_add.jsp");
	}

	/**
	 * 保存医院数据
	 * 
	 * @param request
	 * @param token
	 * @param province
	 * @param city
	 * @param grade
	 * @param name
	 * @param mobile
	 * @param address
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/hospital/mgr/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveBanner(HttpServletRequest request,
			@RequestParam String token, @RequestParam String province,
			@RequestParam String city, @RequestParam int grade,
			@RequestParam String name,
			@RequestParam(required = false) String mobile,
			@RequestParam(required = false) String address) {

		if (Tools.isBlank(token)
				|| !token.equals(toastRepeatSubmitToken(request,
						HOSPITAL_SAVE_KEY)))
			return BaseController.failureModel("你已提交过啦，请刷新页面").toJSON();

		Province provinceResult = hospitalMgrFacade.selectProByUUid(province);
		City cityResult = hospitalMgrFacade.selectCtiByUUid(city);
		if (null == provinceResult || null == cityResult) {
			BaseController.bulidRequest("新增医院", "base_hospital", null,
					Status.FAILURE.message(), null, "失败,根据uuid查询省份和城市过程中出错",
					request);
			return BaseController.failureModel("新增医院出错，请稍后重试").toJSON();
		}

		String gradeTmp = null;
		switch (grade) {
		case 1:
			gradeTmp = "一级医院";
			break;
		case 2:
			gradeTmp = "二级医院";
			break;
		case 3:
			gradeTmp = "三级医院";
			break;
		case 4:
			gradeTmp = "未评级别医院";
			break;
		case 5:
			gradeTmp = "民营医院";
			break;
		case 6:
			gradeTmp = "医学院校";
			break;
		default:
			gradeTmp = "未评级别医院";
			break;
		}

		Hospital hospital = new Hospital();
		hospital.setUuid(Tools.getUUID());
		hospital.setName(Tools.nullValueFilter(name));
		hospital.setGrade(gradeTmp);
		hospital.setMobile(Tools.nullValueFilter(mobile));
		hospital.setAddress(HtmlUtils.htmlEscape(address));
		hospital.setProvince(provinceResult);
		hospital.setCity(cityResult);
		hospital.setIsActive(BaseModel.ACTIVE);

		ActionMessage action = hospitalMgrFacade.saveHospital(hospital);
		if (action.isSuccess()) {
			BaseController.bulidRequest("新增保存医院", "base_hospital", null,
					Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel("添加医院成功").toJSON();
		}
		BaseController.bulidRequest("新增保存医院", "base_hospital", null,
				Status.FAILURE.message(), null, "失败", request);
		return BaseController.failureModel("出错啦，请重新尝试").toJSON();
	}

	/**
	 * 更新医院页面跳转
	 * 
	 * @param request
	 * @param hospitalUuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/hospital/mgr/update/page/{hospitalUuid}")
	public ModelAndView activityUpdatePage(HttpServletRequest request,
			@PathVariable(value = "hospitalUuid") String hospitalUuid) {
		this.buildProvinceList(request);

		if (!Tools.isBlank(hospitalUuid)) {
			Hospital hospital = hospitalMgrFacade
					.selectHospitalByUUid(hospitalUuid);
			HospitalMgrDataItem data = HospitalMgrDataItem.build(hospital);

			if (null != hospital) {
				Province province = hospital.getProvince();
				if (null != province) {
					List<City> cityList = EphemeralData.getIntansce(
							provinceMapper).getCitys(province);
					data.buildCity(cityList);
				}
			}
			request.setAttribute("hospital", data);
			request.setAttribute("token",
					makeRepeatSubmitToken(HOSPITAL_UPDATE_KEY));
		}

		return index(request, "/wrapped/hospital_mgr/hospital_update.jsp");
	}

	/**
	 * 更新医院数据保存
	 * 
	 * @param request
	 * @param token
	 * @param province
	 * @param city
	 * @param grade
	 * @param name
	 * @param hospitalUuid
	 * @param mobile
	 * @param address
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/hospital/mgr/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateBanner(HttpServletRequest request,
			@RequestParam String token, @RequestParam String province,
			@RequestParam String city, @RequestParam int grade,
			@RequestParam String name, @RequestParam String hospitalUuid,
			@RequestParam(required = false) String mobile,
			@RequestParam(required = false) String address) {

		if (Tools.isBlank(token)
				|| !token.equals(toastRepeatSubmitToken(request,
						HOSPITAL_UPDATE_KEY)))
			return BaseController.failureModel("你已提交过啦，请刷新页面").toJSON();

		Province provinceRes = hospitalMgrFacade.selectProByUUid(province);
		City cityResult = hospitalMgrFacade.selectCtiByUUid(city);
		if (null == provinceRes || null == cityResult) {
			BaseController.bulidRequest("更新医院", "base_hospital",
					provinceRes.getId(), Status.FAILURE.message(), null,
					"失败,根据uuid查询省份和城市过程中出错", request);
			return BaseController.failureModel("更新医院出错，请稍后重试").toJSON();
		}

		Hospital hospital = hospitalMgrFacade
				.selectHospitalByUUid(hospitalUuid);
		if (null == hospital) {
			BaseController.bulidRequest("更新医院", "base_hospital", null,
					Status.FAILURE.message(), null, "失败,根据uuid查询医院过程中出错",
					request);
			return BaseController.failureModel("更新医院出错，请稍后重试").toJSON();
		}

		String gradeTmp = null;
		switch (grade) {
		case 1:
			gradeTmp = "一级医院";
			break;
		case 2:
			gradeTmp = "二级医院";
			break;
		case 3:
			gradeTmp = "三级医院";
			break;
		case 4:
			gradeTmp = "未评级别医院";
			break;
		case 5:
			gradeTmp = "民营医院";
			break;
		case 6:
			gradeTmp = "医学院校";
			break;
		default:
			gradeTmp = "未评级别医院";
			break;
		}

		hospital.setName(Tools.nullValueFilter(name));
		hospital.setGrade(gradeTmp);
		hospital.setMobile(Tools.nullValueFilter(mobile));
		hospital.setAddress(HtmlUtils.htmlEscape(address));
		hospital.setProvince(provinceRes);
		hospital.setCity(cityResult);
		hospital.setIsActive(BaseModel.ACTIVE);

		ActionMessage action = hospitalMgrFacade.updateHospital(hospital);
		if (action.isSuccess()) {
			BaseController.bulidRequest("更新保存医院", "base_hospital", null,
					Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel("添加医院成功").toJSON();
		}
		BaseController.bulidRequest("更新保存医院", "base_hospital", null,
				Status.FAILURE.message(), null, "失败", request);
		return BaseController.failureModel("出错啦，请重新尝试").toJSON();
	}

	/**
	 * 移除医院
	 * 
	 * @param request
	 * @param hospitalUuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/hospital/mgr/remove/{hospitalUuid}", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String removeBanner(HttpServletRequest request,
			@PathVariable(value = "hospitalUuid") String hospitalUuid) {

		Hospital hospital = hospitalMgrFacade
				.selectHospitalByUUid(hospitalUuid);
		if (null == hospital) {
			BaseController.bulidRequest("删除医院", "base_hospital", null,
					Status.FAILURE.message(), null, "失败,根据uuid查询医院过程中出错",
					request);
			return BaseController.failureModel("删除医院出错，请稍后重试").toJSON();
		}
		ActionMessage action = hospitalMgrFacade.removeHospital(hospital);
		if (action.isSuccess()) {
			BaseController.bulidRequest("删除医院", "base_hospital", null,
					Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel("删除医院成功").toJSON();
		}
		BaseController.bulidRequest("删除医院", "base_hospital", null,
				Status.FAILURE.message(), null, "失败", request);
		return BaseController.failureModel("出错啦，请重新尝试").toJSON();
	}

	// 查询省份列表
	private void buildProvinceList(HttpServletRequest request) {
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		CommonAppItemVo app = new CommonAppItemVo();
		app.setStatus(Status.SUCCESS);
		app.buildProvince(list);
		request.setAttribute("province_list", app.toJSON());
	}

	/**
	 * @author wangxiong
	 * @createDate: 2016年5月6日
	 * @modifyDate: 2016年5月6日
	 */

	@RequestAuthority(value = Role.NULL, requiredProject = false, logger = false)
	@ResponseBody
	@RequestMapping(value = "/info/getLocation", produces = "text/html;charset=UTF-8")
	public String getLocation(HttpServletRequest request) {
		String str = EphemeralData.getIntansce(provinceMapper).getLocation();
		return str;
	}
	// /**
	// * @author wangxiong
	// * @createDate: 2016年5月27日
	// * @modifyDate: 2016年5月27日
	// */
	//
	// @RequestAuthority(value=Role.SUPER_ADMIN_LEVEL)
	// @ResponseBody
	// @RequestMapping("/hospital/rebuild")
	// public String rebuildHospitalToES(HttpServletRequest request,
	// HttpServletResponse response) {
	// List<Hospital> hospitals = hospitalMgrFacade.selectAllHospital() ;
	// List<Message> msgs = null ;
	// int count = 0 ;
	// for (int i = 0; i<hospitals.size(); i++) {
	// Hospital hospital = hospitals.get(i) ;
	// String data = hospital.getId() + ":" + hospital.getUuid() + ":" +
	// hospital.getName() ;
	// Message msg = new Message("zhiliao", "add", hospital.getUuid(),
	// data.getBytes()) ;
	// msgs.add(msg) ;
	// count ++ ;
	// if (count == 100) {
	// try {
	// producer.send(msgs);
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// return BaseController.failureModel("rebuild失败，请重新尝试").toJSON() ;
	// }
	// count = 0 ;
	// msgs.clear() ;
	// try {
	// TimeUnit.MILLISECONDS.sleep(100) ;
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// return BaseController.successModel("rebuild成功").toJSON() ;
	// }
	//
	// /**
	// * @author wangxiong
	// * @createDate: 2016年5月27日
	// * @modifyDate: 2016年5月27日
	// * @param uuid
	// */
	//
	// @RequestAuthority(value=Role.PRO_EDITOR)
	// @ResponseBody
	// @RequestMapping("/hospital/addToSearch")
	// public String addHospitalToES(HttpServletRequest
	// request,HttpServletResponse response
	// , @RequestParam String uuid) {
	// Hospital hospital = hospitalMgrFacade.selectHospitalByUUid(uuid) ;
	// List<Message> msgs = null ;
	// String data = hospital.getId() + ":" + hospital.getUuid() + ":" +
	// hospital.getName() ;
	// Message msg = new Message("zhiliao", "add", hospital.getUuid(),
	// data.getBytes()) ;
	// msgs.add(msg) ;
	// try {
	// producer.send(msgs);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return BaseController.failureModel("失败了，请重新尝试").toJSON() ;
	// }
	//
	// return BaseController.successModel("成功添加到搜索服务").toJSON() ;
	// }
	//
	// /**
	// * @author wangxiong
	// * @createDate: 2016年5月27日
	// * @modifyDate: 2016年5月27日
	// * @param hospital
	// */
	//
	// @RequestAuthority(value=Role.PRO_EDITOR)
	// @ResponseBody
	// @RequestMapping("/hospital/updateToSearch")
	// public String updateHospitalToES(HttpServletRequest
	// request,HttpServletResponse response
	// , Hospital hospital) {
	// List<Message> msgs = null ;
	// String data = hospital.getId() + ":" + hospital.getUuid() + ":" +
	// hospital.getName() ;
	// Message msg = new Message("zhiliao", "update", hospital.getUuid(),
	// data.getBytes()) ;
	// msgs.add(msg) ;
	// try {
	// producer.send(msgs);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return BaseController.failureModel("失败了，请重新尝试").toJSON() ;
	// }
	//
	// return BaseController.successModel("成功更新到搜索服务").toJSON() ;
	// }
	//
	// /**
	// * @author wangxiong
	// * @createDate: 2016年5月27日
	// * @modifyDate: 2016年5月27日
	// * @param uuid
	// */
	//
	// @RequestAuthority(value=Role.PRO_EDITOR)
	// @ResponseBody
	// @RequestMapping("/hospital/daleteToSearch")
	// public String deleteHospitalToES(HttpServletRequest
	// request,HttpServletResponse response
	// , String uuid) {
	// Hospital hospital = hospitalMgrFacade.selectHospitalByUUid(uuid) ;
	// List<Message> msgs = null ;
	// String data = hospital.getId() + ":" + hospital.getUuid() + ":" +
	// hospital.getName() ;
	// Message msg = new Message("zhiliao", "delete", hospital.getUuid(),
	// data.getBytes()) ;
	// msgs.add(msg) ;
	// try {
	// producer.send(msgs) ;
	// } catch (Exception e) {
	// e.printStackTrace() ;
	// return BaseController.failureModel("失败了，请重新尝试").toJSON() ;
	// }
	//
	// return BaseController.successModel("成功删除该项搜索").toJSON() ;
	// }
	//

	// /**
	// * @author wangxiong
	// * @createDate: 2016年5月6日
	// * @modifyDate: 2016年5月6日
	// */
	// @RequestAuthority(value=Role.NULL, requiredProject = false, logger =
	// false)
	// @ResponseBody
	// @RequestMapping(value="/info/getLocation",produces="text/html;charset=UTF-8")
	// public String getLocation(HttpServletRequest request){
	// String path = request.getSession().getServletContext().getRealPath("/") +
	// File.separator + "file" + File.separator + "location.json";
	// //System.out.println(path);
	// File file = new File(path);
	//
	// if (!file.exists()) {
	// synchronized (this) {
	// String location = this.getString() ;
	// PrintStream printStream = null ;
	// try {
	// printStream = new PrintStream(new FileOutputStream(file));
	// printStream.print(location) ;
	// } catch (FileNotFoundException e) {
	// e.printStackTrace() ;
	// } finally {
	// printStream.close() ;
	// }
	// }
	// }
	// Scanner scanner = null;
	// StringBuffer stringBf = new StringBuffer();
	// try {
	// scanner = new Scanner(new FileInputStream(file));
	// scanner.useDelimiter("\n");
	// while (scanner.hasNext()) {
	// stringBf.append(scanner.next());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// scanner.close();
	// }
	//
	// return stringBf.toString();
	// }
}
