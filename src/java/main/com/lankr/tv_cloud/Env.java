/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月22日
 * 	@modifyDate 2016年6月22日
 *  
 */
package com.lankr.tv_cloud;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Kalean.Xiang
 *
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface Env {
	// 是否忽略平台
	boolean ignore() default false;

	String alias() default "";
}
