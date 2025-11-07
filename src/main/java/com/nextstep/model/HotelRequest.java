package com.nextstep.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HotelRequest {

	
	private String hotelName;
	private String hotelLoc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String hotelBookDate;
	private String CustName;
	private String phoneNo;
	private byte age;
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getHotelLoc() {
		return hotelLoc;
	}
	public void setHotelLoc(String hotelLoc) {
		this.hotelLoc = hotelLoc;
	}
	public String getHotelBookDate() {
		return hotelBookDate;
	}
	public void setHotelBookDate(String hotelBookDate) {
		this.hotelBookDate = hotelBookDate;
	}
	public String getCustName() {
		return CustName;
	}
	public void setCustName(String custName) {
		CustName = custName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public byte getAge() {
		return age;
	}
	public void setAge(byte age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "HotelRequest [hotelName=" + hotelName + ", hotelLoc=" + hotelLoc + ", hotelBookDate=" + hotelBookDate
				+ ", CustName=" + CustName + ", phoneNo=" + phoneNo + ", age=" + age + "]";
	}
	
	
	

}
