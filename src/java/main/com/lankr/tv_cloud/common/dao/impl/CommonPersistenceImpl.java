package com.lankr.tv_cloud.common.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.lankr.tv_cloud.common.dao.CommonPersistence;

@SuppressWarnings("unchecked")
public class CommonPersistenceImpl<T, PK extends Serializable> implements
		CommonPersistence<T, PK> {

	private Class<?> entityClass;

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public RowBounds getRowBounds(int from, int rows) {
		return new RowBounds(from, rows);
	}

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void add(T entity, String sql_alias) {
		sqlSession.insert(sql_alias, entity);
	}

	@Override
	public void del(Serializable id, String sql_alias) {
		sqlSession.delete(sql_alias, id);
	}

	@Override
	public void del(T entity, String sql_alias) {
		sqlSession.delete(sql_alias, entity);
	}

	@Override
	public int update(T entity, String alias_sql) {
		return sqlSession.update(alias_sql, entity);
	}

	@Override
	public List<T> searchAll(String alias_sql) {
		return sqlSession.selectList(alias_sql);
	}

	@Override
	public List<T> searchAllPagination(String sql_alias, int from, int rows) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(sql_alias, entityClass,
				getRowBounds(from, rows));
	}

	@Override
	public List<T> searchAllPagination(String sql_alias, Object param,
			int from, int rows) {
		return sqlSession
				.selectList(sql_alias, param, getRowBounds(from, rows));
	}

	@Override
	public T search(T entity, String alias_sql) {
		return (T) sqlSession.selectOne(alias_sql, entity);
	}

	@Override
	public List<T> searchAll(String sql_alias, Object param) {
		return sqlSession.selectList(sql_alias, param);
	}

	@Override
	public T getById(Serializable id, String sql_alias) {
		return (T) sqlSession.selectOne(sql_alias, id);

	}

	@Override
	public List<T> searchByForeignKey(Serializable key, String sql_slias) {
		return sqlSession.selectList(sql_slias, key);
	}

	@Override
	public int getCountById(Serializable id, String alias_sql) {
		return sqlSession.selectOne(alias_sql, id) ;
	}
}
