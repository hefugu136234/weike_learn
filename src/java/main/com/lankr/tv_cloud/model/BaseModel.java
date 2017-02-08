package com.lankr.tv_cloud.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

@SuppressWarnings("all")
public class BaseModel implements Serializable {

	private static Log logger = LogFactory.getLog(BaseModel.class);

	private static final long serialVersionUID = -7630265962787638291L;
	public static final int UNAPPROVED = 0;// 未审核
	public static final int APPROVED = 1;// 上线
	public static final int UNDERLINE = 2;// 下线

	public static final int ACTIVE = 1;
	public static final int DISABLE = 0;

	public static final int TRUE = 1;
	public static final int FALSE = 0;
	
	protected static Gson gson = new Gson();

	private int id;
	private Date createDate;
	private Date modifyDate;
	private String mark;
	private String uuid;
	private int isActive;
	
	// private static Map<String, Lock> lock_cache = new
	// ConcurrentHashMap<String, Lock>(50, 0.75f);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMark() {
		return mark == null ? "" : mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public boolean hasPersisted() {
		return id > 0;
	}

	public boolean isActive() {
		return getIsActive() == ACTIVE;
	}

	/**
	 * author Kalean.Xiang createDate: 2016年3月22日 modifyDate: 2016年3月22日
	 * override by sub class if needed
	 */
	public int getStatus() {
		return APPROVED;
	}

	/**
	 * author Kalean.Xiang createDate: 2016年3月22日 modifyDate: 2016年3月22日
	 * 提供接口时候，判断model是否可用
	 */
	public boolean apiUseable() {
		return getStatus() == APPROVED && isActive();
	}

	public <T extends BaseModel> boolean isSame(T t) {
		if (t == null)
			return false;
		if (hasPersisted())
			return getId() == t.getId();
		return false;
	}

	public Object getDefaultLock() {
		return getLock("");
	}

	public Object getLock(String sufix) {
		return (sufix + getKey()).intern();
		/*
		 * synchronized (lock_cache) { String key = getKey();
		 * System.out.println(key); Lock _lock = lock_cache.get(key); if (_lock
		 * == null) { _lock = new ReentrantLock(){
		 * 
		 * @Override public void unlock() { super.unlock(); releaseLock(); } };
		 * lock_cache.put(key, _lock); } return _lock; }
		 */
	}

	private String getKey() {
		return getId() + "@" + getClass().getName();
	}

	/*
	 * private void releaseLock(){ lock_cache.remove(getKey()); }
	 */

	public static boolean hasPersisted(BaseModel model) {
		if (model == null)
			return false;
		return model.hasPersisted();
	}
}
