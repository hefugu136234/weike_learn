package com.lankr.tv_cloud.support;

import java.util.List;

public class VodInfoData {

	private int code = -1;

	private String message;

	private List<VodFile> fileSet;

	public boolean isOk() {
		return 0 == code;
	}

	public VodFile getFile() {
		if (fileSet != null && !fileSet.isEmpty()) {
			return fileSet.get(0);
		}
		return null;
	}
		
}
