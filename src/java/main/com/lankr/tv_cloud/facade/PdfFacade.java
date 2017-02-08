package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.PdfInfo;

public interface PdfFacade {

	public Status addPdfInfo(PdfInfo pdfInfo);

	public Status updatePdfInfo(PdfInfo pdfInfo);

	public Status updatePdfInfoStatus(PdfInfo pdfInfo);

	public PdfInfo selectPdfInfoByUuid(String uuid);

	public Pagination<PdfInfo> selectPdfInfoList(String searchValue,
			int from, int pageItemTotal);

}
