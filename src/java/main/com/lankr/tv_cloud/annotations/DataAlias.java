package com.lankr.tv_cloud.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface DataAlias {
	String column() default "";

	Class<?> from() default Object.class;

	Class<?> target() default Object.class;
	
}
