package com.nextstep.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Welcome {
	
	@GetMapping("/welcome")
	public ResponseEntity<String> greet(){
		
		return new ResponseEntity<>("Wel come ............1",HttpStatus.OK);
	}

}
