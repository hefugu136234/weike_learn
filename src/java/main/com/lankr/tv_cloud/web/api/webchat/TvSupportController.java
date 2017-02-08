/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月30日
 * 	@modifyDate 2016年6月30日
 *  
 */
package com.lankr.tv_cloud.web.api.webchat;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.model.QrInteractChannel;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.TvQrAuth;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.qr.QrLoginManagement;
import com.lankr.tv_cloud.qr.QrSearchManagement;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.vo.api.SimpleDataType;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.api.webchat.vo.WxPdf;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxThreeScreen;
import com.lankr.tv_cloud.web.api.webchat.vo.WxUserShowInfo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxVideo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

/**
 * @author Kalean.Xiang
 *
 */
@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class TvSupportController extends BaseWechatController {

	@Autowired
	private QrLoginManagement qrLoginManagement;

	@Autowired
	private CodeProvider codeProvider;
	@Autowired
	private QrSearchManagement qrSearchManagement;

	public static final String USER_KEY = "user_key";

	public static final String QR_UUID = "qr_uuid";

	public static final String SHOW_INFO = "showInfo";

	public static final String OPEN_ID = "openid";

	@Override
	public ModelAndView redirectErrorPage(HttpServletRequest request,
			String errorMessage) {
		// TODO Auto-generated method stub
		return redirectErrorPage(request, errorMessage, "tv/auth_error");
	}

	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/auth/test", method = RequestMethod.GET)
	public ModelAndView tvAuthTest(HttpServletRequest request) {
		if (Config.isProductEnv()) {
			return redirectUrlView("/");
		}
		return redirectPageView("tv/login");
	}

	/**
	 * 2016-07-11 tv授权微信登录扫码的入口
	 * 
	 * @param request
	 * @param uuid
	 * @return 返回一个授权页面
	 */
	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/auth/link", method = RequestMethod.GET)
	public ModelAndView tvAuthLink(HttpServletRequest request,
			@RequestParam String qr_uuid) {
		ActionMessage action = qrLoginManagement.notifyTvAuth(qr_uuid, null);
		String agent = request.getHeader("User-Agent");
		if (agent != null && agent.contains("MicroMessenger")) {
			// 微信
			String authLink = WebChatMenu.LOCAL_URL
					+ "/tv/auth/entrance?qr_uuid=" + qr_uuid;
			authLink = WebChatMenu.wxAuthLinkNoDefault(authLink);
			return redirectUrlView(authLink);
		} else {
			/**
			 * 2016-07-11 需要讨论 若是注册，拿不到openid
			 */
			// setKeySession(qr_uuid, request, QR_UUID);
			// return redirectPageView("tv/auth_page");
			if (Config.isDevEnv()) {
				setKeySession(qr_uuid, request, QR_UUID);
				return redirectPageView("tv/auth_page");
			}
			return redirectErrorPage(request, "请在微信中扫码");
		}
	}

	/**
	 * 不影响微信系统只session值
	 * 
	 * @param request
	 * @param uuid
	 * @param state
	 * @param code
	 * @param redirect_uri
	 * @return
	 */
	//@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/auth/entrance", method = RequestMethod.GET)
	public ModelAndView tvAuthEntrance(HttpServletRequest request,
			@RequestParam String state, @RequestParam String qr_uuid,
			@RequestParam String code) {
		setKeySession(qr_uuid, request, QR_UUID);
		User user = getCurrentUser(request);
		if (user != null) {
			// 有user 进入去确认user
			// 头部公共信息
			setShowInfo(user, request);
			request.getSession().setAttribute(USER_KEY, user);
			return redirectPageView("tv/auth_login");
		}
		String openid = getOpenId(OPENID_KEY, request);
		if (openid == null) {
			openid = getKeySession(OPEN_ID, request);
			if (openid == null) {
				openid = WxUtil.getOpenIdByCode(code, 1);
				if (openid == null || openid.isEmpty()) {
					// 新的错误页面
					return redirectErrorPage(request, "请在微信中扫码");
				}
				setKeySession(openid, request, OPEN_ID);
				setOpenid(openid, request, OPENID_KEY);
			}
		}
		WebchatUser webchatUser = null;
		if (checkUser(openid)) {
			webchatUser = webchatFacade.searchWebChatUserByOpenid(openid);
			if (webchatUser == null) {
				return redirectPageView("tv/auth_page");
			}
		} else {
			return redirectPageView("tv/subscribe");
		}
		user = webchatUser.getUser();
		if (user == null) {
			// 登录
			return redirectPageView("tv/auth_page");
		}

		setShowInfo(user, request);
		request.getSession().setAttribute(USER_KEY, user);
		return redirectPageView("tv/auth_login");
	}

	private boolean checkUser(String openid) {
		int count = webchatFacade.searchCountByOpenid(openid);
		return count > 0;
	}

	private void setShowInfo(User user, HttpServletRequest request) {
		String photo = OptionalUtils.traceValue(user, "avatar");
		photo = WxUtil.getDefaultAvatar(photo);
		request.getSession().setAttribute(
				SHOW_INFO,
				WxUserShowInfo.authShow(
						OptionalUtils.traceValue(user, "nickname"), photo));
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/connect/confirm", method = RequestMethod.GET)
	public ModelAndView connect(HttpServletRequest request) {
		String qr_uuid = getKeySession(QR_UUID, request);
		ActionMessage action = qrLoginManagement.notifyTvAuth(qr_uuid, null);
		return redirectPageView("tv/auth_page");
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/qr/login", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public @ResponseBody String qrLogin(HttpServletRequest request,
			@RequestParam String username, @RequestParam String password) {
		User user = userFacade.login(username, Md5.getMd5String(password));
		ActionMessage action = qrLoginManagement.tvUserCheck(user);
		if (!action.isSuccess()) {
			return actionModel(action).toJSON();
		}
		request.getSession().setAttribute(USER_KEY, user);
		return successModel("登录成功").toJSON();
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/comfim/auth", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public @ResponseBody String tvComfirmAuth(HttpServletRequest request) {

		String qr_uuid = getKeySession(QR_UUID, request);
		// null可以任意转换，只有在真正调用时才会报转换错误
		User oldUser = (User) request.getSession().getAttribute(USER_KEY);
		if (oldUser == null) {
			return actionModel(codeProvider.code(-2001).getActionMessage())
					.toJSON();
		}
		User user = userFacade.getUserByUuid(oldUser.getUuid());
		// if (user == null) {
		// return failureModel("用户不存在或未登录").toJSON() ;
		// }
		//
		// UserReference reference = user.getUserReference();
		// if (reference == null || !reference.apiUseable()) {
		// return failureModel("您当前没有权限登录").toJSON();
		// }
		// Role r = reference.getRole();
		// if (!r.isBoxUser()) {
		// return failureModel("您当前没有权限登录").toJSON();
		// }
		// if (!reference.isDateValid()) {
		// return failureModel("流量卡不足,请充值流量卡后再登录").toJSON();
		// }
		ActionMessage action = qrLoginManagement.tvUserCheck(user);
		if (!action.isSuccess()) {
			return actionModel(action).toJSON();
		}
		ActionMessage actionMessage = qrLoginManagement.notifyTvAuth(qr_uuid,
				user);
		return actionModel(actionMessage).toJSON();
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/auth/go/vip", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String authGoVip(HttpServletRequest request) {
		User oldUser = (User) request.getSession().getAttribute(USER_KEY);
		removeAuthSession(request, USER_KEY, QR_UUID, OPEN_ID);
		if (oldUser != null) {
			// 放置session
			setCurrentSessionUser(oldUser, request);
			return successModel().toJSON();
		} else {
			return failureModel().toJSON();
		}
	}

	private void removeAuthSession(HttpServletRequest request, String userKey,
			String qrUuid, String openId) {
		removeKeySession(request, userKey);
		removeKeySession(request, qrUuid);
		removeKeySession(request, openId);
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/auth/success", method = RequestMethod.GET)
	public ModelAndView tvAuthSuccess(HttpServletRequest request) {
		return redirectErrorPage(request, "授权成功");
	}

	/*
	 * @RequestAuthority(requiredProject = false, logger = false)
	 * 
	 * @RequestMapping(value = "/tv/test", method = RequestMethod.GET) public
	 * ModelAndView test(HttpServletRequest request) { WebchatUser webchatUser =
	 * null ; if (checkUser("ol6vZwsrwh_cKu1lrtLh5o-fVjDU")) { webchatUser =
	 * webchatFacade .searchWebChatUserByOpenid("ol6vZwsrwh_cKu1lrtLh5o-fVjDU");
	 * if (webchatUser == null ) { return redirectPageView("tv/auth_page") ; } }
	 * else { return redirectPageView("tv/subscribe") ; } return
	 * redirectPageView("tv/auth_login") ; }
	 */

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/qr/auth/changeUser", method = RequestMethod.GET)
	public ModelAndView tvChangeUser(HttpServletRequest request) {
		return redirectPageView("tv/auth_page");
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/qr/forget/password", method = RequestMethod.GET)
	public ModelAndView forgetPassword(HttpServletRequest request) {
		return redirectPageView("tv/forget");
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/auth/loginConfirm", method = RequestMethod.GET)
	public ModelAndView loginConfirm(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(USER_KEY);
		if (user == null || !user.apiUseable()) {
			return redirectPageView("tv/auth_page");
		}
		setShowInfo(user, request);
		return redirectPageView("tv/auth_login");
	}

	/**
	 * tv 搜索测试
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/search/test", method = RequestMethod.GET)
	public ModelAndView tvSearchStartPage(HttpServletRequest request) {
		return redirectPageView("tv/search/search_start");
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/search/result", method = RequestMethod.GET)
	public ModelAndView tvSearchResult(HttpServletRequest request,
			@RequestParam String qr_uuid, @RequestParam int referId,
			@RequestParam String device) {
		Resource resource = effectResourceFacade().getResourceById(referId);
		if (resource == null || !resource.apiUseable()) {
			return redirectErrorPage(request, "资源已下线", "tv/search/qr_error");
		}
		request.setAttribute("qr_search_uuid", qr_uuid);
		request.setAttribute("qr_search_res_id", referId);
		request.setAttribute("qr_device", device);
		if (resource.getType() == Type.NEWS) {
			WxResourceItem item = new WxResourceItem();
			item.buildDetailLittle(resource);
			request.setAttribute("res_vo", item);
			request.setAttribute("sub_page", "/tv/search/news_detail.jsp");
			return redirectPageView("tv/search/search_result");
		} else if (resource.getType() == Type.VIDEO) {
			WxResourceItem item = buildResFirstPage(request, resource);
			WxVideo vo = new WxVideo();
			vo.buildData(resource.getVideo());
			item.setVideo(vo);
			request.setAttribute("res_vo", item);
			request.setAttribute("sub_page", "/tv/search/video_detail.jsp");
			return redirectPageView("tv/search/search_result");
		} else if (resource.getType() == Type.PDF) {
			WxResourceItem item = buildResFirstPage(request, resource);
			WxPdf vo = new WxPdf();
			vo.build(resource.getPdf());
			item.setPdf(vo);
			request.setAttribute("res_vo", item);
			request.setAttribute("sub_page", "/tv/search/pdf_detail.jsp");
			return redirectPageView("tv/search/search_result");
		} else if (resource.getType() == Type.THREESCREEN) {
			WxResourceItem item = buildResFirstPage(request, resource);
			WxThreeScreen vo = new WxThreeScreen();
			vo.buildFirstPage(resource.getThreeScreen());
			item.setThreeScreen(vo);
			request.setAttribute("res_vo", item);
			request.setAttribute("sub_page",
					"/tv/search/threescreen_detail.jsp");
			return redirectPageView("tv/search/search_result");
		}
		return redirectErrorPage(request, "资源数据类型未识别", "tv/search/qr_error");
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/search/qr", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String searchQr(HttpServletRequest request,
			@RequestParam String device) {
		String ip = getClientIpAddr(request);
		ActionMessage<QrInteractChannel> actionMessage = qrSearchManagement
				.addSearchQrForResource(ip, device);
		if (actionMessage.isSuccess()) {
			QrInteractChannel qrInteractChannel = actionMessage.getData();
			if (qrInteractChannel != null) {
				long deadline = TimeUnit.MINUTES
						.toMillis(Config.qr_search_deadline);
				return BaseAPIModel.makeSimpleSuccessInnerDataJson(
						"请用微信客户端扫描上面二维码~", 0, new SimpleData("uuid",
								qrInteractChannel.getUuid()), new SimpleData(
								"qrUrl", qrInteractChannel.getQrUrl()),
						new SimpleData("create_time", qrInteractChannel
								.getCreateDate().getTime(),
								SimpleDataType.Number),
						new SimpleData("expired_time", deadline,
								SimpleDataType.Number));
			}
			return failureModel("未能获取到搜索二维码").toJSON();
		} else {
			return actionModel(actionMessage).toJSON();
		}
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/search/qr/connect", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String searchQrConnect(HttpServletRequest request,
			@RequestParam String device, @RequestParam String uuid) {
		String ip = getClientIpAddr(request);
		ActionMessage<QrInteractChannel> actionMessage = qrSearchManagement
				.pollingSearchQr(ip, device, uuid);
		if (actionMessage.getCode() == 207) {
			// 进入交互状态
			QrInteractChannel qrInteractChannel = actionMessage.getData();
			if (qrInteractChannel != null) {
				long time = TimeUnit.SECONDS.toMillis(30);
				if (System.currentTimeMillis()
						- qrInteractChannel.getLastScanDate().getTime() > time) {
					return failureModel("客户端停止搜索").toJSON();
				}
				return BaseAPIModel
						.makeSimpleSuccessInnerDataJson(
								actionMessage.getMessage(),
								actionMessage.getCode(),
								new SimpleData("ticket", qrInteractChannel
										.getTicket()),
								new SimpleData("referId", qrInteractChannel
										.getReferId(), SimpleDataType.Number),
								new SimpleData("update_time", qrInteractChannel
										.getLastScanDate().getTime(),
										SimpleDataType.Number));
			}
		}
		return actionModel(actionMessage).toJSON();
	}

	// 扫描之后返回的搜索页面
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/search/resource/link", method = RequestMethod.GET)
	public ModelAndView tvSearchResource(HttpServletRequest request,
			@RequestParam String qr_uuid) {
		if (Config.isDevEnv()) {
			return tvSearchPage(request, qr_uuid, null);
		}
		String agent = request.getHeader("User-Agent");
		if (agent != null && agent.contains("MicroMessenger")) {
			// 微信
			String authLink = WebChatMenu.LOCAL_URL
					+ "/tv/auth/search/entrance?qr_uuid=" + qr_uuid;
			authLink = WebChatMenu.wxAuthLinkNoDefault(authLink);
			return redirectUrlView(authLink);
		}
		return tvSearchPage(request, qr_uuid, null);
	}

	public ModelAndView tvSearchPage(HttpServletRequest request,
			String qr_uuid, User user) {
		ActionMessage actionMessage = qrSearchManagement.scanQrSearch(qr_uuid,
				user);
		if (actionMessage.isSuccess()) {
			request.setAttribute("uuid", qr_uuid);
			request.setAttribute("user_uuid",
					OptionalUtils.traceValue(user, "uuid"));
			return redirectPageView("tv/search/wx_search");
		}
		return redirectErrorPage(request, actionMessage.getMessage(),
				"tv/search/qr_error");
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/auth/search/entrance", method = RequestMethod.GET)
	public ModelAndView tvSearchEntrance(HttpServletRequest request,
			@RequestParam String state, @RequestParam String qr_uuid,
			@RequestParam String code) {
		User user = getCurrentUser(request);
		String openid = null;
		if (user == null) {
			openid = getOpenId(OPENID_KEY, request);
		}
		if (openid == null) {
			openid = getKeySession(OPEN_ID, request);
		}
		if (openid == null) {
			openid = WxUtil.getOpenIdByCode(code, 1);
		}
		if (openid != null) {
			setKeySession(openid, request, OPEN_ID);
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByOpenid(openid);
			if (webchatUser != null) {
				user = webchatUser.getUser();
			}
		}
		return tvSearchPage(request, qr_uuid, user);
	}

	/**
	 * 接收搜索页面开启中
	 * 
	 * @param request
	 * @param device
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/search/page/alive", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String searchPageAlive(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String ticket) {
		ActionMessage actionMessage = qrSearchManagement.searchPageAlive(uuid,
				ticket);
		return actionModel(actionMessage).toJSON();
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/search/shadow/action", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String searchShadow(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String user_uuid,
			@RequestParam String res_uuid, @RequestParam String ticket) {
		Resource resource = effectResourceFacade().getResourceByUuid(res_uuid);
		if (resource == null || !resource.apiUseable()) {
			return failureModel("资源已下线").toJSON();
		}
		ActionMessage<QrInteractChannel> actionMessage = qrSearchManagement
				.shadowAction(uuid, resource.getId(), ticket);
		if (actionMessage.getCode() == 210) {
			QrInteractChannel qrInteractChannel = actionMessage.getData();
			if (qrInteractChannel != null) {
				return BaseAPIModel
						.makeSimpleSuccessInnerDataJson(
								actionMessage.getMessage(),
								actionMessage.getCode(),
								new SimpleData("ticket", qrInteractChannel
										.getTicket()));
			}
		}
		return actionModel(actionMessage).toJSON();
	}
}
