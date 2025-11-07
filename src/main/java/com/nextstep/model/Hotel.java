package com.nextstep.model;

import java.util.Random;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "hotel_details")
public class Hotel {

	@Id
	private String hotelId;
	private String hotelName;
	private String hotelLoc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String hotelBookDate;

	

	

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
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

	@Override
	public String toString() {
		return "Hotel [hotelId=" + hotelId + ", hotelName=" + hotelName + ", hotelLoc=" + hotelLoc + ", hotelBookDate="
				+ hotelBookDate + "]";
	}

	
}
