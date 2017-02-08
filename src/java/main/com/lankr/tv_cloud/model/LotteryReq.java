package com.lankr.tv_cloud.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LotteryReq extends BaseModel{
	private static final long serialVersionUID = 1581111218447853063L;
	
	private int status;
	private String name;
	private String beginDate;
	private String endDate;
	private int joinTye;
	private int joinTimes;
	private String page;
	private int templateId;
	private String rules;
	private List<Award> awards;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getJoinTye() {
		return joinTye;
	}
	public void setJoinTye(int joinTye) {
		this.joinTye = joinTye;
	}
	public int getJoinTimes() {
		return joinTimes;
	}
	public void setJoinTimes(int joinTimes) {
		this.joinTimes = joinTimes;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public List<Award> getAwards() {
		return awards;
	}
	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	
	//接收页面数据转换字符串日期至Date存入  DB
	public Lottery tidyData(LotteryReq lottery) {
		if(null == lottery || null == lottery.getAwards())
			return null;
		Lottery lotteryAfter = new Lottery();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date beginDate, endDate;
		try {
			beginDate = dateFormat.parse(lottery.getBeginDate());
			endDate = dateFormat.parse(lottery.getEndDate());
		} catch (ParseException e) {
			return null;
		}
		lotteryAfter.setIsActive(lottery.getIsActive());
		lotteryAfter.setStatus(lottery.getStatus());
		lotteryAfter.setId(lottery.getId());
		lotteryAfter.setName(lottery.getName());
		lotteryAfter.setJoinTimes(lottery.getJoinTimes());
		lotteryAfter.setAwards(lottery.getAwards());
		lotteryAfter.setCreateDate(lottery.getCreateDate());
		lotteryAfter.setModifyDate(lottery.getModifyDate());
		lotteryAfter.setMark(lottery.getMark());
		lotteryAfter.setBeginDate(beginDate);
		lotteryAfter.setEndDate(endDate);
		lotteryAfter.setUuid(lottery.getUuid());
		lotteryAfter.setTemplateId(lottery.getTemplateId());
		lotteryAfter.setRules(lottery.getRules());
		return lotteryAfter;
	}
	
	//从 DB 中取出记录将Date转成字符串
	public static LotteryReq buildUpdateViewData(Lottery lottery) {
		if(null == lottery)
			return null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String begin,end;
		begin = end = "";
		try {
			begin = dateFormat.format(lottery.getBeginDate());
			end = dateFormat.format(lottery.getEndDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		LotteryReq updateViewData = new LotteryReq();
		updateViewData.setIsActive(lottery.getIsActive());
		updateViewData.setStatus(lottery.getStatus());
		updateViewData.setId(lottery.getId());
		updateViewData.setUuid(lottery.getUuid());
		updateViewData.setName(lottery.getName());
		updateViewData.setBeginDate(begin);
		updateViewData.setEndDate(end);
		updateViewData.setJoinTimes(lottery.getJoinTimes());
		updateViewData.setMark(lottery.getMark());
		updateViewData.setTemplateId(lottery.getTemplateId());
		updateViewData.setRules(lottery.getRules());
		return updateViewData;
	}
	
	public static LotteryReq getJsonData(String gameData) {
		LotteryReq result = null;
		try {
			result = new Gson().fromJson(gameData, LotteryReq.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
}
