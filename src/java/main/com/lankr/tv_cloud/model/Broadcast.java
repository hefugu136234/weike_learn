package com.lankr.tv_cloud.model;

import java.util.Date;


public class Broadcast extends BaseModel{
	
	private static final long serialVersionUID = 4942131423137880864L;
	
	/**
	 * 直播的类型 1=开放 2=审核 3=pingcode邀请
	 */
	public final static int OPENING_TYPE=1;
	
	public final static int CHECK_TYPE=2;
	
	public final static int PINGCODE_TYPE=3;

	private String name;
	
	private String pinyin;
	
	private User creatUser;
	
	private String castShowJs;
	
	private String castAction;
	
	private String description;
	
	private Date bookStartDate;
	
	private Date bookEndDate;
	
	private int limitNum;
	
	private int castType;
	
	private String label;
	
	private String banner;
	
	private String cover;
	
	private String kv;//背景图
	
	private Date startDate;
	
	private Date endDate;
	
	private String pincode;
	
	private Resource resource;
	
	private String resourceUrl;
	
	private int status;
	
	private int platFormType;
	
	private String tvDescription;
	
	private Speaker speaker;
	
	
	

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	public String getTvDescription() {
		return tvDescription;
	}

	public void setTvDescription(String tvDescription) {
		this.tvDescription = tvDescription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public User getCreatUser() {
		return creatUser;
	}

	public void setCreatUser(User creatUser) {
		this.creatUser = creatUser;
	}

	public String getCastShowJs() {
		return castShowJs;
	}

	public void setCastShowJs(String castShowJs) {
		this.castShowJs = castShowJs;
	}

	public String getCastAction() {
		return castAction;
	}

	public void setCastAction(String castAction) {
		this.castAction = castAction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public Date getBookStartDate() {
		return bookStartDate;
	}

	public void setBookStartDate(Date bookStartDate) {
		this.bookStartDate = bookStartDate;
	}

	public Date getBookEndDate() {
		return bookEndDate;
	}

	public void setBookEndDate(Date bookEndDate) {
		this.bookEndDate = bookEndDate;
	}

	public int getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}

	public int getCastType() {
		return castType;
	}

	public void setCastType(int castType) {
		this.castType = castType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getKv() {
		return kv;
	}

	public void setKv(String kv) {
		this.kv = kv;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPlatFormType() {
		return platFormType;
	}

	public void setPlatFormType(int platFormType) {
		this.platFormType = platFormType;
	}

	

}
