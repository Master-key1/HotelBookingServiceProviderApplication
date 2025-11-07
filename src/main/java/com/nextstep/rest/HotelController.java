package com.nextstep.rest;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nextstep.model.Customer;
import com.nextstep.model.Hotel;
import com.nextstep.model.HotelRequest;
import com.nextstep.service.CustomerService;
import com.nextstep.service.HotelService;

import jakarta.persistence.PrePersist;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private HotelService Hotelservice;
	@Autowired
	CustomerService CustomerService;

	@PostMapping("/add")
	public ResponseEntity<String> addHotel(@RequestBody HotelRequest request) {
		Hotel hotel = new Hotel();
		Customer customer = new Customer();

		hotel.setHotelId(generateHotelId(request));
		hotel.setHotelLoc(request.getHotelLoc());
		hotel.setHotelName(request.getHotelName());
		hotel.setHotelBookDate(request.getHotelBookDate());

		if (customer.getCustId() == null)
			customer.setCustId(request.getPhoneNo());

		customer.setCustName(request.getCustName());
		customer.setAge(request.getAge());
		customer.setCustPhoneNo(request.getPhoneNo());
		System.out.println(request.toString());
		System.out.println(hotel.toString());
		System.out.println(customer.toString());
		System.out.println("Hotel ID :" + hotel.getHotelId());

		Hotelservice.saveHotel(hotel);
		// if(customer != null)
		// CustomerService.saveCustomer(customer);

		return new ResponseEntity<String>(hotel.getHotelId(), HttpStatus.OK);
	}

	@PrePersist
	public String generateHotelId(HotelRequest hotel) {
		Random random = new Random();

		String val = hotel.getHotelName() + "-" + random.nextInt(1000);
		return val;
	}

	
	@GetMapping("/hotels")
	public List<Hotel> getHotels() {
		return Hotelservice.getAllHotels();
	}

	@GetMapping("/customers")
	public List<Customer> getCustomers() {
		return CustomerService.getallCustomer();
	}

	@GetMapping("/{hotelid}")
	@CrossOrigin(origins = "*")

	public Hotel getHotel(@PathVariable String hotelid) {
		return Hotelservice.getHotelById(hotelid);
	}

	@DeleteMapping("/{id}")
	public String deleteHotel(@PathVariable String id) {
		return Hotelservice.deleteHotel(id);
	}
}
