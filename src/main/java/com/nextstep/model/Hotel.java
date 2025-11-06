package com.nextstep.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Hotel {
	

	private String hotel;
	private String hotelName;
	private String hotelLoc;
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String hotelBookDate;
	private Customer customer;
	
	
	public String getHotel() {
		return hotel;
	}
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
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
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	@Override
	public String toString() {
		return "Hotel [hotel=" + hotel + ", hotelName=" + hotelName + ", hotelLoc=" + hotelLoc + ", hotelBookDate="
				+ hotelBookDate + ", customer=" + customer + "]";
	}

	
	
	

}
