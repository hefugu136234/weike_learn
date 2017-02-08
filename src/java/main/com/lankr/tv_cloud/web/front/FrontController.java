package com.lankr.tv_cloud.web.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.RegisterTmp;
import com.lankr.tv_cloud.model.Subscribe;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.BaseWebController;
import com.lankr.tv_cloud.web.api.app.vo.CommonAppItemVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = "/f")
public class FrontController extends BaseFrontController {
	
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/index")
	public ModelAndView frontIndex(HttpServletRequest request, Model model) {
		BaseController.bulidRequest("前台预约首页", null, null,
				Status.SUCCESS.message(), null, "成功", request);
		return new ModelAndView("front/subscribe");
	}

	// 发送短信验证码
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/subscribe/code/send", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String fetchSubscribeCode(@RequestParam String mobile,
			HttpServletRequest request) {
		if (mobile == null || !Tools.isValidMobile(mobile)) {
			return BaseController.failureModel(
					"please input valid mobile number").toJSON();
		}
		Status s = userFacade.sendSubscribeCode(mobile,
				BaseController.getClientIpAddr(request));
		if (Status.SUCCESS == s) {
			return getStatusJson(Status.SUCCESS);
		} else {
			if(s == Status.IN_USE){
				return BaseController.failureModel("我们已经收到了您的体验申请啦，不需要重复提交哦~").toJSON();
			}
			return BaseController.failureModel(s.message()).toJSON();
		}
	}

	// 验证验证码
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/subscribe/code/valid", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String validRegisterCode(@RequestParam String code,
			String mobile) {
		RegisterTmp tmp = userFacade.validSubscribeCode(code, mobile);
		if (tmp == null || !tmp.isActive()) {
			return BaseController.failureModel("validation failure").toJSON();
		}
		if (Math.abs(System.currentTimeMillis() - tmp.getCreateDate().getTime()) > 10 * 60 * 1000) {
			return BaseController.failureModel("验证码已过期").toJSON();
		}
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(new SimpleData(
				"valid_code", tmp.getUuid()));
	}

	/**
	 * 加载省份数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/list/province", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getProvince(HttpServletRequest request) {
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		if (list == null || list.isEmpty()) {
			return BaseController.failureModel("省份数据加载出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		app.setStatus(Status.SUCCESS);
		app.buildProvince(list);
		return app.toJSON();
	}
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/city/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getCityList(HttpServletRequest request,
			@PathVariable String uuid) {
		Province province = provinceMapper.selectProByUUid(uuid);
		if (province == null) {
			return BaseController.failureModel("uuid参数出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		List<City> list = EphemeralData.getIntansce(provinceMapper).getCitys(
				province);
		if (list == null || list.isEmpty()) {
			return BaseController.failureModel("uuid参数出错").toJSON();
		}
		app.setStatus(Status.SUCCESS);
		app.buildCity(list);
		return app.toJSON();
	}
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/hospital/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getHospitalList(HttpServletRequest request,
			@PathVariable String uuid) {
		City city = provinceMapper.selectCtiByUUid(uuid);
		if (city == null) {
			return BaseController.failureModel("uuid参数出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		List<Hospital> list = hospitalMapper
				.selectHosListByCityId(city.getId());
		if (list == null || list.isEmpty()) {
			return BaseController.failureModel("uuid参数出错").toJSON();
		}
		app.setStatus(Status.SUCCESS);
		app.buildHospital(list);
		return app.toJSON();
	}

	/**
	 * 加载科室
	 * 
	 * @param categoryName
	 * @param spreadable
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/list/department", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getDepartment(HttpServletRequest request) {
		List<Departments> departments = hospitalMapper.selectDePatList();
		if (departments == null || departments.isEmpty()) {
			return BaseController.failureModel("科室数据加载出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		app.setStatus(Status.SUCCESS);
		app.buildDepartments(departments);
		return app.toJSON();
	}

	/**
	 * 保存预约
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/subscribe/save", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String subscribeSave(HttpServletRequest request, String name,
			String valid_code,
			@RequestParam String userType,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) String mail,
			@RequestParam(required = false) String company,
			@RequestParam(required = false) String group,
			@RequestParam(required = false) String position,
			@RequestParam(required = false) String mark,// 办公室地址
			@RequestParam(required = false) String hosipitalUuid,
			@RequestParam(required = false) String departmentUuid) {
		RegisterTmp tmp = userFacade.getRegisterTmpByUuid(valid_code);
		if (tmp == null || !tmp.isActive()) {
			BaseController.bulidRequest("前台预约提交", null, null,
					Status.FAILURE.message(), null, "phone="+phone+" 预约码失效", request);
			return BaseController.failureModel("预约码失效或者已经被使用").toJSON();
		}
		Subscribe subscribe = new Subscribe();
		subscribe.setUuid(Tools.getUUID());
		subscribe.setName(name);
		subscribe.setMobile(tmp.getMobile());
		subscribe.setPhone(phone);
		subscribe.setMail(mail);
		subscribe.setCompany(company);
		subscribe.setGroup(group);
		subscribe.setPosition(position);
		if (hosipitalUuid != null) {
			Hospital hospital = hospitalMapper
					.selectHospitalByUUid(hosipitalUuid);
			subscribe.setHospital(hospital);
		}
		if (departmentUuid != null) {
			Departments departments = hospitalMapper
					.selectDePatByUuid(departmentUuid);
			subscribe.setDepartments(departments);
		}
		subscribe.setStatus(0);
		subscribe.setUserType(userType);
		subscribe.setMark(mark);
		subscribe.setIsActive(1);
		BaseAPIModel baApiModel = new BaseAPIModel();
		Status status = subscribeFacade.addSubscribe(subscribe);
		if (status == Status.SUCCESS) {
			baApiModel.setStatus(status);
			userFacade.disableRegisterTmp(tmp);
			BaseController.bulidRequest("前台预约提交", null, null,
					Status.SUCCESS.message(), null, "phone="+phone+" 预约保存成功", request);
		} else {
			baApiModel.setStatus(status);
			baApiModel.setMessage("预约保存失败");
			BaseController.bulidRequest("前台预约提交", null, null,
					Status.FAILURE.message(), null, "phone="+phone+" 预约保存失败", request);
		}
		return baApiModel.toJSON();
	}

//	@RequestAuthority(requiredProject = false, logger = true)
//	@RequestMapping(value = "/update/password", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
//	@ResponseBody
//	public String updatePassword(HttpServletRequest request,
//			String oldPassword, String password) {
//		User currentUser = getCurrentUser(request);
//		if (oldPassword == null || oldPassword.isEmpty()) {
//			return getStatusJson("原始密码不能为空");
//		}
//		if (password == null || password.isEmpty()) {
//			return getStatusJson("新密码不能为空");
//		}
//		User user = userFacade.getUserByUuid(currentUser.getUuid());
//		if (!Md5.getMd5String(oldPassword).equals(user.getPassword())) {
//			return getStatusJson("原始密码不正确");
//		}
//		currentUser.setPassword(Md5.getMd5String(password));
//		Status staus = userFacade.updateUser(currentUser);
//		if (staus == Status.SUCCESS) {
//			BaseController.bulidRequest("前台用户修改密码", "user", user.getId(),
//					request);
//		}
//		return getStatusJson(staus);
//	}

//	@RequestAuthority(requiredProject = false, logger = true)
//	@RequestMapping(value = "/login/out")
//	public ModelAndView loginOut(HttpServletRequest request) {
//		// request.getSession().invalidate();
//		User user = getCurrentUser(request);
//		removeCurrentUser(request);
//		BaseController.bulidRequest("前台用户登出", "user", user.getId(), request);
//		return new ModelAndView("redirect:/f/login/page");
//	}

	// 跳转诊所修改页面
//	@RequestAuthority(logger = false)
//	@RequestMapping(value = "/clinic/info/page", method = RequestMethod.GET)
//	public ModelAndView changeClinicPage(HttpServletRequest request) {
//		return index(request, "/front/edit.jsp");
//	}
//
//	// 预约信息表页面
//	@RequestAuthority(logger = false)
//	@RequestMapping(value = "/subscribe/list", method = RequestMethod.GET)
//	public ModelAndView subscribeList(HttpServletRequest request) {
//		return index(request, "/front/list.jsp");
//	}
	
	//微信前台预约
	@RequestMapping(value = "/wx/subscribe/entrance", method = RequestMethod.GET)
	public ModelAndView entrance(HttpServletRequest request){
		return new ModelAndView("wechat/subscribe");
	}
	
	@RequestMapping(value = "/wx/subscribe/select", method = RequestMethod.GET)
	public ModelAndView wxselect(HttpServletRequest request){
		return new ModelAndView("wechat/subscribe_select");
	}
	
	@RequestMapping(value = "/wx/normal", method = RequestMethod.GET)
	public ModelAndView normal(HttpServletRequest request){
		return new ModelAndView("wechat/subscribe_normal_form");
	}
	
	@RequestMapping(value = "/wx/brand", method = RequestMethod.GET)
	public ModelAndView brand(HttpServletRequest request){
		return new ModelAndView("wechat/subscribe_form");
	}


}
