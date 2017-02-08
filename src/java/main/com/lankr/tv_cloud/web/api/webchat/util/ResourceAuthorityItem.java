package com.lankr.tv_cloud.web.api.webchat.util;

import com.lankr.tv_cloud.model.ResourceAccessIgnore;
import com.lankr.tv_cloud.utils.Tools;

public class ResourceAuthorityItem {
	
	//打钩代表不能浏览，不能观看
	
	private boolean noRegister;//未注册
	
	private boolean noRealName;//未实名
	
	private boolean realNamed;//已经实名

	public boolean isNoRegister() {
		return noRegister;
	}

	public void setNoRegister(boolean noRegister) {
		this.noRegister = noRegister;
	}

	public boolean isNoRealName() {
		return noRealName;
	}

	public void setNoRealName(boolean noRealName) {
		this.noRealName = noRealName;
	}

	public boolean isRealNamed() {
		return realNamed;
	}

	public void setRealNamed(boolean realNamed) {
		this.realNamed = realNamed;
	}
	
	public void buildAuth(String value){
		if(Tools.isBlank(value))
			return ;
		if(value.contains(ResourceAccessIgnore.NO_REGISTERD)){
			this.setNoRegister(true);
		}else if(value.contains(ResourceAccessIgnore.HAS_REGISTERD_NO_RELNAME)){
			this.setNoRealName(true);
		}else if(value.contains(ResourceAccessIgnore.HAS_RELNAME)){
			this.setRealNamed(true);
		}
	}
	

}
