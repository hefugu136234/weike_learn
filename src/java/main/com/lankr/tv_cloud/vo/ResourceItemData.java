package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.tv.ResourceItem;

public class ResourceItemData extends BaseAPIModel{


	private ResourceItem open_info;

	private VideoVo video;

	private PdfVo pdf;

	private NewsVo news;
	
	private boolean collectionStatus;
	
	private int collectionCount;
	
	private boolean praiseStatus;
	
	private int praiseCount;
	
	private String spakerName;
	
	private String spakerHosptial;
	
	
	
	

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

	public void build(Resource resource) {
		if (resource == null)
			return;
		setStatus(Status.SUCCESS);
		open_info = new ResourceItem();
		open_info.build(resource);
		if (resource.getType() == Type.VIDEO) {
			video = new VideoVo();
			video.format(resource.getVideo());
		} else if (resource.getType() == Type.NEWS) {
			news = new NewsVo();
			news.format(resource.getNews());

		} else if (resource.getType() == Type.PDF) {
			pdf = new PdfVo();
			pdf.format(resource.getPdf());
		}
	}
	
	public void build(Resource resource,boolean collectionStatus,int collectionCount,boolean praiseStatus,int praiseCount){
		if (resource == null)
			return;
		setStatus(Status.SUCCESS);
		this.setCollectionStatus(collectionStatus);
		this.setCollectionCount(collectionCount);
		this.setPraiseStatus(praiseStatus);
		this.setPraiseCount(praiseCount);
		this.setSpakerName(OptionalUtils.traceValue(resource, "speaker.name"));
		this.setSpakerHosptial(OptionalUtils.traceValue(resource, "speaker.hospital.name"));
		open_info = new ResourceItem();
		open_info.build(resource);
		if (resource.getType() == Type.VIDEO) {
			video = new VideoVo();
			video.format(resource.getVideo());
		} else if (resource.getType() == Type.NEWS) {
			news = new NewsVo();
			news.format(resource.getNews());

		} else if (resource.getType() == Type.PDF) {
			pdf = new PdfVo();
			pdf.format(resource.getPdf());
		}
	}


}

class VideoVo implements GeneralVo<Video> {
	String uuid;
	int duration;
	String fileId;


	@Override
	public void format(Video t) {
		uuid = t.getUuid();
		duration = t.getDuration();
		fileId = t.getFileId();
	}
}

class PdfVo implements GeneralVo<PdfInfo> {
	String uuid;
	String taskId;
	int pages;

	@Override
	public void format(PdfInfo t) {
		uuid = t.getUuid();
		taskId = t.getTaskId();
		try {
			pages = Integer.valueOf(t.getPdfnum());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class NewsVo implements GeneralVo<NewsInfo> {
	String uuid;

	@Override
	public void format(NewsInfo t) {
		uuid = t.getUuid();
	}
}
