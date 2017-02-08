package com.lankr.tv_cloud.web.api.webchat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.model.QrcodeScanRecode;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.SignatureUtil;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.util.WxAccseeTokenByCode;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class WxBusinessControl extends BaseWechatController {
	/**
	 * 2016-04-19 有关微信有关的所有的跳转授权，都来自此处
	 */

	/**
	 * 微信接口，不需要验证
	 * 
	 * @param signature微信加密签名
	 *            ，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 * @param timestamp时间戳
	 * @param nonce随机数
	 * @param echostr随机字符串
	 * @param request
	 * @return请原样返回echostr参数内容，则接入生效
	 * 
	 *                               加密/校验流程如下： 1.
	 *                               将token、timestamp、nonce三个参数进行字典序排序 2.
	 *                               将三个参数字符串拼接成一个字符串进行sha1加密 3.
	 *                               开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 */
	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/qq/entrance", method = RequestMethod.GET)
	@ResponseBody
	public String fristSignature(HttpServletRequest request) {
		System.out.println("首次接入认证");
		String echostr = request.getParameter("echostr");
		System.out.println("echostr：" + echostr);
		if (checkChatSignature(request)) {
			return echostr;
		} else {
			System.out.println("不是微信接入");
			return null;
		}
	}

	/**
	 * 微信推送消息入口，推送消息，不需要验证
	 * 
	 * @param request
	 * @return
	 */
	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/qq/entrance", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getWeiXinMessage(HttpServletRequest request) {
		if (checkChatSignature(request)) {
			String respMessage = webchatFacade.handleMessage(request);
			return respMessage;
		} else {
			return "数据来源不可用";
		}
	}

	/**
	 * 不是来自微信
	 * 
	 * @param request
	 * @return
	 */
	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/not/wx", method = RequestMethod.GET)
	public ModelAndView notWx(HttpServletRequest request) {
		return redirectPageView("wechat/not_webchat");
	}

	/**
	 * 所有二维码的推送消息的链接入口
	 */
	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/business/auth/qr/common", method = RequestMethod.GET)
	public ModelAndView authCommonSkip(HttpServletRequest request,
			@RequestParam int state, @RequestParam String code,
			@RequestParam long sceneid, @RequestParam String redirect_uri) {
		String authFlag = authJudyUser(request, code);
		if (authFlag.equals(WX_LOGIN_ING)) {
			User user = getCurrentUser(request);
			// 记录sceneid 查看次数
			senceRecord(sceneid, user);
		}
		String authUrl = ultimateAuthUrl(authFlag, redirect_uri);
		return redirectUrlView(authUrl);
	}

	/**
	 * 处理二维码相关
	 * 
	 * @param sceneid
	 * @param user
	 */
	public void senceRecord(long sceneid, User user) {
		QrScene qrScene = qrCodeFacade.selectQrSceneByScenId(sceneid);
		if (qrScene != null) {
			int type = qrScene.getType();
			if (type == QrScene.RESOURCE_TYPE) {
				QrCode qrCode = qrCodeFacade.selectQrCodeByScenId(qrScene
						.getSceneid());
				qrCodeFacade.updateQrSaoCount(qrCode);
			}
			QrcodeScanRecode qrcodeScanRecode = qrCodeFacade
					.selectRecodeByUser(user.getId(), qrScene.getId());
			if (qrcodeScanRecode == null) {
				qrcodeScanRecode = new QrcodeScanRecode();
				qrcodeScanRecode.setUuid(Tools.getUUID());
				qrcodeScanRecode.setIsActive(BaseModel.ACTIVE);
				qrcodeScanRecode.setStatus(BaseModel.APPROVED);
				qrcodeScanRecode.setQrScene(qrScene);
				qrcodeScanRecode.setUser(user);
				qrcodeScanRecode.setScancount(1);
				qrcodeScanRecode.setViewcount(1);
				qrCodeFacade.addQrcodeScanRecode(qrcodeScanRecode);
			} else {
				qrcodeScanRecode
						.setViewcount(qrcodeScanRecode.getViewcount() + 1);
				qrCodeFacade.updateQrcodeScanRecode(qrcodeScanRecode);
			}
		}
	}

	/**
	 * 对request的合法性认证
	 * 
	 * @param request
	 * @return
	 */
	public boolean checkChatSignature(HttpServletRequest request) {
		boolean flag = false;
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		if (signature == null || signature.isEmpty())
			return flag;

		if (timestamp == null || timestamp.isEmpty())
			return flag;

		if (nonce == null || nonce.isEmpty())
			return flag;

		System.out.println("signature：" + signature);
		System.out.println("timestamp：" + timestamp);
		System.out.println("nonce：" + nonce);

		if (signature.equals(SignatureUtil.generateEventMessageSignature(
				WebChatMenu.ZHILIAOTOKEN, timestamp, nonce))) {
			flag = true;
		}
		return flag;
	}


	/**
	 * 2016-04-28 所有的登录，一般页面分享，菜单，自动跳转的入口 模板消息的链接入口 state 以后可以规整为来源
	 */
	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/auth/common/skip", method = RequestMethod.GET)
	public ModelAndView authCommonSkip(HttpServletRequest request,
			@RequestParam String state, @RequestParam String code,
			@RequestParam String redirect_uri) {
		String authFlag = authJudyUser(request, code);
		String authUrl = ultimateAuthUrl(authFlag, redirect_uri);
		return redirectUrlView(authUrl);
	}
	
	/**
	 * 2016-12-19 授权获取信息的回调页面,获取游客信息
	 */
	@RequestMapping(value = "/auth/common/visitor", method = RequestMethod.GET)
	public ModelAndView authCommonVisitor(HttpServletRequest request,
			@RequestParam String state, @RequestParam String code,
			@RequestParam String redirect_uri) {
		//1.开始获取openid
		WxAccseeTokenByCode accseeTokenByCode=WxUtil.getOpenIdByCode(code);
		if(accseeTokenByCode==null){
			setKeySession(WebChatInterceptor.NOT_HANDLE_VISITOR, request, WebChatInterceptor.NOT_HANDLE_VISITOR);
			return visitorView(redirect_uri);
		}
		String openid=accseeTokenByCode.getOpenid();
		if(Tools.isBlank(openid)){
			setKeySession(WebChatInterceptor.NOT_HANDLE_VISITOR, request, WebChatInterceptor.NOT_HANDLE_VISITOR);
			return visitorView(redirect_uri);
		}
		//session 放置openid 和信息获取标志
		setVisitorSession(request,openid);
		//剩下的获取用户信息交给后台线程处理
		webchatFacade.visitorWxInfo(accseeTokenByCode);
		return visitorView(redirect_uri);
	}
	
	//set session openid和信息获取标志
	public void setVisitorSession(HttpServletRequest request,String opneid){
		setOpenid(opneid, request, OPENID_KEY);
		setKeySession(WebChatInterceptor.HANDLE_VISITOR, request, WebChatInterceptor.HANDLE_VISITOR);
	}
	
	public ModelAndView visitorView(String redirect_uri){
		if(Tools.isBlank(redirect_uri)){
			redirect_uri=WX_INDEX;
		}
		return redirectUrlView(redirect_uri);
	}

}
