package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.WxSubjectFacade;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxMenuLevel {

	private String levelName;

	private String levelUuid;

	private List<WxMenuItem> items;

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelUuid() {
		return levelUuid;
	}

	public void setLevelUuid(String levelUuid) {
		this.levelUuid = levelUuid;
	}

	public List<WxMenuItem> getItems() {
		return items;
	}

	public void setItems(List<WxMenuItem> items) {
		this.items = items;
	}


	public void build(WxSubject wxSubject, WxSubjectFacade wxSubjectFacade,
			ResourceFacade cacheResourceFacade) {
		this.setLevelUuid(wxSubject.getUuid());
		this.setLevelName(wxSubject.getName());
		List<WxSubject> childList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(
					wxSubject.getId(), WxSubject.TYPE_CATEGORY, WxSubject.ALL_LEVEL);
		if(Tools.isEmpty(childList)){
			return ;
		}
		this.items=new ArrayList<WxMenuItem>();
		for (WxSubject wxSubject2 : childList) {
			WxMenuItem item = new WxMenuItem();
			item.buildBaseData(wxSubject2);
			item.buildSimpleResCount(cacheResourceFacade,
					OptionalUtils.traceInt(wxSubject2, "reflectId"));
			this.items.add(item);
		}

	}

}
