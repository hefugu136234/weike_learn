package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.tv_cloud.facade.PdfFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.PdfInfo;

public class PdfFacadeImp extends FacadeBaseImpl implements PdfFacade {

	@Override
	public Status addPdfInfo(PdfInfo pdfInfo) {
		// TODO Auto-generated method stub
		try {
			pdfInfoDao.add(pdfInfo, getSqlAlias("addPdfInfo"));
			// 添加到资源表
			recodeResource(pdfInfo);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("add pdf occur an error", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public Status updatePdfInfo(PdfInfo pdfInfo) {
		try {
			pdfInfoDao.update(pdfInfo, getSqlAlias("updatePdfInfo"));
			recodeResource(pdfInfo);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("update pdf occur an error", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updatePdfInfoStatus(PdfInfo pdfInfo) {
		// TODO Auto-generated method stub
		try {
			pdfInfoDao.update(pdfInfo, getSqlAlias("updatePdfInfoStatus"));
			recodeResource(pdfInfo);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("update pdf status error", e);
		}
		return Status.FAILURE;
	}

	@Override
	public PdfInfo selectPdfInfoByUuid(String uuid) {
		// TODO Auto-generated method stub
		return pdfInfoDao.getById(uuid, getSqlAlias("selectPdfInfoByUuid"));
	}

	@Override
	public Pagination<PdfInfo> selectPdfInfoList(String searchValue, int from,
			int pageItemTotal) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from pdf_info where isActive=1 and (name like '%"
				+ searchValue + "%' or namePinyin like '%" + searchValue + "%')";
		Pagination<PdfInfo> pagination = initPage(sql, from, pageItemTotal);
		List<PdfInfo> list = pdfInfoDao.searchAllPagination(
				getSqlAlias("selectPdfInfoList"), searchValue, from,
				pageItemTotal);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	protected String namespace() {
		return "pdf_info";
	}

}
