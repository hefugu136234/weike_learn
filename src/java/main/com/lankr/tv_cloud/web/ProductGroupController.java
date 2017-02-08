package com.lankr.tv_cloud.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.ProductGroup;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.ViewSharing;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ActiviteCodeSurface;
import com.lankr.tv_cloud.vo.OptionAddition;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.vo.ProductGroupSurface;
import com.lankr.tv_cloud.vo.ProductGroupVo;
import com.lankr.tv_cloud.vo.ShowShareListSurface;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
@SuppressWarnings("all")	//modified by mayuan

@Controller
@RequestMapping("/project/group")
public class ProductGroupController extends AdminWebController {

	private static final String ADD_GROUP_KEY = "add_group_key";

	private static final String UPDATE_GROUP_KEY = "update_group_key";

	private static final String ADD_MANUFACTURER_KEY = "add_manufacturer_key";

	private static final String UPDATE_MANUFACTURER_KEY = "update_manufacturer_key";

	/**
	 * 厂商列表页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/manufacturer/list/page")
	public ModelAndView manufacturerListPage(HttpServletRequest request) {
		return index(request, "/wrapped/product/manufacturer_list.jsp");
	}

	@RequestMapping(value = "/manufacturer/list/data")
	@RequestAuthority(value = Role.PRO_EDITOR)
	public @ResponseBody String manufacturerListData(HttpServletRequest request) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Manufacturer> pagination = groupFacade
				.selectManufacturerList(searchValue, from, pageItemTotal);
		ProductGroupSurface suface = new ProductGroupSurface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.buildMuman(pagination.getResults());
		BaseController.bulidRequest("后台查看厂商列表", "manufacturer", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	/**
	 * 厂商新增页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/manufacturer/add/page")
	public ModelAndView manufacturerAddPage(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(ADD_MANUFACTURER_KEY, token);
		return index(request, "/wrapped/product/manufacturer_add.jsp");
	}

	/**
	 * 厂商新增保存
	 * 
	 * @param request
	 * @param token
	 * @param name
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/manufacturer/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String addManufacturerData(HttpServletRequest request,
			@RequestParam String token, @RequestParam String name,
			@RequestParam String address, @RequestParam String taskId,
			@RequestParam String serialNum) {
		String repeat_token = toastRepeatSubmitToken(request,
				ADD_MANUFACTURER_KEY);
		if (!token.equals(repeat_token)) {
			return BaseController.failureModel("重复提交数据").toJSON();
		}
		String uuid = Tools.getUUID();
		Manufacturer muManufacturer = new Manufacturer();
		muManufacturer.setUuid(uuid);
		muManufacturer.setName(Tools.nullValueFilter(name));
		muManufacturer.setPinyin(Tools.getPinYin(name));
		muManufacturer.setIsActive(BaseModel.APPROVED);
		muManufacturer.setStatus(BaseModel.APPROVED);
		muManufacturer.setAddress(address);
		muManufacturer.setMark("");
		muManufacturer.setSerialNum(serialNum);
		muManufacturer.setTaskId(Tools.nullValueFilter(taskId));
		Status status = groupFacade.addManufacturer(muManufacturer);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台新增厂商", "manufacturer", muManufacturer.getId(),
					Status.SUCCESS.message(), null, "失败", request);
			return BaseController.successModel().toJSON();
		}else{
			BaseController.bulidRequest("后台新增厂商", "manufacturer", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return BaseController.failureModel("保存失败").toJSON();
	}

	/**
	 * 厂商更新页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping("/manufacturer/update/page/{uuid}")
	public ModelAndView updateManufacturerPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Manufacturer manufacturer = groupFacade.selectManufacturerByUuid(uuid);
		if (manufacturer == null || !manufacturer.isActive()) {
			request.setAttribute("error_info", "厂商不存在或不可用");
			BaseController.bulidRequest("后台查看厂商详情", "manufacturer", null,
					Status.FAILURE.message(), null, "uuid="+uuid+" 厂商不可用", request);
			return manufacturerListPage(request);
		} else {
			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.getSession().setAttribute(UPDATE_MANUFACTURER_KEY, token);
			ProductGroupVo data = new ProductGroupVo();
			data.buildData(manufacturer);
			request.setAttribute("manufacturer", manufacturer);
			BaseController.bulidRequest("后台查看厂商详情", "manufacturer", manufacturer.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return index(request, "/wrapped/product/manufacturer_update.jsp");
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/manufacturer/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateManufacturerStatus(
			HttpServletRequest request, @RequestParam String uuid) {
		Manufacturer manufacturer = groupFacade.selectManufacturerByUuid(uuid);
		if (manufacturer == null || !manufacturer.isActive()) {
			BaseController.bulidRequest("后台厂商状态修改", "manufacturer", null,
					Status.FAILURE.message(), null, "uuid="+uuid+" 厂商信息不存在", request);
			return BaseController.failureModel("厂商不存在或不可用").toJSON();
		}
		manufacturer
				.setStatus(manufacturer.getStatus() == BaseModel.APPROVED ? BaseModel.UNAPPROVED
						: BaseModel.APPROVED);
		Status status = groupFacade.updateManufacturerStatus(manufacturer);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台厂商状态修改", "manufacturer", manufacturer.getId(),
					Status.SUCCESS.message(), null, "成功,状态转变为="+manufacturer.getStatus(), request);
			ProductGroupVo data = new ProductGroupVo();
			data.buildData(manufacturer);
			data.setStatus(status);
			return data.toJSON();
		}else{
			BaseController.bulidRequest("后台厂商状态修改", "manufacturer", manufacturer.getId(),
					Status.FAILURE.message(), null, "失败,状态转变为="+manufacturer.getStatus(), request);
		}
		return BaseController.failureModel("厂商状态更新失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/manufacturer/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateManufacturer(HttpServletRequest request,
			@RequestParam String token, @RequestParam String uuid,
			@RequestParam String name, @RequestParam String address,
			@RequestParam String taskId, @RequestParam String serialNum) {
		Manufacturer manufacturer = groupFacade.selectManufacturerByUuid(uuid);
		if (manufacturer == null || !manufacturer.isActive()) {
			BaseController.bulidRequest("后台厂商信息修改", "manufacturer", null,
					Status.FAILURE.message(), null, "uuid="+uuid+" 厂商信息不存在", request);
			return BaseController.failureModel("厂商不存在或不可用").toJSON();
		}
		String server_token = toastRepeatSubmitToken(request,
				UPDATE_MANUFACTURER_KEY);
		if (!token.equals(server_token)) {
			return BaseController.failureModel("重复提交数据").toJSON();
		} else {
			manufacturer.setName(Tools.nullValueFilter(name));
			manufacturer.setPinyin(Tools.getPinYin(name));
			manufacturer.setTaskId(taskId);
			manufacturer.setAddress(address);
			manufacturer.setSerialNum(serialNum);
			Status status = groupFacade.updateManufacturer(manufacturer);
			if (status == Status.SUCCESS) {
				BaseController.bulidRequest("后台厂商信息修改", "manufacturer", manufacturer.getId(),
						Status.SUCCESS.message(), null, "成功", request);
				return BaseController.successModel().toJSON();
			}else{
				BaseController.bulidRequest("后台厂商信息修改", "manufacturer", manufacturer.getId(),
						Status.FAILURE.message(), null, "失败", request);
			}
		}
		return BaseController.failureModel("信息修改失败").toJSON();
	}

	/**
	 * 厂商下的产品组列表
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/product/list/page/{uuid}")
	public ModelAndView productListPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Manufacturer manufacturer = groupFacade.selectManufacturerByUuid(uuid);
		if (manufacturer == null || !manufacturer.isActive()) {
			request.setAttribute("error_info", "厂商不存在或不可用");
			return manufacturerListPage(request);
		}
		request.setAttribute("manufacturer_name", manufacturer.getName());
		request.setAttribute("uuid", uuid);
		return index(request, "/wrapped/product/group_list.jsp");
	}

	@RequestMapping(value = "/product/list/data/{uuid}")
	@RequestAuthority(value = Role.PRO_EDITOR)
	public @ResponseBody String productListData(HttpServletRequest request,
			@PathVariable String uuid) {
		int manufacturerId = 0;
		Manufacturer manufacturer = groupFacade.selectManufacturerByUuid(uuid);
		if (manufacturer != null) {
			manufacturerId = manufacturer.getId();
		}
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ProductGroup> pagination = groupFacade.selectProgroupList(
				searchValue, from, pageItemTotal, manufacturerId);
		ProductGroupSurface suface = new ProductGroupSurface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.buildGroup(pagination.getResults());
		BaseController.bulidRequest("后台查看厂商下的产品组列表", "product_group", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	/**
	 * 产品组新增页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/product/add/page/{pageUuid}")
	public ModelAndView productAddPage(HttpServletRequest request,
			@PathVariable String pageUuid) {
		List<Manufacturer> list = groupFacade.selectManufacturer();
		List<OptionAddition> addList = OptionAddition.buildData(list);
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.setAttribute("pageUuid", pageUuid);
		request.setAttribute("manufacturer_list", addList);
		request.getSession().setAttribute(ADD_GROUP_KEY, token);
		return index(request, "/wrapped/product/group_add.jsp");
	}

	/**
	 * 搜索厂商
	 */
	// @RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/product/search/Manufacturer", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String searchManufacturer(HttpServletRequest request,
			@RequestParam(required = false) String q) {
		List<Manufacturer> list = groupFacade.selectManufacturerByQ(q);
		OptionAdditionList model = new OptionAdditionList();
		model.buildData(list);
		return gson.toJson(model);
	}

	/**
	 * 产品组新增保存
	 * 
	 * @param request
	 * @param token
	 * @param name
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/product/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String addproductData(HttpServletRequest request,
			@RequestParam String token, @RequestParam String name,
			@RequestParam String manufacturer_uuid,
			@RequestParam String serialNum) {
		Manufacturer manufacturer = groupFacade
				.selectManufacturerByUuid(manufacturer_uuid);
		if (manufacturer == null || !manufacturer.isActive()) {
			BaseController.bulidRequest("后台新增产品组", "product_group", null,
					Status.FAILURE.message(), null, "厂商uuid="+manufacturer_uuid+" 不存在", request);
			return BaseController.failureModel("厂商不存在或不可用").toJSON();
		}
		String repeat_token = toastRepeatSubmitToken(request, ADD_GROUP_KEY);
		if (!token.equals(repeat_token)) {
			return BaseController.failureModel("重复提交数据").toJSON();
		}
		String uuid = Tools.getUUID();
		ProductGroup productGroup = new ProductGroup();
		productGroup.setUuid(uuid);
		productGroup.setName(Tools.nullValueFilter(name));
		productGroup.setPinyin(Tools.getPinYin(name));
		productGroup.setIsActive(BaseModel.APPROVED);
		productGroup.setStatus(BaseModel.APPROVED);
		productGroup.setMark("");
		productGroup.setManufacturer(manufacturer);
		productGroup.setSerialNum(serialNum);
		Status status = groupFacade.addProgroup(productGroup);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台新增产品组", "product_group", productGroup.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return BaseController.successModel().toJSON();
		}else{
			BaseController.bulidRequest("后台新增产品组", "product_group", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return BaseController.failureModel("保存失败").toJSON();
	}

	/**
	 * 产品组更新页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping("/product/update/page/{uuid}")
	public ModelAndView updateproductPage(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam String pageUuid) {
		ProductGroup productGroup = groupFacade.selectProgroupByUuid(uuid);
		if (productGroup == null || !productGroup.isActive()) {
			request.setAttribute("error_info", "此产品组不存在或不可用");
			BaseController.bulidRequest("后台查看产品组详情", "product_group", null,
					Status.FAILURE.message(), null, "uuid="+uuid+" 不可用", request);
			return productListPage(request, pageUuid);
		} else {
			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.setAttribute("uuid", uuid);
			request.setAttribute("pageUuid", pageUuid);
			request.getSession().setAttribute(UPDATE_GROUP_KEY, token);
			ProductGroupVo data = new ProductGroupVo();
			data.buildData(productGroup);
			request.setAttribute("productGroup", data);
			BaseController.bulidRequest("后台查看产品组详情", "product_group", productGroup.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return index(request, "/wrapped/product/group_update.jsp");
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/product/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateProductStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		ProductGroup productGroup = groupFacade.selectProgroupByUuid(uuid);
		if (productGroup == null || !productGroup.isActive()) {
			BaseController.bulidRequest("后台产品组状态改变", "product_group", null,
					Status.FAILURE.message(), null, "uuid="+uuid+" 产品组不存在", request);
			return BaseController.failureModel("产品组不存在或不可用").toJSON();
		}
		productGroup
				.setStatus(productGroup.getStatus() == BaseModel.APPROVED ? BaseModel.UNAPPROVED
						: BaseModel.APPROVED);
		Status status = groupFacade.updateProgroupStatus(productGroup);
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台产品组状态改变", "product_group", productGroup.getId(),
					Status.SUCCESS.message(), null, "成功，状态改变为="+productGroup.getId(), request);
			ProductGroupVo data = new ProductGroupVo();
			data.buildData(productGroup);
			data.setStatus(status);
			return data.toJSON();
		}else{
			BaseController.bulidRequest("后台产品组状态改变", "product_group", productGroup.getId(),
					Status.FAILURE.message(), null, "失败，状态改变为="+productGroup.getId(), request);
		}
		return BaseController.failureModel("产品组信息状态更新失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/product/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateProduct(HttpServletRequest request,
			@RequestParam String token, @RequestParam String uuid,
			@RequestParam String name, @RequestParam String manufacturer_uuid,
			@RequestParam String serialNum) {
		ProductGroup productGroup = groupFacade.selectProgroupByUuid(uuid);
		if (productGroup == null || !productGroup.isActive()) {
			BaseController.bulidRequest("后台产品组信息修改", "product_group", null,
					Status.FAILURE.message(), null, "uuid="+uuid+" 产品组不存在", request);
			return BaseController.failureModel("产品组不存在或不可用").toJSON();
		}
		Manufacturer manufacturer = groupFacade
				.selectManufacturerByUuid(manufacturer_uuid);
		if (manufacturer == null || !manufacturer.isActive()) {
			return BaseController.failureModel("厂商不存在或不可用").toJSON();
		}
		String server_token = toastRepeatSubmitToken(request, UPDATE_GROUP_KEY);
		if (!token.equals(server_token)) {
			return BaseController.failureModel("重复提交数据").toJSON();
		} else {
			productGroup.setName(Tools.nullValueFilter(name));
			productGroup.setPinyin(Tools.getPinYin(name));
			productGroup.setManufacturer(manufacturer);
			productGroup.setSerialNum(serialNum);
			Status status = groupFacade.updateProgroup(productGroup);
			if (status == Status.SUCCESS) {
				BaseController.bulidRequest("后台产品组信息修改", "product_group", productGroup.getId(),
						Status.SUCCESS.message(), null, "成功", request);
				return BaseController.successModel().toJSON();
			}else{
				BaseController.bulidRequest("后台产品组信息修改", "product_group", productGroup.getId(),
						Status.FAILURE.message(), null, "失败", request);
			}
		}
		return BaseController.failureModel("信息修改失败").toJSON();
	}

	/**
	 * 生成流量卡
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/active/add/page")
	public ModelAndView addCodePage(HttpServletRequest request) {
		List<Manufacturer> list = groupFacade.selectManufacturer();
		List<OptionAddition> addList = OptionAddition.buildData(list);
		request.setAttribute("manufacturer_list", addList);
		return index(request, "/wrapped/product/activation_add.jsp");
	}

	/**
	 * 产品组关联
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/active/group/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String searchGroup(HttpServletRequest request,
			@PathVariable String uuid) {
		Manufacturer manufacturer = groupFacade.selectManufacturerByUuid(uuid);
		if (manufacturer == null || !manufacturer.isActive()) {
			return BaseController.failureModel("厂商不可用").toJSON();
		}
		List<ProductGroup> list = groupFacade
				.getProductGroupListById(manufacturer.getId());
		OptionAdditionList model = new OptionAdditionList();
		model.setStatus(Status.SUCCESS);
		model.buildGroup(list);
		return model.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/active/list/page")
	public ModelAndView listCodePage(HttpServletRequest request) {
		return index(request, "/wrapped/product/activation_list.jsp");
	}
	
	//modified by mayuan start
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/share/list/page")
	public ModelAndView shareResPage(HttpServletRequest request) {
		return index(request, "/wrapped/product/share_list.jsp");
	}
	//modified by mayuan end

	@RequestMapping(value = "/active/list/data")
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	public @ResponseBody String activeListData(HttpServletRequest request) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageItemTotal = sizeObj == null ? 10 : Integer
				.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ActivationCode> pagination = activationFacade
				.selectActivationList(searchValue, from, pageItemTotal);
		ActiviteCodeSurface suface = new ActiviteCodeSurface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.build(pagination.getResults());
		return gson.toJson(suface);
	}
	
	//modified by mayuan --> show sharedetail 
	@RequestMapping(value="/share/list/data")
	@RequestAuthority(value=Role.PRO_EDITOR, logger=false)
	@ResponseBody
	public String shareListData(HttpServletRequest request){
		String searchValue=nullValueFilter(request.getParameter("sSearch"));
		Object tmp_iDisplayLen = request.getParameter("iDisplayLength");
		int pageSize = tmp_iDisplayLen == null ? 10 : Integer.valueOf((String) tmp_iDisplayLen);
		Object tmp_iDisplayStart = request.getParameter("iDisplayStart");
		int start = tmp_iDisplayStart == null ? 0 : Integer.valueOf((String) tmp_iDisplayStart);
		
		Pagination<ViewSharing> pagination = activationFacade.selectShareList(searchValue, start, pageSize);
		ShowShareListSurface suface = new ShowShareListSurface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		suface.build(pagination.getResults());
		return gson.toJson(suface);
	}
	//modified by mayuan end

	@RequestAuthority(value = Role.PRO_EDITOR,logger = false)
	@RequestMapping(value = "/active/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String addActiveData(HttpServletRequest request,
			@RequestParam String group_uuid, @RequestParam String time,
			@RequestParam String num) {
		ProductGroup productGroup = groupFacade
				.selectProgroupByUuid(group_uuid);
		if (productGroup == null || !productGroup.isActive()) {
			return BaseController.failureModel("产品组不存在或不可用").toJSON();
		}
		int timeint = Integer.parseInt(time);
		int numint = 1;
		if (!num.isEmpty()) {
			numint = Integer.parseInt(num);
		}
		if (numint <= 0) {
			numint = 1;
		}
		if (numint > 1000) {
			numint = 1000;
		}
		Status status=activationFacade.addBuildActivite(numint, productGroup, timeint);
		 if (status == Status.SUCCESS) {
		 return BaseController.successModel().toJSON();
		 }
		return BaseController.failureModel("增加激活码失败！").toJSON();
	}
	
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/code/view", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String codeView(HttpServletRequest request,
			@RequestParam String uuid) {
		ActivationCode code = activationFacade.getActivationByUuid(uuid);
		if (code == null || !code.isActive()) {
			return BaseController.failureModel("激活码不可用").toJSON();
		}
		BaseAPIModel base=BaseController.successModel();
		base.setMessage(code.getActiveCode());
		return base.toJSON();
	}
	
	@RequestAuthority(value = Role.PRO_EDITOR,logger=false)
	@RequestMapping(value = "/code/disable", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String codeDisable(HttpServletRequest request,
			 @RequestParam String uuid) {
		ActivationCode code = activationFacade.getActivationByUuid(uuid);
		if (code == null) {
			return BaseController.failureModel("激活码不存在").toJSON();
		}
		code.setIsActive(BaseModel.DISABLE);
		Status status=activationFacade.updateDisCode(code);
		if (status == Status.SUCCESS) {
			return BaseController.successModel().toJSON();
		}
		return BaseController.failureModel("激活码禁用失败").toJSON();
	}
	
	/*public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("0000");
		System.out.println(df.format(3000));
	}*/
}
