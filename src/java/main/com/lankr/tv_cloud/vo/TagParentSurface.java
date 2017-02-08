package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TagParent;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class TagParentSurface extends DataTableModel<TagParentVo> {
	
	public void buildParenData(List<TagParent> parentList) {
		if (null == parentList || parentList.size() == 0)
			return;
		for (TagParent tagParent : parentList) {
			if (null == parentList)
				return;
			TagParentVo data = new TagParentVo();
			data.buildData(tagParent);
			aaData.add(data);
		}
	}
}
