package com.lankr.tv_cloud.support;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.vo.CCVideoData;

public class CCSparkServerTools {

	private static Log logger = LogFactory.getLog(CCSparkServerTools.class);

	private static Gson gson = new Gson();

	public static String thqs(Map<String, String> params) {
		StringBuffer qs = new StringBuffer("");
		try {
			if (params != null) {
				params.put("userid", Config.cc_user_id);
				//由于页面添加了 禁止缓存，所以每个页面都会带上“_= 时间戳” 这样一个参数，去除之;
				params.remove("_");
				Set<String> set = params.keySet();
				String[] strs = set.toArray(new String[set.size()]);
				Arrays.sort(strs);
				if (strs != null) {
					for (int i = 0; i < strs.length; i++) {
						String key = strs[i];
						qs.append(key)
								.append("=")
								.append(URLEncoder.encode(params.get(key),
										"utf-8"));
						if (i < strs.length - 1) {
							qs.append("&");						
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String time = System.currentTimeMillis() / 1000 + "";
		String fq = qs + "&time=" + time + "&salt=" + Config.cc_key;
		String hash = Md5.getMd5String(fq);
		return qs + "&time=" + time + "&hash=" + hash;
	}

	public static CCVideoData requestCCVideoData(String ccVideoId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("videoid", ccVideoId);
		String queryString = thqs(params);
		CCVideoData cc = null;
		// 从cc服务器获取视频数据
		String url = "http://spark.bokecc.com/api/video?" + queryString;
		try {

			String data = HttpUtils.sendGetRequest(url);
			cc = gson.fromJson(data, CCVideoData.class);
		} catch (Exception e) {
			logger.error("fetch url " + url + " has an exception", e);
			cc = new CCVideoData();
			cc.setError("error");
		}
		return cc;
	}

	public static void xmlParse(String xml_data, Object obj) {
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(
					new ByteArrayInputStream(xml_data
							.getBytes(StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8.toString());
			String handler = "";
			while (reader.hasNext()) {
				reader.next();
				if (reader.isStartElement()) {
					handler = reader.getName().toString();
				} else if (reader.isCharacters()) {
					try {
						Method m = obj.getClass().getMethod("set" + handler,
								String.class);
						m.invoke(obj, reader.getText());
					} catch (Exception e) {
						logger.error("tag name: <" + handler + "> error ");
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
