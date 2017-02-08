package com.lankr.tv_cloud.support.qiniu;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.support.qiniu.common.QiniuException;
import com.lankr.tv_cloud.support.qiniu.http.Client;
import com.lankr.tv_cloud.support.qiniu.http.Response;
import com.lankr.tv_cloud.support.qiniu.storage.BucketManager;
import com.lankr.tv_cloud.support.qiniu.storage.UploadManager;
import com.lankr.tv_cloud.support.qiniu.storage.model.DefaultPutRet;
import com.lankr.tv_cloud.support.qiniu.storage.model.FileInfo;
import com.lankr.tv_cloud.support.qiniu.util.Auth;
import com.lankr.tv_cloud.utils.Tools;

public final class QiniuUtils {

	private static final String ACCESS_KEY = Config.qn_access_key;

	private static final String SECRET_KEY = Config.qn_secret_key;

	public static final String DEF_BUCKET = "lankr";
	

	private static Gson gson = new Gson();

	public static String getSimpleUploadPolicy(String name, long period_time,
			TimeUnit unit) {
		Auth au = Auth.create(ACCESS_KEY, SECRET_KEY);

		// String scope = DEF_BUCKET + ":" + name;
		// long deadline =
		// TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() +
		// unit.toSeconds(period_time));
		// String returnBody =
		// "{\"name\":$(fname),\"size\":$(fsize),\"hash\":$(etag)}";
		// Policy p = new Policy(scope, deadline);
		// p.returnBody = returnBody;
		// String json = gson.toJson(p);
		// String encodedPutPolicy = UrlSafeBase64.encodeToString(json);
		// String sign = au.sign(encodedPutPolicy);
		// String encode_sign = UrlSafeBase64.encodeToString(sign);
		//
		// return ACCESS_KEY + ":" + encode_sign + ":" + encodedPutPolicy;
		return au.uploadToken(DEF_BUCKET, name);
	}

	public static String fetchQrUrl(String content) {
		File f = FileUtils.getTempDirectory();
		File p = null;
		try {
			p = File.createTempFile(Tools.getUUID(), ".png", f);
			Tools.makeQr(p, content);
			UploadManager um = new UploadManager();
			Response resp = um.put(p, null,
					getSimpleUploadPolicy(null, 0, null));
			String ret = new String(resp.body());
			UploaderResp r = gson.fromJson(ret, UploaderResp.class);
			return Config.qn_cdn_host + "/" + r.key;
		} catch (Throwable th) {
			th.printStackTrace();
		} finally {
			if (p != null) {
				p.delete();
			}
		}
		return null;
	}

	public static String webUploadUeFile(byte[] bytes, String key) {
//		if (Tools.isBlank(key)) {
//			key = Tools.getUUID();
//		}
		try {
			UploadManager um = new UploadManager();
			Response resp = um.put(bytes, null,
					getSimpleUploadPolicy(null, 0, null));
			String ret = new String(resp.body());
			UploaderResp r = gson.fromJson(ret, UploaderResp.class);
//			System.out.println("hash:"+r.hash);
//			System.out.println("key:"+r.key);
			return Config.qn_cdn_host + "/" + r.key;
		} catch (Throwable th) {
			th.printStackTrace();
		}
		return null;
	}

	private static class UploaderResp {
		String hash;
		String key;
	}
	
	/**
	 * 2017-01-11 
	 * 获取视频信息的元信息avinfo
	 * @param args
	 */
	public static QiniuVideoAvinfo videoAvinfo(String url){
		if(Tools.isBlank(url))
			return null;
		url=url+"?avinfo";
		Client client=new Client();
		QiniuVideoAvinfo info=null;
		try {
			Response response = client.get(url);
			info=response.jsonToObject(QiniuVideoAvinfo.class);
			return info;
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 2017-01-11 
	 * 获取视频信息某一帧的缩略图
	 * @param args
	 */
	public static String QiniuVideoCover(String url,int duration){
		if(Tools.isBlank(url))
			return null;
		int time=randomByRound(duration);
		url=url+"?vframe/jpg/offset/"+time+"/w/640/h/480";
		Auth auth = Auth.create(Config.qn_access_key, Config.qn_secret_key);
		BucketManager bucketManager = new BucketManager(auth);
		try {
			DefaultPutRet defaultPutRet = bucketManager.fetch(url,
					QiniuUtils.DEF_BUCKET);
			if (defaultPutRet != null && defaultPutRet.key != null
					&& !defaultPutRet.key.isEmpty()) {
				String imageUrl = Config.qn_cdn_host + "/" + defaultPutRet.key;
				return imageUrl;
			}
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//获取固定数据的随机数
	public static int randomByRound(int num){
		if(num>10){
			Random random=new Random();
			num=random.nextInt(10)+1;
		}
		return num;
	}

	public static void main(String[] args) {
		System.out.println(fetchQrUrl("test"));
	}

}
