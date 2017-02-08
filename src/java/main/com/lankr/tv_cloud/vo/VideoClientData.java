package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class VideoClientData extends DataTableModel<VideoClientItem> {

	private String categoryUuid;

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	private String categoryName;

	public void buildData(Video video) {
		if (video == null)
			return;
		VideoClientItem item = new VideoClientItem();
		item.format(video);
		aaData.add(item);
	}

	public void buildData(List<Video> videos) {
		if (videos == null || videos.isEmpty())
			return;
		for (Video video : videos) {
			buildData(video);
		}
	}
}
