package com.lankr.tv_cloud.vo.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.utils.Tools;

public class BaseAPIModel {

	protected static Gson gson = new GsonBuilder().disableHtmlEscaping()
			.create();

	private String status = Status.FAILURE.message();
	private int code = CodeConstant.OK;
	public long access_time = System.currentTimeMillis();
	private String message;
	// 数据是否加密
//	private boolean encrypted;	
//	public boolean isEncrypted() {
//		return encrypted;
//	}
//
//	public void setEncrypted(boolean encrypted) {
//		this.encrypted = encrypted;
//	}

	private String toast;

	public String getToast() {
		return toast;
	}

	public void setToast(String toast) {
		this.toast = toast;
	}

	private String suggestion;

	// 用户提示客户端的处理工作
	// public void addCode(int code) {
	// if (action_code == null) {
	// action_code = new ArrayList<Integer>(1);
	// }
	// action_code.add(code);
	// }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatus(Status status) {
		if (status != null)
			setStatus(status.message());
	}

	public String toJSON() {
		return gson.toJson(this);
	}

	public static enum JsonConvertor {
		GSON, JACKSON
	}

	public String toJSON(JsonConvertor convertor) {
		if (convertor == JsonConvertor.JACKSON) {
			try {
				return mapper.writeValueAsString(this);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return toJSON();
	}

	/*
	 * public String toXML() { // need implements return ""; }
	 */

	public static String makeSimpleSuccessInnerDataJson(SimpleData... data) {
		return makeSimpleInnerDataJson(null, 0, Status.SUCCESS.message(), data);
	}

	public static String makeSimpleSuccessInnerDataJson(String message,
			int code, SimpleData... data) {
		return makeSimpleInnerDataJson(message, code, Status.SUCCESS.message(),
				data);
	}

	private static String makeSimpleInnerDataJson(String message, int code,
			String status, SimpleData... data) {
		BaseAPIModel bam = new BaseAPIModel();
		bam.status = status;
		bam.setMessage(message);
		bam.setCode(code);
		JsonElement je = gson.toJsonTree(bam);
		if (data == null || data.length == 0)
			return bam.toJSON();
		for (int i = 0; i < data.length; i++) {
			SimpleData sd = data[i];
			if (sd.dataType == null || SimpleDataType.String == sd.dataType) {
				je.getAsJsonObject().addProperty(sd.key, (String) sd.value);
			} else if (SimpleDataType.Number == sd.dataType) {
				je.getAsJsonObject().addProperty(sd.key, (Number) sd.value);
			} else if (SimpleDataType.Boolean == sd.dataType) {
				je.getAsJsonObject().addProperty(sd.key, (Boolean) sd.value);
			}
		}
		return gson.toJson(je);
	}

	public static String makeSimpleFailureInnerDataJson(SimpleData... data) {
		return makeSimpleInnerDataJson(null, 0, Status.FAILURE.message(), data);
	}

	// public static void main(String[] args) throws Exception{
	// ClassPool pool = ClassPool.getDefault();
	// CtClass clazz = pool.makeClass("TTest");
	// CtClass strClass = pool.get("java.lang.String");
	// clazz.addField(new CtField(strClass, "a", clazz));
	// Class c = clazz.toClass();
	// Object obj = c.newInstance();
	// Field f =
	// // System.out.println(gson.toJson(clazz.toClass().newInstance()));
	// }

	public static String makeWrappedSuccessDataJson(Object data) {
		return makeWrappedSuccessBaseAPIModel(data).toJSON();
	}

	public static String makeWrappedSuccessDataJson(Object data,
			JsonConvertor convertor) {
		return makeWrappedSuccessBaseAPIModel(data).toJSON(convertor);
	}

	private static ObjectMapper mapper = new ObjectMapper();
	static {
		// mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.setVisibilityChecker(mapper.getSerializationConfig()
				.getDefaultVisibilityChecker()
				.withFieldVisibility(Visibility.ANY)
				.withGetterVisibility(Visibility.NONE)
				.withSetterVisibility(Visibility.NONE)
				.withCreatorVisibility(Visibility.NONE));
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static BaseAPIModel makeWrappedSuccessBaseAPIModel(Object data) {
		return new CaseTmp(data);
	}

	private static class CaseTmp extends BaseAPIModel {
		CaseTmp(Object obj) {
			setStatus(Status.SUCCESS);
			data = obj;
		}

		Object data;
	}

	public String notNullString(String val) {
		return Tools.nullValueFilter(val);
	}

	public static final class CodeConstant {

		/***
		 * 无需任何操作
		 */
		public static final Integer OK = 0;

		/***
		 * tv端需要重新登录
		 */
		public static final Integer TV_RELOGIN = 1;

		/***
		 * 用户需要充值
		 */
		public static final Integer TV_RECHARGE = 2;

		/***
		 * 需要验证码
		 */
		public static final Integer VALIDATE_CODE_NEEDED = 3;

	}

}
