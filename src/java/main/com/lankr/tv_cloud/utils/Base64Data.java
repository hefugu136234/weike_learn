package com.lankr.tv_cloud.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Data {

	/**
	 * 加密
	 */
	public static String encodeBy64(String value) {
		String encrypt = null;
		if (value == null || value.isEmpty())
			return encrypt;
		try {
			byte[] ret = value.getBytes("UTF-8");
			encrypt = Base64.encodeBase64String(ret);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encrypt;
	}

	/**
	 * 解密
	 * 
	 * @param args
	 */
	public static String decodeBy64(String value) {
		String descryp = null;
		if (value == null || value.isEmpty())
			return descryp;
		byte[] bytes = Base64.decodeBase64(value);
		try {
			descryp = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return descryp;
	}



	public static void main(String[] args) {
		// String valueString=buildEncrypt("12345",1234556,"12132424");
		// valueString=encodeBy64(valueString);
		// System.out.println(valueString);
		// System.out.println(decodeBy64(valueString));
		// System.out.println(new Date().getTime());
		System.out.println(encodeBy64("http://vrassets.lankr.net/ljHOtOU1nd6laYbgGhE4v45Z5Wdk"));
	}

}
