package com.lankr.tv_cloud.support;

import java.util.Arrays;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.lankr.tv_cloud.Config;

public class TencentVodServer {

	private static Log logger = LogFactory.getLog(TencentVodServer.class);

	private static final String VOD_HOST = "vod.api.qcloud.com";

	private static Gson gson = new Gson();

	public static void main(String[] args) {
		// System.out.println(TencentVodServer.fetchVideoInfo(
		// "16092504232103621638").isOk());

		// fetchVideoPlayUrls("16092504232103621638");
		
		String[] strs = new String[]{"1","2","3","4"};
		String[]  news = Arrays.copyOfRange(strs, 0,3);
		for (String string : news) {
			System.out.println(string);
		}

	}

	public static VodInfoData fetchVideoInfo(String fileId) {
		try {
			// 构建参数
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("fileIds.1", fileId);
			params.put("Action", "DescribeVodInfo");
			// String signature = Signature.buildSignature(params,
			// "DescribeVodInfo",
			// Config.qq_secretKey, Signature.GET, VOD_HOST);
			// String plainText = Sign.makeSignPlainText(params, Signature.GET,
			// VOD_HOST, Signature.serverUri);
			String json = Request.send(params,
					Config.qq_secretId,
					Config.qq_secretKey, Signature.GET,
					VOD_HOST, Signature.serverUri, null);
			return gson.fromJson(json, VodInfoData.class);
		} catch (Throwable ignore) {
			return null;
		}
	}

	public static VodPlayUrlsData fetchVideoPlayUrls(String fileId) {
		try {
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("fileId", fileId);
			params.put("Action", "DescribeVodPlayUrls");
			String json = Request.send(params,
					Config.qq_secretId,
					Config.qq_secretKey, Signature.GET,
					VOD_HOST, Signature.serverUri, null);
			VodPlayUrlsData data = gson.fromJson(json, VodPlayUrlsData.class);
			data.setJson_data(json);
			return data;
		} catch (Throwable ignore) {
			return null;
		}
	}

}
