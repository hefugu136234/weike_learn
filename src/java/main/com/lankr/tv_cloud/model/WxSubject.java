package com.lankr.tv_cloud.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class WxSubject extends BaseModel{
	public final static String HAS_LEVEL="has_level" ;//查询带层级
	public final static String NO_LEVEL="no_level";//查询不带层级
	public final static String ALL_LEVEL="all_level";//所有级别子分类
	//学科的类型
	public final static int TYPE_CATEGORY=1;//分类
	
	public final static int TYPE_ACTIVITY=2;//活动
	
	public final static int ROOT=1;
	
	public final static int NOT_TOOT=0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1846555497790072562L;

	private String name;
	
	private String pinyin;
	
	private int rootType;
	
	private int reflectId;
	
	private String typeProperty;
	
	private int isRoot;
	
	private WxSubject parent;
	
	private int status;

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

	public int getRootType() {
		return rootType;
	}

	public void setRootType(int rootType) {
		this.rootType = rootType;
	}

	public int getReflectId() {
		return reflectId;
	}

	public void setReflectId(int reflectId) {
		this.reflectId = reflectId;
	}

	public String getTypeProperty() {
		return typeProperty;
	}

	public void setTypeProperty(String typeProperty) {
		this.typeProperty = typeProperty;
	}

	public int getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(int isRoot) {
		this.isRoot = isRoot;
	}

	public WxSubject getParent() {
		return parent;
	}

	public void setParent(WxSubject parent) {
		this.parent = parent;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean isRootCentain(){
		return getIsRoot()==ROOT;
	}
	
	public  class WxSubjectProperty{
		public final static int SUB_PAGE_TYPE=1;//子页面
		
		public final static int RES_PAGE_TYPE=2;//资源页
		
		public int type;
		
		public String cover;
		
		public WxSubjectProperty(int type,String cover){
			this.type=type;
			this.cover=cover;
		}
		public WxSubjectProperty(){
			
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getCover() {
			return cover;
		}

		public void setCover(String cover) {
			this.cover = cover;
		}
		
		
	}
	
	public static WxSubjectProperty typeJsonForObject(String json){
		if(json==null||json.isEmpty()){
			return null;
		}
		Gson gson=new Gson();
		try {
			WxSubjectProperty wxSubjectProperty=gson.fromJson(json, WxSubjectProperty.class);
			return wxSubjectProperty;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String typeJsonToStirng(WxSubjectProperty wxSubjectProperty){
		if(wxSubjectProperty==null){
			return null;
		}
		Gson gson=new Gson();
		try {
			String json=gson.toJson(wxSubjectProperty);
			return json;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public WxSubjectProperty instanceWxSubjectProperty(int type,String cover){
		WxSubjectProperty property=new WxSubjectProperty(type, cover);
		return property;
	}

}
