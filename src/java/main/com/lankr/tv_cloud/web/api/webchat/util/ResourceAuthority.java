package com.lankr.tv_cloud.web.api.webchat.util;

import com.lankr.tv_cloud.model.ResourceAccessIgnore;
import com.lankr.tv_cloud.utils.Tools;

public class ResourceAuthority {

	// 需要将反向权限，转化正向权限
	private ResourceAuthorityItem noScan;// 不能浏览

	private ResourceAuthorityItem noView;// 不能观看

	public ResourceAuthorityItem getNoScan() {
		return noScan;
	}

	public void setNoScan(ResourceAuthorityItem noScan) {
		this.noScan = noScan;
	}

	public ResourceAuthorityItem getNoView() {
		return noView;
	}

	public void setNoView(ResourceAuthorityItem noView) {
		this.noView = noView;
	}


	public void buildAuth(String[] authItem) {
		if(authItem==null||authItem.length!=2){
			return ;
		}
		String authVlaue=authItem[0];//代表横坐标，（具体的权限值）
		String authType=authItem[1];//代表纵坐标，（具体权限分类，观看和浏览）
		if(Tools.isBlank(authType)){
			return ;
		}
		if(authType.contains(ResourceAccessIgnore.IGNORE_PRE_VIEW)){
			// 不能浏览
			if(this.noScan==null){
				this.noScan=new ResourceAuthorityItem();
			}
			this.noScan.buildAuth(authVlaue);
		}else if(authType.contains(ResourceAccessIgnore.IGNORE_WATCH)){
			// 不能观看
			if(this.noView==null){
				this.noView=new ResourceAuthorityItem();
			}
			this.noView.buildAuth(authVlaue);
		}

	}
	
	public static ResourceAuthority changeAuthority(
			ResourceAccessIgnore resourceAccessIgnore) {
		if (resourceAccessIgnore == null) {
			return null;
		}
		String auth = resourceAccessIgnore.getDetail();
		if (Tools.isBlank(auth)) {
			return null;
		}
		String[] authArray = auth.split(";");
		if (authArray == null || authArray.length == 0) {
			return null;
		}
		ResourceAuthority authority = new ResourceAuthority();
		for (String item : authArray) {
			String[] authItem = item.split(",");
			authority.buildAuth(authItem);
		}
		return authority;
	}

}
