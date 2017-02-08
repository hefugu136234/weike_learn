package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.List;


public class VotesPost {
	public List<VotePostItem> posts;
	
	
	public class VotePostItem {
		public String s_uuid;
		public List<String> selected_options;
	}

}
