package com.lankr.tv_cloud.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class UserProjectClientData extends DataTableModel<UserProjectClientItem>{

	public void buildData(User user,Project project,WebchatFacade webchatFacade,CertificationFacade certificationFacade) {
		if (user == null)
			return;
		UserProjectClientItem item = new UserProjectClientItem();
		item.setUsername(HtmlUtils.htmlEscape(user.getUsername()));
		item.setMark(Tools.nullValueFilter(HtmlUtils.htmlEscape(user.getMark())));
		item.setNickname(Tools.nullValueFilter(HtmlUtils.htmlEscape(user
				.getNickname())));
		item.setPhoneNumber(Tools.nullValueFilter(HtmlUtils.htmlEscape(user
				.getPhone())));
		item.setUuid(user.getUuid());
//		item.setCreateDate(Tools.df1.format(user.getModifyDate()));		
		item.setCreateDate(Tools.df1.format(user.getCreateDate()));	
		item.setRoleName(user.getMainRole().getRoleDesc());
		item.setUserType(OptionalUtils.traceInt(user,"userExpand.type"));
//		item.setIsActive(user.getIsActive());
		
		//modified by mayuan
		UserReference ur = user.getUserReference();
		
		/**
		List<UserReference> urs = user.getUser_reference();
		UserReference ur = null;
		if (urs != null && !urs.isEmpty()) {
			for (UserReference userReference : urs) {
				if (userReference.getProject().isSame(project)) {
					ur = userReference;
					break;
				}
			}
		}*/
		
		if(ur != null){
			item.setIsActive(ur.getIsActive());
			item.setRoleName(ur.getRole().getRoleDesc());
			
			//modified by mayuan
			Date validDate = ur.getValidDate();
			if(null != validDate){
				item.setValidDate(Tools.df1.format(validDate));
			}else{
				item.setValidDate(" ");
			}
		}
		
		WebchatUser webchatUser=webchatFacade.searchWebChatUserByUserId(user.getId());
		item.setWxNickName(OptionalUtils.traceValue(webchatUser, "nickname"));
		Certification certification=certificationFacade.getCertifiActiveByUserId(user);
		item.setRealName(OptionalUtils.traceValue(certification, "name"));
		aaData.add(item);
	}

	public void buildData(List<User> users,Project project,WebchatFacade webchatFacade,CertificationFacade certificationFacade) {
		if (users == null || users.isEmpty())
			return;
		for (User user : users) {
			buildData(user,project,webchatFacade,certificationFacade);
		}
	}

	/*============未注册用户列表开始 2016-12-21=============*/
	public void buildWebcahtUserData(List<WebchatUser> results) {
		if (results == null || results.isEmpty())
			return;
		for (WebchatUser user : results)
			buildWechatUserData(user);
	}

	private void buildWechatUserData(WebchatUser wechatUser) {
		if (null == wechatUser)
			return;
		UserProjectClientItem item = new UserProjectClientItem();
		item.setUuid(wechatUser.getUuid());
		item.setWxNickName(OptionalUtils.traceValue(wechatUser,"nickname"));
		item.setCreateDate(Tools.df1.format(wechatUser.getCreateDate()));
		item.setPhoto(OptionalUtils.traceValue(wechatUser,"photo"));
		item.setOpenId(OptionalUtils.traceValue(wechatUser,"openId"));
		item.setUnionId(OptionalUtils.traceValue(wechatUser,"unionid"));
		item.setIsActive(wechatUser.getIsActive());
		aaData.add(item);
	}
	/*============未注册用户列表结束 wechatuser 2016-12-21=============*/

}
