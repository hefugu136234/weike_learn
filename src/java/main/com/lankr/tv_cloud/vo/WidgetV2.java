package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WidgetV2 {

	public transient static final String TYPE_RESOURCE = "RESOURCE";
	public transient static final String TYPE_ACTIVITY = "ACTIVITY";
	public transient static final String TYPE_CATEGORY = "CATEGORY";

	private String scriptId;

	private String imageUrl;

	private int x;

	private int y;

	private int offset_x;

	private int offset_y;

	private String mark;

	private Ref ref;

	public static class Ref {

		String type;

		String uuid;
	}

	public boolean isValid() {
		return !Tools.isBlank(OptionalUtils.traceValue(ref, "type"))
				&& !Tools.isBlank(OptionalUtils.traceValue(ref, "uuid"));
	}
}
