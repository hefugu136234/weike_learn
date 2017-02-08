package com.lankr.tv_cloud.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.SubWidgetItem;
import com.lankr.tv_cloud.vo.WidgetV2;

public class TvLayout extends BaseModel {

	public static final int TYPE_HOME = 1; // 首页页面

	public static final int TYPE_INNER = 0; // 子页面

	public static final int TYPE_INNER_V2 = 2; // 新版本的子页面

	public static final String TYPE_RESOURCE = "RESOURCE";

	public static final String TYPE_ACTIVITY = "ACTIVITY";

	public static final String TYPE_CATEGORY = "CATEGORY";
	
	public static final String TYPE_BROADCAST = "BROADCAST";

	/**
	 * 
	 */
	private static final long serialVersionUID = -5265371604803424453L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getWidgets() {
		return widgets;
	}

	public void setWidgets(String widgets) {
		this.widgets = widgets;
	}

	private String name;

	private Category category;

	private int status;

	private User user;

	private String widgets;

	private int type;

	private Project project;

	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private static Gson gson = new Gson();

	public boolean isWidgetsValid() {
		try {
			List<SubWidgetItem> items = gson.fromJson(widgets,
					new TypeToken<ArrayList<SubWidgetItem>>() {
					}.getType());
			if (items == null || items.isEmpty())
				return false;
			for (SubWidgetItem subWidgetItem : items) {
				if (Tools.isBlank(subWidgetItem.getCategoryId())) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isHomeLayout() {
		return type == TYPE_HOME;
	}

	public boolean isHomeWidgetsValid() {
		List<WidgetV2> items = gson.fromJson(widgets,
				new TypeToken<ArrayList<WidgetV2>>() {
				}.getType());
		if (items == null || items.isEmpty())
			return false;
		for (WidgetV2 widgetV2 : items) {
			if (!widgetV2.isValid())
				return false;
		}
		return true;
	}
}
