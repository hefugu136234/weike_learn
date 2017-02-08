package com.lankr.tv_cloud.utils;

import io.netty.util.CharsetUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Kalean.Xiang
 * */
public class DESUtil {
	/**
	 * 硬编码token,切勿修改 a8eb29ec
	 * */
	private final static byte[] secretKey = "a8eb29ec".getBytes();

	public static String encrypt(String encryptString)
			throws Exception {
		SecretKeySpec key = new SecretKeySpec(secretKey, "DES");
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);// Cipher.ENCRYPT_MODE（加密标识）
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));// 加密
		return Base64.encodeToString(encryptedData, Base64.DEFAULT);// Base64加密生成在Http协议中传输的字符串
	}

	public static String decrypt(String decryptString) throws Exception {
		byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
		SecretKeySpec key = new SecretKeySpec(secretKey, "DES");
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decryptData = cipher.doFinal(byteMi);
		return new String(decryptData);
	}
	public static void main(String[] args) throws Exception {
//		
////		DESUtil des = new DESUtil("1234567");
//		// DES 加密文件
//		// des.encryptFile("G:/test.doc", "G:/ 加密 test.doc");
//		// DES 解密文件
//		// des.decryptFile("G:/ 加密 test.doc", "G:/ 解密 test.doc");
////		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		String str1 = "{\"status\":\"success\",\"code\":0,\"access_time\":1477286991918,\"updated_api\":\"http://api.fir.im/apps/latest/12345?api_token=ecb5cbffe2cbc6e7d648d5ba1b874645\"}";
//		// DES 加密字符串
//		String str2 = encryptStringData(str1);
//		// DES 解密字符串
////		str2="/jD1L4IpqHy0pvRAQGmNF5TjBKr0yQyb+JJ0U0mYVyyIIJK9Yu06ZbdEnlCqJJNqlvcvyWZwVzG1QZsDYc1nRFhVo9rdFahUSoWr7Z6NslRGy7HEl1A0W19iGDeGFNHAoTwbZPfjxVutIZdraToI/MpaN5Cvr3CJYK8RjLY6tR2093OZqYbci2F4oDsw7yBGYXAZFi9r7R8=";
//		
//		DES1 d = new DES1();
//		
//		System.out.println(secretKey.length());
//		
//		System.out.println(" 加密前： " + str1);
//		System.out.println(" 加密后： " + str2);
//		
//		String strd = d.encrypt(str1, secretKey);
//		System.out.println(" d加密后： " + strd);
//		
//		System.out.println(" d解密后： " + d.decrypt(strd, secretKey));
//		
//		
//		
//		String deStr = decryptStringData(str2);
//		System.out.println(" 解密后： " + deStr);
	}
}