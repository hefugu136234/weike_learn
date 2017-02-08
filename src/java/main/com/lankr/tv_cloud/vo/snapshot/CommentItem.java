package com.lankr.tv_cloud.vo.snapshot;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lankr.orm.mybatis.mapper.MessageMapper;
import com.lankr.tv_cloud.cache.page.PageCacheConfig;
import com.lankr.tv_cloud.facade.MessageFacade;
import com.lankr.tv_cloud.facade.impl.MessageFacadeImpl;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.SpringContextHolder;

@SuppressWarnings("unused")
public class CommentItem extends AbstractItem<Message>{
	private UserItem user;
	private String body;
	private String target;
	private Integer parentId;

	public CommentItem(Message t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		user = (UserItem) buildBaseModelProperty(t.getUser(),
				UserItem.class);
		body = t.getBody();
		parentId = t.getParentId();
		Message message = this.getParentMessge(parentId);
		target = t.getReferType() == Message.REFER_TYPE_RESOURCE ? "<font color=\"green\">资源</font>" : OptionalUtils.traceValue(message, "user.nickname");
	}

	private Message getParentMessge(Integer id) {
		MessageFacade messageFacade = SpringContextHolder.getBean("messageFacade");
		return messageFacade.getById(id);
	}
}
