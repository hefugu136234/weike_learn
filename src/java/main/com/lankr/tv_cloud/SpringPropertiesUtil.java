/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月22日
 * 	@modifyDate 2016年6月22日
 *  
 */
package com.lankr.tv_cloud;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author Kalean.Xiang
 *
 */
public class SpringPropertiesUtil extends PropertyPlaceholderConfigurer {

	private static Map<String, String> propertiesMap;
	private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

	@Override
	public void setSystemPropertiesMode(int systemPropertiesMode) {
		super.setSystemPropertiesMode(systemPropertiesMode);
		springSystemPropertiesMode = systemPropertiesMode;
	}

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		super.processProperties(beanFactory, props);

		propertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String valueStr = resolvePlaceholder(keyStr, props,
					springSystemPropertiesMode);
			propertiesMap.put(keyStr, valueStr);
		}
	}

	public static Object getProperty(String name) throws Exception {
		return propertiesMap.get(name);
	}

	public static String getStringProperty(String name) throws Exception {
		Object obj = getProperty(name);
		if (obj != null) {
			return String.valueOf(obj);
		}
		return null;
	}

	public static boolean getBooleanProperty(String name) throws Exception {
		String obj = getStringProperty(name);
		if (obj != null) {
			return Boolean.valueOf(obj);
		}
		return false;
	}

	public static int getIntProperty(String name) throws Exception {
		String obj = getStringProperty(name);
		if (obj != null) {
			return Integer.valueOf(obj);
		}
		return 0;
	}

	public static float getFloatProperty(String name) throws Exception {
		String obj = getStringProperty(name);
		if (obj != null) {
			return Float.valueOf(obj);
		}
		return 0;
	}
}