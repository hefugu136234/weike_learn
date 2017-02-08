package com.lankr.tv_cloud.web.auth;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;

@Retention(RUNTIME)
@Target({ METHOD })
public @interface RequestAuthority {

	public int value() default Role.PRO_ADMIN;

	public boolean requiredProject() default true;

	@Deprecated
	public boolean redirect() default true;

	public boolean logger() default true;

	public boolean requiredToken() default true;
	
	/**
	 * @he 添加修改，做微信分享使用
	 * 2016-06-20
	 * @return
	 */
	public int wxShareType() default WxSignature.WX_NO_SHARE;
	
	public boolean phoneView() default false;

}
