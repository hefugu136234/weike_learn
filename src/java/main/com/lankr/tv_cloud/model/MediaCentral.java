/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月19日
 * 	@modifyDate 2016年5月19日
 *  所有需要多媒体的存储(如：图片)，进行统一管理
 */
package com.lankr.tv_cloud.model;

import java.util.Date;

public class MediaCentral extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int MEDIA_TYPE_IMAGE = 0;

	public static final int MEDIA_TYPE_NUMERICAL = 1;

	public static final int MEDIA_TYPE_TEXT = 2;

	public static final int MEDIA_TYPE_DATE = 3;

	/**
	 * refer type code
	 * */
	// begin
	public static final int REFER_TYPE_CATEGORY = 1;
	public static final int REFER_TYPE_RESOURCE = 2;
	public static final int REFER_TYPE_ACTIVITYEXPERT = 3;
	public static final int REFER_TYPE_NORMALCOLLECT = 4; 
	public static final int REFER_TYPE_QUESTIONNAIRE = 5;
	// end

	/**
	 * sign code
	 * */
	// begin
	/*
	 * public static final int SIGN_DEFAULT = 0; public static final int
	 * SIGN_TV_BACKGROUND = 1; public static final int SIGN_WX_BACKGROUND = 2;
	 * public static final int SIGN_TV_KV = 3; public static final int
	 * SIGN_WX_KV = 4; public static final int SIGN_TV_COVER = 5; public static
	 * final int SIGN_WX_COVER = 6; public static final int SIGN_WEB_BACKGROUND
	 * = 7; public static final int SIGN_WEB_KV = 8; public static final int
	 * SIGN_WEB_COVER = 9;
	 */

	/* -------------- modify by xm ---------------- */
	public static final int SIGN_DEFAULT = 0;
	public static final int SIGN_WX_COVER = 1;
	public static final int SIGN_APP_COVER = 2;
	public static final int SIGN_TV_COVER = 3;
	public static final int SIGN_WEB_COVER = 4;
	public static final int SIGN_WX_KV = 5;
	public static final int SIGN_APP_KV = 6;
	public static final int SIGN_TV_KV = 7;
	public static final int SIGN_WEB_KV = 8;
	public static final int SIGN_WX_BACKGROUND = 9;
	public static final int SIGN_APP_BACKGROUND = 10;
	public static final int SIGN_TV_BACKGROUND = 11;
	public static final int SIGN_WEB_BACKGROUND = 12;
	
	public static final int SIGN_WX_SUBJECT = 13;
	
	
	
	public static final int SIGN_COURSE_WEB_COVER = 14;
	public static final int SIGN_COURSE_WECHAT_COVER = 15;
	public static final int SIGN_COURSE_WECHAT_BG = 18;
	
	public static final int SIGN_COURSE_CHAPTERS_QUESTIONNAARE = 19;
	
	public static final int SIGN_COMPILATION_WEB_COVER = 16;
	public static final int SIGN_COMPILATION_WECHAT_COVER = 17;
	

	/* ------------------------------------------- */
	// end

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public int getReferType() {
		return referType;
	}

	public void setReferType(int referType) {
		this.referType = referType;
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

	private String name;

	private String url;

	// 媒体的大小 reserved
	private int size;

	// 宽 reserved
	private int width;

	// 高 reserved
	private int height;

	private int mediaType;

	// 关联的表
	private int referType; // 水平扩展

	// 关联的id字段
	private int referId;

	// 业务标识
	private int sign;// 纵向扩展

	private int status;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getNumber() {
		return number;
	}

	public void setNumber(float number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	private String text;

	private float number;

	private Date date;

	public static MediaCentral initCategory(Category category, int sign) {
		if (!hasPersisted(category))
			return null;
		return product(REFER_TYPE_CATEGORY, category.getId(), sign,
				MEDIA_TYPE_IMAGE);
	}
	
	public static MediaCentral initActivityExpert(ActivityExpert expert, int sign) {
		/*if (!hasPersisted(expert))
			return null;*/
		return product(REFER_TYPE_ACTIVITYEXPERT, expert.getId(), sign,
				MEDIA_TYPE_IMAGE);
	}
	
	public static MediaCentral initNormalCollect(NormalCollect collect,
			int sign) {
		return product(REFER_TYPE_NORMALCOLLECT, collect.getId(), sign,
				MEDIA_TYPE_IMAGE);
	}

	private static MediaCentral product(int referType, int referId, int sign,
			int mimeType) {
		MediaCentral mc = new MediaCentral();
		mc.setReferType(referType);
		mc.setReferId(referId);
		mc.setSign(sign);
		// 默认图片，可扩展
		mc.setMediaType(mimeType);
		return mc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.model.BaseModel#getDefaultLock()
	 */
	@Override
	public Object getDefaultLock() {
		if (hasPersisted()) {
			return super.getDefaultLock();
		}
		return getLock(referType + "" + referId + "" + sign);
	}

	public static MediaCentral initQuestionnaire(Questionnaire questionnaire, int sign) {
		return product(REFER_TYPE_QUESTIONNAIRE, questionnaire.getId(), sign,
				MEDIA_TYPE_IMAGE);
	}
}
