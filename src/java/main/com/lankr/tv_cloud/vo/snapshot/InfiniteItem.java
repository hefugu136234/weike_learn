/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月6日
 * 	@modifyDate 2016年6月6日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.BaseModel;

/**
 * @author Kalean.Xiang
 *
 */
public abstract class InfiniteItem<T extends BaseModel> extends AbstractItem<T> {

	/**
	 * @param t
	 */
	private transient static final int INFINITE = -1;

	private transient static final int IGNORE_PARENT = 0;

	private transient int mDeep = 0;

	private InfiniteItem<T> parent;

	public abstract T makeParent();

	public abstract Class<? extends InfiniteItem<T>> childClass();

	public InfiniteItem(T t) {
		this(t, 1);
	}

	/**
	 * parent deeply
	 * */
	public InfiniteItem(T t, int deep) {
		super(t);
		mDeep = deep;
	}

	/**
	 * @param t
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		if (mDeep > IGNORE_PARENT) {
			buildParent(--mDeep);
		} else if (mDeep == INFINITE) {
			buildParent(INFINITE);
		}
	}

	private void buildParent(int deep) {
		T p = makeParent();
		if (p != null && p.apiUseable()) {
			try {
				parent = childClass().getConstructor(p.getClass(), int.class)
						.newInstance(new Object[] { p, deep });
				parent.build();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
