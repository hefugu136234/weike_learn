package com.lankr.tv_cloud.model;

/**
 * @author Kalean.Xiang
 *
 */
public class IntegralConsume extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4127065868629497633L;

	public static final int TYPE_GOODS = 1; // 虚拟商品

	public static final int TYPE_LIVE = 2; // 直播

	public static final int TYPE_ENTITY_GOODS = 3; // 实体商品
	
	public static final int TYPE_OFFLINE_ACTIVITY=4;//线下活动消耗积分

	public static final int SIGN_USER_UNCERTIFICATED = 0;// 用户不需要实名的

	public static final int SIGN_USER_CERTIFICATED = 1;// 用户需要实名的

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

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMedias() {
		return medias;
	}

	public void setMedias(String medias) {
		this.medias = medias;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private String name;

	private String pinyin;

	private String cover;

	private float price;

	private int type; // 消耗类型 商品或者其他

	private int integral;

	private String description;

	private String medias;

	private int number;

	private int status;

	private int referId;
	
	private int userLimited; //次数限制，0表示无限制
	
	private int sign;	//兑换是否需要实名，0：未实名，1:实名

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getUserLimited() {
		return userLimited;
	}

	public void setUserLimited(int userLimited) {
		this.userLimited = userLimited;
	}

	public int getReferId() {
		return referId;
	}

	public void setReferId(int referId) {
		this.referId = referId;
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年5月4日
	 * @modifyDate: 2016年5月4日
	 * @return 是否是商品
	 */
	public boolean isGoods() {
		return type == TYPE_GOODS || type == TYPE_ENTITY_GOODS;
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年5月4日
	 * @modifyDate: 2016年5月4日
	 * @return 是否是实体商品
	 */
	public boolean isEntityGoods() {
		return type == TYPE_ENTITY_GOODS;
	}

	public boolean needUserCertificated() {
		return sign == SIGN_USER_CERTIFICATED;
	}
}
