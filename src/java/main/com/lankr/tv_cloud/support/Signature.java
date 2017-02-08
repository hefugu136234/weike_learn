package com.lankr.tv_cloud.support;

import java.util.Random;
import java.util.TreeMap;

import com.google.gson.Gson;

public class Signature {
	
	public static final String GET="GET";
	
	public static final String POST="POST";
	
	public static final String DefaultRegion="gz";
	
	public static final String DEFserverHost="cvm.api.qcloud.com";//一般接口的host 查询服务器的接口DescribeInstances
	
	public static final String DEFACTION="DescribeInstances";//查询服务器的默认接口action ，验证签名
	
	public static final String ACTION="MultipartUploadVodFile";//上传视频的action
	
	public static final String serverHost="vod.qcloud.com";//上传视频的 MultipartUploadVodFile
	
	public static final String version = "SDK_JAVA_1.2";
	
	public static final String serverUri = "/v2/index.php";//请求的url的地址
	
	/**
	 * 签名生成每一个接口，签名不同 ，且时时变化，
	 * @param args
	 */
	
	
	public static void main(String[] args) {
//		provingSign();
		
		//System.out.println(convertUnicode("\u9274\u6743\u5931\u8d25\uff0c\u8bf7\u53c2\u8003\u6587\u6863\u4e2d\u9274\u6743\u90e8\u5206\u3002"));
		String secretId="AKIDvD0l7x3KjDWEQmQTONQbktvZDZCFR7Ms";
		String secretKey="Ekux3hUXxokxjtPaBwrYvgC2O1l1PD3W";
		TreeMap<String, Object> map=new TreeMap<String, Object>();
		map.put("SecretId", secretId);
		map.put("fileIds.1", "16092504232103620201");
		System.out.println(buildSignature(map, "DescribeVodInfo",secretKey, GET, "vod.api.qcloud.com"));
	}
	
	/**
	 * 将Unicode转中文
	 * @param ori
	 * @return
	 */
	public static String convertUnicode(String ori){
        char aChar;
        int len = ori.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = ori.charAt(x++);
            if (aChar == '\\') {
                aChar = ori.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = ori.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
 
        }
        return outBuffer.toString();
	}
	
	/**
	 * 验证DescribeInstances 判断是否签名有效
	 * 核心参数变化=Action，接口名称
	 * 
	 * 当code返回0时，代表签名生成没有问题
	 */
	public static void provingSign(){
		TreeMap<String, Object> map=new TreeMap<String, Object>();
		String secretId="AKIDvD0l7x3KjDWEQmQTONQbktvZDZCFR7Ms";
		String secretKey="Ekux3hUXxokxjtPaBwrYvgC2O1l1PD3W";
		map.put("SecretId", secretId);
		map.put("SecretKey", secretKey);
		String signature=buildSignature(map, DEFACTION, secretKey, GET, DEFserverHost);
		if(signature!=null){
			map.put("Signature", signature);
			String url = "https://" + DEFserverHost + serverUri;
			String result=Request.sendRequest(url, map, GET, null);
			System.out.println(result);
			Gson gson=new Gson();
			QQData data=gson.fromJson(result, QQData.class);
			System.out.println("code"+":"+data.getCode());
			System.out.println("message"+":"+convertUnicode(data.getMessage()));
		}
	}
	
	/**
	 * 
	 * @param secretId=腾讯云的秘钥id
	 * @param secretKey=腾讯云的秘钥key
	 * @return
	 */
	public static String buildSignature(TreeMap<String, Object> map,String action,String secretKey,String sendMethod,String host){
		/**
		 * 生成签名，必备参数(最少参数)
		 * secretId=AKIDvD0l7x3KjDWEQmQTONQbktvZDZCFR7Ms
		 * secretKey=Ekux3hUXxokxjtPaBwrYvgC2O1l1PD3W
		 * Nonce
		 * RequestClient
		 * Timestamp
		 * Action=请求的接口名称
		 */
		/**
		 * RequestMethod=请求方式
		 * DefaultRegion=sh Region=""
		 */
		map.put("Action", action);
		map.put("Region", DefaultRegion);
		map.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE));
		map.put("Timestamp", System.currentTimeMillis() / 1000);
		map.put("RequestClient", version);
		String plainText = Sign.makeSignPlainText(map, sendMethod, host, serverUri);
		String signature=null;
		try {
			 signature=Sign.sign(plainText, secretKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("签名："+signature);
		return signature;
	} 
	
	
	/**
	 * hu
	 * @param secretId
	 * @param secretKey
	 * @return
	 * sendMethod=post
	 * 服务端自己生成签名
	 */
	public static String getSignatureByUPloadVod(String secretId,String secretKey){
		TreeMap<String, Object> map=new TreeMap<String, Object>();
		map.put("SecretId", secretId);
		map.put("SecretKey", secretKey);
		String signature=buildSignature(map, ACTION, secretKey, POST, serverHost);
		return signature;
	}
	
	public static String getSignatureVodOfJs(String secretKey,String args){
		String signature=null;
		try {
			 signature=Sign.sign(args, secretKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("签名："+signature);
		return signature;
	}
	
	class QQData{
		int code;
		String message;
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		
	}

}
