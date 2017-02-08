package com.lankr.tv_cloud.model;

import java.util.Date;

import com.lankr.tv_cloud.model.Resource.Type;

public interface Resourceable {

	public int getPrototypeId();
	
	public String getName();

	public String getDescription();

	public Date getCreateDate();

	public Date getModifyDate();

	public String getUuid();

	public String getCover();

	public String getQr();

	public Category getCategory();

	public int getStatus();

	public BaseModel resource();
	
	public int getIsActive();
	
	public Type getType();
	
	public Resource getResource();
}
