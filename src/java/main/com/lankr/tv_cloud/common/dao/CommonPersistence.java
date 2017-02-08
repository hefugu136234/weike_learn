package com.lankr.tv_cloud.common.dao;

import java.io.Serializable;
import java.util.List;

public interface CommonPersistence<T, PK extends Serializable> {

	public void add(T entity, String sql_alias);

	public void del(Serializable id, String sql_alias);

	public void del(T entity, String sql_alias);

	public int update(T entity, String alias_sql);
	
	public int getCountById(Serializable id, String alias_sql);

	public List<T> searchAll(String alias_sql);

	public T search(T entity, String alias_sql);

	public T getById(Serializable id, String sql_alias);
	
	public List<T> searchByForeignKey(Serializable key,String sql_alias);

	public List<T> searchAllPagination(String sql_alias, int from, int rows);
	
	public List<T> searchAllPagination(String sql_alias, Object param,int from,int rows);
	
	public List<T> searchAll(String sql_alias,Object param);

}
