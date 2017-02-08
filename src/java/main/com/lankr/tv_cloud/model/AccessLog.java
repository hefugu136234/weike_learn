package com.lankr.tv_cloud.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;

public class AccessLog {
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(String accessDate) {
		this.accessDate = accessDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAccess_uri() {
		return access_uri;
	}

	public void setAccess_uri(String access_uri) {
		this.access_uri = access_uri;
	}

	public String getPlateform() {
		return plateform;
	}

	public void setPlateform(String plateform) {
		this.plateform = plateform;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public String getPlam_version() {
		return plam_version;
	}

	public void setPlam_version(String plam_version) {
		this.plam_version = plam_version;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public Integer getTable_id() {
		return table_id;
	}

	public void setTable_id(Integer table_id) {
		this.table_id = table_id;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Integer responseTime) {
		this.responseTime = responseTime;
	}

	public String getFieldA() {
		return fieldA;
	}

	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}

	public String getFieldB() {
		return fieldB;
	}

	public void setFieldB(String fieldB) {
		this.fieldB = fieldB;
	}

	public String getFieldC() {
		return fieldC;
	}

	public void setFieldC(String fieldC) {
		this.fieldC = fieldC;
	}

	public int getFieldD() {
		return fieldD;
	}

	public void setFieldD(int fieldD) {
		this.fieldD = fieldD;
	}

	public int getFieldE() {
		return fieldE;
	}

	public void setFieldE(int fieldE) {
		this.fieldE = fieldE;
	}

	public int getFieldF() {
		return fieldF;
	}

	public void setFieldF(int fieldF) {
		this.fieldF = fieldF;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	private Integer id;
	private String accessDate;
	private Integer userId;
	private String ip;
	private String access_uri;
	private String plateform;
	private String user_agent;
	private String plam_version;
	private Integer isActive;
	private Integer code;
	private String mark;
	private String resource;
	private String table_name;
	private Integer table_id;
	private String operate;
	private String status;
	private String title;
	private Integer responseTime;
	private String fieldA;
	private String fieldB;
	private String fieldC;
	private int fieldD;
	private int fieldE;
	private int fieldF;
	private String openId;

	public void buildData(HttpServletRequest request,
			HttpServletResponse response) {
		this.setUserId(BaseController.readIntData(request,
				BaseController.LOG_USERID));
		this.setIp(BaseController.getClientIpAddr(request));
		this.setAccess_uri(request.getRequestURI());
		this.setPlateform(BaseController.readStirngData(request,
				BaseController.LOG_PLATEFORM));
		this.setUser_agent(BaseController.readStirngData(request,
				BaseController.LOG_USER_AGEN));
		this.setPlam_version(BaseController.readStirngData(request,
				BaseController.LOG_VERSION));
		this.setIsActive(1);
		this.setCode(response.getStatus());
		this.setMark(BaseController.readStirngData(request,
				BaseController.API_LOG_MARK));
		this.setResource(BaseController.readStirngData(request,
				BaseController.API_LOG_RESOURCE));
		this.setTable_name(BaseController.readStirngData(request,
				BaseController.LOG_TABLE_NAME));
		this.setTable_id(BaseController.readIntData(request,
				BaseController.LOG_TABLE_ID));
		this.setOperate(BaseController.readStirngData(request,
				BaseController.LOG_OPERATE));
		this.setStatus(BaseController.readStirngData(request,
				BaseController.LOG_STATUS));
		this.setTitle(BaseController.readStirngData(request,
				BaseController.LOG_TITLE));
		this.setResponseTime(BaseController.readIntData(request,
				BaseController.LOG_RESPONSETIME));
		this.setFieldA(BaseController.readStirngData(request,
				BaseController.LOG_FIELDA));
		this.setFieldB(BaseController.readStirngData(request,
				BaseController.LOG_FIELDB));
		this.setFieldC(BaseController.readStirngData(request,
				BaseController.LOG_FIELDC));
		String openId = getOpenId(BaseWechatController.OPENID_KEY, request);
		openId=Tools.nullValueFilter(openId);
		this.setOpenId(openId);
	}
	
	protected String getOpenId(String key, HttpServletRequest request) {
		return request.getSession().getAttribute(key) == null ? null : request
				.getSession().getAttribute(key).toString();
	}

}
