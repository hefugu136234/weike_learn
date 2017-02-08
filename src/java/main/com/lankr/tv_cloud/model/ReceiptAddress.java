package com.lankr.tv_cloud.model;

public class ReceiptAddress extends BaseModel{
	
	public final static int DEFAULT_ADDRESS=1;
	
	public final static int NOt_DEFAULT_ADDRESS=0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8549530843999846502L;

	private float latitude;
	
	private float longitude;
	
	private User user;
	
	private String name;
	
	private String phone;
	
	private City city;
	
	private District district;
	
	private String address;
	
	private String postCode;
	
	private int defaultAddress;
	
	private int status;

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public int getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(int defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	


}
