/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月6日
 * 	@modifyDate 2016年6月6日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;

/**
 * @author Kalean.Xiang
 *
 */
public class ResourceSimpleItem extends AbstractItem<Resource> {

	private int rank;

	private float rate;

	private int viewCount;

	private String cover;

	private String qr;

	private CategoryItem category;

	private String descript;

	private String type;

	public long updated_at; // ms

	// 编号
	private String rCode;

	public String getrCode() {
		return rCode;
	}

	public void setrCode(String rCode) {
		this.rCode = rCode;
	}

	private SpeakerItem speaker;
	
	
	

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public CategoryItem getCategory() {
		return category;
	}

	public void setCategory(CategoryItem category) {
		this.category = category;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(long updated_at) {
		this.updated_at = updated_at;
	}

	public SpeakerItem getSpeaker() {
		return speaker;
	}

	public void setSpeaker(SpeakerItem speaker) {
		this.speaker = speaker;
	}

	/**
	 * @param t
	 */
	public ResourceSimpleItem(Resource t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		rank = t.getRank();
		rate = t.getRate();
		viewCount = t.getViewCount();
		cover = t.getCoverTaskId();
		if (!StringUtils.isEmpty(cover) && !cover.startsWith("http")) {
			cover = "http://cloud.lankr.cn/api/image/" + t.getCoverTaskId();
		}
		rCode = t.getCode();
		Date updated = t.getUpdated_at();
		if (updated != null) {
			updated_at = updated.getTime();
		}
		qr = t.getQrTaskId();
		category = (CategoryItem) buildBaseModelProperty(t.getCategory(), CategoryItem.class);
		speaker = (SpeakerItem) buildBaseModelProperty(t.getSpeaker(), SpeakerItem.class);
		type = t.getType().name();	//20160614 xm
	}

}
