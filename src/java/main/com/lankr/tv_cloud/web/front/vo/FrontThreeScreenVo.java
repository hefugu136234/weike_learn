package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.ThreeScreen;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.front.util.FrontWxOpenUtil;

public class FrontThreeScreenVo{
	
	private String videoTime;//00:00:00
	
	private String division;//对应关系
	
	private String fileId;
	
	private int pdfNum;//总页数
	
	private String pdfTaskId;
	
	private String videoCover;
	
	private String screenCover;
	
	private List<Integer> pdfList;

	public String getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public int getPdfNum() {
		return pdfNum;
	}

	public void setPdfNum(int pdfNum) {
		this.pdfNum = pdfNum;
	}

	public String getPdfTaskId() {
		return pdfTaskId;
	}

	public void setPdfTaskId(String pdfTaskId) {
		this.pdfTaskId = pdfTaskId;
	}

	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	public String getScreenCover() {
		return screenCover;
	}

	public void setScreenCover(String screenCover) {
		this.screenCover = screenCover;
	}
	
	
	
	
	public List<Integer> getPdfList() {
		return pdfList;
	}

	public void setPdfList(List<Integer> pdfList) {
		this.pdfList = pdfList;
	}

	public void buildData(ThreeScreen threeScreen){
		this.setFileId(OptionalUtils.traceValue(threeScreen, "fileId"));
		String division=OptionalUtils.traceValue(threeScreen, "division");
		this.setDivision(division);
		this.setPdfTaskId(OptionalUtils.traceValue(threeScreen, "pdfTaskId"));
		this.setVideoCover(OptionalUtils.traceValue(threeScreen, "videoCover"));
		String cover = OptionalUtils.traceValue(threeScreen, "coverTaskId");
		cover = WxUtil.getResourceCover(cover);
		this.setScreenCover(cover);
		int videoTime=OptionalUtils.traceInt(threeScreen, "videoTime");
		String time=FrontWxOpenUtil.videoTime(videoTime);
		this.setVideoTime(time);
		this.pdfList=new ArrayList<Integer>();
		List<ThreeScreenRelatJson> list=ThreeScreenRelatJson.getJsonList(division);
		if(list==null||list.isEmpty())
			return ;
		this.setPdfNum(list.size());
		for (ThreeScreenRelatJson threeScreenRelatJson : list) {
			this.pdfList.add(threeScreenRelatJson.getPdf());
		}
	}
	
	
	

}
