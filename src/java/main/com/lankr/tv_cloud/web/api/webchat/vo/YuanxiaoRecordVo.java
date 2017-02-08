package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.List;

import com.lankr.tv_cloud.tmp.YuanxiaoRecord;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class YuanxiaoRecordVo extends BaseAPIModel{
	private List<YuanxiaoRecord> list;

	public List<YuanxiaoRecord> getList() {
		return list;
	}

	public void setList(List<YuanxiaoRecord> list) {
		this.list = list;
	}
	
	
}
