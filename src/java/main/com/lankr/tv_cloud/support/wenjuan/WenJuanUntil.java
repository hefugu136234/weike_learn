package com.lankr.tv_cloud.support.wenjuan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.WenJuanConfig;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;

public class WenJuanUntil {
	/**
	 * 2016-05-18 关于问卷网的所有接口
	 */

	/**
	 * 问卷网用户，第三方登录问卷网
	 * 
	 * @return 登录的链接 参数 site user ctime email
	 * 
	 * @he 2016-8-3
	 * 问卷网换了v3版接口
	 * 现在参数对应原来的参数：
	 * wj_appkey=site
	 * wj_user=user
	 * wj_timestamp=ctime 以秒为单位
	 * wj_email=email
	 * signature=md5
	 */
	public static String thirdLogin() {
		String apiUrl = "/openapi/v3/login/?";
		// 加问卷网host
		apiUrl = WenJuanConfig.wenjuan_host + apiUrl;
		Map<String, String> map = new HashMap<String, String>();
		String site = WenJuanConfig.wenjuan_site;
		map.put("wj_appkey", site);
		apiUrl = apiUrl + "wj_appkey=" + site;
		String user = WenJuanConfig.wenjuan_user;
		map.put("wj_user", user);
		apiUrl = apiUrl + "&wj_user=" + user;
		String ctime = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Tools.getCurrentDate().getTime()));
		map.put("wj_timestamp", ctime);
		apiUrl = apiUrl + "&wj_timestamp=" + ctime;
		String email = WenJuanConfig.wenjuan_email;
		map.put("wj_email", email);
		apiUrl = apiUrl + "&wj_email=" + email;
		String signature = wenjuanMd5(map);
		apiUrl = apiUrl + "&wj_signature=" + signature;
		return apiUrl;
	}
	
	/**
	 * 微信中返回的答卷链接页面 uuid=user的uuid proj_id=问卷网，对应的问卷id repeat=1，问卷网的可重复答题(可选)
	 * @he 2016-8-3
	 * 问卷网换了v3版接口
	 * wj_short_id 项目id
	 * wj_appkey=site
	 * wj_respondent=user 对应答题的source标识
	 */
	public static String wxViewLink(String uuid, String proj_id, String repeat,
			String redirect_uri,String chapterUuid) {
		String apiUrl = "/s/" + proj_id + "/?";
		apiUrl = WenJuanConfig.wenjuan_host + apiUrl;
		Map<String, String> map = new HashMap<String, String>();
		String site = WenJuanConfig.wenjuan_site;
		map.put("wj_appkey", site);
		apiUrl = apiUrl + "wj_appkey=" + site;
		String wj_respondent = uuid;
		map.put("wj_respondent", wj_respondent);
		apiUrl = apiUrl + "&wj_respondent=" + wj_respondent;
		/**
		 * 答题完毕之后回调地址，将答题的信息返回给服务器
		 */
		String wj_callback = Config.host + BaseWechatController.WX_PRIOR+"/wenjuan/entrance?chapter="+chapterUuid;
		map.put("wj_callback", wj_callback);
		apiUrl = apiUrl + "&wj_callback=" + urlEscape(wj_callback);
		
		map.put("wj_repeat", repeat);
		apiUrl = apiUrl + "&wj_repeat=" + repeat;
		
		
		if (!redirect_uri.startsWith("http")&&!redirect_uri.startsWith("https")) {
			redirect_uri = Config.host + redirect_uri;
		}
		map.put("wj_redirect_uri", redirect_uri);
		apiUrl = apiUrl + "&wj_redirect_uri=" + urlEscape(redirect_uri);
		
		String wj_signature = wenjuanMd5(map);
		apiUrl = apiUrl + "&wj_signature=" + wj_signature;
		
		return apiUrl;
	}
	
	/**
	 * 查看问卷网答题者最新一条答卷
	 * respondent 答题者的uuid
	 * @he 2016-8-3
	 * 问卷网换了v3版接口
	 * wj_timestamp 秒
	 */
	public static String pullAnswerLatest(String wj_short_id,String wj_respondent){
		String apiUrl = "/openapi/v3/get_rspd_detail/?";
		apiUrl = WenJuanConfig.wenjuan_host + apiUrl;
		Map<String, String> map = new HashMap<String, String>();
		
		String wj_appkey = WenJuanConfig.wenjuan_site;
		map.put("wj_appkey", wj_appkey);
		apiUrl = apiUrl + "wj_appkey=" + wj_appkey;
		
		String wj_user = WenJuanConfig.wenjuan_user;
		map.put("wj_user", wj_user);
		apiUrl = apiUrl + "&wj_user=" + wj_user;
		
		map.put("wj_short_id", wj_short_id);
		apiUrl = apiUrl + "&wj_short_id=" + wj_short_id;
		
		map.put("wj_respondent", wj_respondent);
		apiUrl = apiUrl + "&wj_respondent=" + wj_respondent;
		
		String wj_datatype="json";//默认html (html/json)
		map.put("wj_datatype", wj_datatype);
		apiUrl = apiUrl + "&wj_datatype=" + wj_datatype;
		
		String wj_timestamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Tools.getCurrentDate().getTime()));
		map.put("wj_timestamp", wj_timestamp);
		apiUrl = apiUrl + "&wj_timestamp=" + wj_timestamp;
		
		String wj_signature = wenjuanMd5(map);
		apiUrl = apiUrl + "&wj_signature=" + wj_signature;
		
		return apiUrl;
	}

	/**
	 * 问卷网的所有接口都需要将参数 做MD5签名
	 */
	public static String wenjuanMd5(Map<String, String> map) {
		String md5 = "";
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		for (String string : list) {
			md5 += map.get(string);
		}
		md5 = md5 + WenJuanConfig.wenjuan_secret_key;
		md5 = Md5.getMd5String(md5);
		return md5;
	}

	/***
	 * 问卷网的url的escape转义
	 * 
	 * @param args
	 */
	public static String urlEscape(String url) {
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByExtension("js");
		String escape = "escape('" + url + "')";
		try {
			Object object = engine.eval(escape);
			// System.out.println(object.toString());
			if (object != null) {
				return object.toString();
			}
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		// Map<String, String> map = new HashMap<String,String>();
		// map.put("user", "1001");
		// map.put("site", "100");
		// map.put("ctime", "2014-01-13 10:11");
		// map.put("email", "keithsun@**.com");
		// String md5_param = wenjuanMd5(map);
		String val = "www.baidu.com达到";
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByExtension("js");
		String escape = "escape('" + val + "')";
		try {
			Object object = engine.eval(escape);
			System.out.println(object.toString());
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// StringEscapeUtils.escapeJava(val);
		// System.out.println(val);
	}

}
