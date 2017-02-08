package com.lankr.tv_cloud.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.CategorySnapshot;

public class Category extends BaseModel {

	private static final long serialVersionUID = 8444995220878992316L;

	private String name;
	private String pinyin;
	private Project project;
	private User user;
	private Category parent;
	private List<Category> children;
	private CategoryExpand expand;
	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(int hierarchy) {
		this.hierarchy = hierarchy;
	}

	private int sign;
	private int left;
	private int right;
	private int hierarchy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public CategoryExpand getExpand() {
		return expand;
	}

	public void setExpand(CategoryExpand expand) {
		this.expand = expand;
	}

	public List<CategorySnapshot> makeChildrenClientData() {
		List<CategorySnapshot> css = null;
		if (children != null && !children.isEmpty()) {
			css = new ArrayList<CategorySnapshot>();
			for (int i = 0; i < children.size(); i++) {
				Category c = children.get(i);
				CategorySnapshot cs = new CategorySnapshot();
				cs.setId(c.getUuid());
				cs.setText(HtmlUtils.htmlEscape(c.getName()));
				cs.setChildren(c.getChildren() != null
						&& !c.getChildren().isEmpty());
				cs.setEditable(true);
				cs.setAddable(true);
				cs.setDeletable(!cs.isChildren());
				css.add(cs);
			}
		}
		return css;
	}

}
