package com.lankr.tv_cloud.support.ueditor;

import com.google.gson.Gson;

public class Config {

	String imageActionName = "uploadimage"; /* 执行上传图片的action名称 */
	String imageFieldName = "upfile"; /* 提交的图片表单名称 */
	int imageMaxSize = 2048000;/* 上传大小限制，单位B */
	String[] imageAllowFiles = { ".png", ".jpg", ".jpeg", ".gif", ".bmp" }; /* 上传图片格式显示 */
	boolean imageCompressEnable = true; /* 是否压缩图片,默认是true */
	int imageCompressBorder = 1600; /* 图片压缩最长边限制 */
	String imageInsertAlign = "none"; /* 插入的图片浮动方式 */
	String imageUrlPrefix = ""; /* 图片访问路径前缀 */
	String imagePathFormat = "/ueditor/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}"; /*
																					 * 上传保存路径
																					 * ,
																					 * 可以自定义保存路径和文件名格式
																					 */
	private transient static Config config = null;

	private transient static Gson gson = new Gson();

	public static String getConfigJson() {
		if (config == null) {
			config = new Config();
		}
		return gson.toJson(config);
	}

}
