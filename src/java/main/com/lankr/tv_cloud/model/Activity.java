package com.lankr.tv_cloud.model;

import java.util.Date;

public class Activity extends BaseModel {
	
	private static final long serialVersionUID = 2915335631146283608L;

	public static final int INFINITE = -1;
	public static final int TYPE_PUBLIC = 0;
	public static final int TYPE_PROTECTED = 1;

	private String name;
	private String pinyin;
	private Date startDate;
	private Date endDate;
	private String code;
	private Integer members;
	private Category category;
	// 活动创建者
	private User user;
	// 参与类型， 开放或者审批
	private int joinType;
	// 人数限制 -1 表示没有限制
	private int plimit;
	private String description;
	private int status;
	
	public int getCollected() {
		return collected;
	}
	public void setCollected(int collected) {
		this.collected = collected;
	}
	public int getAuthentic() {
		return authentic;
	}
	public void setAuthentic(int authentic) {
		this.authentic = authentic;
	}
	private int collected;
	
	private int authentic;
	
	private ActivityConfig config;
	

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
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getJoinType() {
		return joinType;
	}
	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}
	public int getPlimit() {
		return plimit;
	}
	public void setPlimit(int plimit) {
		this.plimit = plimit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer getMembers() {
		return members;
	}
	public void setMembers(int members) {
		this.members = members;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ActivityConfig getConfig() {
		return config;
	}
	public void setConfig(ActivityConfig config) {
		this.config = config;
	}
	
	public boolean allowCollected(){
		return collected == TRUE;
	}
	
	public boolean isNeedRealName(){
		return authentic==TRUE;
	}
	
}
