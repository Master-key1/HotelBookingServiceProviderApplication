package com.nextstep.service;

import com.nextstep.model.Hotel;
import com.nextstep.repo.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {

	@Autowired
	private HotelRepo repo;

	public Hotel saveHotel(Hotel hotel) {
		return repo.save(hotel);
	}

	public List<Hotel> getAllHotels() {
		return repo.findAll();
	}

	public Hotel getHotelById(String hotelId) {
		return repo.findById(hotelId).orElse(null);
	}

	public String deleteHotel(String hotelId) {
		repo.deleteById(hotelId);
		return "Hotel Deleted Successfully";
	}
	
}
