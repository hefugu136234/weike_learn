package com.lankr.tv_cloud.web.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FrontAuth {

	public boolean requiredAuth() default true;

	public boolean logger() default true;
}
