package com.lankr.tv_cloud;


public class WenJuanConfig {
	@Env
	public static String wenjuan_host;
	@Env
	public static String wenjuan_site;
	@Env
	public static String wenjuan_secret_key;
	//问卷网的用户
	@Env
	public static String wenjuan_user;
	@Env
	public static String wenjuan_email;
	@Env
	public static String wenjuan_mobile;
	
	
	public void init() {
		try {
			ConfigUtils.driven(WenJuanConfig.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	

}
