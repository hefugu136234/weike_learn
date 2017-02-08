package com.lankr.tv_cloud.utils;

import java.lang.reflect.Field;

public class OptionalUtils {

	private final static String EMPTY_STRING = "".intern();

	/**
	 * @param obj
	 *            被调用的对象
	 * @param exp
	 *            表达式 如 xx.xx.xx 需要有封装的getXx()方法调用，主要 如果返回值为boolean 则需要主要
	 *            为isXx()
	 * */
	public static String traceValue(Object obj, String exp) {
		return traceValue(obj, exp, EMPTY_STRING);
	}

	public static int traceInt(Object obj, String exp) {
		return traceValue(obj, exp, Integer.class, 0);
	}

	public static boolean traceBoolean(Object obj, String exp) {
		return traceValue(obj, exp, Boolean.class, false);
	}

	public static float traceFloat(Object obj, String exp) {
		return traceValue(obj, exp, Float.class, 0f);
	}

	public static long traceLong(Object obj, String exp) {
		return traceValue(obj, exp, Long.class, 0L);
	}

	public static <T> T traceValue(Object obj, String exp, Class<T> t, T def) {
		if (obj == null || exp == null || exp.isEmpty())
			return def;
		String[] fields = exp.split("\\.");
		Object value = obj;
		for (int i = 0; i < fields.length; i++) {
			value = invoke(value, fields[i]);
			if (value == null) {
				return def;
			}
		}
		try {
			return t.cast(value);
		} catch (Exception e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * @param obj
	 *            被调用的对象
	 * @param exp
	 *            表达式 如 xx.xx.xx 需要有封装的getXx()方法调用，主要 如果返回值为boolean 则需要主要
	 *            为isXx()
	 * @param def
	 *            默认值
	 * */
	public static String traceValue(Object obj, String exp, String def) {
		return traceValue(obj, exp, String.class, def);
	}

	private static Object invoke(Object obj, String field) {
		if (obj == null)
			return null;
		Field f = null;
		Class<?> clazz = obj.getClass();
		do {
			try {
				f = clazz.getDeclaredField(field);
				f.setAccessible(true);
				return f.get(obj); // 返回obj对象上f字段的值
			} catch (Exception e) {
				if (e instanceof NoSuchFieldException) {
					clazz = clazz.getSuperclass();
				}
			}
		} while (Object.class != clazz);

		return null;
	}

	public static String boxGetMethod(Field field) {
		if (field == null)
			return null;
		if (field.getType().equals(boolean.class)) {
			return "is" + capitalize(field.getName());
		}
		return "get" + capitalize(field.getName());
	}

	public static String capitalize(String str) {
		return capitalize(str, null);
	}

	public static String capitalize(String str, char[] delimiters) {
		int delimLen = (delimiters == null ? -1 : delimiters.length);
		if (str == null || str.length() == 0 || delimLen == 0) {
			return str;
		}
		int strLen = str.length();
		StringBuffer buffer = new StringBuffer(strLen);
		boolean capitalizeNext = true;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);
			if (isDelimiter(ch, delimiters)) {
				buffer.append(ch);
				capitalizeNext = true;
			} else if (capitalizeNext) {
				buffer.append(Character.toTitleCase(ch));
				capitalizeNext = false;
			} else {
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}

	private static boolean isDelimiter(char ch, char[] delimiters) {
		if (delimiters == null) {
			return Character.isWhitespace(ch);
		}
		for (int i = 0, isize = delimiters.length; i < isize; i++) {
			if (ch == delimiters[i]) {
				return true;
			}
		}
		return false;
	}
}
