package com.lankr.tv_cloud.web;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.lankr.orm.mybatis.mapper.CategoryExpandMapper;
import com.lankr.orm.mybatis.mapper.HospitalMapper;
import com.lankr.orm.mybatis.mapper.ProvinceMapper;
import com.lankr.orm.mybatis.mapper.ShareGiftMpper;
import com.lankr.orm.mybatis.mapper.SpeakerMapper;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ActivationCodeFacade;
import com.lankr.tv_cloud.facade.ActivityFacade;
import com.lankr.tv_cloud.facade.ActivityOpusFacade;
import com.lankr.tv_cloud.facade.ApplicableFacade;
import com.lankr.tv_cloud.facade.AssetFacade;
import com.lankr.tv_cloud.facade.BannerFacade;
import com.lankr.tv_cloud.facade.BroadcastFacade;
import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.CommonPraiseFacade;
import com.lankr.tv_cloud.facade.GameMgrFacade;
import com.lankr.tv_cloud.facade.GroupFacade;
import com.lankr.tv_cloud.facade.IntegralFacade;
import com.lankr.tv_cloud.facade.LuckyTmpFacade;
import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.MessageFacade;
import com.lankr.tv_cloud.facade.MyCollectionFacade;
import com.lankr.tv_cloud.facade.NewsFacade;
import com.lankr.tv_cloud.facade.NormalCollectFacade;
import com.lankr.tv_cloud.facade.NormalCollectQuestionnaireFacade;
import com.lankr.tv_cloud.facade.NormalCollectScheduleFacade;
import com.lankr.tv_cloud.facade.NotificationFacade;
import com.lankr.tv_cloud.facade.OfflineActivityFacade;
import com.lankr.tv_cloud.facade.PageRemainFacade;
import com.lankr.tv_cloud.facade.PdfFacade;
import com.lankr.tv_cloud.facade.PraiseFacade;
import com.lankr.tv_cloud.facade.ProjectCodeFacade;
import com.lankr.tv_cloud.facade.ProjectFacade;
import com.lankr.tv_cloud.facade.QrCodeFacade;
import com.lankr.tv_cloud.facade.QuestionnaireFacade;
import com.lankr.tv_cloud.facade.ReceiptAddressFacade;
import com.lankr.tv_cloud.facade.ResourceAccessIgnoreFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.ResourceGroupFacade;
import com.lankr.tv_cloud.facade.SignUpUserFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.SubscribeFacade;
import com.lankr.tv_cloud.facade.TagFacade;
import com.lankr.tv_cloud.facade.ThreeScreenFacade;
import com.lankr.tv_cloud.facade.UserFacade;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.facade.WxModelMessageFacade;
import com.lankr.tv_cloud.facade.WxSubjectFacade;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.DESUtil;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleDataType;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.web.AbsHandlerIntercepor.RedirectTile;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

public abstract class BaseController implements ResourceAPIUnionInterface {

	public static final String AUTH_KEY = "auth_token_key_request_scope";

	public static final String PROJECT_UUID = "be528a45-4d61-11e5-b667-8c6a6fec53d9";

	protected static Gson gson = new Gson();

	@Autowired
	protected UserFacade userFacade;

	@Autowired
	protected ProjectFacade projectFacade;

	@Autowired
	protected AssetFacade assetFacade;

	@Autowired
	protected APIFacade apiFacade;

	@Autowired
	protected NewsFacade newsFacade;

	@Autowired
	protected ResourceFacade resourceFacade;

	@Autowired
	protected ApplicableFacade applicableFacade;

	@Autowired
	protected MyCollectionFacade myCollectionFacade;

	@Autowired
	protected ActivationCodeFacade activationFacade;

	@Autowired
	protected WebchatFacade webchatFacade;

	@Autowired
	protected PraiseFacade praiseFacade;

	@Autowired
	protected ProvinceMapper provinceMapper;

	@Autowired
	protected HospitalMapper hospitalMapper;

	@Autowired
	protected GroupFacade groupFacade;

	@Autowired
	protected ThreeScreenFacade threeScreenFacade;

	@Autowired
	protected PdfFacade pdfFacade;

	@Autowired
	protected SpeakerMapper speakerMapper;

	@Autowired
	protected ShareGiftMpper shareGiftMpper;

	@Autowired
	protected SubscribeFacade subscribeFacade;

	@Autowired
	protected CategoryExpandMapper categoryExpandMapper;

	@Autowired
	protected ResourceFacade cacheResourceFacade;

	@Autowired
	protected APIFacade cacheAPIFacade;

	@Autowired
	protected QrCodeFacade qrCodeFacade;

