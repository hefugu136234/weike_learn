package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class CollectQuestionnaireVo extends BaseAPIModel{

	private String uuid;
	
	private String name;
	
	private String questionnaireName;
	
	private String questionnaireMark;
	
	private int collectTime;
	
	private int collectNum;
	
	public String getQuestionnaireName() {
		return questionnaireName;
	}

	public void setQuestionnaireName(String questionnaireName) {
		this.questionnaireName = questionnaireName;
	}

	public String getQuestionnaireMark() {
		return questionnaireMark;
	}

	public void setQuestionnaireMark(String questionnaireMark) {
		this.questionnaireMark = questionnaireMark;
	}

	private String picUrl;
	
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String resourceUrl;
	
	private String selectItem;
	
	private List<ChosenItem> optionAdditions;
	
	public String getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(String selectItem) {
		this.selectItem = selectItem;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public List<ChosenItem> getOptionAdditions() {
		return optionAdditions;
	}

	public void setOptionAdditions(List<ChosenItem> optionAdditions) {
		this.optionAdditions = optionAdditions;
	}
	
	
	
	public int getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(int collectTime) {
		this.collectTime = collectTime;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}

	public void buildData(NormalCollect normalCollect, NormalCollectQuestionnaire normalCollectQuestionnaire, List<Questionnaire> list){
		this.setStatus(Status.SUCCESS);
		this.name = normalCollect.getName();
		this.uuid = normalCollect.getUuid();
		if (normalCollectQuestionnaire != null) {
			Questionnaire questionnaire=normalCollectQuestionnaire.getQuestionnaire();
			if(questionnaire!=null){
				this.setSelectItem(questionnaire.getUuid());
				this.questionnaireName = normalCollectQuestionnaire.getName();
				this.questionnaireMark = normalCollectQuestionnaire.getMark();
				this.collectNum=normalCollectQuestionnaire.getCollectNum();
				this.collectTime=normalCollectQuestionnaire.getCollectTime();
			}
			this.picUrl = normalCollectQuestionnaire.getCover();
		}
		optionAdditions=new ArrayList<ChosenItem>();
		DynamicSearchVo search=DynamicSearchVo.buildQuestionnaire(list, "");
		optionAdditions=search.getItems();
	}
}
