/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月22日
 * 	@modifyDate 2016年6月22日
 *  
 */
package com.lankr.tv_cloud;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
class ConfigUtils {
	private static Log logger = LogFactory.getLog(ConfigUtils.class);

	static void driven(Class clazz) throws Exception {
		String env = SpringPropertiesUtil.getStringProperty("env");
		Field[] fs = clazz.getDeclaredFields();
		if (fs == null)
			return;
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			Env e = f.getAnnotation(Env.class);
			if (e != null) {
				String key = Tools.isBlank(e.alias()) ? f.getName() : e.alias();
				if (!e.ignore()) {
					key = env + "." + key;
				}
				try {
					if (!Modifier.isStatic(f.getModifiers())) {
						throw new Exception("static field needed #"
								+ f.getName());
					}
					Class<?> type = f.getType();

					if (type == String.class) {
						f.set(null, SpringPropertiesUtil.getStringProperty(key));
					} else if (type == Boolean.class || type == boolean.class) {
						f.setBoolean(null,
								SpringPropertiesUtil.getBooleanProperty(key));
					} else if (type == Integer.class || type == int.class) {
						f.setInt(null, SpringPropertiesUtil.getIntProperty(key));
					} else if (type == Float.class || type == float.class) {
						f.setFloat(null,
								SpringPropertiesUtil.getFloatProperty(key));
					}					
				} catch (Exception exc) {
					logger.error(clazz.getName() + "#" + f.getName()
							+ " initialize accur an error", exc);
				}
			}
		}
	}

}