	@Autowired
	protected ActivityFacade activityFacade;

	@Autowired
	protected BannerFacade bannerFacade;

	@Autowired
	protected ActivityOpusFacade activityOpusFacade;

	@Autowired
	protected TagFacade tagFacade;

	@Autowired
	protected IntegralFacade integralFacade;

	@Autowired
	protected CertificationFacade certificationFacade;

	@Autowired
	protected BroadcastFacade broadcastFacade;

	@Autowired
	private WxModelMessageFacade wxModelMessageFacade;

	@Autowired
	private WxModelMessageFacade wxUndueModelMessageFacade;

	@Autowired
	protected LuckyTmpFacade luckyTmpFacade;

	@Autowired
	protected ReceiptAddressFacade receiptAddressFacade;

	@Autowired
	protected MessageFacade messageFacade;

	@Autowired
	protected GameMgrFacade gameMgrFacade;

	@Autowired
	protected WxSubjectFacade wxSubjectFacade;

	@Autowired
	protected QuestionnaireFacade questionnaireFacade;

	@Autowired
	protected NormalCollectFacade normalCollectFacade;

	@Autowired
	protected MediaCentralFacade mediaCentralFacade;

	@Autowired
	protected PageRemainFacade pageRemainFacade;

	@Autowired
	protected NotificationFacade notificationFacade;

	@Autowired
	protected ResourceGroupFacade resourceGroupFacade;

	@Autowired
	protected CommonPraiseFacade commonPraiseFacade;

	@Autowired
	protected NormalCollectQuestionnaireFacade normalCollectQuestionnaireFacade;

	@Autowired
	protected NormalCollectScheduleFacade normalCollectScheduleFacade;

	@Autowired
	protected OfflineActivityFacade offlineActivityFacade;

	@Autowired
	protected CodeProvider codeProvider;

	@Autowired
	protected ProjectCodeFacade projectCodeFacade;

	@Autowired
	protected SignUpUserFacade signUpUserFacade;
	
	@Autowired
	protected ResourceAccessIgnoreFacade resourceAccessIgnoreFacade;

	protected Project default_project;

	public Project globalDefaultProject() {
		synchronized (PROJECT_UUID) {
			if (default_project == null) {
				default_project = projectFacade.getProjectByUuid(PROJECT_UUID);
			}
			return default_project;
		}
	}

	public WxModelMessageFacade getModelMessageFacade() {
		// System.out.println("env------------" + Config.env);
		// if (Config.env.equals(Config.Environment.PRODUCT.getValue())) {
		// return wxUndueModelMessageFacade;
		// }
//		if (Config.isProductEnv()) {
//			return wxUndueModelMessageFacade;
//		}
		return wxModelMessageFacade;
	}

	protected static String getEmptyJson() {
		return "{}";
	}

	public static BaseAPIModel successModel() {
		BaseAPIModel model = new BaseAPIModel();
		model.setStatus(Status.SUCCESS);
		return model;
	}

	public static BaseAPIModel successModel(String message) {
		BaseAPIModel model = new BaseAPIModel();
		model.setStatus(Status.SUCCESS);
		model.setMessage(message);
		return model;
	}

	public static BaseAPIModel failureModel(String message) {
		BaseAPIModel model = new BaseAPIModel();
		model.setStatus(Status.FAILURE);
		model.setMessage(message);
		return model;
	}

	public static BaseAPIModel actionModel(ActionMessage action) {
		if (action == null) {
			return failureModel();
		}
		if (action.getStatus() == Status.SUCCESS) {
			BaseAPIModel model = BaseAPIModel
					.makeWrappedSuccessBaseAPIModel(action.getData());
			model.setCode(action.getCode());
			model.setMessage(action.getMessage());
			return model;
		} else {
			BaseAPIModel m = failureModel(action.getMessage());
			m.setCode(action.getCode());
			return m;
		}
	}

	/**
	 * @author Kalean.Xiang | added 2016/10/21 | algorithm DES
	 * @param apiModel
	 * */
	public static String encriptedAPIJson(BaseAPIModel apiModel) {
		if (apiModel == null)
			return null;
		return encriptedAPIJson(apiModel.toJSON());
	}

