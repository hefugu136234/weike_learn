package com.lankr.tv_cloud.vo;
import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Advertisement;
import com.lankr.tv_cloud.model.AdvertisementPosition;
import com.lankr.tv_cloud.utils.Tools;

public class AdverDetail {

	private String res_url;

	private int limit_time;

	private List<AdvertisementPosition> postionList;

	private String name;
	
	private String mark;
	
	private String uuid;
	
	private int positionId;

	public String getRes_url() {
		return res_url;
	}

	public void setRes_url(String res_url) {
		this.res_url = res_url;
	}

	public int getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(int limit_time) {
		this.limit_time = limit_time;
	}

	public List<AdvertisementPosition> getPostionList() {
		return postionList;
	}

	public void setPostionList(List<AdvertisementPosition> postionList) {
		this.postionList = postionList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public void formatData(Advertisement adver,List<AdvertisementPosition> list){
		try {
			uuid = adver.getUuid();
			name =  HtmlUtils.htmlEscape(Tools.nullValueFilter(adver.getName()));
			limit_time = adver.getLimit_time();
			mark = HtmlUtils.htmlEscape(Tools.nullValueFilter(adver.getMark()));
			res_url=adver.getRes_url();
			positionId=adver.getAdvertisementPosition().getId();
			postionList=list;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
