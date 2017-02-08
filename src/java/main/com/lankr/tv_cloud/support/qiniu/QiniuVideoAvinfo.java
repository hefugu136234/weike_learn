package com.lankr.tv_cloud.support.qiniu;

public class QiniuVideoAvinfo {

	private AvinfoFormat format;

	public AvinfoFormat getFormat() {
		return format;
	}

	public void setFormat(AvinfoFormat format) {
		this.format = format;
	}

	public class AvinfoFormat {
		String size;

		String duration;

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

	}

}
