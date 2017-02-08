package com.lankr.tv_cloud.model;

public class LogisticsInfo extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -109200678123427959L;

	private int status;
	
	private String logistics;
	
	private IntegralRecord integralRecord;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLogistics() {
		return logistics;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	public IntegralRecord getIntegralRecord() {
		return integralRecord;
	}

	public void setIntegralRecord(IntegralRecord integralRecord) {
		this.integralRecord = integralRecord;
	}
	
	
	

}
