package com.lankr.tv_cloud.web.api.tv;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Media;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class MediaVo extends BaseAPIModel {

	private List<MediaTv> mediaTvs;

	private String qrCode;

	private ClinicTV clinicTv;

	

	public ClinicTV getClinicTv() {
		return clinicTv;
	}

	public void setClinicTv(ClinicTV clinicTv) {
		this.clinicTv = clinicTv;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public List<MediaTv> getMediaTvs() {
		return mediaTvs;
	}

	public void setMediaTvs(List<MediaTv> mediaTvs) {
		this.mediaTvs = mediaTvs;
	}

	class ClinicTV {
		String name;
		String type;
		String address;
		String contactPhone;
		String description;
	}

	class MediaTv {
		String uuid;
		String createDate;
		String taskId;
		String type;
		String description;
		String originHost;
	}

	public void fommatData(List<Media> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		if (mediaTvs == null) {
			mediaTvs = new ArrayList<MediaVo.MediaTv>();
		}
		for (Media media : list) {
			MediaTv tv = new MediaTv();
			tv.uuid = media.getUuid();
			tv.createDate = Tools.formatYMDHMSDate(media.getCreateDate());
			tv.taskId = media.getTaskId();
			tv.type = media.getType();
			tv.description = media.getDescription();
			tv.originHost = media.getOriginHost();
			mediaTvs.add(tv);
		}

	}


}
