package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.CategoryExpand;
import com.lankr.tv_cloud.model.CategoryExpandStatus;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class MediaCentralVo extends BaseAPIModel {

	private List<MediaCentralSufaces> cateExpands = new ArrayList<MediaCentralSufaces>();

	public List<MediaCentralSufaces> build(List<MediaCentral> mediaCentrals) {
		if(null != mediaCentrals && mediaCentrals.size() >0){
			for(MediaCentral central : mediaCentrals){
				MediaCentralSufaces sufaces = new MediaCentralSufaces();
				sufaces.setTypeId(central.getSign());
				sufaces.setTaskId(central.getUrl());
				sufaces.setText(central.getText());
				cateExpands.add(sufaces);
			}
		}
		return cateExpands;
	}

	public class MediaCentralSufaces {
		private Integer typeId;
		private String taskId;
		private String text;
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public Integer getTypeId() {
			return typeId;
		}
		public void setTypeId(Integer typeId) {
			this.typeId = typeId;
		}
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
	}
}
