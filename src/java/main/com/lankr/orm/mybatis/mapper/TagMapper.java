package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TagParent;

public interface TagMapper {
	
	public int saveParentTag(TagParent tagParent);
	
	public void saveChildTag(TagChild tChild);

	public List<TagParent> queryParentTagLit(SubParams subParams);

	public List<TagChild> queryChildTagLit(SubParams subParams);

	public TagParent selectParentTagByUuid(String uuid);

	public TagChild selectChildTagByUuid(String uuid);

	public void deleteChildTag(TagChild tagChild);
	
	public void deleteParentTag(TagParent tagParent);

	public void updateParentTag(TagParent tagParent);

	public void updateChildTag(TagChild tagChild);

	public TagParent selectParentTagByName(String tagName);

	public TagChild selectChildTagByName(String tagName);

	public List<TagParent> queryParentTagLitWithoutPageOption();

	public List<TagChild> queryChildTagListWithoutPageOption(int parent_id);

	public List<TagChild> getTagsByResourceId(int id);
	
}
