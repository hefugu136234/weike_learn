/** 
 *  author Kalean.Xiang
 *  createDate: 2016年3月22日
 * 	modifyDate: 2016年3月22日
 *  
 */
package com.lankr.tv_cloud.facade;

public interface BaseFacade<T> {

	public ActionMessage add(T t);

	public ActionMessage del(T t);

	public T getByUuid(String uuid);

	public ActionMessage update(T t);
}
