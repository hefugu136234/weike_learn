/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月19日
 * 	@modifyDate 2016年5月19日
 *  
 */
package com.lankr.tv_cloud.model;

import java.util.Date;

public class ResourceGroup extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	// 课程等级
	public static final int TYPE_COURSE_SEGMENT = 1;
	
	public static final int TYPE_GENERAL_COLLECT = 2;

	public static final int SIGN_DEFAULT = 0;

	private String name;

	private Resource resource; // 关联的资源Id

	private int type; // 关联的表

	private int referId; // 关联的id

	private int sign;

	private int status;

	private int viewCount;
	
	private Date recommendDate;

	public Date getRecommendDate() {
		return recommendDate;
	}

	public void setRecommendDate(Date recommendDate) {
		this.recommendDate = recommendDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getReferId() {
		return referId;
	}

	public void setReferId(int referId) {
		this.referId = referId;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
}
