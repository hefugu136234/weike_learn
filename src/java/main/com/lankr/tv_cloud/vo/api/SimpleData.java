package com.lankr.tv_cloud.vo.api;

import java.util.List;

public class SimpleData {
	String key;

	public SimpleData(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public SimpleData(String key, Object value, SimpleDataType dataType) {
		this.key = key;
		this.value = value;
		this.dataType = dataType;
	}

	Object value;
	SimpleDataType dataType;

}
