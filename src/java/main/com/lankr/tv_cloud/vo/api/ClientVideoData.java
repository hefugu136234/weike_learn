package com.lankr.tv_cloud.vo.api;
import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.vo.ClientVideo;

public class ClientVideoData extends BaseAPIModel{
	private List<ClientVideo> videoList;
	private String categoryUUid;
	private String categoryName;

	public List<ClientVideo> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<ClientVideo> videoList) {
		this.videoList = videoList;
	}
	
	
	
	public String getCategoryUUid() {
		return categoryUUid;
	}

	public void setCategoryUUid(String categoryUUid) {
		this.categoryUUid = categoryUUid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void fommatData(List<Video> list,Category category) throws Exception{
		categoryUUid=category.getUuid();
		categoryName=category.getName();
		if(list==null||list.isEmpty())
			return ;
		videoList =new ArrayList<ClientVideo>();
		for(Video video:list){
			ClientVideo clientVideo=new ClientVideo();
			clientVideo.formatData(video);
			videoList.add(clientVideo);
		}
	}

}
