package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class OfflineActivityItem extends AbstractItem<OfflineActivity> {

	private String description;

	private String bookStartDate;

	private String bookEndDate;

	private String address;

	private int enrollType;

	private int limitNum;

	private String cover;

	private String price;
	
	private String createDate;
	
	public String status;
	
	private String initiatorName;
	

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getEnrollType() {
		return enrollType;
	}

	public void setEnrollType(int enrollType) {
		this.enrollType = enrollType;
	}

	public int getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	

	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

	public OfflineActivityItem(OfflineActivity offlineActivity) {
		super(offlineActivity);
	}
	
	

	
	@Override
	public boolean createMarker() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean modifyMarker() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void buildextra() throws Exception {
		// TODO Auto-generated method stub
		this.address = OptionalUtils.traceValue(t, "address");

		this.enrollType = OptionalUtils.traceInt(t, "enrollType");

		this.limitNum = OptionalUtils.traceInt(t, "limitNum");

		this.price = OptionalUtils.traceValue(t, "price");
		
		this.createDate=Tools.formatYMDHMSDate(t.getCreateDate());
		
		this.initiatorName=OptionalUtils.traceValue(t, "initiatorUser.nickname");
	}

	public void buildDetail() {
		this.buildList();
		
		this.description = OptionalUtils.traceValue(t, "description");

		this.bookStartDate = Tools.formatYMDHMSDate(t.getBookStartDate());

		this.bookEndDate = Tools.formatYMDHMSDate(t.getBookEndDate());

		this.cover=OptionalUtils.traceValue(t, "cover");
	}
	
	public void buildList(){
		try {
			this.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
