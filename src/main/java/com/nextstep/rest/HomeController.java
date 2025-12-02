package com.nextstep.rest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


	 @GetMapping("/")
	    public String showIndexPage() {
	        return "index"; // loads templates/index.html
	    }

}
