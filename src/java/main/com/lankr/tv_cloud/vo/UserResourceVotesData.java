package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class UserResourceVotesData extends BaseAPIModel {

	private List<SubjectItem> items = new ArrayList<SubjectItem>();
	


	/**
	 * @param subject
	 *            投票的主题
	 * @voted_options 用户投选的选项的id
	 * */
	public void buildSubjectItem(ResourceVoteSubject subject,
			List<Integer> voted_options) {
		items.add(new SubjectItem().build(subject, voted_options));
	}
}

class SubjectItem {

	String uuid;

	String title;

	// 题型
	int type;

	// 用户是否已经投票
	boolean voted;

	List<OptionItem> options;

	SubjectItem build(ResourceVoteSubject subject, List<Integer> voted_options) {
		uuid = subject.getUuid();
		title = subject.getTitle();
		type = subject.getType();
		voted = (voted_options != null && voted_options.size() > 0);
		List<ResourceVoteOption> os = subject.getOptions();
		if (os != null) {
			options = new ArrayList<OptionItem>(os.size());
			for (ResourceVoteOption resourceVoteOption : os) {
				options.add(new OptionItem().build(resourceVoteOption,
						voted_options));
			}
		}
		return this;
	}
}

class OptionItem {

	String uuid;

	String option;
	// 投选该项的总数
	Integer count = null;
	// 用户是否已经投了该项
	boolean selected;

	OptionItem build(ResourceVoteOption o, List<Integer> voted_options) {
		uuid = o.getUuid();
		option = o.getOption();
		count = o.getCount();
		if (voted_options != null && voted_options.size() > 0) {
			//count = o.getCount();
			selected = voted_options.contains(o.getId());
		}
		return this;
	}
}
