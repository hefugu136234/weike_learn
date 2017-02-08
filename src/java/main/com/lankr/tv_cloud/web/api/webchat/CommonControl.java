package com.lankr.tv_cloud.web.api.webchat;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.vo.CommonItemVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class CommonControl extends BaseWechatController {

	/**
	 * 所有的公用数据的请求 如 省市区医院等
	 */
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/province/data", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getProvince(HttpServletRequest request) {
		CommonItemVo vo = new CommonItemVo();
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		if(!Tools.isEmpty(list)){
			vo.buildProvince(list);
		}
		return vo.toJSON();
	}

	/**
	 * 由省获取市
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/city/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getCityList(HttpServletRequest request,
			@PathVariable String uuid) {
		CommonItemVo vo = new CommonItemVo();
		if (uuid != null && !uuid.isEmpty()) {
			Province province = provinceMapper.selectProByUUid(uuid);
			if (province != null) {
				List<City> list = EphemeralData.getIntansce(provinceMapper)
						.getCitys(province);
				vo.buildCity(list);
			}
		}
		return vo.toJSON();
	}

	/**
	 * 由市获取区
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/district/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getDistrictList(HttpServletRequest request,
			@PathVariable String uuid) {
		CommonItemVo vo = new CommonItemVo();
		if (uuid != null && !uuid.isEmpty()) {
			City city = provinceMapper.selectCtiByUUid(uuid);
			if (city != null) {
				List<District> list = EphemeralData.getIntansce(provinceMapper)
						.getDistricts(city);
				vo.buildDistrict(list);
			}
		}
		return vo.toJSON();
	}

	/**
	 * 由市获取医院
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/hospital/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getHospitalList(HttpServletRequest request,
			@PathVariable String uuid) {
		CommonItemVo vo = new CommonItemVo();
		if (uuid != null && !uuid.isEmpty()) {
			City city = provinceMapper.selectCtiByUUid(uuid);
			if (city != null) {
				List<Hospital> list = hospitalMapper.selectHosListByCityId(city
						.getId());
				vo.buildHospital(list);
			}
		}
		return vo.toJSON();
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/department/data", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getDepartmentData(HttpServletRequest request) {
		CommonItemVo vo = new CommonItemVo();
		List<Departments> departments = hospitalMapper.selectDePatList();
		if (!Tools.isEmpty(departments)) {
			vo.buildDepartments(departments);
		}
		return vo.toJSON();
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/manufacturer/data", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getManufacturerData(HttpServletRequest request) {
		CommonItemVo vo = new CommonItemVo();
		List<Manufacturer> manlist = groupFacade.selectManufacturer();
		if (!Tools.isEmpty(manlist)) {
			vo.buildManufacturer(manlist);
		}
		return vo.toJSON();
	}

	/**
	 * 作品学科的一级目录
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/opus/category/root", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String opusRoot(HttpServletRequest request,
			@RequestParam String showCategory) {
		String[] cateGroup = showCategory.split(":");
		List<Category> list = new ArrayList<Category>();
		for (String string : cateGroup) {
			Category category = assetFacade.getCategoryByName(string);
			if (category != null) {
				list.add(category);
			}
		}
		CommonItemVo vo = new CommonItemVo();
		vo.buildCate(list);
		return vo.toJSON();
	}

	/**
	 * 作品学科的二级目录
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/opus/category/second/{uuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String opusSecond(HttpServletRequest request,
			@PathVariable String uuid) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		List<Category> list = category.getChildren();
		CommonItemVo vo = new CommonItemVo();
		vo.buildCate(list);
		return vo.toJSON();
	}

	/**
	 * 页面向服务器发送停留记录，5秒发送一次
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/record/page/remain", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String recordPageRemain(HttpServletRequest request,
			@RequestParam String uuid) {
		return updateRemainTime(uuid);
	}

	// 判断在resource_preview页面是否登录
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/is/login/info", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String isLoginInfo(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null) {
			return successModel("not login").toJSON();
		}
		return successModel("login").toJSON();
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/record/prior/url", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String recordPriorUrl(HttpServletRequest request,
			@RequestParam String url) {
		setPriorUrl(request, url);
		return successModel().toJSON();
	}
}