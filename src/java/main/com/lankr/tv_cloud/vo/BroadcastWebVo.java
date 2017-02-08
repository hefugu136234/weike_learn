package com.lankr.tv_cloud.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.tools.Tool;

import com.lankr.tv_cloud.broadcast.CurrentLivePlatfromData;
import com.lankr.tv_cloud.broadcast.LiveActionShowJs;
import com.lankr.tv_cloud.broadcast.LivePlatform;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class BroadcastWebVo extends BaseAPIModel {

	private String uuid;

	private String name;
	
	private String mark;//简介
	

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	private String createDate;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	private String castShowJs;

	private String castAction;

	private String description;

	private String bookStartDate;

	private String bookEndDate;

	private int limitNum;

	private int castType;

	private String label;

	private String banner;

	private String cover;

	private String kv;

	private String startDate;

	private String endDate;

	private String pincode;

	private String resourceName;

	private String resourceUuid;

	private String resourceUrl;

	private int isStatus;

	private int platFormType;

	private String platFormName;
	
	private int integral;
	
	private String tvDescription;
	
	private LiveActionShowJs liveActionShowJs;
	
	
	
	

	public LiveActionShowJs getLiveActionShowJs() {
		return liveActionShowJs;
	}

	public void setLiveActionShowJs(LiveActionShowJs liveActionShowJs) {
		this.liveActionShowJs = liveActionShowJs;
	}

	public String getTvDescription() {
		return tvDescription;
	}

	public void setTvDescription(String tvDescription) {
		this.tvDescription = tvDescription;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	private List<LivePlatform> livePlatformList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCastShowJs() {
		return castShowJs;
	}

	public void setCastShowJs(String castShowJs) {
		this.castShowJs = castShowJs;
	}

	public String getCastAction() {
		return castAction;
	}

	public void setCastAction(String castAction) {
		this.castAction = castAction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public String getBookStartDate() {
		return bookStartDate;
	}

	public void setBookStartDate(String bookStartDate) {
		this.bookStartDate = bookStartDate;
	}

	public String getBookEndDate() {
		return bookEndDate;
	}

	public void setBookEndDate(String bookEndDate) {
		this.bookEndDate = bookEndDate;
	}

	public int getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}

	public int getCastType() {
		return castType;
	}

	public void setCastType(int castType) {
		this.castType = castType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getKv() {
		return kv;
	}

	public void setKv(String kv) {
		this.kv = kv;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceUuid() {
		return resourceUuid;
	}

	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public int getPlatFormType() {
		return platFormType;
	}

	public void setPlatFormType(int platFormType) {
		this.platFormType = platFormType;
	}

	public List<LivePlatform> getLivePlatformList() {
		return livePlatformList;
	}

	public void setLivePlatformList(List<LivePlatform> livePlatformList) {
		this.livePlatformList = livePlatformList;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPlatFormName() {
		return platFormName;
	}

	public void setPlatFormName(String platFormName) {
		this.platFormName = platFormName;
	}

	public void buildTableData(Broadcast broadcast) {
		this.setName(broadcast.getName());
		this.setCreateDate(Tools.df1.format(broadcast.getCreateDate()));
		this.setLimitNum(broadcast.getLimitNum());
		this.setCastType(broadcast.getCastType());
		this.setIsStatus(broadcast.getStatus());
		String resName=OptionalUtils.traceValue(broadcast,
				"resource.name");
		String resUrl=broadcast.getResourceUrl();
		if(resName.isEmpty()){
			this.setResourceName("无");
		}else{
			this.setResourceName("有");
		}
		LivePlatform livePlat = CurrentLivePlatfromData
				.getCurrentLiveFromData().get(broadcast.getPlatFormType());
		this.setPlatFormName(OptionalUtils.traceValue(livePlat, "platfromName"));
		this.setUuid(broadcast.getUuid());
	}

	public void buildUpdateTableData(Broadcast broadcast) {
		this.setName(broadcast.getName());
		this.setCreateDate(Tools.df1.format(broadcast.getCreateDate()));
		this.setLimitNum(broadcast.getLimitNum());
		this.setCastType(broadcast.getCastType());
		this.setIsStatus(broadcast.getStatus());
		this.setResourceName(OptionalUtils.traceValue(broadcast,
				"resource.name"));
		LivePlatform livePlat = CurrentLivePlatfromData
				.getCurrentLiveFromData().get(broadcast.getPlatFormType());
		this.setPlatFormName(OptionalUtils.traceValue(livePlat, "platfromName"));
		this.setUuid(broadcast.getUuid());
	}

	public void buildUpdateData(Broadcast broadcast) {

		this.setName(broadcast.getName());
		this.setLimitNum(broadcast.getLimitNum());
		this.setCastType(broadcast.getCastType());
		this.setPlatFormType(broadcast.getPlatFormType());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String bookStart, bookEnd, start, end;
		bookStart=bookEnd=start=end=null;
		try {
			bookStart = dateFormat.format(broadcast.getBookStartDate());
			bookEnd = dateFormat.format(broadcast.getBookEndDate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try {
			start = dateFormat.format(broadcast.getStartDate());
			end = dateFormat.format(broadcast.getEndDate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		this.setBookStartDate(Tools.nullValueFilter(bookStart));
		this.setBookEndDate(Tools.nullValueFilter(bookEnd));
		this.setStartDate(Tools.nullValueFilter(start));
		this.setEndDate(Tools.nullValueFilter(end));
		this.setUuid(broadcast.getUuid());
		if(broadcast.getPlatFormType()==CurrentLivePlatfromData.YIDUO_PLAT){
			this.setCastAction(Tools.nullValueFilter(broadcast.getCastAction()));
		}else if(broadcast.getPlatFormType()==CurrentLivePlatfromData.ZHILIAO_PLAT){
			LiveActionShowJs liveActionShowJs=LiveActionShowJs.parseMessage(broadcast.getCastShowJs());
			if(liveActionShowJs!=null){
				this.setLiveActionShowJs(liveActionShowJs);
			}
		}else if(broadcast.getPlatFormType()==CurrentLivePlatfromData.BAIDU_PLAT){
			this.setCastAction(Tools.nullValueFilter(broadcast.getCastAction()));
		}
		
		this.setCastShowJs(Tools.nullValueFilter(broadcast.getCastShowJs()));
		this.setMark(Tools.nullValueFilter(broadcast.getMark()));
		this.setDescription(Tools.nullValueFilter(broadcast.getDescription()));
		this.setTvDescription(OptionalUtils.traceValue(broadcast, "tvDescription"));
		this.setPincode(Tools.nullValueFilter(broadcast.getPincode()));
	}

	public void buildPlatFromList() {
		Map<Integer, LivePlatform> map = CurrentLivePlatfromData
				.getCurrentLiveFromData();
		if (map != null && map.size() > 0) {
			this.livePlatformList = new ArrayList<LivePlatform>(map.values());
		}
	}
	
	
	public void buildConfig(Broadcast broadcast){
		this.setUuid(broadcast.getUuid());
		this.setName(broadcast.getName());
		this.setBanner(Tools.nullValueFilter(broadcast.getBanner()));
	}

}
