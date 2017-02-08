package com.lankr.tv_cloud.web.api.webchat.util;


public class ResourceAuthorityUtil {
	
	/**
	 * 资源权限针对给一个用户，是否可看，是否可浏览（权限转为正向）
	 */
	
	private boolean reScan;//true=可 false=不可
	
	private boolean needLogin;
	
	private String reScanResult;
	
	private boolean resView;
	
	private String  resViewResult;
	
	

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public boolean isReScan() {
		return reScan;
	}

	public void setReScan(boolean reScan) {
		this.reScan = reScan;
	}

	public boolean isResView() {
		return resView;
	}

	public void setResView(boolean resView) {
		this.resView = resView;
	}
	
	public String getReScanResult() {
		return reScanResult;
	}

	public void setReScanResult(String reScanResult) {
		this.reScanResult = reScanResult;
	}

	public String getResViewResult() {
		return resViewResult;
	}

	public void setResViewResult(String resViewResult) {
		this.resViewResult = resViewResult;
	}
	
	public boolean registerFlag(boolean noRegister,boolean hasUser){
		if(noRegister){
			if(!hasUser){
				return false;
			}
		}
		return true;
	}
	
	public boolean needRealName(boolean noRealName,boolean realNamed){
		if(noRealName){
			if(!realNamed){
				return false;
			}
		}
		return true;
	}
	
	public boolean noAuth(boolean realNamed){
		if(realNamed){
			return false;
		}
		return true;
	}
	
	public void buildScan(ResourceAuthority authority,boolean hasUser,boolean realNamed){
		ResourceAuthorityItem noScan=authority.getNoScan();
		if(noScan!=null){
			boolean flag=registerFlag(noScan.isNoRegister(),hasUser);
			if(!flag){
				this.setReScan(false);
				this.setReScanResult("未注册用户，无权限浏览资源页面");
				return ;
			}
			flag=needRealName(noScan.isNoRealName(),realNamed);
			if(!flag){
				this.setReScan(false);
				this.setReScanResult("未实名用户，无权限浏览资源页面");
				return ;
			}
			flag=noAuth(noScan.isRealNamed());
			if(!flag){
				this.setReScan(false);
				this.setReScanResult("无权限浏览资源页面");
				return ;
			}
		}
		this.setReScan(true);
	}
	
	public void buildView(ResourceAuthority authority,boolean hasUser,boolean realNamed){
		ResourceAuthorityItem noView=authority.getNoView();
		if(noView!=null){
			boolean flag=registerFlag(noView.isNoRegister(),hasUser);
			if(!flag){
				this.setResView(false);
				this.setNeedLogin(true);
				this.setResViewResult("未注册用户，无权限观看资源内容");
				return ;
			}
			flag=needRealName(noView.isNoRealName(),realNamed);
			if(!flag){
				this.setResView(false);
				this.setResViewResult("未实名用户，无权限观看资源内容");
				return ;
			}
			flag=noAuth(noView.isRealNamed());
			if(!flag){
				this.setResView(false);
				this.setResViewResult("无权限观看资源内容");
				return ;
			}
		}
		this.setResView(true);
	}

	public static ResourceAuthorityUtil changeAuthorityUtil(ResourceAuthority authority,boolean hasUser,boolean realNamed){
		if(authority==null)
		return null;
		ResourceAuthorityUtil util=new ResourceAuthorityUtil();
		util.buildScan(authority, hasUser, realNamed);
		util.buildView(authority, hasUser, realNamed);
		return util;
	}

}
