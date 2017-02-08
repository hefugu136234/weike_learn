/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月6日
 * 	@modifyDate: 2016年4月6日
 *  旨在统一所有文本消息输出
 */
package com.lankr.tv_cloud.model;

public class Message extends BaseModel {

	public static final int SIGN_DEF = 0;
	
	public static final int TYPE_DEF = 0;
	
	public static final int COMMENT_TYPE = 1 ;//评论 type
	
	public static final int REPLY_COMMENT_TYPE = 2 ;//回复 type
	
	public static final int MESSAGE_REFER_TYPE_OPUS = 1; //用户
	
	public static final int REFER_TYPE_NOTIFICAITON = 2;//发送的提醒消息
	
	public static final int REFER_TYPE_RESOURCE = 3;//评论资源
	
	public static final int REFER_TYPE_MESSAGE = 4;//评论回复
	
	public static final int REFER_TYPE_OFFLINEACTIVITY = 5;//评论线下活动
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getReferId() {
		return referId;
	}

	public void setReferId(int referId) {
		this.referId = referId;
	}

	public int getReferType() {
		return referType;
	}

	public void setReferType(int referType) {
		this.referType = referType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	private String name;

	private String body;

	private int referId; // 关联的id

	private int referType;// 对应的表

	private int type;

	private User user;

	private int sign; // 对应的类别代码

	private int status;

	private int praise; // 点赞数
	
	private int parentId ;

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

}
