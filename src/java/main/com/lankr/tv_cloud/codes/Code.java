/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月6日
 * 	@modifyDate 2016年5月6日
 *  
 */
package com.lankr.tv_cloud.codes;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;

public class Code {

	private final int id;

	private final String name;

	private final String message;

	private final String status;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public Code(int id, String name, String message, String status) {
		this.id = id;
		this.name = name;
		this.message = message;
		this.status = status;
	}

	public ActionMessage getActionMessage() {
		ActionMessage msg = new ActionMessage();
		// if (isOk()) {
		// msg = ActionMessage.successStatus(null);
		// msg.setMessage(message);
		// } else {
		// msg = ActionMessage.failStatus(message);
		// msg.setCode(id);
		// }
		msg.setCode(id);
		msg.setMessage(message);
		if (isOk() || Status.SUCCESS.message().equals(status)) {
			msg.setStatus(Status.SUCCESS);
		} else {
			msg.setStatus(Status.FAILURE);
		}
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月20日
	 * @modifyDate 2016年5月20日
	 * 
	 */
	@Override
	public String toString() {
		return "code: " + id + " ,message: " + message;
	}

	public boolean isOk() {
		return 0 == id;
	}

	// public boolean isNotOk() {
	// return !isOk();
	// }
	// 目前返回 不为正确的状态,将来可能会有更加细致的区分
	public boolean isBad() {
		return !isOk();
	}

}
