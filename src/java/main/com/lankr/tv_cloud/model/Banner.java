package com.lankr.tv_cloud.model;

@SuppressWarnings("unused")
public class Banner extends BaseModel {

	private static final long serialVersionUID = 8292360661855697354L;
	
	public static final int TYPE_TV = 1;
	public static final int TYPE_APP = 2;
	public static final int TYPE_WECHAT = 3;
	public static final int TYPE_WEB = 4;
	//展示位置
	public final static int POSITION_ALL=0;//所有位置
	public final static int POSITION_INDEX=1;//首页banner
	public final static int POSITION_BORADCAST=2;//直播
	public final static int POSITION_ACTIVITY=3;//活动
	public final static int POSITION_COURSE=4;//课程
	
	private String title;
	private String imageUrl;
	private String refUrl;
	private int type;
	private int position;
	// 0 未审核 1已上线 2已下线
	private int state;
	private long validDate;
	private String taskId;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getRefUrl() {
		return refUrl;
	}

	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public long getValidDate() {
		return validDate;
	}

	public void setValidDate(Long validDate) {
		this.validDate = validDate;
	}
}
