package com.lankr.tv_cloud.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Questionnaire extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5776382507862511157L;

	private String name;
	
	private String pinyin;
	
	private int status;
	
	private String qProperty;
	
	private String urlLink;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getqProperty() {
		return qProperty;
	}

	public void setqProperty(String qProperty) {
		this.qProperty = qProperty;
	}

	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}
	
	public class QuestionnaireProperty{
		//问卷的json属性
		public static final String ALLOW_REPEAT="1";
		
		public static final String No_REPEAT="0";
		
		public String repeat;

		public String getRepeat() {
			return repeat;
		}

		public void setRepeat(String repeat) {
			this.repeat = repeat;
		}
		
	}
	
	public static QuestionnaireProperty JsonForObject(String json){
		if(json==null||json.isEmpty()){
			return null;
		}
		Gson gson=new Gson();
		try {
			QuestionnaireProperty questionnaireProperty=gson.fromJson(json, QuestionnaireProperty.class);
			return questionnaireProperty;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String JsonToStirng(QuestionnaireProperty questionnaireProperty){
		if(questionnaireProperty==null){
			return null;
		}
		Gson gson=new Gson();
		try {
			String json=gson.toJson(questionnaireProperty);
			return json;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public QuestionnaireProperty instanceQProperty(String repeat){
		QuestionnaireProperty questionnaireProperty=new QuestionnaireProperty();
		questionnaireProperty.setRepeat(repeat);
		return questionnaireProperty;
	}

}
