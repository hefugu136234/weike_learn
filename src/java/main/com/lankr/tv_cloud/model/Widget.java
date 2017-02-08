package com.lankr.tv_cloud.model;

@SuppressWarnings("all")
public class Widget extends BaseModel {
	
	private String taskId;
	private int x;
	private int y;
	private int offset_x;
	private int offset_y;
	private User user;
	private Category category;
	private float version;
	private String tv_cover;
	private Project project;
	
	public Widget(int x, int y, int offset_x, int offset_y, String tv_cover) {
		super();
		this.x = x;
		this.y = y;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
		this.tv_cover = tv_cover;
	}

	public Widget(){}
	
	
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getOffset_x() {
		return offset_x;
	}
	
	public void setOffset_x(int offset_x) {
		this.offset_x = offset_x;
	}
	
	public int getOffset_y() {
		return offset_y;
	}
	
	public void setOffset_y(int offset_y) {
		this.offset_y = offset_y;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public String getTv_cover() {
		return tv_cover;
	}

	public void setTv_cover(String tv_cover) {
		this.tv_cover = tv_cover;
	}


	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
