package com.nextstep.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nextstep.model.Hotel;

@RestController

public class HotelRestController {

	@PostMapping(value="/get")
	public ResponseEntity<String> gettBooking(@RequestBody Hotel hotel) {

		return new ResponseEntity<>("your bokking successfully....!"+hotel.toString(), HttpStatus.ACCEPTED);

	}

}
