package com.lankr.tv_cloud.support;

public class VodFile {

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getCdnStatus() {
		return cdnStatus;
	}

	public void setCdnStatus(int cdnStatus) {
		this.cdnStatus = cdnStatus;
	}

	private String fileId;
	private String fileName;
	private String size;
	private String duration;
	private String status;
	private String imageUrl;
	private int cdnStatus;

	public String statusMessage() {
		if ("2".equals(status)) {
			return "正常";
		} else if ("4".equals(status)) {
			return "视频转码中";
		} else if ("7".equals(status)) {
			return "视频转码失败";
		} else if ("100".equals(status)) {
			return "视频已删除";
		} else if ("1".equals(status)) {
			return "视频审核未通过";
		} else if ("5".equals(status)) {
			return "发布中";
		} else if ("0".equals(status)) {
			return "视频初始化中";
		} else {
			return "视频等待处理，请稍后再试";
		}
	}

	public boolean isOk() {
		return "2".equals(status);
	}
}
