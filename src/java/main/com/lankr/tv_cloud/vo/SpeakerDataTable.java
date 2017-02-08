package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class SpeakerDataTable extends DataTableModel<SpeakerVo> {

	public void addItem(SpeakerVo vo) {
		getAaData().add(vo);
	}
}
