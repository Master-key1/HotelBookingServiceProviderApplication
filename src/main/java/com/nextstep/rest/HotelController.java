package com.nextstep.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nextstep.model.Customer;
import com.nextstep.model.Hotel;
import com.nextstep.model.HotelRequest;
import com.nextstep.service.HotelService;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private HotelService service;

	@PostMapping("/add")
	public ResponseEntity<String> addHotel(@RequestBody HotelRequest request) {
		Hotel hotel = new Hotel();
		Customer customer = new Customer();
		
		hotel.setHotelLoc(request.getHotelLoc());
		hotel.setHotelName(request.getHotelName());
		hotel.setHotelBookDate(request.getHotelBookDate());
		customer.setCustName(request.getCustName());
		customer.setAge(request.getAge());
		customer.setCustPhoneNo(request.getPhoneNo());
		System.out.println(request.toString());
		System.out.println(hotel.toString());
		System.out.println(customer.toString());
		
		 service.saveHotel(hotel);
		 
		 return new ResponseEntity<String>(hotel.getHotelId(),HttpStatus.OK);
	}

	@GetMapping("/all")
	public List<Hotel> getHotels() {
		return service.getAllHotels();
	}

	@GetMapping("/{id}")
	public Hotel getHotel(@PathVariable String id) {
		return service.getHotelById(id);
	}

	@DeleteMapping("/{id}")
	public String deleteHotel(@PathVariable String id) {
		return service.deleteHotel(id);
	}
}
