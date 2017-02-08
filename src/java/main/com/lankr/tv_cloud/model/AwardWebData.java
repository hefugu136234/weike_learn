package com.lankr.tv_cloud.model;

public class AwardWebData{

	private int id;
	private String uuid;
	private String name;
	private int number;
	private int maxWinTimes;
	private double conditional;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getMaxWinTimes() {
		return maxWinTimes;
	}
	public void setMaxWinTimes(int maxWinTimes) {
		this.maxWinTimes = maxWinTimes;
	}
	public double getConditional() {
		return conditional;
	}
	public void setConditional(double conditional) {
		this.conditional = conditional;
	}
	public AwardWebData format(Award award) {
		AwardWebData data = new AwardWebData();
		data.setId(award.getId());
		data.setUuid(award.getUuid());
		data.setName(award.getName());
		data.setNumber(award.getNumber());
		data.setMaxWinTimes(award.getMaxWinTimes());
		data.setConditional(award.getConditional());
		return data;
	}
}
