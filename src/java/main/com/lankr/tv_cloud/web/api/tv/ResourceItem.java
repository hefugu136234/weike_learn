package com.lankr.tv_cloud.web.api.tv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.SpeakerVo;

public class ResourceItem {

	public String getUuid() {
		return uuid;
	}

	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public int getRank() {
		return rank;
	}

	public float getRate() {
		return rate;
	}

	public int getViewCount() {
		return viewCount;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public CategoryVO getCategory() {
		return category;
	}

	public String getDescript() {
		return descript;
	}

	public String getType() {
		return type;
	}

	private String uuid;

	private String date;

	private String name;

	private String pinyin;

	private int rank;

	private float rate;

	private int viewCount;

	private String cover;

	private String qr;

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	private CategoryVO category;

	private String descript;

	private String type;

	public long updated_at; // ms

	// 编号
	private String code;

	private SpeakerVo speaker;
	
	private List<String> tags;

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public ResourceItem build(Resource resource) {
		if (resource == null)
			return this;
		uuid = resource.getUuid();
		date = Tools.formatYMDHMSDate(resource.getCreateDate());
		name = resource.getName();
		pinyin = resource.getPinyin();
		rank = resource.getRank();
		rate = resource.getRate();
		viewCount = resource.getViewCount();
		cover = resource.getCoverTaskId();
		if (!StringUtils.isEmpty(cover) && !cover.startsWith("http")) {
			cover = "http://cloud.lankr.cn/api/image/"
					+ resource.getCoverTaskId();
		}
		qr = resource.getQrTaskId();
		Category c = resource.getCategory();
		if (c != null)
			category = new CategoryVO(c.getUuid(), c.getName(), null);
		descript = Tools.nullValueFilter(resource.getMark());
		type = resource.getType().name();
		code = OptionalUtils.traceValue(resource, "code");

		Date updated = resource.getUpdated_at();
		if (updated != null) {
			updated_at = updated.getTime();
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

	private void buildSpeaker(Speaker s) {
		if (s != null) {
			speaker = new SpeakerVo();
			speaker.build(s);
		}else{
			speaker = new SpeakerVo();
			speaker.setName("");
			speaker.setHospitalName("");
		}
	}

}
