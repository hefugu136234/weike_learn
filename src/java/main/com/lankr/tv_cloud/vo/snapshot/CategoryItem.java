/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月6日
 * 	@modifyDate 2016年6月6日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Category;

/**
 * @author Kalean.Xiang
 *
 */
public class CategoryItem extends InfiniteItem<Category> {

	/**
	 * @param t
	 */
	public CategoryItem(Category t) {
		super(t);
	}

	public CategoryItem(Category t, int deep) {
		super(t, deep);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.InfiniteItem#makeParent()
	 */
	@Override
	public Category makeParent() {
		return t.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.InfiniteItem#childClass()
	 */
	@Override
	public Class<? extends InfiniteItem<Category>> childClass() {
		return getClass();
	}

}
