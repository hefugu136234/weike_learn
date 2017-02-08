package com.lankr.tv_cloud.facade.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.BannerFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.TagFacade;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TagParent;
import com.lankr.tv_cloud.web.BaseController;

@SuppressWarnings("all")
public class TagFacadeImpl extends FacadeBaseImpl implements TagFacade {

	@Autowired
	private ResourceFacade resourceFacade;
	
	@Override
	public Status saveParentTag(TagParent tParent) throws Exception {
		try {
			tagMapper.saveParentTag(tParent);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save Tag Error", e);
			throw new Exception("保存 Tag 失败");
		}
		return Status.SUCCESS;
	}

	@Override
	protected String namespace() {
		return null;
	}

	@Override
	public Pagination<TagParent> queryParentTagList(String searchValue,
			int startPage, int pageSize) {
		// 设置查询参数
		SubParams subParams = new SubParams();
		subParams.setSize(pageSize);
		subParams.setStart(startPage);
		subParams.setQuery(searchValue);

		// 初始化分页信息
		searchValue = filterSQLSpecialChars(searchValue);
		StringBuffer sqlBuffer = new StringBuffer(
				" select count(id) from tags_parent where isActive=1 ");
		sqlBuffer.append(" and (name like '%" + searchValue
				+ "%' or mark like '%" + searchValue + "%')");
		Pagination<TagParent> pagination = initPage(sqlBuffer.toString(),
				startPage, pageSize);

		// 查询数据
		List<TagParent> tagParentsList = tagMapper.queryParentTagLit(subParams);
		pagination.setResults(tagParentsList);
		return pagination;
	}

	@Override
	public Pagination<TagChild> queryChildTagList(String searchValue,
			int startPage, int pageSize, int parent_id) {
		// 设置查询参数
		SubParams subParams = new SubParams();
		subParams.setSize(pageSize);
		subParams.setStart(startPage);
		subParams.setQuery(searchValue);
		subParams.setId(parent_id);

		// 初始化分页信息
		searchValue = filterSQLSpecialChars(searchValue);
		StringBuffer sqlBuffer = new StringBuffer(
				" select count(id) from tags_child where isActive=1 and parentId= " + parent_id);
		sqlBuffer.append(" and (name like '%" + searchValue
				+ "%' or mark like '%" + searchValue + "%')");
		Pagination<TagChild> pagination = initPage(sqlBuffer.toString(),
				startPage, pageSize);

		// 查询数据
		List<TagChild> tagChildList = tagMapper.queryChildTagLit(subParams);
		pagination.setResults(tagChildList);
		return pagination;
	}

	@Override
	public TagParent selectParentTagByUuid(String uuid) {
		TagParent tagParent = null;
		try {
			tagParent = tagMapper.selectParentTagByUuid(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Query ParentTag By Uuid Error", e);
		}
		return tagParent;
	}

	@Override
	public Status deleteParentTag(TagParent tagParent) {
		try {
			tagMapper.deleteParentTag(tagParent);
			return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete ParentTag Error", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status saveChildTag(TagChild tChild) throws Exception {
		try {
			tagMapper.saveChildTag(tChild);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save ChildTag Error", e);
			throw new Exception("保存二级标签失败");
		}
		return Status.SUCCESS;
	}

	@Override
	public TagChild selectChildTagByUuid(String uuid) {
		TagChild tagChild = null;
		try {
			tagChild = tagMapper.selectChildTagByUuid(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Query ChildTag By Uuid Error", e);
		}
		return tagChild;
	}

	@Override
	public Status deleteChildTag(TagChild tagChild) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		Status transactionResult = Status.FAILURE;
		try {
			transactionResult = transaction.execute(new TransactionCallback<Status>() {
				@Override
				public Status doInTransaction(TransactionStatus arg0) {
					tagMapper.deleteChildTag(tagChild);
					resourceFacade.delResTagByResourceIdAndTagId(
							null, tagChild.getId());
					return Status.SUCCESS;
				}
			});
		} catch (Exception e) {
			logger.error(e);
			return Status.FAILURE;
		}
		return transactionResult;
	}

	@Override
	public Status updateParentTag(TagParent tagParent) {
		try {
			tagMapper.updateParentTag(tagParent);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update ParentTag Error", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Status updateChildTag(TagChild tagChild) {
		try {
			tagMapper.updateChildTag(tagChild);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update ChildTag Error", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public boolean selectParentTagByName(String tag_name) {
		TagParent tagParent = null;
		try {
			tagParent = tagMapper.selectParentTagByName(tag_name);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Select ParentTag By Name Error", e);
		}
		if(null == tagParent){
			return false;
		}else {
			return true;
		}
	}

	@Override
	public boolean selectChildTagByName(String tag_name) {
		TagChild tagChild = null;
		try {
			tagChild = tagMapper.selectChildTagByName(tag_name);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Select ChildTag By Name Error", e);
		}
		if(null == tagChild){
			return false;
		}else {
			return true;
		}
	}

	@Override
	public List<TagParent> queryParentTagListWithoutPageOption() {
		// 查询数据
		return tagMapper.queryParentTagLitWithoutPageOption();
	}

	@Override
	public List<TagChild> queryChildTagListWithoutPageOption(int parent_id) {
		return tagMapper.queryChildTagListWithoutPageOption(parent_id);
	}

	@Override
	public List<TagChild> getTagsByResourceId(int id) {
		return tagMapper.getTagsByResourceId(id);
	}

}
