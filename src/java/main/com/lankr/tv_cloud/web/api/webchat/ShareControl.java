package com.lankr.tv_cloud.web.api.webchat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.SharingRes;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class ShareControl extends BaseWechatController {
	/**
	 * 所有有关分享的操作
	 */

	/**
	 * 2016-06-21 修改 当一般页面分享成功后的日志
	 * 
	 * @param request
	 * @param url
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/common/share/page", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String shareResCommon(HttpServletRequest request,
			@RequestParam String url) {
		if (url.length() > 150) {
			url = url.substring(0, 150);
		}
		bulidRequest("微信页面分享", null, null, Status.SUCCESS.message(), null, url,
				request);
		return successModel().toJSON();
	}

	/**
	 * 当资源分享成功后的日志 2016-06-21 修改
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/share/res", method = RequestMethod.POST,produces = "text/json; charset=utf-8")
	@ResponseBody
	public String shareRes(HttpServletRequest request,
			@RequestParam String type, @RequestParam String uuid,
			@RequestParam(required = false) String oriUserUuid) {
		Resource resource = resourceFacade.getResourceByUuid(uuid);
		if (resource == null) {
			return failureModel().toJSON();
		}
		if (resource.getType() == Type.NEWS) {
			String categoryName = OptionalUtils.traceValue(resource,
					"category.name");
			if (categoryName.equals("分享有礼")) {
				// 分享有礼
				User user = getCurrentUser(request);
				User oriUser = null;
				if (oriUserUuid != null && !oriUserUuid.isEmpty()) {
					oriUser = userFacade.getUserByUuid(oriUserUuid);
				}
				SharingRes sharingRes = new SharingRes();
				sharingRes.setUuid(Tools.getUUID());
				sharingRes.setResource(resource);
				sharingRes.setUser(user);
				sharingRes.setApproach(type);
				sharingRes.setIsActive(BaseModel.ACTIVE);
				sharingRes.setStatus(BaseModel.APPROVED);
				sharingRes.setOriUser(oriUser);
				shareGiftMpper.addShareRes(sharingRes);
				bulidRequest("微信分享-分享有礼-" + type, "sharing_res",
						sharingRes.getId(), Status.SUCCESS.message(),
						resource.getName(), "成功", request);
				return successModel().toJSON();
			}
		}
		bulidRequest("微信资源分享", "resource", resource.getId(),
				Status.SUCCESS.message(), resource.getName(), "资源uuid=" + uuid,
				request);
		return successModel().toJSON();
	}

	/**
	 * 对直播分享以后的记录
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/cast/share/record", method = RequestMethod.POST,produces = "text/json; charset=utf-8")
	@ResponseBody
	public String castShare(HttpServletRequest request,
			@RequestParam String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null) {
			return failureModel().toJSON();
		}
		bulidRequest("微信直播分享", "broadcast", broadcast.getId(),
				Status.SUCCESS.message(), broadcast.getName(),
				"直播uuid=" + uuid, request);
		return successModel().toJSON();
	}

}
