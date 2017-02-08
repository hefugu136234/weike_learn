package com.lankr.tv_cloud.web.api.webchat.util;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class MyAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator{
	
	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {
		/*
		String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
		return Introspector.decapitalize(shortClassName);*/
		
		return definition.getBeanClassName();
	}

}
