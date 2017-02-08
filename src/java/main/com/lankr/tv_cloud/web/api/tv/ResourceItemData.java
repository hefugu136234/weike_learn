package com.lankr.tv_cloud.web.api.tv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.ThreeScreen;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.GeneralVo;
import com.lankr.tv_cloud.vo.SpeakerVo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ResourceItemData extends BaseAPIModel {

	private ResourceItem open_info;

	private boolean favorited;

	private boolean cacheable;

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	private VideoVo video;

	private PdfVo pdf;

	private NewsVo news;

	private ThreeScreenVo threeScreen;

	private SpeakerVo speaker;

	private List<ResourceItem> related;
	
	private List<String> tags;

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<ResourceItem> getRelated() {
		return related;
	}

	public void buildRelatedResources(List<Resource> resources) {
		if (resources == null)
			return;
		related = new ArrayList<ResourceItem>();
		for (Resource resource : resources) {
			ResourceItem item = new ResourceItem();
			item.build(resource);
			related.add(item);
		}
	}

	private void buildSpeaker(Speaker s) {
		if (s != null) {
			speaker = new SpeakerVo();
			speaker.build(s);
		}
	}

	public ResourceItemData build(Resource resource) {
		if (resource == null)
			return this;
		setStatus(Status.SUCCESS);
		open_info = new ResourceItem();
		open_info.build(resource);
		if (resource.getType() == Type.VIDEO) {
			video = new VideoVo();
			video.format(resource.getVideo());
			// 为视频的时候可以缓存
			cacheable = true;
		} else if (resource.getType() == Type.NEWS) {
			news = new NewsVo();
			news.format(resource.getNews());

		} else if (resource.getType() == Type.PDF) {
			pdf = new PdfVo();
			pdf.format(resource.getPdf());
			if (Tools.isBlank(open_info.getCover())) {
				open_info.setCover(resource.getPdf().getPdfDefCover());
			}
		} else if (resource.getType() == Type.THREESCREEN) {
			threeScreen = new ThreeScreenVo();
			threeScreen.format(resource.getThreeScreen());
		}
		buildSpeaker(resource.getSpeaker());
		if (null != resource.getTags() && resource.getTags().size() > 0) {
			tags = new ArrayList<String>();
			for (TagChild tag : resource.getTags()) {
				tags.add(tag.getName());
			}
		}
		return this;
	}

	public void setQrURL(String qr) {
		if (open_info != null) {
			open_info.setQr(qr);
		}
	}
}

class VideoVo implements GeneralVo<Video> {
	String uuid;
	String ccVideoId;
	int duration;
	boolean need_price;
	float price;
	String fileId;
	String plays_metainfo;

	@Override
	public void format(Video t) {
		uuid = t.getUuid();
		ccVideoId = t.getCcVideoId();
		duration = t.getDuration();
		need_price = t.isNeed_price();
		price = t.getPrice();
		fileId = t.getFileId();
		plays_metainfo = VodData.resolveJsonData(t.getPlays_metainfo());
	}
}

class PdfVo implements GeneralVo<PdfInfo> {

	String uuid;
	String taskId;
	int pages;
	String pdfUrl;
	int show_type;
	// 控制客户端是否显示缩略图
	boolean show_thumbnails = false;

	@Override
	public void format(PdfInfo t) {
		taskId = t.getTaskId();
		pages = Integer.valueOf(t.getPdfnum());
		uuid = t.getUuid();
		pdfUrl = Config.qn_cdn_host + "/" + taskId;
		show_type = t.getShowType();
	}

	// @Override
	// public void format(PdfInfo t) {
	// uuid = t.getUuid();
	// taskId = t.getTaskId();
	// try {
	// pages = Integer.valueOf(t.getPdfnum());
	// String data = HttpUtils
	// .sendGetRequest("http://cloud.lankr.cn/api/pdf/" + taskId);
	// PdfUrl pu = gson.fromJson(data, PdfUrl.class);
	// if (pu != null) {
	// pdfUrl = pu.url;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// class PdfUrl {
	// String url;
	// }
}

class NewsVo implements GeneralVo<NewsInfo> {
	String uuid;

	@Override
	public void format(NewsInfo t) {
		uuid = t.getUuid();
	}
}

class ThreeScreenVo implements GeneralVo<ThreeScreen> {

	String uuid;
	int duration;
	String fileId;
	String plays_metainfo;
	String pdfUrl;
	int pages;
	String bridge;

	@Override
	public void format(ThreeScreen t) {
		uuid = t.getUuid();
		duration = t.getVideoTime();
		fileId = t.getFileId();
		plays_metainfo = VodData.resolveJsonData(t.getPlays_metainfo());
		pdfUrl = Config.qn_cdn_host + "/" + t.getPdfTaskId();
		pages = t.getPdfNum();
		bridge = t.getDivision();
		// bridge = fakeBridge();
	}

	// private String fakeBridge() {
	// List<BridgeItem> items = new ArrayList<BridgeItem>();
	// items.add(new BridgeItem(0, 1));
	// items.add(new BridgeItem(13, 2));
	// items.add(new BridgeItem(25, 3));
	// items.add(new BridgeItem(56, 4));
	// items.add(new BridgeItem(120, 5));
	// items.add(new BridgeItem(130, 6, "此处有一段描述"));
	// items.add(new BridgeItem(160, 7));
	// return new Gson().toJson(items);
	// }

}

class BridgeItem {
	int time;
	int page;
	String desc;

	public BridgeItem(int time, int page) {
		this.time = time;
		this.page = page;
	}

	public BridgeItem(int time, int page, String desc) {
		this.time = time;
		this.page = page;
		this.desc = desc;
	}
}

class VodData {
	int code;
	String message;
	List<VodPlayItem> playSet;

	private transient static Gson gson = new Gson();

	public static String resolveJsonData(String plays_metainfo) {
		if (Tools.isBlank(plays_metainfo)) {
			return null;
		}
		try {
			VodData vd = gson.fromJson(plays_metainfo, VodData.class);
			Collections.sort(vd.playSet);
			return gson.toJson(vd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plays_metainfo;
	}
}

class VodPlayItem implements Comparable<VodPlayItem> {
	String url;
	int definition;
	int vbitrate;
	int vheight;
	int vwidth;

	// 保证最大码率的MP4的在最后一位
	private int intervene() {
		try {
			if (url.toLowerCase().endsWith(".mp4")) {
				return 100000000 + vbitrate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vbitrate;
	}

	@Override
	public int compareTo(VodPlayItem o) {
		return intervene() - o.intervene();
	}
}
