package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class RecourceDetailVo extends BaseAPIModel {
	
	/** 返回统一的一个资源详情的数据， 包含video pdf 三分屏 */
	
	private String uuid;
	private String date;
	private String name;
	private int viewCount;
	private String cover;
	private String descript;
	private String type;
	private int rank;
	private float rate;
	private boolean collectionStatus;
	private int collectionCount;
	private boolean praiseStatus;
	private int praiseCount;
	private String spakerName;
	private String spakerHosptial;
	private RecourceTypeData typeData;
	private boolean voteNoUser;

	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public RecourceTypeData getTypeData() {
		return typeData;
	}
	public void setTypeData(RecourceTypeData typeData) {
		this.typeData = typeData;
	}
	public boolean isCollectionStatus() {
		return collectionStatus;
	}
	public void setCollectionStatus(boolean collectionStatus) {
		this.collectionStatus = collectionStatus;
	}
	public int getCollectionCount() {
		return collectionCount;
	}
	public void setCollectionCount(int collectionCount) {
		this.collectionCount = collectionCount;
	}
	public boolean isPraiseStatus() {
		return praiseStatus;
	}
	public void setPraiseStatus(boolean praiseStatus) {
		this.praiseStatus = praiseStatus;
	}
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}
	public String getSpakerName() {
		return spakerName;
	}
	public void setSpakerName(String spakerName) {
		this.spakerName = spakerName;
	}
	public String getSpakerHosptial() {
		return spakerHosptial;
	}
	public void setSpakerHosptial(String spakerHosptial) {
		this.spakerHosptial = spakerHosptial;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public boolean isVoteNoUser() {
		return voteNoUser;
	}
	public void setVoteNoUser(boolean voteNoUser) {
		this.voteNoUser = voteNoUser;
	}

	public void buildData(Resource resource,boolean collectionStatus,int collectionCount,
										boolean praiseStatus,int praiseCount,boolean voteNoUser){
		if (resource == null)
			return;
		setStatus(Status.SUCCESS);
		this.setVoteNoUser(voteNoUser);
		this.setCollectionStatus(collectionStatus);
		this.setCollectionCount(collectionCount);
		this.setPraiseStatus(praiseStatus);
		this.setPraiseCount(praiseCount);
		this.setSpakerName(OptionalUtils.traceValue(resource, "speaker.name"));
		this.setSpakerHosptial(OptionalUtils.traceValue(resource, "speaker.hospital.name"));
		this.setUuid(resource.getUuid());
		this.setName(Tools.nullValueFilter(resource.getName()));
		this.setCover(Tools.nullValueFilter(resource.getCoverTaskId()));
		this.setDate(Tools.formatYMDHMSDate(resource.getCreateDate()));
		this.setDescript(Tools.nullValueFilter(resource.getMark()));
		this.setRank(resource.getRank());
		this.setRate(resource.getRate());
		this.setType(resource.getType().name());
		this.setViewCount(resource.getViewCount());
		this.typeData=new RecourceTypeData();
		if (resource.getType() == Type.VIDEO) {
			this.typeData.buildData(resource.getVideo());
		} else if (resource.getType() == Type.NEWS) {
			this.typeData.buildData(resource.getNews());
		} else if (resource.getType() == Type.PDF) {
			this.typeData.buildData(resource.getPdf());
		}else if(resource.getType() == Type.THREESCREEN){
			this.typeData.buildData(resource.getThreeScreen());
		}
	}
	
	public void buildPdfDetail(Resource resource){
		this.setSpakerName(OptionalUtils.traceValue(resource, "speaker.name"));
		this.setSpakerHosptial(OptionalUtils.traceValue(resource, "speaker.hospital.name"));
		this.setUuid(resource.getUuid());
		this.setName(Tools.nullValueFilter(resource.getName()));
		this.setCover(Tools.nullValueFilter(resource.getCoverTaskId()));
		this.setType(resource.getType().name());
	}
}
