package com.lankr.tv_cloud;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Config {
	private static Log logger = LogFactory.getLog(Config.class);

	public static enum Environment {
		PRODUCT, DEV, TEST;

		public String getValue() {
			return name().toLowerCase();
		}
	}

	@Autowired
	SpringPropertiesUtil configs;

	@Env(ignore = true, alias = "env")
	public static String env;

	@Env
	public static String cc_key;
	@Env
	public static String cc_user_id;
	@Env
	public static String qq_secretId;
	@Env
	public static String qq_secretKey;
	@Env
	public static String host;
	@Env
	public static boolean mysql_logger_enable;
	@Env
	public static String sms_template_code;
	@Env
	public static String wx_appid;
	@Env
	public static String wx_appsecret;
	@Env
	public static String wx_open_appid;
	@Env
	public static String wx_open_appsecret;
	@Env
	public static String qn_access_key;
	@Env
	public static String qn_secret_key;
	@Env
	public static String qn_cdn_host;
	@Env
	public static boolean sendMessage;
	@Env
	public static String messageServer;
	@Env
	public static String search_host;
	@Env
	public static boolean page_cache_enable;
	@Env
	public static int qr_search_deadline;
	@Env
	public static String zbox_live_loginName;
	@Env
	public static String zbox_live_password;	
	@Env
	public static String box_app_api_token;
	@Env
	public static String box_app_id;
	@Env
	public static String baidu_live_ak;
	

	public void init() {
		try {
			ConfigUtils.driven(Config.class);
			logger.info("========================================="
					+ env
					+ " environment loaded finished===================================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isProductEnv() {
		try {
			return Environment.PRODUCT.getValue().equals(env);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isDevEnv() {
		try {
			return Environment.DEV.getValue().equals(env);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isTestEnv() {
		try {
			return Environment.TEST.getValue().equals(env);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}