package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class WxPdf {
	private String pdfTaskId;// pdftaskid
	
	private int pdfnum;

	public String getPdfTaskId() {
		return pdfTaskId;
	}

	public void setPdfTaskId(String pdfTaskId) {
		this.pdfTaskId = pdfTaskId;
	}

	public int getPdfnum() {
		return pdfnum;
	}

	public void setPdfnum(int pdfnum) {
		this.pdfnum = pdfnum;
	}

	public void build(PdfInfo info) {
		this.setPdfTaskId(OptionalUtils.traceValue(info, "taskId"));
		String num = OptionalUtils.traceValue(info, "pdfnum");
		int pdfnum = 1;
		if (!num.isEmpty()) {
			pdfnum = Integer.parseInt(num);
		}
		this.setPdfnum(pdfnum);
	}

}
