package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ResourceVotesData extends BaseAPIModel {
	private String uuid;
	private String title;
	private int type;
	public List<VoteOption> options;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<VoteOption> getOptions() {
		return options;
	}

	public void setOptions(List<VoteOption> options) {
		this.options = options;
	}
	

	public ResourceVoteSubject parseToSubject() {
		if (options == null || options.isEmpty() || StringUtils.isEmpty(title))
			return null;
		ResourceVoteSubject subject = new ResourceVoteSubject();
		subject.setTitle(title);
		//0-3
		subject.setType(Math.min(Math.max(type, 0), 3));
		subject.setUuid(uuid);
		List<ResourceVoteOption> opts = new ArrayList<ResourceVoteOption>();
		for (VoteOption vo : options) {
			opts.add(vo.parseToOption());
		}
		subject.setOptions(opts);
		return subject;
	}

	public ResourceVotesData formatFromVote(ResourceVoteSubject subject) {
		if (subject == null)
			return this;
		setStatus(Status.SUCCESS);
		uuid = subject.getUuid();
		title = subject.getTitle();
		type = subject.getType();
		List<ResourceVoteOption> opts = subject.getOptions();
		if (opts != null) {
			if (options != null) {
				options.clear();
			} else {
				options = new ArrayList<>();
			}
			for (ResourceVoteOption resourceVoteOption : opts) {
				if (resourceVoteOption != null) {
					options.add(new VoteOption()
							.formatFromVoteOption(resourceVoteOption));
				}
			}
		}
		return this;
	}
}

class VoteOption {
	String uuid;
	String option;
	int count;

	ResourceVoteOption parseToOption() {
		ResourceVoteOption o = new ResourceVoteOption();
		o.setOption(option);
		o.setUuid(uuid);
		return o;
	}

	VoteOption formatFromVoteOption(ResourceVoteOption opt) {
		uuid = opt.getUuid();
		option = opt.getOption();
		count = opt.getCount();
		return this;
	}

}
