package com.lankr.tv_cloud.support.qiniu.util;

import com.lankr.tv_cloud.support.qiniu.common.Config;

/**
 * URL安全的Base64编码和解码
 */

public final class UrlSafeBase64 {

    private UrlSafeBase64() {
    }   // don't instantiate

    /**
     * 编码字符串
     *
     * @param data 待编码字符串
     * @return 结果字符串
     */
    public static String encodeToString(String data) {
        return encodeToString(data.getBytes(Config.UTF_8));
    }

    /**
     * 编码数据
     *
     * @param data 字节数组
     * @return 结果字符串
     */
    public static String encodeToString(byte[] data) {
        return Base64.encodeToString(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * 解码数据
     *
     * @param data 编码过的字符串
     * @return 原始数据
     */
    public static byte[] decode(String data) {
        return Base64.decode(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }
    
    public static void main(String[] args) {
		System.out.println(encodeToString("c10e287f2b1e7f547b20a9ebce2aada26ab20ef2"));
	}
}
