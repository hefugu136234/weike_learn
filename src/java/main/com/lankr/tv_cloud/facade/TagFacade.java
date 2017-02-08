package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TagParent;

public interface TagFacade {

	public Status saveParentTag(TagParent tParent) throws Exception;

	public Pagination<TagParent> queryParentTagList(String searchValue,
			int startPage, int pageSize);

	public Pagination<TagChild> queryChildTagList(String searchValue,
			int startPage, int pageSize, int parent_id);

	public TagParent selectParentTagByUuid(String uuid);

	public Status deleteParentTag(TagParent tagParent);

	public Status saveChildTag(TagChild tChild) throws Exception;

	public TagChild selectChildTagByUuid(String uuid);

	public Status deleteChildTag(TagChild tagChild);

	public Status updateParentTag(TagParent tagParent);

	public Status updateChildTag(TagChild tagChild);

	public boolean selectParentTagByName(String tag_name);

	public boolean selectChildTagByName(String tag_name);

	public List<TagParent> queryParentTagListWithoutPageOption();

	public List<TagChild> queryChildTagListWithoutPageOption(int parent_id);

	public List<TagChild> getTagsByResourceId(int id);

}
