package com.lankr.tv_cloud.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceAccessIgnore;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ResourceAccessIgnoreVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = "/resource/access")
public class ResourceAccessIgnoreController extends AdminWebController {

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/permissions/{resourceUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getResourcePermissions(HttpServletRequest request,
			@PathVariable(value = "resourceUuid") String resourceUuid) {
		ResourceAccessIgnoreVo ignoreVo = new ResourceAccessIgnoreVo();
		Resource resource = resourceFacade.getResourceByUuid(resourceUuid);
		if (null == resource)
			return failureModel("该资源信息有误，请重试").toJSON();
		ResourceAccessIgnore resourceAccess = resourceAccessIgnoreFacade
				.getByResourceId(resource.getId());
		if (null != resourceAccess) {
			ignoreVo.build(resourceAccess);
		}
		return gson.toJson(ignoreVo);
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/permissions", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String saveResourcePermissions(@RequestParam String permissions,
			@RequestParam String resourceUuid, HttpServletRequest request) {
		Resource resource = resourceFacade.getResourceByUuid(resourceUuid);
		if (null == resource)
			return failureModel("该资源信息有误，请重试").toJSON();
		ActionMessage<?> action = ActionMessage.successStatus();

		ResourceAccessIgnore resourceAccess = resourceAccessIgnoreFacade
				.getByResourceId(resource.getId());
		if (null == resourceAccess) {
			resourceAccess = new ResourceAccessIgnore();
			resourceAccess.setUuid(Tools.getUUID());
			resourceAccess.setIsActive(BaseModel.ACTIVE);
			resourceAccess.setStatus(BaseModel.APPROVED);
			resourceAccess.setResource(resource);
			resourceAccess.setDetail(permissions);
			action = resourceAccessIgnoreFacade.add(resourceAccess);
		} else {
			resourceAccess.setDetail(permissions);
			action = resourceAccessIgnoreFacade.update(resourceAccess);
		}
		if (action.isSuccess()) {
			return successModel().toJSON();
		}
		return failureModel(action.getMessage()).toJSON();
	}
}
