package com.lankr.tv_cloud.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ActivityConfigMedia {

	public static final int MEDIA_TV_BACKGROUND = 0;
	public static final int MEDIA_WX_BACKGROUND = 1;
	public static final int MEDIA_TV_KV = 2;
	public static final int MEDIA_WX_KV = 3;
	public static final int MEDIA_TV_COVER = 4;
	public static final int MEDIA_WX_COVER = 5;
	
	// 图片类别
	private int type;
	// media的连接地址
	private String url;
	private String destUrl;

	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDestUrl() {
		return destUrl;
	}

	public void setDestUrl(String destUrl) {
		this.destUrl = destUrl;
	}

	public static class Snapshot {
		transient static Gson gson = new Gson();
		int type;
		String url;

		private ActivityConfigMedia prototype() {
			if (type == MEDIA_TV_BACKGROUND || type == MEDIA_WX_BACKGROUND
					|| type == MEDIA_TV_KV || type == MEDIA_WX_KV
					|| type == MEDIA_WX_COVER || type == MEDIA_TV_COVER) {
				ActivityConfigMedia media = new ActivityConfigMedia();
				media.type = this.type;
				media.url = this.url;
				return media;
			}
			return null;
		}

		public static List<ActivityConfigMedia> convertMedias(String json)
				throws Exception {
			List<Snapshot> snapshots = gson.fromJson(json,
					new TypeToken<List<Snapshot>>() {
					}.getType());

			if (snapshots == null || snapshots.isEmpty()) {
				return null;
			}
			List<ActivityConfigMedia> medias = new ArrayList<ActivityConfigMedia>(
					snapshots.size());
			List<Integer> _cacheType = new ArrayList<Integer>();
			for (Snapshot snap : snapshots) {
				ActivityConfigMedia media = snap.prototype();
				if (media != null && !_cacheType.contains(media.type)) {
					medias.add(media);
					_cacheType.add(media.type);
				}
			}
			return medias;
		}
	}
}
