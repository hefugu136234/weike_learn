package com.lankr.tv_cloud.broadcast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class ZhiLiaoLivePlatfrom {
	/**
	 * 知了平台直播创建的接口
	 */
	// 创建后台直播
	/**
	 * 
	 * @param subject
	 * @param startTime
	 * @param endTime
	 * @param speakerInfo
	 * @return speakerInfo不是必须
	 */
	public static String creatLive(Broadcast broadcast) {
		Map<String, String> map = new HashMap<String, String>();
		String subject = OptionalUtils.traceValue(broadcast, "name");
		map.put("subject", subject);
		long startTime = broadcast.getStartDate().getTime();
		map.put("startTime", String.valueOf(startTime));
		map.put("opened", "true");
		String description = OptionalUtils.traceValue(broadcast, "mark");
		map.put("description", description);
		String speakerInfo = OptionalUtils
				.traceValue(broadcast, "speaker.name");
		if (!speakerInfo.isEmpty()) {
			map.put("speakerInfo", speakerInfo);
		}
		long endTime = broadcast.getEndDate().getTime();
		map.put("endTime", String.valueOf(endTime));
		String organizerToken = getCommand();
		map.put("organizerToken", organizerToken);
		String panelistToken = getCommand();
		map.put("panelistToken", panelistToken);
		String attendeeToken = getCommand();
		map.put("attendeeToken", attendeeToken);
		map.put("loginName", Config.zbox_live_loginName);
		map.put("password", Config.zbox_live_password);
		String host = CurrentLivePlatfromData.getCurrentLiveFromData()
				.get(CurrentLivePlatfromData.ZHILIAO_PLAT).getRequestUrl();
		String requestUrl=host+"/integration/site/webcast/created";
		String message=HttpUtils.sendPostRequest(requestUrl, map);
		return message;
	}

	/**
	 * 产生一个6-15位的随机口令
	 * 
	 * @return
	 */
	public static String getCommand() {
		Random random = new Random();
		int length = random.ints(6, 15).limit(1).sum();
		return Tools.generateShortUuid(length);
	}

	public static void main(String[] args) {
		Random random = new Random();
		int length = random.ints(6, 15).limit(1).sum();
		System.out.println(length);
		System.out.println(Tools.generateShortUuid(length));
	}

}
