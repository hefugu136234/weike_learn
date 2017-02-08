package com.lankr.tv_cloud;

public class TemplateMessgaeId {
	@Env
	public static boolean message_swich;
	@Env
	public static String broadCastBook_id;
	@Env
	public static String broadCastBookCheck_id;
	@Env
	public static String realNameNotice_id;
	@Env
	public static String oupsCheck_id;
	@Env
	public static String registerSuccess_id;
	@Env
	public static String realNameSubmit_id;
	@Env
	public static String exchageGoods_id;
	@Env
	public static String deliverGoods_id;
	@Env
	public static String oupsSubmit_id;
	@Env
	public static String integralDaily_id;
	@Env
	public static String vipDeliverGoods_id;
	@Env
	public static String onActivityResourceChanged_id;
	@Env
	public static String beforeHourBroadcast_id;

	public void init() {
		try {
			ConfigUtils.driven(TemplateMessgaeId.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
