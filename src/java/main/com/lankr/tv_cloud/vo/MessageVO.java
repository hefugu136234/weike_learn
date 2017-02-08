/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月12日
 * 	@modifyDate: 2016年4月12日
 *  
 */
package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
public class MessageVO {

	private String uuid;

	private String name;

	private String body;

	private String createDate;

	private int praise;

	private UserInfo userInfo;

	public static MessageVO build(Message message) {
		if (message == null)
			return null;
		MessageVO vo = new MessageVO();
		vo.uuid = message.getUuid();
		vo.body = message.getBody();
		vo.createDate = Tools.formatYMDHMSDate(message.getCreateDate());
		vo.praise = message.getPraise();
		vo.userInfo = UserInfo.build(message.getUser());
		return vo;
	}

	public static List<MessageVO> build(List<Message> msgs) {
		if (msgs != null) {
			List<MessageVO> vs = new ArrayList<MessageVO>(msgs.size());
			for (int i = 0; i < msgs.size(); i++) {
				vs.add(MessageVO.build(msgs.get(i)));
			}
			return vs;
		}
		return null;
	}
}