	/**
	 * @author Kalean.Xiang | added 2016/10/21 | algorithm DES
	 * @param apiJsonString
	 *            要加密的字符串
	 * @throws Exception
	 * */
	public static String encriptedAPIJson(String apiJsonString) {
		try {
			System.out.println(apiJsonString);
			String encrypted = DESUtil.encrypt(apiJsonString);
			System.out.println("encrypted=====" + encrypted);
			System.out.println("original====" + DESUtil.decrypt(encrypted));
			return makeSimpleSuccessInnerDataJson(new SimpleData(
					"encrypted_data", encrypted), new SimpleData("encrypted",
					true, SimpleDataType.Boolean));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return failureModel("数据加密出错").toJSON();

		// model.setEncrypted(true);
		// return model.toJSON();
	}

	public static BaseAPIModel failureModel() {
		BaseAPIModel model = new BaseAPIModel();
		model.setStatus(Status.FAILURE);
		model.setMessage(Status.ERROR.message());
		return model;
	}

	public static String makeSimpleSuccessInnerDataJson(SimpleData... data) {
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(data);
	}

	@Deprecated
	public static String getStatusJson(Status status) {
		if (status == null)
			return getEmptyJson();
		return getStatusJson(status.message());
	}

	@Deprecated
	public static String getStatusJson(String message) {
		return "{\"status\":\"" + message + "\"}";
	}

	protected static String nullValueFilter(String value) {
		return value == null ? "" : value;
	}

	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	static Pattern p = Pattern.compile("^1[^1|^2|^6\\D]\\d{9}$");

	public static boolean isValidMobile(String mobile) {
		return p.matcher(mobile).matches();
	}

	public static enum Platform {
		TV_BOX, ANDROID, IOS, WEBAPP, WECHAT, PCWEB
	}

	public static final String LOG_PLATEFORM = "log_plateform";
	public static final String LOG_USERID = "log_userid";
	public static final String API_LOG_RESOURCE = "_api_log_resource";
	public static final String API_LOG_MARK = "_api_log_mark";
	public static final String LOG_TABLE_NAME = "log_table_name";
	public static final String LOG_TABLE_ID = "log_table_id";
	public static final String LOG_VERSION = "log_version";
	public static final String LOG_OPERATE = "log_operate";
	public static final String LOG_USER_AGEN = "log_user_agen";
	public static final String LOG_STATUS = "log_status";
	public static final String LOG_TITLE = "log_title";
	public static final String LOG_RESPONSETIME = "log_responsetime";
	public static final String LOG_FIELDA = "log_fielda";
	public static final String LOG_FIELDB = "log_fieldb";
	public static final String LOG_FIELDC = "log_fieldc";

	public static final String WX_TEMPLE = "wx_temple";

	public static void logForResource(HttpServletRequest request, String data) {
		request.setAttribute(API_LOG_RESOURCE, data);
	}

	public static void logForMark(HttpServletRequest request, String data) {
		request.setAttribute(API_LOG_MARK, data);
	}

	public static String readLoggerResource(HttpServletRequest request) {
		return (String) request.getAttribute(API_LOG_RESOURCE);
	}

	public static String readLoggerMark(HttpServletRequest request) {
		return (String) request.getAttribute(API_LOG_MARK);
	}

	public static Integer readIntData(HttpServletRequest request, String key) {
		Object object = request.getAttribute(key);
		return object == null ? new Integer(0) : (Integer) object;
	}

	public static String readStirngData(HttpServletRequest request, String key) {
		Object object = request.getAttribute(key);
		return object == null ? "" : String.valueOf(object);
	}

	public static void bulidRequest(String mark, String table_name,
			Integer table_id, String status, String title, String fieldA,
			HttpServletRequest request) {
		request.setAttribute(API_LOG_MARK, mark);
		request.setAttribute(LOG_TABLE_NAME, table_name);
		request.setAttribute(LOG_TABLE_ID, table_id);
		request.setAttribute(LOG_STATUS, status);
		request.setAttribute(LOG_TITLE, title);
		request.setAttribute(LOG_FIELDA, fieldA);
	}

	public static void bulidRequest(String fieldB, HttpServletRequest request) {
		if (fieldB != null && fieldB.equals(WX_TEMPLE)) {
			request.setAttribute(LOG_FIELDB, "来自微信模板消息");
		} else {
			request.setAttribute(LOG_FIELDB, fieldB);
		}
	}

	public static void bulidRequestFiledC(String fieldC,
			HttpServletRequest request) {
		request.setAttribute(LOG_FIELDC, fieldC);
	}

	// public static void bulidRequest(String mark, String table_name,
	// Integer table_id, HttpServletRequest request) {
	// request.setAttribute(API_LOG_MARK, mark);
	// request.setAttribute(LOG_TABLE_NAME, table_name);
	// request.setAttribute(LOG_TABLE_ID, table_id);
	//
	// }

	public static void bulidRequest(String mark, Resource resource,
			String uuid, HttpServletRequest request) {
		if (resource == null || !resource.isValid()) {
			request.setAttribute(LOG_STATUS, Status.FAILURE.message());
			request.setAttribute(LOG_TABLE_ID, null);
			request.setAttribute(LOG_TITLE, null);
			String logfielda = "uuid=" + uuid + ": 资源不存在或已下线";
			request.setAttribute(LOG_FIELDA, logfielda);
		} else {
			request.setAttribute(LOG_STATUS, Status.SUCCESS.message());
			request.setAttribute(LOG_TABLE_ID, resource.getId());
			String title = "资源类型:" + boxLogMark(resource) + " 名称："
					+ resource.getName();
			request.setAttribute(LOG_TITLE, title);
			request.setAttribute(LOG_FIELDA, null);
		}
		request.setAttribute(API_LOG_MARK, mark);
		request.setAttribute(LOG_TABLE_NAME, "resource");
	}

	public static String boxLogMark(Resource resource) {
		if (resource == null)
			return null;
		if (resource.getType() == Type.VIDEO) {
			return "视频";
		} else if (resource.getType() == Type.PDF) {
			return "PDF";
		} else if (resource.getType() == Type.NEWS) {
			return "新闻";
		} else if (resource.getType() == Type.THREESCREEN) {
			return "三分屏";
		}
		return null;
	}

	// 判断请求是否是恶意攻击
	public static boolean isHostileAttack(HttpServletRequest request) {
		return false;
	}

	// 判断请求是否过于频繁
	public static boolean isRequestFast() {
		return false;
	}

	private static void isSameClientRequest() {

	}

	private class ClientRequestCache extends
			LruCache<String, HttpServletRequest> {
		@Override
		protected int configMaxMemorySize() {
			return super.configMaxMemorySize();
		}
	}

	/**
	 * default resource facade effect,如果想调用缓存的资源，则需要重写该方法
	 * */
	protected ResourceFacade effectResourceFacade() {
		return resourceFacade;
	}

	protected APIFacade effectApiFacade() {
		return apiFacade;
	}

	public Resource getResourceByUuid(String uuid) {
		Resource resource = getResourceByUuid(effectResourceFacade(), uuid);
		return resource;
	}

	public static <T> T getHttpRequestWrappedData(Class<T> t) {
		HttpServletRequest request = getHandleRequest();
		if (request == null)
			return null;
		return t.cast(request.getAttribute(AUTH_KEY));
	}

	@Override
	public Resource getResourceByUuid(ResourceFacade service, String uuid) {
		return service.getResourceByUuid(uuid);
	}

	public static HttpServletRequest getHandleRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		if (ra instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) ra).getRequest();
		}
		return null;
	}

	public Platform requestKind() {
		return (Platform) getHandleRequest().getAttribute(
				AbsHandlerIntercepor.PLATFORM_REQUEST_KEY);
	}

	public boolean isRequestFromWechat() {
		Platform type = requestKind();
		if (type == Platform.WECHAT) {
			return true;
		}
		if (type == null) {
			return isRequestMobile();
		}
		return false;
	}

	public boolean isRequestMobile() {
		String user_agent = getHandleRequest().getHeader("User-Agent");
		return !Tools.isBlank(user_agent)
				&& user_agent.toLowerCase().contains("mobile");
	}

	// 跳转到指定的有
	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年3月25日
	 * @modifyDate: 2016年3月25日
	 * @return 是否已经重新转向，true | false
	 * 
	 */
	protected boolean authGrantedRedirected(HttpServletRequest request,
			HttpServletResponse response, User user) {
		try {
			HttpSession session = request.getSession();
			if (session != null) {
				RedirectTile redirect_tile = (RedirectTile) session
						.getAttribute(AbsHandlerIntercepor.LOGIN_FORWARD_URL_KEY);
				session.setAttribute(
						AbsHandlerIntercepor.LOGIN_FORWARD_URL_KEY, null);
				if (redirect_tile == null)
					return false;
				if (onLoginToRedirected(redirect_tile.auth, user)) {
					response.sendRedirect(redirect_tile.url);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年3月25日
	 * @modifyDate: 2016年3月25日 should override this method by subclass when the
	 *              rule has been changed
	 */
	protected boolean onLoginToRedirected(RequestAuthority target_auth,
			User currentUser) throws Exception {
		if (target_auth == null)
			return true;
		if (currentUser == null)
			return false;
		int role = target_auth.value();
		if (role == Role.SUPER_ADMIN_LEVEL) {
			return currentUser.getMainRole().isSuperAdmin();
		}
		if (target_auth.requiredProject()) {
			if (currentUser.getStubProject() != null) {
				Role hr = currentUser.getProUserRole();
				return hr.getLevel() <= role;
			}
		}
		return false;
	}

}
