/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月3日
 * 	@modifyDate 2016年6月3日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.ResourceGroup;

/**
 * @author Kalean.Xiang
 *
 */
public class ResourceGroupItem extends AbstractItem<ResourceGroup> {
	

	/**
	 * @param t
	 */
	public ResourceGroupItem(ResourceGroup t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
	}

}
