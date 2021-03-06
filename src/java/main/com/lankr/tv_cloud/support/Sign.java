package com.lankr.tv_cloud.support;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
//import sun.misc.BASE64Encoder;
//import org.apache.commons.codec.binary.Base64;

public class Sign {
	// 编码方式
    private static final String CONTENT_CHARSET = "UTF-8";

    // HMAC算法
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    /**
     * @brief 签名
     * @author gavinyao@tencent.com
     * @date 2014-08-13 21:07:27
     *
     * @param signStr 被加密串
     * @param secret 加密密钥
     *
     * @return
     * @throws NoSuchAlgorithmException 
     */
    
    
    public static String sign(String signStr, String secret) 
    		throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException 
    {

        String sig = null;
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
        mac.init(secretKey);
        byte[] hash = mac.doFinal(signStr.getBytes(CONTENT_CHARSET));

        // base64
        //sig = new String(new BASE64Encoder().encode(hash).getBytes());
        //sig = new String(Base64.encodeBase64(hash));
        sig = new String(Base64.encode(hash));

        return sig;
    }
    
    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println(sign("a", "a"));
	}
    
    public static String makeSignPlainText(TreeMap<String, Object> requestParams, String requestMethod, String requestHost, String requestPath) {

        String retStr = "";
        retStr += requestMethod;
        retStr += requestHost;
        retStr += requestPath;
        retStr += buildParamStr(requestParams);
        return retStr;
    }

    protected static String buildParamStr(TreeMap<String, Object> requestParams) {
        return buildParamStr(requestParams, "GET");
    }

    protected static String buildParamStr(TreeMap<String, Object> requestParams, String requestMethod) {

        String retStr = "";
        for(String key: requestParams.keySet()) {
            if (retStr.length()==0) {
                retStr += '?';
            } else {
                retStr += '&';
            }
            retStr += key.replace("_", ".") + '=' + requestParams.get(key).toString();

        }

        return retStr;
    }
}
