package com.nextstep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HotelBookingServiceProviderApplication {

	public static void main(String[] args) {
		 ConfigurableApplicationContext context  =SpringApplication.run(HotelBookingServiceProviderApplication.class, args);
	
	}

}
