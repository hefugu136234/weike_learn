package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.facade.MessageFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.utils.SpringContextHolder;

public class ShowCommentItem extends InfiniteItem<Message>{
	
	private String resName;

	private int id;
	
	private String content;
	
	private int praiseNum;
	
	private int integral;
	
	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	private UserItem userView;
	
	private ResourceSimpleItem resource;
	
	public ResourceSimpleItem getResource() {
		return resource;
	}

	public void setResource(ResourceSimpleItem resource) {
		this.resource = resource;
	}

	private boolean isDelete = false ;
	
	private boolean isPraise = false ;
	
	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserItem getUserView() {
		return userView;
	}

	public void setUserView(UserItem userView) {
		this.userView = userView;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isPraise() {
		return isPraise;
	}

	public void setPraise(boolean isPraise) {
		this.isPraise = isPraise;
	}
	
	

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public ShowCommentItem(Message message,int deep) {
		super(message,deep);
	}

	@Override
	public Message makeParent() {
		MessageFacade messageFacade = SpringContextHolder.getBean("messageFacade");
		int id = t.getParentId() ;
		if (id == 0)
			return null ;
		Message message = messageFacade.getById(id);
		return message ;
	}

	@Override
	public Class<? extends InfiniteItem<Message>> childClass() {
		return ShowCommentItem.class;
	}
	
	/* (non-Javadoc)
	 * @see com.lankr.tv_cloud.vo.snapshot.InfiniteItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		super.buildextra();
		this.praiseNum = t.getPraise();
		this.id = t.getId();
		this.content = t.getBody();
//		this.userView = new CommentUserItem(t.getUser());
//		userView.build();
		this.userView = (UserItem) buildBaseModelProperty(t.getUser(), UserItem.class);		
		if(Message.COMMENT_TYPE == t.getType() && Message.REFER_TYPE_RESOURCE == t.getReferType())
			this.resource = this.makeResource(t.getReferId());	
	}
	
	
	private ResourceSimpleItem makeResource(int referId) {
		ResourceFacade resourceFacade = SpringContextHolder.getBean("resourceFacade");
		return (ResourceSimpleItem) buildBaseModelProperty(resourceFacade.getResourceById(referId), ResourceSimpleItem.class);
	}

}
