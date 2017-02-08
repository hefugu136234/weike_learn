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
public class VrResourceController extends BaseWechatController{
	
	@RequestMapping(value = "/vr/test/show", method = RequestMethod.GET)
	public ModelAndView vrTestShow(HttpServletRequest request) {
		return redirectPageView("wechat/resource/vr");
	}
	
	@RequestMapping(value = "/vr/doiwn/a", method = RequestMethod.GET)
	public ModelAndView vrDownA(HttpServletRequest request) {
		return redirectPageView("wechat/resource/vr");
	}
	
	@RequestMapping(value = "/vr/doiwn/b", method = RequestMethod.GET)
	public ModelAndView vrDownB(HttpServletRequest request) {
		return redirectPageView("wechat/resource/vr");
	}
	
	@RequestMapping(value = "/vr/doiwn/c", method = RequestMethod.GET)
	public ModelAndView vrDownC(HttpServletRequest request) {
		return redirectPageView("wechat/resource/vr");
	}
	
	@RequestMapping(value = "/vr/doiwn/d", method = RequestMethod.GET)
	public ModelAndView vrDownD(HttpServletRequest request) {
		return redirectPageView("wechat/resource/vr");
	}

}
