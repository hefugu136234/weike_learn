/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月2日
 * 	@modifyDate 2016年6月2日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import java.lang.reflect.ParameterizedType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.JsonConvertor;

/**
 * @author Kalean.Xiang
 *
 */
@JsonInclude(value = Include.NON_NULL)
public abstract class AbstractItem<T extends BaseModel> {

	protected transient T t;

	public AbstractItem(T t) {
		this.t = t;
	}

	protected String uuid;

	protected String name;

	private long createTime;

	private long modifyTime;
	// 状态码
	protected int _status;

	private String mark;
	
	

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	// 返回状态码
	private int code;

	// may override by child
	public String getName() {
		return traceValue("name");
	}

	public boolean modifyMarker() {
		return true;
	}

	public boolean createMarker() {
		return true;
	}
	
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public final void build() {
		if (t == null)
			return;
		uuid = t.getUuid();
		_status = t.getStatus();
		name = getName();
		mark = traceValue("mark");
		try {
			if (createMarker()) {
				createTime = t.getCreateDate().getTime();
			}
			if (modifyMarker()) {
				modifyTime = t.getModifyDate().getTime();
			}
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
		try {
			buildextra();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param build
	 *            build needed
	 * */
	public String itemApiJson(boolean rebuild) {
		if (rebuild) {
			build();
		}
		return BaseAPIModel.makeWrappedSuccessDataJson(this,
				JsonConvertor.JACKSON);
	}

	public String itemApiJson() {
		return itemApiJson(true);
	}

	protected abstract void buildextra() throws Exception;

	@SuppressWarnings("unchecked")
	public Class<T> peekBuildClass() {
		return (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected String traceValue(String exp) {
		// return OptionalUtils.traceValue(t, "name");
		return OptionalUtils.traceValue(t, exp);
	}

	public boolean useable(BaseModel model) {
		if (model == null)
			return false;
		return model.apiUseable();
	}

	protected <M extends BaseModel> AbstractItem<M> buildBaseModelProperty(M m,
			Class<? extends AbstractItem<M>> clazz) {
		if (!useable(m))
			return null;
		AbstractItem<M> item;
		try {
			item = clazz.getConstructor(m.getClass()).newInstance(m);
			item.build();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
