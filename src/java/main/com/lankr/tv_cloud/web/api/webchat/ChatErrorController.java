package com.lankr.tv_cloud.web.api.webchat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class ChatErrorController extends BaseWechatController{
	/**
	 * 2016-06-07 
	 * 系统出错
	 * 慢慢升级v2版本
	 */
	/**
	 * 所有出错页面的汇总页面 一般由/auth/logined/before接口跳入
	 */
	@RequestAuthority(requiredProject = false, logger = false,wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/error/page", method = RequestMethod.GET)
	public ModelAndView errorPage(HttpServletRequest request) {
		String error = errorMessage(request);
		if (error != null && !error.isEmpty()) {
			error = "数据出错，请刷新页面";
		}
		return redirectErrorPage(request, error);
	}

}
