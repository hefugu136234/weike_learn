package com.lankr.tv_cloud.facade;

import java.util.ArrayList;
import java.util.List;

public class ActionMessage<T> {

	public Status getStatus() {
		return status;
	}

	public ActionMessage setStatus(Status status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private Status status;

	private String message;

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ActionMessage(Status status) {
		this.status = status;
	}
	
	public ActionMessage() {
	}
	
	private static final ActionMessage success = new ActionMessage(
			Status.SUCCESS);

	public static ActionMessage failStatus(String message) {
		ActionMessage msg = new ActionMessage(Status.FAILURE);
		msg.setMessage(message);
		return msg;
	}

	public static ActionMessage successStatus() {
		return success;
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月6日
	 * @modifyDate 2016年5月6日 can wrapped data
	 */
	public static ActionMessage successStatus(Object wrapped) {
		ActionMessage msg = new ActionMessage(Status.SUCCESS);
		msg.setData(wrapped);
		return msg;
	}

	public boolean isSuccess() {
		return status == Status.SUCCESS;
	}

	private List<String> warnings;

	private T data;

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void addWarnings(String warning) {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		warnings.add(warning);
	}
}
