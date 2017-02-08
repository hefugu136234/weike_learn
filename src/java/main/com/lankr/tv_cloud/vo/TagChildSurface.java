package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class TagChildSurface extends DataTableModel<TagChildVo>{
	
	public void buildChildData(List<TagChild> childTag) {
		if (null == childTag || childTag.size() == 0)
			return;
		for (TagChild tag : childTag) {
			if (null == childTag)
				return;
			TagChildVo data = new TagChildVo();
			data.buildData(tag);
			aaData.add(data);
		}
	}
	
}
