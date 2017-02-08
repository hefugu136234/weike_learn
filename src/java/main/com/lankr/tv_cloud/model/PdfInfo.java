package com.lankr.tv_cloud.model;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.utils.Tools;

public class PdfInfo extends BaseModel implements Resourceable {

	private static final long serialVersionUID = -8343120734181831392L;
	private Category category;
	private String name;
	private String namePinyin;
	private String pdfsize;
	private String pdfnum;
	// 0 审核 1上线 2下线
	private int status;
	private String taskId;
	private String qrTaskId;
	private String coverTaskId;
	private int showType;

	private Resource resource;
	private Speaker speaker;

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}
	
	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPdfsize() {
		return pdfsize;
	}

	public void setPdfsize(String pdfsize) {
		this.pdfsize = pdfsize;
	}

	public String getPdfnum() {
		return pdfnum;
	}

	public void setPdfnum(String pdfnum) {
		this.pdfnum = pdfnum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getQrTaskId() {
		return qrTaskId;
	}

	public void setQrTaskId(String qrTaskId) {
		this.qrTaskId = qrTaskId;
	}

	public String getCoverTaskId() {
		return coverTaskId;
	}

	public void setCoverTaskId(String coverTaskId) {
		this.coverTaskId = coverTaskId;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return getMark();
	}

	@Override
	public String getCover() {
		return getCoverTaskId();
	}

	@Override
	public String getQr() {
		// TODO Auto-generated method stub
		return getQrTaskId();
	}

	@Override
	public BaseModel resource() {
		return this;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.PDF;
	}

	@Override
	public Resource getResource() {
		return resource;
	}

	@Override
	public int getPrototypeId() {
		// TODO Auto-generated method stub
		return getId();
	}

	public String getPdfDownloadUrl() {
		return Config.qn_cdn_host + "/" + taskId;
	}

	public String getPdfDefCover() {
		if (Tools.isBlank(coverTaskId)) {
			return getPdfDownloadUrl()
					+ "?odconv/jpg/page/1/density/160/quality/80/resize/600";
		}
		return coverTaskId;
	}
}
