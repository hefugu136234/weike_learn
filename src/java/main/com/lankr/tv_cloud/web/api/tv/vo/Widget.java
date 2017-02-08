package com.lankr.tv_cloud.web.api.tv.vo;

import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.web.api.tv.ResourceItem;

public class Widget {

	public static final String TYPE_NORMAL = "NORMAL";

	public static final String TYPE_RESOURCE = "RESOURCE";

	public static enum RefType {
		RESOURCE, CATEGORY, ACTIVITY
	}

	private String type;

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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	private int x;

	private int y;

	private int offset_x;

	private int offset_y;

	private String mark;

	private String imageUrl;

	private ResourceItem resource;

	public String getImageUrl() {
		return imageUrl;
	}

	public void buildResouce(ResourceFacade facade) {
		if (TYPE_RESOURCE.equals(type) && ref != null) {
			Resource res = facade.getResourceByUuid(ref.uuid);
			if (res != null) {
				resource = new ResourceItem();
				resource.build(res);
			}
		}
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	private Ref ref;

	public RefType getRefType() {
		if (ref == null)
			return null;
		RefType[] type = RefType.values();
		for (RefType refType : type) {
			if (refType.name().equalsIgnoreCase(ref.type)) {
				return refType;
			}
		}
		return null;
	}

	public String getRefUuid() {
		if (ref != null) {
			return ref.uuid;
		}
		return null;
	}

	private class Ref {

		private String type;

		private String uuid;

	}

}
